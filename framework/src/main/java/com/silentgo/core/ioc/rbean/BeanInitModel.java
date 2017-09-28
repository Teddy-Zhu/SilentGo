package com.silentgo.core.ioc.rbean;

public class BeanInitModel {

    Class<?> beanClass;
    boolean isSingle = true;
    boolean createImmediately = true;
    boolean needInject = false;
    Object originObject;
    String beanName;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }

    public boolean isCreateImmediately() {
        return createImmediately;
    }

    public void setCreateImmediately(boolean createImmediately) {
        this.createImmediately = createImmediately;
    }

    public boolean isNeedInject() {
        return needInject;
    }

    public void setNeedInject(boolean needInject) {
        this.needInject = needInject;
    }

    public Object getOriginObject() {
        return originObject;
    }

    public void setOriginObject(Object originObject) {
        this.originObject = originObject;
    }
}
