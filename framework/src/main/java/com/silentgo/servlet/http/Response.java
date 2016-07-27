package com.silentgo.servlet.http;

import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Project : silentgo
 * com.silentgo.servlet.web
 *
 * @author    <Acc href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</Acc>
 * <p>
 * Created by  on 16/7/18.
 */
public class Response extends HttpServletResponseWrapper {
    public Response(javax.servlet.http.HttpServletResponse response) {
        super(response);
    }


}
