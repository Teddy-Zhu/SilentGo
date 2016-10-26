package com.silentgo.core.config;

import com.silentgo.core.action.ActionChain;
import com.silentgo.core.aop.annotationintercept.IAnnotation;
import com.silentgo.core.aop.annotationintercept.support.AnnotationInterceptor;
import com.silentgo.core.aop.validator.support.ValidatorInterceptor;
import com.silentgo.core.cache.CacheManager;
import com.silentgo.core.cache.EhCache;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.ioc.bean.SilentGoBeanFactory;
import com.silentgo.core.plugin.event.EventListener;
import com.silentgo.core.route.RoutePaser;
import com.silentgo.core.route.support.routeparse.DefaultRouteParser;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.orm.base.DBType;
import com.silentgo.utils.ClassKit;
import com.silentgo.utils.CollectionKit;
import com.silentgo.utils.json.GsonPaser;
import com.silentgo.utils.json.JsonPaser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.config
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/2.
 */
public class BaseConfig extends InterConfig {

    Map<String, AbstractConfig> abstractConfigMap = new HashMap() {{
        put(Const.FileUploadConfig, new FileUploadConfig(ClassKit.getWebRootPath() + "/UploadFile", -1, 10240, true));
    }};

    private String propfile = "application.properties";

    private Class<? extends BeanFactory> beanClass = SilentGoBeanFactory.class;

    private final ThreadLocal<DBConnect> threadConnect = new ThreadLocal<>();

    private List<Class<? extends IAnnotation>> annotationIntecepters = new ArrayList<>();

    private JsonPaser jsonPaser = new GsonPaser();

    private RoutePaser routePaser = new DefaultRouteParser();

    private List<Class<? extends BaseFactory>> factories = new ArrayList<>();

    private String dbType;
    private String BaseViewPath = Const.BaseView;

    private List<ActionChain> actionChains = new ArrayList<>();

    private Map<String, String> staticMapping = new HashMap<>();

    private List<Class<? extends EventListener>> eventListeners = new ArrayList<>();

    private Class<? extends CacheManager> cacheClz = EhCache.class;

    private List<String> staticEndWith = new ArrayList<>();
    private List<String> staticStartWith = new ArrayList<>();

    private List<String> scanPackages = new ArrayList<>();

    private List<String> scanJars = new ArrayList<>();

    private List<Config> extraConfig = new ArrayList<>();

    private boolean devMode = false;

    private String encoding = "utf-8";

    private int contextPathLength;

    public Class<? extends CacheManager> getCacheClz() {
        return cacheClz;
    }

    public void setCacheClz(Class<? extends CacheManager> cacheClz) {
        this.cacheClz = cacheClz;
    }

    @SuppressWarnings("unchecked")
    private ArrayList interceptors = new ArrayList() {{
        add(new AnnotationInterceptor());
        add(new ValidatorInterceptor());
    }};

    public List<Class<? extends BaseFactory>> getFactories() {
        return factories;
    }

    public List<Class<? extends IAnnotation>> getAnnotationIntecepters() {
        return annotationIntecepters;
    }

    public List<ActionChain> getActionChains() {
        return actionChains;
    }

    public Map<String, String> getStaticMapping() {
        return staticMapping;
    }

    public JsonPaser getJsonPaser() {
        return jsonPaser;
    }

    public void setJsonPaser(JsonPaser jsonPaser) {
        this.jsonPaser = jsonPaser;
    }

    public RoutePaser getRoutePaser() {
        return routePaser;
    }

    public void setRoutePaser(RoutePaser routePaser) {
        this.routePaser = routePaser;
    }

    public String getBaseViewPath() {
        return BaseViewPath;
    }

    public void setBaseViewPath(String baseViewPath) {
        BaseViewPath = baseViewPath;
    }

    public List<String> getStaticStartWith() {
        return staticStartWith;
    }

    public List<String> getStaticEndWith() {
        return staticEndWith;
    }

    public void setStaticStartWith(List<String> staticStartWith) {
        this.staticStartWith = staticStartWith;
    }

    public List<String> getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(List<String> scanPackages) {
        this.scanPackages = scanPackages;
    }

    public List<String> getScanJars() {
        return scanJars;
    }

    public void setScanJars(List<String> scanJars) {
        this.scanJars = scanJars;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getContextPathLength() {
        return contextPathLength;
    }

    public void setContextPathLength(int contextPathLength) {
        this.contextPathLength = contextPathLength;
    }

    public ArrayList getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(ArrayList interceptors) {
        this.interceptors = interceptors;
    }

    public boolean addAbstractConfig(AbstractConfig config) {
        return CollectionKit.MapAdd(abstractConfigMap, config.name(), config);
    }

    public AbstractConfig getConfig(String type) {
        return abstractConfigMap.get(type);
    }

    public AbstractConfig getConfig(DBType type) {
        return abstractConfigMap.get(type.getName());
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public Class<? extends BeanFactory> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<? extends BeanFactory> beanClass) {
        this.beanClass = beanClass;
    }

    public ThreadLocal<DBConnect> getThreadConnect() {
        return threadConnect;
    }

    public String getPropfile() {
        return propfile;
    }

    public void setPropfile(String propfile) {
        this.propfile = propfile;
    }

    public List<Config> getExtraConfig() {
        return extraConfig;
    }

    public List<Class<? extends EventListener>> getEventListeners() {
        return eventListeners;
    }
}
