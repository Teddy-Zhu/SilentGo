package com.silentgo.shiro;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.ActionChain;
import com.silentgo.core.action.ActionParam;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.subject.WebSubject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Project : SilentGo
 * Package : com.silentgo.shiro
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/10.
 */
public class ShiroAction extends ActionChain {

    private static final Logger logger = LoggerFactory.getLogger(ShiroAction.class);


    @Override
    public void doAction(ActionParam param) throws Throwable {
        Long start = System.currentTimeMillis();

        final Throwable[] t = {null};
        Request request = param.getRequest();
        Response response = param.getResponse();
        final Subject subject = createSubject(request, response);
        subject.execute(() -> {
            updateSessionLastAccessTime(request, response);
            try {
                next(param);
            } catch (Throwable throwable) {
                t[0] = throwable;
            }
        });

        if (t[0] != null) {
            throw t[0];
        }
        logger.debug("end shiro action : {}", System.currentTimeMillis() - start);
    }

    private WebSecurityManager getSecurityManager() {
        return SilentGo.me().getFactory(ShiroFactory.class).getDefaultWebSecurityManager();
    }

    private boolean isHttpSessions() {
        return getSecurityManager().isHttpSessionMode();
    }

    private WebSubject createSubject(ServletRequest request, ServletResponse response) {
        return new WebSubject.Builder(getSecurityManager(), request, response).buildWebSubject();
    }

    private void updateSessionLastAccessTime(ServletRequest request, ServletResponse response) {
        if (!isHttpSessions()) { //'native' sessions
            Subject subject = SecurityUtils.getSubject();
            //Subject should never _ever_ be null, but just in case:
            if (subject != null) {
                Session session = subject.getSession(false);
                if (session != null) {
                    try {
                        session.touch();
                    } catch (Throwable t) {
                        logger.error("session.touch() method invocation has failed.  Unable to update" +
                                "the corresponding session's last access time based on the incoming request.", t);
                    }
                }
            }
        }
    }

}
