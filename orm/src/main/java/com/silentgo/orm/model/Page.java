package com.silentgo.orm.model;

import java.util.ArrayList;
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

    private List<T> result = new ArrayList<T>();
    private int pageNumber = 1;
    private int pageSize = 20;
    private int totalCount = 0;
    private int totalPage = 0;

    public Page() {
    }

    public Page(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public Page(List<T> result, int pageNumber, int pageSize, int totalCount, int totalPage) {
        this.result = result;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
    }

    public Page(List<T> result, int pageNumber, int pageSize, int totalCount) {
        this.result = result;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
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
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getStart() {
        return (pageNumber - 1) * pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
