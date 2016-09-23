package com.silentgo.core.config.support;

import com.silentgo.core.config.Const;
import com.silentgo.core.config.FileUploadConfig;
import com.silentgo.core.config.SilentGoConfig;
import com.silentgo.utils.StringKit;

import java.io.File;

/**
 * Project : silentgo
 * com.silentgo.core.config.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/2.
 */
public class ConfigChecker {

    public static void Check(SilentGoConfig config) throws RuntimeException {
        FileUploadConfig fileUploadConfig = ((FileUploadConfig) config.getConfig(Const.FileUploadConfig));
        String path = fileUploadConfig.getUploadPath();
        if (!StringKit.isBlank(path)) {
            File file = new File(path);
            file.mkdirs();
            if (!file.isDirectory()) {
                throw new RuntimeException("upload file path is invalid");
            }
        }
        if (fileUploadConfig.isAutoSave()) {
            File file = new File(path + "/Saved");
            file.mkdirs();
        }
    }
}
