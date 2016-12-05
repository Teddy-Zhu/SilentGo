package com.silentgo.orm.source.druid;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.silentgo.orm.base.DBConfig;
import com.silentgo.orm.base.DBConnect;
import com.silentgo.orm.base.DBManager;
import com.silentgo.orm.base.DBPool;
import com.silentgo.utils.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.source.druid
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/12/5.
 */
public class DruidManager implements DBManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DruidManager.class);

    // 配置获取连接等待超时的时间
    private long maxWait = DruidDataSource.DEFAULT_MAX_WAIT;

    // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    private long timeBetweenEvictionRunsMillis = DruidDataSource.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
    // 配置连接在池中最小生存的时间
    private long minEvictableIdleTimeMillis = DruidDataSource.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
    // 配置发生错误时多久重连
    private long timeBetweenConnectErrorMillis = DruidDataSource.DEFAULT_TIME_BETWEEN_CONNECT_ERROR_MILLIS;


    private String validationQuery = "select 1";
    private boolean testWhileIdle = true;
    private boolean testOnBorrow = false;
    private boolean testOnReturn = false;

    private boolean removeAbandoned = false;
    private long removeAbandonedTimeoutMillis = 300 * 1000;

    private boolean logAbandoned = false;

    private int maxPoolPreparedStatementPerConnectionSize = -1;

    // 配置监控统计拦截的filters
    private String filters;    // 监控统计："stat"    防SQL注入："wall"     组合使用： "stat,wall"
    private List<Filter> filterList;

    public Map<String, DruidPool> poolHashMap;

    @Override
    public void initial(DBConfig... configs) {
        poolHashMap = new ConcurrentHashMap<>();
        for (DBConfig config : configs) {
            config.getCallBack().before(this);
            DruidDataSource ds = new DruidDataSource();
            ds.setUrl(config.getUrl());
            ds.setUsername(config.getUserName());
            ds.setPassword(config.getPassword());
            ds.setDriverClassName(config.getDriver());
            ds.setInitialSize(config.getMinActive());
            ds.setMinIdle(config.getMinIdle());
            ds.setMaxActive(config.getMaxActive());
            ds.setMaxWait(maxWait);
            ds.setTimeBetweenConnectErrorMillis(timeBetweenConnectErrorMillis);
            ds.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            ds.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

            ds.setValidationQuery(validationQuery);
            ds.setTestWhileIdle(testWhileIdle);
            ds.setTestOnBorrow(testOnBorrow);
            ds.setTestOnReturn(testOnReturn);

            ds.setRemoveAbandoned(removeAbandoned);
            ds.setRemoveAbandonedTimeoutMillis(removeAbandonedTimeoutMillis);
            ds.setLogAbandoned(logAbandoned);

            ds.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);

            if (StringKit.isNotBlank(filters))
                try {
                    ds.setFilters(filters);
                } catch (SQLException e) {
                    LOGGER.error("druid set filters error", e);
                    throw new RuntimeException(e);
                }

            if (filterList != null) {
                List<Filter> targetList = ds.getProxyFilters();
                for (Filter add : filterList) {
                    if (targetList.stream().anyMatch(filter -> filter.getClass().equals(add.getClass()))) {
                        continue;
                    }
                    targetList.add(add);
                }
            }
            DruidPool pool = new DruidPool(ds);
            poolHashMap.put(config.getName(), pool);
            config.getCallBack().after(this);
        }
    }

    @Override
    public DBPool getPool(String name) {
        return poolHashMap.get(name);
    }

    @Override
    public DBConnect getConnect(String name) {
        return getPool(name).getDBConnect();
    }

    @Override
    public DBConnect getUnSafeConnect(String name) {
        return getPool(name).getUnSafeDBConnect();
    }

    @Override
    public boolean releaseUnSafeConnect(String name, DBConnect connect) {
        return getPool(name).releaseUnSafeDBConnect(connect);
    }

    @Override
    public boolean releaseConnect(String name, DBConnect connect) {
        return getPool(name).releaseDBConnect(connect);
    }

    @Override
    public DBConnect getThreadConnect(String name) {
        return getPool(name).getThreadConnect();
    }

    @Override
    public boolean setThreadConnect(String name, DBConnect connect) {
        return getPool(name).setThreadConnect(connect);
    }

    @Override
    public boolean destory() {
        poolHashMap.forEach((k, v) -> v.destory());
        return true;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public long getTimeBetweenConnectErrorMillis() {
        return timeBetweenConnectErrorMillis;
    }

    public void setTimeBetweenConnectErrorMillis(long timeBetweenConnectErrorMillis) {
        this.timeBetweenConnectErrorMillis = timeBetweenConnectErrorMillis;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isRemoveAbandoned() {
        return removeAbandoned;
    }

    public void setRemoveAbandoned(boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    public long getRemoveAbandonedTimeoutMillis() {
        return removeAbandonedTimeoutMillis;
    }

    public void setRemoveAbandonedTimeoutMillis(long removeAbandonedTimeoutMillis) {
        this.removeAbandonedTimeoutMillis = removeAbandonedTimeoutMillis;
    }

    public boolean isLogAbandoned() {
        return logAbandoned;
    }

    public void setLogAbandoned(boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }

    public int getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }

    public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public List<Filter> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<Filter> filterList) {
        this.filterList = filterList;
    }
}
