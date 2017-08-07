package com.silentgo.orm.base;

import com.silentgo.utils.reflect.SGMethod;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.base
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 2017/8/2.
 */
public class DaoResolveEntity {

    boolean isBaseDao;

    Class<?> daoClass;

    Object[] objects;
    Integer objectIndex;
    Integer objectParsedIndex;
    List<String> parsedMethod;
    BaseTableInfo tableInfo;
    SQLTool sqlTool;
    Boolean isHandled;
    BaseDaoDialect daoDialect;
    Map<String, Object> nameObjects;
    SGMethod sgMethod;

    public String getMethodName() {
        return sgMethod.getName();
    }

    public Class<?> getReturnType() {
        return sgMethod.getMethod().getReturnType();
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }

    public Integer getObjectIndex() {
        return objectIndex;
    }

    public void setObjectIndex(Integer objectIndex) {
        this.objectIndex = objectIndex;
    }

    public List<String> getParsedMethod() {
        return parsedMethod;
    }

    public void setParsedMethod(List<String> parsedMethod) {
        this.parsedMethod = parsedMethod;
    }

    public BaseTableInfo getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(BaseTableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public SQLTool getSqlTool() {
        return sqlTool;
    }

    public void setSqlTool(SQLTool sqlTool) {
        this.sqlTool = sqlTool;
    }

    public BaseDaoDialect getDaoDialect() {
        return daoDialect;
    }

    public void setDaoDialect(BaseDaoDialect daoDialect) {
        this.daoDialect = daoDialect;
    }

    public Map<String, Object> getNameObjects() {
        return nameObjects;
    }

    public void setNameObjects(Map<String, Object> nameObjects) {
        this.nameObjects = nameObjects;
    }

    public Method getMethod() {
        return sgMethod.getMethod();
    }

    public Boolean getHandled() {
        return isHandled;
    }

    public void setHandled(Boolean handled) {
        isHandled = handled;
    }


    public DaoResolveEntity resolved() {
        isHandled = true;
        return this;
    }

    public SGMethod getSgMethod() {
        return sgMethod;
    }

    public void setSgMethod(SGMethod sgMethod) {
        this.sgMethod = sgMethod;
    }

    public Class<?> getDaoClass() {
        return daoClass;
    }

    public void setDaoClass(Class<?> daoClass) {
        this.daoClass = daoClass;
    }

    public void increaseParsedObjectIndex() {
        this.objectParsedIndex += 1;
    }

    public Integer getObjectParsedIndex() {
        return objectParsedIndex;
    }

    public void setObjectParsedIndex(Integer objectParsedIndex) {
        this.objectParsedIndex = objectParsedIndex;
    }

    public boolean isBaseDao() {
        return isBaseDao;
    }

    public void setBaseDao(boolean baseDao) {
        isBaseDao = baseDao;
    }

    public void clearSqlTool() {
        if (sqlTool != null) {
            sqlTool.setObjects(null);
            sqlTool.setParams(null);
        }
    }
}
