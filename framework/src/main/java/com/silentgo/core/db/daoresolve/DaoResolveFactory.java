package com.silentgo.core.db.daoresolve;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.support.BaseFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.db.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/2.
 */
@Factory
public class DaoResolveFactory extends BaseFactory {

    private List<DaoResolver> resolverList = new ArrayList<>();

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {
        resolverList.add(new CommonDaoResolver());
        resolverList.add(new DeleteDaoResolver());
        resolverList.add(new QueryDaoResolver());
        resolverList.add(new OrderDaoResovler());
        resolverList.add(new GroupDaoResovler());
        resolverList.add(new LimitDaoResovler());
        resolverList.add(new UpdateDaoResolver());
        resolverList.add(new CountDaoResolver());
        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        resolverList.clear();
        return true;
    }

    public List<DaoResolver> getResolverList() {
        return resolverList;
    }
}
