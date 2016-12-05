package com.silentgo.core.db;

import com.silentgo.core.SilentGo;
import com.silentgo.core.build.Factory;
import com.silentgo.core.exception.AppBuildException;
import com.silentgo.core.exception.AppReleaseException;
import com.silentgo.core.support.BaseFactory;
import com.silentgo.orm.base.*;
import com.silentgo.orm.base.annotation.Table;
import com.silentgo.orm.infobuilder.BaseTableBuilder;
import com.silentgo.orm.kit.BaseTableInfoKit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;

/**
 * Project : parent
 * Package : com.silentgo.core.db
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/10/2.
 */
@Factory
public class DaoFactory extends BaseFactory {

    @Override
    public boolean initialize(SilentGo me) throws AppBuildException {

        me.getFactory(SqlFactory.class);

        DBType type = DBType.parse(me.getConfig().getDbType());

        if (type == null) return false;

        //build table info
        me.getAnnotationManager().getClasses(Table.class).forEach(tableclass -> BaseTableBuilder.me().initialBaseModel(type.getName(), tableclass, type));


        return true;
    }

    @Override
    public boolean destroy(SilentGo me) throws AppReleaseException {
        return true;
    }

}
