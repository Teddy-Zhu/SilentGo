package com.silentgo.core.ioc.bean.support;

import com.silentgo.core.SilentGo;
import com.silentgo.core.db.BaseDao;
import com.silentgo.core.db.BaseTableInfo;
import com.silentgo.core.db.DaoFactory;
import com.silentgo.core.db.TableModel;
import com.silentgo.core.ioc.bean.BeanDefinition;
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

        Class<? extends TableModel> tableClass = (Class<? extends TableModel>) ClassKit.getGenericClass(clz)[0];

        DaoFactory daoFactory = SilentGo.getInstance().getFactory(DaoFactory.class);

        BaseTableInfo baseTableInfo = daoFactory.getTableInfoMap().get(tableClass);
        if (baseTableInfo != null) {
            daoFactory.getClassTableInfoMap().put((Class<? extends BaseDao>) clz, baseTableInfo);
            daoFactory.getReflectMap().put((Class<? extends BaseDao>) clz, tableClass);
        }

        BeanBuildKit.buildBaseDaoInterface(beanDefinitions, clz);

        for (Method method : clz.getDeclaredMethods()) {
            Annotation[] ans = method.getAnnotations();
            daoFactory.getMethodListMap().put(method, Arrays.asList(ans));
        }

    }
}
