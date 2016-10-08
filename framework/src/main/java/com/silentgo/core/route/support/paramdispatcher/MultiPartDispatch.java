package com.silentgo.core.route.support.paramdispatcher;

import com.silentgo.core.SilentGo;
import com.silentgo.core.action.ActionParam;
import com.silentgo.core.config.Const;
import com.silentgo.core.config.FileUploadConfig;
import com.silentgo.core.exception.AppParameterPaserException;
import com.silentgo.core.exception.AppParameterResolverException;
import com.silentgo.core.route.ParameterDispatcher;
import com.silentgo.core.route.Route;
import com.silentgo.core.route.annotation.ParamDispatcher;
import com.silentgo.core.route.support.paramvalueresolve.ParameterResolveFactory;
import com.silentgo.servlet.http.MultiFile;
import com.silentgo.servlet.http.MultiPartRequest;
import com.silentgo.servlet.http.Request;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private static final String FILE_CLEANING_TRACKER_ATTRIBUTE
            = MultiPartDispatch.class.getName() + ".FileCleaningTracker";

    @Override
    public void release(ActionParam param) {
        if (param.getRequest() instanceof MultiPartRequest) {
            ((MultiPartRequest) param.getRequest()).delete();
        }
    }

    private FileCleaningTracker getTracker(SilentGo me) {
        return (FileCleaningTracker) me.getContext().getAttribute(FILE_CLEANING_TRACKER_ATTRIBUTE);
    }

    @Override
    public Integer priority() {
        return 20;
    }

    @Override
    public void dispatch(ParameterResolveFactory parameterResolveFactory, ActionParam param, Route route, Object[] args) throws AppParameterResolverException, AppParameterPaserException {
        Request request = param.getRequest();

        ServletFileUpload servletFileUpload = new ServletFileUpload();
        if (!ServletFileUpload.isMultipartContent(request)) return;

        FileUploadConfig config = (FileUploadConfig) SilentGo.getInstance().getConfig().getConfig(Const.FileUploadConfig);


        DiskFileItemFactory factory = new DiskFileItemFactory();

        factory.setRepository(new File(config.getUploadPath()));
        factory.setSizeThreshold(config.getSizeThreshold());
        factory.setFileCleaningTracker(getTracker(SilentGo.getInstance()));

        servletFileUpload.setSizeMax(config.getMaxSize());
        servletFileUpload.setFileItemFactory(factory);
        servletFileUpload.setHeaderEncoding(SilentGo.getInstance().getConfig().getEncoding());
        try {
            List<FileItem> items = servletFileUpload.parseRequest(request);

            List<MultiFile> files = new ArrayList<>();
            items.stream().filter(item -> !item.isFormField()).forEach(item -> {
                String fileName = item.getName();
                String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
                String name = item.getFieldName();
                String contentType = item.getContentType();
                long size = item.getSize();
                File file = null;
                InputStream inputStream = null;
                try {
                    if (config.isAutoSave()) {
                        file = new File(config.getUploadPath() + "/Saved/" + name + new Date().getTime() + "." + ext);
                        item.write(file);
                    } else {
                        inputStream = item.getInputStream();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                files.add(new MultiFile(name, fileName, contentType, ext, size, inputStream, file));
            });
            if (files.size() > 0) {
                param.setRequest(new MultiPartRequest(request, files));
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }

        return;
    }

}
