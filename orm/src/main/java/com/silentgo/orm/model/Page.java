package com.silentgo.orm.model;

import com.silentgo.orm.base.Pager;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 2016/10/10.
 */
public class Page<T> {

    private List<T> result = new ArrayList<T>();

    private Pager pager;

    private int totalCount = 0;
    private int totalPage = 0;

    public Page() {
        this.pager = new Pager();
    }

    public Page(int pageNumber, int pageSize) {
        this.pager = new Pager(pageNumber, pageSize);
    }

    public Page(List<T> result, int pageNumber, int pageSize, int totalCount, int totalPage) {
        this.result = result;
        this.pager = new Pager(pageNumber, pageSize);
        this.totalCount = totalCount;
        this.totalPage = totalPage;
    }

    public Page(List<T> result, int pageNumber, int pageSize, int totalCount) {
        this.result = result;
        this.pager = new Pager(pageNumber, pageSize);
        this.totalCount = totalCount;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getPageNumber() {
        return pager.getPageNum();
    }

    public void setPageNumber(int pageNumber) {
        this.pager.setPageNum(pageNumber);
    }

    public int getPageSize() {
        return this.pager.getPageSize();
    }

    public void setPageSize(int pageSize) {
        this.pager.setPageSize(pageSize);
    }

    public int getTotalPage() {
        return (int) Math.ceil((double) totalCount / this.pager.getPageSize());
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getStart() {
        return this.pager.getStart();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Pager getPager() {
        return pager;
    }
}
