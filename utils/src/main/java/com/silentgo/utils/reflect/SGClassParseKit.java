package com.silentgo.utils.reflect;

import com.silentgo.utils.StringKit;
import com.silentgo.utils.asm.ParameterNameUtils;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils.reflect
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2017/1/5.
 */
public class SGClassParseKit {
    private static final Log LOGGER = LogFactory.get();

    public static SGClass parse(Class<?> clz) {
        SGClass sgClass = new SGClass();
        sgClass.setClz(clz);
        sgClass.setConstructors(new ArrayList<>());
        Constructor[] constructors = clz.getConstructors();

        for (Constructor constructor : constructors) {
            SGConstructor sgConstructor = parseConstructor(constructor);
            if (sgConstructor.isDefault()) {
                sgClass.setDefaultConstructor(sgConstructor);
            } else {
                sgClass.getConstructors().add(sgConstructor);
            }
        }


        sgClass.setMethodMap(new HashMap<>());

        Method[] methods = clz.getMethods();
        boolean needNamed = false;
        for (Method method : methods) {
            int modifier = method.getModifiers();

            if (method.isBridge()) {
                continue;
            }
            if (Modifier.isStatic(modifier)
                    || Modifier.isNative(modifier)
                    || method.getDeclaringClass().equals(Object.class)) {
                continue;
            }
            if (method.getParameters().length > 0 && !method.getParameters()[0].isNamePresent()) {
                needNamed = true;
            }
            sgClass.getMethodMap().put(method, parseMethod(method));
        }

        if (needNamed) {
            findMethodParameterName(sgClass.getMethodMap());
        }

        sgClass.setFieldMap(new HashMap<>());
        Field[] fields = clz.getFields();

        for (Field field : fields) {
            int modifier = field.getModifiers();
            if (Modifier.isStatic(modifier) || Modifier.isNative(modifier) || Modifier.isFinal(modifier)) {
                continue;
            }
            SGField sgField = parseField(field);
            sgField.setName(field.getName());
            sgClass.getFieldMap().put(field.getName(), sgField);
            findGetSetMethod(sgClass.getMethodMap(), sgField);
        }

        fields = clz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (sgClass.getFieldMap().containsKey(field.getName()))
                continue;
            SGField sgField = parseField(field);
            sgField.setName(field.getName());
            sgClass.getFieldMap().put(field.getName(), sgField);
            findGetSetMethod(sgClass.getMethodMap(), sgField);
        }

