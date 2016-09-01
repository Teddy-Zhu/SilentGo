package com.silentgo.core.route.support.paramdispatcher;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.ActionParam;
import com.silentgo.core.config.FileUploadConfig;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.exception.AppParameterResolverException;
import com.silentgo.core.exception.AppRouteDispatchException;
import com.silentgo.core.route.ParameterDispatcher;
import com.silentgo.core.route.Route;
import com.silentgo.core.route.annotation.ParamDispatcher;
import com.silentgo.core.route.support.paramvalueresolve.ParameterResolveFactory;
import com.silentgo.kit.StringKit;
import com.silentgo.servlet.http.ContentType;
import com.silentgo.servlet.http.MultiFile;
import com.silentgo.servlet.http.Request;
import com.silentgo.servlet.http.RequestMethod;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Project : silentgo
 * com.silentgo.core.route.support.paramdispatcher
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/1.
 */
@ParamDispatcher
public class MultiPartDispatch implements ParameterDispatcher {

    private static FileCleaningTracker tracker = new FileCleaningTracker();

    @Override
    public void release() {
        tracker.exitWhenFinished();
    }

    @Override
    public Integer priority() {
        return 20;
    }

    @Override
    public void dispatch(ParameterResolveFactory parameterResolveFactory, ActionParam param, Route route, Object[] args) throws AppParameterResolverException, AppParameterPaserException {
        Request request = param.getRequest();

        ServletFileUpload servletFileUpload = new ServletFileUpload();
        FileUploadConfig config = SilentGo.getInstance().getConfig().getFileUploadConfig();
        ServletFileUpload.isMultipartContent(param.getRequest());
        servletFileUpload.setSizeMax(config.getMaxSize());

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(config.getUploadPath()));
        factory.setSizeThreshold(config.getSizeThreshold());
        factory.setFileCleaningTracker(tracker);

        try {
            List<FileItem> items = servletFileUpload.parseRequest(param.getRequest());

            List<MultiFile> files = new ArrayList<>();
            items.stream().filter(item -> !item.isFormField()).forEach(item -> {
                String fileName = item.getName().substring(item.getName().lastIndexOf("\\") + 1);
                String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
                String name = item.getFieldName();
                String contentType = item.getContentType();
                long size = item.getSize();
                File file = null;
                if (config.isAutoSave()) {
                    try {
                        file = new File(config.getUploadPath() + "/" + name + new Date().getTime() + ext);
                        item.write(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    files.add(new MultiFile(name, fileName, contentType, ext, size, item.getInputStream(), file));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } catch (FileUploadException e) {
            e.printStackTrace();
        }

        return;
    }

}
