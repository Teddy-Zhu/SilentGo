package com.silentgo.orm.model;

import java.util.List;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/10.
 */
public class Page<T> {

    private List<T> result;
    private int pageNumber;
    private int pageSize;
    private int totalPage;

    public Page() {
    }

    public Page(List<T> result, int pageNumber, int pageSize, int totalPage) {
        this.result = result;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

}
