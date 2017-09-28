package com.silentgo.core.ioc.rbean;

import com.silentgo.core.SilentGo;
import com.silentgo.core.config.Const;
import com.silentgo.core.ioc.annotation.Inject;
import com.silentgo.core.ioc.annotation.Lazy;
import com.silentgo.core.ioc.bean.Bean;
import com.silentgo.core.ioc.bean.BeanFactory;
import com.silentgo.core.ioc.exception.BeanException;
import com.silentgo.core.kit.CGLibKit;
import com.silentgo.utils.ReflectKit;
import com.silentgo.utils.StringKit;
import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;
import com.silentgo.utils.reflect.SGClass;

import java.util.ArrayList;
import java.util.List;

public class BeanModel extends Bean {
    private static final Log LOGGER = LogFactory.get();

    private Class<?> beanClass;

    /**
     * bean名称
     */
    private String name;

    private Object singleCachedObject;

    private Object singleCachedInjectObject;

    private boolean isSingle;

    private boolean needInject;

    /**
     * 接口类 如果有 interfaceClass = beanClass
     */
    private Class<?> interfaceClass;

    private List<BeanFiledModel> fields;

    public BeanModel(BeanInitModel beanInitModel) {

        if (beanInitModel.getOriginObject() != null) {
            buildWithObject(beanInitModel);
        } else {
            buildWithClass(beanInitModel);
        }

        initInterfaceClass();

        initField();
    }

    private void initField() {
        this.fields = new ArrayList<>();
        SGClass sgClass = ReflectKit.getSGClass(this.beanClass);

        sgClass.getFieldMap().forEach((name, field) -> {
            Inject inject = field.getAnnotation(Inject.class);
            Lazy lazy = field.getAnnotation(Lazy.class);
            //filter no annotation class
            if (inject != null) {
                if (Const.DEFAULT_NONE.equals(inject.value()))
                    fields.add(new BeanFiledModel(field.getType().getName(), field));
                else
                    fields.add(new BeanFiledModel(inject.value(), field));
            }
        });
    }

    private void buildWithObject(BeanInitModel beanInitModel) {
        this.singleCachedObject = beanInitModel.getOriginObject();
        this.beanClass = beanInitModel.getBeanClass() == null ? this.singleCachedObject.getClass() : beanInitModel.getBeanClass();

        this.name = StringKit.isBlank(beanInitModel.getBeanName()) ? this.beanClass.getName() : beanInitModel.getBeanName();
        this.needInject = beanInitModel.isNeedInject();
        this.isSingle = beanInitModel.isSingle();

        if (beanInitModel.isNeedInject() && beanInitModel.isCreateImmediately()) {
            this.singleCachedInjectObject = CGLibKit.Proxy(this.singleCachedObject);
        }

    }

    private void buildWithClass(BeanInitModel beanInitModel) {
        if (beanInitModel.getBeanClass() == null) {
            throw new BeanException("bean class can not be null");
        }

        this.beanClass = beanInitModel.getBeanClass() == null ? singleCachedObject.getClass() : beanInitModel.getBeanClass();
        this.needInject = beanInitModel.isNeedInject();
        this.name = StringKit.isBlank(beanInitModel.getBeanName()) ? this.beanClass.getName() : beanInitModel.getBeanName();
        this.isSingle = beanInitModel.isSingle();


        if (beanInitModel.isSingle()) {
            if (beanInitModel.isCreateImmediately()) {
                this.singleCachedObject = ReflectKit.init(this.beanClass);
                if (beanInitModel.isNeedInject()) {
                    this.singleCachedInjectObject = CGLibKit.Proxy(this.singleCachedObject);
                }
            }
        } else {
            if (this.beanClass.isInterface())
                throw new BeanException("the bean class is interface, can not be structured");
        }
    }


    private void initInterfaceClass() {
        this.interfaceClass = beanClass.isInterface() ? beanClass : beanClass.getInterfaces().length > 0 ? beanClass.getInterfaces()[0] : beanClass;
    }

    @Override
    public Object getObject() {
        return isSingle ? getSingleBean() : getNewBean();
    }


    private Object getSingleBean() {
        return resolveBeanField(needInject ? this.singleCachedInjectObject : this.singleCachedObject);
    }


    private Object resolveBeanField(Object targetObject) {

        BeanFactory beanFactory = SilentGo.me().getBeanFactory();

        for (BeanFiledModel field : this.fields) {

            if (field.getValue(targetObject) != null) continue;
            Bean bean = beanFactory.getBean(field.getBeanName());
            field.setValue(targetObject, bean.getObject());
        }
        return targetObject;
    }

    private Object getNewBean() {
        Object newTarget = ReflectKit.init(this.beanClass);
        return resolveBeanField(needInject ? CGLibKit.Proxy(newTarget) : newTarget);
    }


    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public String getBeanName() {
        return name;
    }

    @Override
    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    @Override
    public Object getOriginObject() {
        if (isSingle) {
            return singleCachedObject;
        } else {
            if (!beanClass.isInterface()) {
                return ReflectKit.init(this.beanClass);
            }
            return null;
        }
    }


    public List<BeanFiledModel> getFields() {
        return fields;
    }
}
