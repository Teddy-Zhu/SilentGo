# SilentGo 框架

> 轻量级的Java web 框架 

## 功能简介

- 高效路由
    > 静态路由
    > 动态路由,支持正则
    > Restful
    > 动态参数解析(url参数,request参数等)
    
- 简单易用的Orm (结合SilentGo Orm)
    > model,dao 生成工具
    > 支持手写sql
    > 简易语句免代码(根据规则书写方法名,配合注解,动态生成sql)
- IOC
    > 自动注入
- AOP
    > 支持全局拦截器,类拦截器,方法拦截器,参数拦截器
    > 动态拦截器(正则匹配,包路径匹配)
    > 自定义注解拦截器
- Filter
    > 可配置的filter责任链
- 静态文件
    > 文件地址 rewrite
- 插件
    > 工厂模式的插件机制
    > 事件驱动插件,缓存插件等
- 可拓展性
    > 强大的拓展性,可以定制具体功能,  
      例如 路由解析,参数解析,路由映射规则,类型转换规则等