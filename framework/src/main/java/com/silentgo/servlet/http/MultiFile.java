package com.silentgo.servlet.http;

import java.io.File;
import java.io.InputStream;

/**
 * Project : silentgo
 * com.silentgo.servlet.http
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/1.
 */
public class MultiFile {

    String formName;

    String fileName;

    String tmpName;

    String ext;

    String contentType;

    File file;

    public String getContentType() {
        return contentType;
    }

    public String getFormName() {
        return formName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getExt() {
        return ext;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTmpName() {
        return tmpName;
    }

    public void setTmpName(String tmpName) {
        this.tmpName = tmpName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
