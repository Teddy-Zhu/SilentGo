package com.silentgo.orm.base;

import java.sql.Connection;

/**
 * Project : silentgo
 * com.silentgo.orm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/13.
 */
public class DBConfig {

    private ManagerInitialCallBack callBack;
    private String name;
    private String driver;
    private String url;
    private String userName;
    private String password;

    private int defaultTranscationLevel = Connection.TRANSACTION_REPEATABLE_READ;
    /**
     * connect timeout time (s)
     */
    private int statementTimeout;
    private int maxActive;
    private int minIdle;
    /**
     * active connect count when init
     */
    private int minActive;
    /**
     * max alive time (s)
     */
    private int maxIdle;

    private int timeOut;

    public DBConfig() {
    }

    public DBConfig(String name, String driver, String url, String userName, String password, int defaultTranscationLevel, int statementTimeout, int maxActive, int minIdle, int minActive, int maxIdle, int timeOut) {
        this.name = name;
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.defaultTranscationLevel = defaultTranscationLevel;
        this.statementTimeout = statementTimeout;
        this.maxActive = maxActive;
        this.minIdle = minIdle;
        this.minActive = minActive;
        this.maxIdle = maxIdle;
        this.timeOut = timeOut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatementTimeout() {
        return statementTimeout;
    }

    public void setStatementTimeout(int statementTimeout) {
        this.statementTimeout = statementTimeout;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMinActive() {
        return minActive;
    }

    public void setMinActive(int minActive) {
        this.minActive = minActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getDefaultTranscationLevel() {
        return defaultTranscationLevel;
    }

    public void setDefaultTranscationLevel(int defaultTranscationLevel) {
        this.defaultTranscationLevel = defaultTranscationLevel;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public ManagerInitialCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(ManagerInitialCallBack callBack) {
        this.callBack = callBack;
    }
}
