package com.silentgo.orm.generate;

/**
 * Project : parent
 * Package : com.silentgo.orm.generate
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/27.
 */
public class TableColumn {

    public String name;
    public String colName;
    public String typeString;
    public boolean nullAble;
    public boolean isAutoincrement;
    public String description;
    public String typeName;
    public boolean hasDefault;

    public boolean isHasDefault() {
        return hasDefault;
    }

    public boolean isAutoincrement() {
        return isAutoincrement;
    }

    public void setAutoincrement(boolean autoincrement) {
        isAutoincrement = autoincrement;
    }

    public void setHasDefault(boolean hasDefault) {
        this.hasDefault = hasDefault;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        try {
            this.typeName = Class.forName(typeString).getSimpleName();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.typeString = typeString;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNullAble() {
        return nullAble;
    }

    public void setNullAble(boolean nullAble) {
        this.nullAble = nullAble;
    }
}
