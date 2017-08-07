package com.silentgo.orm.sqlparser.daoresolve;

public class ResolvedParam {

    private String name;
    private int index;

    public ResolvedParam(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
