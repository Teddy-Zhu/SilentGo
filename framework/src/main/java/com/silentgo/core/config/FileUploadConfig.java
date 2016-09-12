package com.silentgo.core.config;

/**
 * Project : silentgo
 * com.silentgo.core.config
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/1.
 */
public class FileUploadConfig {

    private String uploadPath;

    private Integer maxSize;

    private int sizeThreshold;

    private boolean autoSave;

    public FileUploadConfig() {
    }

    public FileUploadConfig(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public FileUploadConfig(String uploadPath, Integer maxSize, int sizeThreshold, boolean autoSave) {
        this.uploadPath = uploadPath;
        this.maxSize = maxSize;
        this.sizeThreshold = sizeThreshold;
        this.autoSave = autoSave;
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public int getSizeThreshold() {
        return sizeThreshold;
    }

    public void setSizeThreshold(int sizeThreshold) {
        this.sizeThreshold = sizeThreshold;
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
}
