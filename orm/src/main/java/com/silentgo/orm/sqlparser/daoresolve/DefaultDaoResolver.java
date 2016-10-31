package com.silentgo.orm.sqlparser.daoresolve;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/28.
 */
public class DefaultDaoResolver {

    private List<DaoResolver> resolverList = new ArrayList<>();

    public DefaultDaoResolver() {
        resolverList = new ArrayList<>();
        resolverList.add(new CommonDaoResolver());
        resolverList.add(new DeleteDaoResolver());
        resolverList.add(new QueryDaoResolver());
        resolverList.add(new GroupDaoResovler());
        resolverList.add(new UpdateDaoResolver());
        resolverList.add(new SelectDaoResolver());
        resolverList.add(new CountDaoResolver());
        resolverList.add(new SetDaoResolver());
        resolverList.add(new WhereDaoResolver());
        resolverList.add(new OrderDaoResovler());
        resolverList.add(new LimitDaoResovler());
    }

    public List<DaoResolver> getResolverList() {
        return resolverList;
    }

    public void setResolverList(List<DaoResolver> resolverList) {
        this.resolverList = resolverList;
    }
}
