package com.silentgo.servlet.oreilly.multipart;

import com.silentgo.core.SilentGo;
import com.silentgo.core.config.Const;
import com.silentgo.core.config.FileUploadConfig;

import java.io.File;
import java.util.Calendar;

/**
 * Project : SilentGo
 * Package : com.silentgo.servlet.fileupload.oreilly.servlet
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/12/7.
 */
public class AutoSaveFilePolicy implements FileRenamePolicy {
    @Override
    public File rename(File f) {

        FileUploadConfig config = (FileUploadConfig) SilentGo.me().getConfig().getConfig(Const.FileUploadConfig);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String fileName = f.getName();
        String newFileName = config.getUploadPath() +
                year + month + day +
                File.separator +
                System.currentTimeMillis() +
                fileName.substring(fileName.lastIndexOf('.'), fileName.length());

        File dest = new File(newFileName);

        File dir = dest.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dest;
    }
}
