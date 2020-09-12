package com.ming.ioc;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ming
 * @time 2020/9/8 20:20
 */
public class BeanDefinition {
    //bean标签的class属性
    private String clazzName;

    //bean标签的class属性对应的class对象
    private Class<?> classType;

    //bean标签的id属性和name属性都会存储到该属性值，id和name属性是二选一使用的
    private String beanName;

    //初始话方法
    private String initMethod;
    //该信息是默认的配置，如果不配置就默认是 singleton
    private String scope;


    /**
     * Bean中的属性信息
     */
    private List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();

    /**
     * scope 类型
     */
    private final String SCOPE_SINGLETON = "singleton"; //单例
    private final String SCOPE_PROTOTYPE = "prototype";//多例


    public BeanDefinition(String clazzName, String beanName) {
        this.clazzName = clazzName;
        this.beanName = beanName;
        this.classType = resolveClassName(clazzName);
    }

    private Class<?> resolveClassName(String clazzName) {
        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public Class<?> getClassType() {
        return classType;
    }

    public void setClassType(Class<?> classType) {
        this.classType = classType;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getInitMethod() {
        return initMethod;
    }

    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isSingleton(){
        return SCOPE_SINGLETON.equals(this.scope);
    }

    public boolean isPrototype(){
        return SCOPE_PROTOTYPE.equals(this.scope);
    }

    public List<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(List<PropertyValue> propertyValues) {
        this.propertyValues = propertyValues;
    }

    public void addPropertyValues(PropertyValue propertyValue) {
        this.propertyValues.add(propertyValue);
    }
}
