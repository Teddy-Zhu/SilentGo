package com.silentgo.core.render.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.config.Const;
import com.silentgo.core.exception.AppException;
import com.silentgo.servlet.http.HttpStatus;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.Response;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Project : silentgo
 * com.silentgo.core.render.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/23.
 */
public class ErrorRener {

    private static String contentType = "text/html; charset=";

    private static String doctitle = "<!DOCTYPE html>";

    private static String html = "<html lang=\"%s\">%s</html>";

    private static String head = "<meta charset=\"%s\"><title>%s</title>";

    private static String style = "<style>body{margin: 0} .errortitle{text-align: center;width: 100%;font-size: 1.5em ; box-shadow: 0 0 5px #ffffff;background: #03A9F4;color: white;border-bottom: 1px solid #2196F3;} .errorcontent{padding: .3em .1em .3em 1em;width: 100% ;background: #545454;color: white;} .footer{text-align: center;width: 100%;padding: .3em .1em .1em 0em;border-top: 1px solid #dadada;} .error{padding: .3em .1em .1em 0em} .simple{margin-top: .5em;margin-bottom: .5em;font-size: 1.1em} .detail .line{margin-bottom: .3em}</style>";
    private static String body = "<body>%s</body>";

    private static String errortitle = "<div class=\"errortitle\"><div class=\"error\">%s</div></div>";

    private static String errordetailcontent = "<div class=\"errorcontent\">%s</div>";

    private static String errordetailSimple = "<div class=\"simple\">Message:%s</div>";

    private static String errordetail = "<div class=\"detail\">%s</div>";

    private static String errordetailline = "<div class=\"line\">%s</div>";

    private static String footer = "<div class=\"footer\">Powered By SilentGo</div>";

    public void render(Request request, Response response, AppException error, boolean isDev) {
        render(request, response, error.getCode(), error, isDev);
    }

    public void render(Request request, Response response, HttpStatus.Code code, Throwable error, boolean isDev) {
        render(request, response, code.getCode(), error, isDev);
    }

    public void render(Request request, Response response, int code, Throwable error, boolean isDev) {

        String encoding = SilentGo.me().getConfig().getEncoding();

        response.setContentType(contentType + encoding);
        response.setStatus(code);


        String errorString = getErrorString(request, encoding, code, error, isDev);

        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(errorString);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }

    private String getTitle(int code) {
        return HttpStatus.getMessage(code);
    }

    private String getErrorString(Request request, String encoding, int code, Throwable error, boolean isDev) {
        boolean isnull = error == null;
        String lines = getThrowableInfo(error, isDev, new int[]{-1});
        String errords = String.format(errordetail, lines);
        String errorsimple = isDev && !isnull ? String.format(errordetailSimple, error.getMessage()) : Const.EmptyString;
        String errorContent = isDev && !isnull ? String.format(errordetailcontent, errorsimple + errords) : Const.EmptyString;

        String title = code + " " + getTitle(code);

        String headHtml = String.format(head, encoding, title) + style;

        String errorTitle = String.format(errortitle, title);

        String bodyHtml = String.format(body, errorTitle + errorContent + footer);

        return String.format(html, request.getLocale(), headHtml + bodyHtml);
    }

    private String getThrowableInfoCause(Throwable error, boolean isDev) {
        if (error == null) return Const.EmptyString;
        return getThrowableInfo(error.getCause(), isDev, new int[]{-1});
    }

    private String getThrowableInfo(Throwable error, boolean isDev, int[] index) {
        if (!isDev || error == null) return Const.EmptyString;
        index[0]++;
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < index[0]; i++) {
            space.append("&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        StringBuilder lineBuffer = new StringBuilder();

        for (StackTraceElement stackTraceElement : error.getStackTrace()) {
            lineBuffer.append(getStackTraceElement(stackTraceElement, space.toString()));
        }

        Throwable[] superesseds = error.getSuppressed();
        for (Throwable superessed : superesseds) {
            lineBuffer.append(getThrowableInfo(superessed, isDev, index));
        }

        Throwable caused = error.getCause();
        lineBuffer.append(getThrowableInfo(caused, isDev, index));

        return lineBuffer.toString();
    }

    private String getStackTraceElement(StackTraceElement stack, String space) {
        String trace = stack.toString();
        if (ignores.stream().anyMatch(ignore -> trace.contains(ignore))) {
            return Const.EmptyString;
        }

        return String.format(errordetailline, space + trace);
    }

    private ArrayList<String> ignores = new ArrayList<String>() {{
        add("$$FastClassByCGLIB$$");
        add("cglib.reflect.FastMethod");
    }};
}
