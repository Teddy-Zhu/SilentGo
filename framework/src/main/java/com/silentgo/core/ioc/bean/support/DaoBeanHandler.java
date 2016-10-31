package com.silentgo.core.ioc.bean.support;

import com.silentgo.core.SilentGo;
import com.silentgo.orm.base.BaseDao;
import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.core.db.DaoFactory;
import com.silentgo.orm.base.TableModel;
import com.silentgo.core.ioc.bean.BeanDefinition;
import com.silentgo.orm.infobuilder.BaseTableBuilder;
import com.silentgo.utils.ClassKit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.core.ioc.bean.support
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/29.
 */
public class DaoBeanHandler implements BeanHandler {
    @Override
    public <T extends Annotation> boolean hasHandle(T t, Class<?> clz) {
        return clz.isInterface() && BaseDao.class.isAssignableFrom(clz);
    }

    @Override
    public <T extends Annotation> void handle(T t, Class<?> clz, List<BeanDefinition> beanDefinitions) {

        SilentGo.me().getFactory(DaoFactory.class);

        Object target = BaseTableBuilder.me().initialDao((Class<? extends BaseDao>) clz);

        BeanBuildKit.buildBaseDaoInterface(beanDefinitions, target, clz);

    }
}
