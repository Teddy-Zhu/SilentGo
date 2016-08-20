package com.silentgo.core.route.support;

import com.alibaba.fastjson.JSON;
import com.silentgo.config.Const;
import com.silentgo.core.aop.MethodAdviser;
import com.silentgo.core.aop.MethodParam;
import com.silentgo.core.route.RegexRoute;
import com.silentgo.core.route.annotation.PathVariable;
import com.silentgo.kit.CollectionKit;
import com.silentgo.kit.StringKit;
import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;
import com.silentgo.kit.typeconvert.ConvertKit;
import com.silentgo.kit.typeconvert.ITypeConvertor;
import com.silentgo.kit.typeconvert.support.CommonConvertor;
import com.silentgo.kit.typeconvert.support.TypeConvert;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * Project : silentgo
 * com.silentgo.core.route.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/19.
 */
public class RouteParamPaser {

    public static final Logger LOGGER = LoggerFactory.getLog(RouteParamPaser.class);


    public static boolean parsePathVariable(Request request, RegexRoute route, Matcher macher) {
        String[] pathParameters = new String[macher.groupCount()];
        for (int i = 1, len = macher.groupCount(); i <= len; i++) {
            pathParameters[i - 1] = macher.group(i);
        }
        Map<String, String> path = new HashMap<>();
        route.getNames().forEach(name -> CollectionKit.MapAdd(path, name, macher.group(name)));
        request.setPathNamedParameters(path);
        request.setPathParameters(pathParameters);
        return true;
    }

    public static Object[] parseParams(MethodAdviser adviser, Request request, Response response) {
        MethodParam[] methodParams = adviser.getParams();
        Object[] parameters = new Object[methodParams.length];
        Map<String, Object> parsedParameters = new HashMap<>();

        Map<String, Object> hash = getParamHash(request);
        String jsonHash = JSON.toJSONString(hash);
        jsonHash = jsonHash.equals("{}") ? Const.EmptyString : jsonHash;

        for (int i = 0, len = methodParams.length; i < len; i++) {
            MethodParam methodParam = methodParams[i];
            if (Request.class.isAssignableFrom(methodParam.getType())) {
                parameters[i] = request;
                continue;
            }
            if (Response.class.isAssignableFrom(methodParam.getType())) {
                parameters[i] = response;
                continue;
            }

            Object object = hash.get(methodParam.getName());
            String jsonString = methodParam.getAnnotations().stream().anyMatch(annotation ->
                    PathVariable.class.equals(annotation.annotationType())) ?
                    request.getPathNamedParameter(methodParam.getName()) :
                    (object == null ? jsonHash :
                            (object instanceof String ?
                                    object.toString() : JSON.toJSONString(object)));
            try {
                if (methodParam.getType().isArray()) {
                    parameters[i] = JSON.parseArray(jsonString, methodParam.getType());
                    continue;
                } else if (TypeConvert.isBaseType(methodParam.getType())) {
                    ITypeConvertor typeConvertor = new ConvertKit().getTypeConvert(String.class, methodParam.getType());
                    typeConvertor = typeConvertor == null ? new CommonConvertor() : typeConvertor;
                    parameters[i] = typeConvertor.convert(jsonString);
                    continue;
                }
                parameters[i] = JSON.parseObject(jsonString, methodParam.getType());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                parameters[i] = null;
            }
            CollectionKit.MapAdd(parsedParameters, methodParam.getName(), parameters[i], true);
        }
        return parameters;
    }

    private static <T extends Annotation> T getAnnotation(List<Annotation> annotations, Class<T> clz) {
        Optional o = annotations.stream().filter(annotation -> annotation.annotationType().equals(clz)).findFirst();
        return o.isPresent() ? (T) o.get() : null;
    }

    private static Map<String, Object> getParamHash(Request request) {
        Map<String, String[]> paramMap = request.getParameterMap();

        Map<String, Object> prasedParamMap = new HashMap<>();

        paramMap.forEach((k, v) -> {
            String[] ks = k.indexOf('.') > 0 ? k.split(".") : new String[]{k};
            if (ks.length == 0 || StringKit.isNullOrEmpty(ks[0])) return;

            if (ks.length == 1) {
                CollectionKit.MapAdd(prasedParamMap, ks[0], v[0]);
            }
            if (ks.length > 1) {
                Map<String, Object> map = getMap(prasedParamMap, ks[0]);
                for (int i = 1, len = ks.length - 1; i < len; i++) {
                    map = getMap(map, ks[i]);
                }
                CollectionKit.MapAdd(map, ks[ks.length - 1], v[0]);
            }
        });
        return prasedParamMap;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getMap(Map<String, Object> source, String key) {
        Object obj = source.get(key);
        return (Map<String, Object>) (obj == null ? source.put(key, new HashMap<String, Object>()) : obj);
    }
}
