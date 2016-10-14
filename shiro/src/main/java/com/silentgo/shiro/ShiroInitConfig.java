package com.silentgo.shiro;

import com.silentgo.core.config.Config;
import com.silentgo.core.config.SilentGoConfig;
import org.apache.shiro.realm.AuthorizingRealm;

/**
 * Project : SilentGo
 * Package : com.silentgo.shiro
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/14.
 */
public class ShiroInitConfig implements Config {

    private AuthorizingRealm realm;

    public ShiroInitConfig(AuthorizingRealm realm) {
        this.realm = realm;
    }

    @Override
    public void initialBuild(SilentGoConfig config) {

        //enable shiro
        config.addExtraFactory(ShiroFactory.class);
        config.addExtraAction(new ShiroAction());

        config.addAbstractConfig(new ShiroConfig(realm));

        //initial interceptor
        config.addExtraAnInterceptor(RequiresAuthenticationAnnotationResolver.class);
        config.addExtraAnInterceptor(RequiresRolesAnnotationResolver.class);
        config.addExtraAnInterceptor(RequiresGuestAnnotationResolver.class);
        config.addExtraAnInterceptor(RequiresUserAnnotationResolver.class);
        config.addExtraAnInterceptor(RequiresPermissionsAnnotationResolver.class);

    }

    @Override
    public void afterInit(SilentGoConfig config) {

    }
}
