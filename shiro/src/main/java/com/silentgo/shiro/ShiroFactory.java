package com.silentgo.shiro;

import com.silentgo.core.SilentGo;
import com.silentgo.core.cache.CacheFactory;
import com.silentgo.core.cache.EhCache;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.utils.PropKit;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.web.env.MutableWebEnvironment;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

import javax.servlet.ServletContext;

/**
 * Project : SilentGo
 * Package : com.silentgo.shiro
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/10.
 */
public class ShiroFactory extends BaseFactory {
    public static final String Name = "Shiro";

    private DefaultWebSecurityManager defaultWebSecurityManager;

    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        return defaultWebSecurityManager;
    }

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {


        ShiroConfig config = (ShiroConfig) me.getConfig().getConfig(Name);

        me.getFactory(CacheFactory.class);
        PropKit prop = me.getConfig().getUserProp();

        RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher();
        retryLimitHashedCredentialsMatcher.setHashAlgorithmName(prop.getValue(Dict.SHIRO_CREDENTIALS_MATCHER_HASHALGORITHMNAME, "md5"));
        retryLimitHashedCredentialsMatcher.setHashIterations(prop.getInt(Dict.SHIRO_CREDENTIALS_MATCHER_HASHITERATIONS, 2));
        retryLimitHashedCredentialsMatcher.setStoredCredentialsHexEncoded(prop.getBool(Dict.SHIRO_CREDENTIALS_MATCHER_STOREDCREDENTIALSHEXENCODED, true));
        EhCacheManager ehCacheManager = new EhCacheManager();
        EhCache cache = (EhCache) me.getConfig().getCacheManager();
        ehCacheManager.setCacheManager(cache.getCacheManager());

        JavaUuidSessionIdGenerator sessionIdGenerator = new JavaUuidSessionIdGenerator();

        SimpleCookie sessionIdCookie = new SimpleCookie(prop.getValue(Dict.SHIRO_SESSION_COOKIENAME, "lc4e"));
        sessionIdCookie.setMaxAge(prop.getInt(Dict.SHIRO_SESSION_IDCOOKIE_MAXAGE, -1));
        sessionIdCookie.setHttpOnly(prop.getBool(Dict.SHIRO_SESSION_IDCOOKIE_HTTPONLY, true));

        SimpleCookie rememberMeCookie = new SimpleCookie(prop.getValue(Dict.SHIRO_SESSION_REMEMBER_COOKIENAME, "rlc4e"));
        rememberMeCookie.setHttpOnly(prop.getBool(Dict.SHIRO_SESSION_REMEMBER_ME_HTTPONLY, true));
        rememberMeCookie.setMaxAge(prop.getInt(Dict.SHIRO_SESSION_REMEMBER_ME_MAXAGE, 2592000));

        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCipherKey(Base64.decode(prop.getValue(Dict.SECURITY_KEY, "4AvVhmFLUs0KTA3Kprsdag==")));
        rememberMeManager.setCookie(rememberMeCookie);

        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName(prop.getValue(Dict.SHIRO_SESSION_ACTIVE_NAME, "shiro-activeSessionCache"));
        sessionDAO.setSessionIdGenerator(sessionIdGenerator);
        sessionDAO.setCacheManager(ehCacheManager);


        config.getRealm().setCredentialsMatcher(retryLimitHashedCredentialsMatcher);
        config.getRealm().setCachingEnabled(false);
        config.getRealm().setCacheManager(ehCacheManager);

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        QuartzSessionValidationScheduler sessionValidationScheduler = new QuartzSessionValidationScheduler(sessionManager);
        sessionValidationScheduler.setSessionValidationInterval(prop.getLong(Dict.SHIRO_SESSION_VALIDATIONINTERVAL, 1800000L));

        sessionManager.setGlobalSessionTimeout(prop.getLong(Dict.SHIRO_SESSION_GLOBALSESSIONTIMEOUT, 1800000L));
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionValidationInterval(prop.getLong(Dict.SHIRO_SESSION_VALIDATIONINTERVAL, 360000L));
        sessionManager.setSessionValidationScheduler(sessionValidationScheduler);
        sessionManager.setSessionIdCookieEnabled(prop.getBool(Dict.SHIRO_SESSION_IDCOOKIEENABLED, true));
        sessionManager.setSessionIdCookie(sessionIdCookie);


        sessionValidationScheduler.setSessionManager(sessionManager);

        defaultWebSecurityManager = new DefaultWebSecurityManager(config.getRealm());
        defaultWebSecurityManager.setCacheManager(ehCacheManager);
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager);
        defaultWebSecurityManager.setSessionManager(sessionManager);


        ServletContext servletContext = me.getContext();

        ShiroLoader shiroLoader = new ShiroLoader();

        shiroLoader.initEnvironment(servletContext);
        WebEnvironment environment = (WebEnvironment) servletContext.getAttribute(ShiroLoader.ENVIRONMENT_ATTRIBUTE_KEY);
        ((MutableWebEnvironment) environment).setWebSecurityManager(defaultWebSecurityManager);
        servletContext.setAttribute(ShiroLoader.ENVIRONMENT_ATTRIBUTE_KEY, environment);

        return false;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {


        return false;
    }
}