        sgClass.getMethodMap().forEach(((method, sgMethod) -> {
            String name = methodNameToFieldName(method.getName());
            if (name != null && !sgClass.getFieldMap().containsKey(name)) {
                SGField field = new SGField();
                field.setGetMethod(sgMethod);
                field.setName(name);
                field.setAnnotationMap(new HashMap<>());
                field.setType(sgMethod.getMethod().getReturnType());
                sgClass.getFieldMap().put(name, field);
            }
        }));
        return sgClass;
    }

    private static String methodNameToFieldName(String methodName) {
        if (methodName.startsWith("get") && methodName.length() > 3) {
            return methodNameToFieldName0(methodName, 3);
        } else if (methodName.startsWith("is") && methodName.length() > 2) {
            return methodNameToFieldName0(methodName, 2);
        }
        return null;
    }

    private static String methodNameToFieldName0(String methodName, int index) {
        if (Character.isUpperCase(methodName.charAt(index))) {
            if (methodName.length() > index + 1 && Character.isUpperCase(methodName.charAt(index + 1))) {
                return methodName.substring(index);
            } else {
                return StringKit.firstToLower(methodName.substring(index));
            }
        }
        return methodName.substring(index);
    }

    private static void findMethodParameterName(Map<Method, SGMethod> methodMap) {
        methodMap.forEach((s, sgMethod) -> {
            String[] parameterNames = ParameterNameUtils.getMethodParameterNames(sgMethod.getMethod());
            String[] preParameterNames = sgMethod.getParameterNames();
            if (parameterNames != null && parameterNames.length == preParameterNames.length) {
                for (int i = 0; i < preParameterNames.length; i++) {
                    SGParameter sgParameter = sgMethod.getParameterMap().get(preParameterNames[i]);
                    sgParameter.setName(parameterNames[i]);
                    sgMethod.getParameterMap().remove(preParameterNames[i]);
                    sgMethod.getParameterMap().put(parameterNames[i], sgParameter);
                }
                sgMethod.setParameterNames(parameterNames);
            }
        });
    }

    private static void findGetSetMethod(Map<Method, SGMethod> methodMap, SGField field) {
        String methodName = StringKit.firstToUpper(field.getField().getName());
        Class<?> type = field.getField().getType();
        if (methodName.length() > 1 && Character.isUpperCase(methodName.charAt(1))) {
            methodName = field.getField().getName();
        }
        String getFlag = "get", setFlag = "set";
        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            getFlag = "is";
        }
        String getMethodName = getFlag + methodName;
        String setMethodName = setFlag + methodName;

        field.setGetMethod(findGetMethod(methodMap, getMethodName, type));
        field.setSetMethod(findSetMethod(methodMap, setMethodName));
    }

    private static SGMethod findGetMethod(Map<Method, SGMethod> methodMap, String methodName, Class<?> type) {
        for (Map.Entry<Method, SGMethod> methodSGMethodEntry : methodMap.entrySet()) {
            SGMethod method = methodSGMethodEntry.getValue();
            if (methodName.equals(method.getName())) {
                if (method.getMethod().getReturnType().equals(type) && method.getParameterMap().size() == 0)
                    return method;
            }
        }
        return null;
    }

    private static SGMethod findSetMethod(Map<Method, SGMethod> methodMap, String methodName) {
        for (Map.Entry<Method, SGMethod> methodSGMethodEntry : methodMap.entrySet()) {
            SGMethod method = methodSGMethodEntry.getValue();
            if (methodName.equals(method.getName())) {
                if ((method.getMethod().getReturnType().equals(Void.class) || method.getMethod().getReturnType().equals(void.class)))
                    return method;
            }
        }
        return null;
    }

    private static SGMethod parseMethod(Method method) {
        SGMethod sgMethod = new SGMethod();
        sgMethod.setMethod(method);
        sgMethod.setName(method.getName());
        sgMethod.setClassName(method.getDeclaringClass().getName());
        sgMethod.setFullName(sgMethod.getClassName() + "." + sgMethod.getName());
        sgMethod.setAnnotationMap(new HashMap<>());

        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            sgMethod.getAnnotationMap().put(annotation.annotationType(), annotation);
        }

        Parameter[] parameters = method.getParameters();
        sgMethod.setParameterMap(new HashMap<>());
        sgMethod.setParameterNames(new String[parameters.length]);
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            sgMethod.getParameterNames()[i] = parameter.getName();
            sgMethod.getParameterMap().put(parameter.getName(), parseParameter(parameter));
        }

        return sgMethod;
    }

    private static SGField parseField(Field field) {
        SGField sgField = new SGField();
        sgField.setField(field);
        sgField.setType(field.getType());
        sgField.setAnnotationMap(new HashMap<>());
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            sgField.getAnnotationMap().put(annotation.annotationType(), annotation);
        }


        return sgField;
    }

    private static SGConstructor parseConstructor(Constructor constructor) {

        SGConstructor sgConstructor = new SGConstructor();
        sgConstructor.setAnnotationMap(new HashMap<>());
        sgConstructor.setConstructor(constructor);

        Parameter[] parameters = constructor.getParameters();
        sgConstructor.setDefault(parameters.length == 0);

        sgConstructor.setParameterMap(new HashMap<>());
        sgConstructor.setParameterNames(new String[parameters.length]);
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            sgConstructor.getParameterNames()[i] = parameter.getName();
            sgConstructor.getParameterMap().put(parameter.getName(), parseParameter(parameter));
        }

        sgConstructor.setAnnotationMap(new HashMap<>());
        Annotation[] annotations = constructor.getAnnotations();
        for (Annotation annotation : annotations) {
            sgConstructor.getAnnotationMap().put(annotation.annotationType(), annotation);
        }
        return sgConstructor;
    }

    private static SGParameter parseParameter(Parameter parameter) {
        SGParameter sgParameter = new SGParameter();

        sgParameter.setParameter(parameter);
        sgParameter.setAnnotationMap(new HashMap<>());

        Annotation[] annotations = parameter.getAnnotations();
        for (Annotation annotation : annotations) {
            sgParameter.getAnnotationMap().put(annotation.annotationType(), annotation);
        }
        sgParameter.setName(parameter.getName());

        sgParameter.setClz(parameter.getType());
        return sgParameter;
    }

}
