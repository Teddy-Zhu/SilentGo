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

    String ext;

    String conetentType;

    long size;

    InputStream inputStream;

    File file;


    public MultiFile(String formName, String fileName, String ext, String conetentType, long size, InputStream inputStream, File file) {
        this.formName = formName;
        this.fileName = fileName;
        this.ext = ext;
        this.inputStream = inputStream;
        this.size = size;
        this.file = file;
        this.conetentType = conetentType;
    }

    public String getConetentType() {
        return conetentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public File getFile() {
        return file;
    }

    public long getSize() {
        return size;
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
}
