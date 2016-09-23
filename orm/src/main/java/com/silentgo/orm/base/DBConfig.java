package com.silentgo.orm.base;

/**
 * Project : silentgo
 * com.silentgo.orm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/13.
 */
public class DBConfig {

    private String name;
    private String driver;
    private String url;
    private String userName;
    private String password;
    /**
     * connect timeout time (s)
     */
    private int statementTimeout;
    private int maxActive;
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

    public DBConfig(String name, int timeOut, String driver, String url, String userName, String password, int statementTimeout, int maxActive, int minActive, int maxIdle) {
        this.name = name;
        this.timeOut = timeOut;
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.statementTimeout = statementTimeout;
        this.maxActive = maxActive;
        this.minActive = minActive;
        this.maxIdle = maxIdle;
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
}
