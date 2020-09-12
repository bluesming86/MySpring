package com.ming.test;

import com.ming.ioc.BeanDefinition;
import com.ming.ioc.PropertyValue;
import com.ming.ioc.RuntimeBeanReference;
import com.ming.ioc.TypedStringValue;
import com.ming.po.User;
import com.ming.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ming
 * @time 2020/9/8 16:46
 */
public class TestSpringV2 {

    //存储单例bean实例的Map容器
    private Map<String,Object> singletonObjects = new HashMap<String, Object>();

    //存储
    private Map<String,BeanDefinition> beanDefinitions = new HashMap<String, BeanDefinition>();


    @Before
    public void before(){
        //解析xml

    }

    @Test
    public void test2(){
        UserService userService = (UserService) getBean("userService");


        Map<String,Object> params = new HashMap<String, Object>();
        params.put("name", "风骚小妲己");
        List<User> users = userService.queryUsers(params);
        System.out.println(users);
    }

   /* @Test
    public void test(){
        UserService userService = getUserService();

        Map<String,Object> params = new HashMap<String, Object>();
        params.put("name", "风骚小妲己");
        List<User> users = userService.queryUsers(params);
        System.out.println(users);
    }
    public UserService getUserService(){

        UserServiceImpl userService = new UserServiceImpl();
        UserDaoImpl userDao = new UserDaoImpl();
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/ming");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("root");

        userDao.setDataSource(basicDataSource);
        userService.setUserDao(userDao);
        return  userService;
    }
*/
    //通过配置解决扩展型问题
    //用xml配置文件进行对bean的创建
    //1.管理new出来的bean的class信息（要new几个对象，就要配置几个class信息）
    //<bean id="bean的唯一名称" class="要new的对象的完全路径"></bean>
    //2/管理要new出来的bean的属性的依赖关系 （）
        //<property  name="依赖名称" ref="要建立关系的另外一个bean的唯一name"/>
    //3.读取静态的信息，去创建对象
    //BeanDefinition类 -- 》 用来存储<bean>标签中的信息
    //Map<String,BeanDefinition>
    //4.利用反射从BeanDefinition中获取class信息，并创建对象
    public Object getBean(String beanName){
        //1.首先从singletonObjects集合中获取对应的beanName的实例
        Object singletonObject = this.singletonObjects.get(beanName);
        //2.如果有对象，则直接返回
        if (singletonObject != null){
            return singletonObject;
        }
        //3. 如果没有该对象，则获取对应的beanDefinition信息
        BeanDefinition beanDefinition = this.beanDefinitions.get(beanName);
        //4.判断是单例还是多例，如果是单例走单例创建Bean流程
        /*
        //这种方式不好，因此，竟然BeanDefinition 是scope的提供者，那么你直接告诉我你单例，还是多例就好了，我就不要去具体判断
        String scope = beanDefinition.getScope();
        if ("singleton".equals(scope)){

        } else if ("prototype".equals(scope)){

        }*/

        if (beanDefinition.isSingleton()){
            //    单例流程中，需要将创建的单例对象存入singletonObjects中
            singletonObject = doCreateBean(beanDefinition);

            //放入缓存集合
            this.singletonObjects.put(beanName,singletonObject);
        } else if (beanDefinition.isPrototype()){
            //5.如果是多例，走多例的创建Bean流程
            singletonObject = doCreateBean(beanDefinition);
        }

        return singletonObject;
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        //1.Bean的实例化
        Object bean = createBeanByConstructor(beanDefinition);
        //2.Bean的属性填充（依赖注入）
        populateBean(bean, beanDefinition);
        //3.Bean的初始化
        initializeBean(bean, beanDefinition);

        return bean;
    }
    //3.Bean的初始化
    private void initializeBean(Object bean, BeanDefinition beanDefinition) {

        //TODO 需要针对Aware接口标记的类进行特殊处理

        //TODO 可以进行InitializingBean接口处理

        invokeInitMethod(bean, beanDefinition);

    }

    /**
     * 初始化方法调用
     * @param bean
     * @param beanDefinition
     */
    private void invokeInitMethod(Object bean, BeanDefinition beanDefinition) {
        try{
            String initMethod = beanDefinition.getInitMethod();
            if (initMethod == null || "".equals(initMethod)){
                return ;
            }

            Class<?> classType = beanDefinition.getClassType();
            Method method = classType.getDeclaredMethod(initMethod);
            method.setAccessible(true);
            method.invoke(bean);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    //2.Bean的属性填充（依赖注入）
    private void populateBean(Object bean, BeanDefinition beanDefinition) {
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue pv : propertyValues) {
            String name = pv.getName();
            Object value = pv.getValue();//这不是我们需要给Bean设置的Value值

            //需要转换
            Object valueToUse = resoleValue(value);

            setProperty(bean, name , valueToUse);
        }
    }

    private void setProperty(Object bean, String name, Object valueToUse) {
        try{
            //反射处理, 给属性赋值

            Class<?> aClass = bean.getClass();
            Field field = aClass.getDeclaredField(name);
            field.setAccessible(true);//反射的对象在使用时取消java语言访问检查。
            field.set(bean, valueToUse);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private Object resoleValue(Object value) {
        if(value instanceof TypedStringValue){
            TypedStringValue typedStringValue = (TypedStringValue) value;
            Class<?> targetType = typedStringValue.getTargetType();
            String stringValue = typedStringValue.getValue();

            //TODO 可以用 策略模式 来处理这块 if else 判断
            if (targetType == Integer.class){
                return Integer.parseInt(stringValue);
            } if (targetType == String.class){

                return stringValue;
            }
        } else if(value instanceof RuntimeBeanReference){
            RuntimeBeanReference beanReference = (RuntimeBeanReference) value;
            String ref = beanReference.getRef();
            //递归调用  容易造成 循环依赖
            return getBean(ref);
        }
        return null;
    }

    //通过构造器  创建Bean 对象， 其实就是通过反射完成 实例化
    private Object createBeanByConstructor(BeanDefinition beanDefinition) {
        //TODO 静态工厂方法/工厂实例方法

        //构造器方法去创建Bean实例
        try {
            Class<?> classType = beanDefinition.getClassType();
            //选择无参构造器
            Constructor<?> constructor = classType.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
