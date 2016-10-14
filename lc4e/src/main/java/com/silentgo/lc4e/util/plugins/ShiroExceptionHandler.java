package com.silentgo.lc4e.util.plugins;

import com.silentgo.core.SilentGo;
import com.silentgo.core.exception.annotaion.ExceptionHandler;
import com.silentgo.core.exception.support.IExceptionHandler;
import com.silentgo.core.render.RenderModel;
import com.silentgo.core.render.support.JspRender;
import com.silentgo.lc4e.entity.Message;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;

/**
 * Created by teddy on 2015/7/19.
 */
@ExceptionHandler({AuthenticationException.class, UnauthenticatedException.class})
public class ShiroExceptionHandler implements IExceptionHandler {

    private static final String view = SilentGo.getInstance().getConfig().getBaseViewPath();

    @Override
    public RenderModel resolve(Response response, Request request, Throwable ex) {
        request.setAttribute("message", new Message(ex.getMessage()));
        return new RenderModel(new JspRender(view), "/pages/exception", request, response);
    }
}
