package com.silentgo.shiro;

import com.silentgo.core.SilentGo;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import java.util.concurrent.atomic.AtomicInteger;


public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        //retry count + 1
        AtomicInteger retryCount = (AtomicInteger) SilentGo.me().getConfig().getCacheManager().get("passwordRetryCache", username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            SilentGo.me().getConfig().getCacheManager().set("passwordRetryCache", username, retryCount);
        }
        if (retryCount.incrementAndGet() > 5) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }

        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            //clear retry count
            SilentGo.me().getConfig().getCacheManager().evict("passwordRetryCache", username);
        }
        return matches;
    }
}
