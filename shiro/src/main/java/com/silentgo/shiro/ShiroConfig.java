package com.silentgo.shiro;

import com.silentgo.core.config.AbstractConfig;
import org.apache.shiro.realm.AuthorizingRealm;

/**
 * Project : SilentGo
 * Package : com.silentgo.shiro
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/10.
 */
public class ShiroConfig extends AbstractConfig {
    @Override
    public String name() {
        return ShiroFactory.Name;
    }
    private AuthorizingRealm realm;

    public ShiroConfig(AuthorizingRealm realm) {
        this.realm = realm;
    }

    public AuthorizingRealm getRealm() {
        return realm;
    }

    public void setRealm(AuthorizingRealm realm) {
        this.realm = realm;
    }
}
