package com.silentgo.core.config;

/**
 * Project : silentgo
 * com.silentgo.core.config
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/1.
 */
public class FileUploadConfig  extends AbstractConfig{

    private String uploadPath;

    private Integer maxSize;

    private boolean autoSave;

    public FileUploadConfig() {
    }

    public FileUploadConfig(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public FileUploadConfig(String uploadPath, Integer maxSize, boolean autoSave) {
        this.uploadPath = uploadPath;
        this.maxSize = maxSize;
        this.autoSave = autoSave;
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public String name() {
        return Const.FileUploadConfig;
    }
}
