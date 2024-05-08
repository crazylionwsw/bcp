package com.fuze.bcp.mqreceiver.service;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.fuze.bcp.api.mq.bean.DynamicDescribeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lfq on 2017/6/18.
 */
@Service
public class SpringDynamicService implements ApplicationContextAware {

    // 这个是给不能使用项目ApplicationContext的地方准备的
    public static ApplicationContext applicationContext;

    static Logger logger = LoggerFactory.getLogger(SpringDynamicService.class);

    public static Class<?> loadJarToClass(String jarPath, String className, URLClassLoader classLoader) throws NoSuchMethodException, MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, FileNotFoundException {
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        boolean accessible = method.isAccessible();
        try {
            if (accessible == false) {
                method.setAccessible(true);     // 设置方法的访问权限
            }
            URL url = new java.net.URL(jarPath);
            method.invoke(classLoader, url);
            logger.info("读取jar文件[name={" + jarPath + "}]");
            Class<?> clazz = classLoader.loadClass(className);
            return clazz;
        } catch (Exception e) {
            logger.error("ClassNotFoundException", e);
            throw e;
        } finally {
            method.setAccessible(accessible);
        }
    }

    public static Map<String, Object> loadJarToObject(String jarPath, String className, String beanId, URLClassLoader classLoader) throws Exception {
        Map<String, Object> dynamicBean = new HashMap<>();
        Class clazz = null;
        try {
            clazz = loadJarToClass(jarPath, className, classLoader);
            Object action = clazz.newInstance();
            dynamicBean.put("beanId", beanId);
            dynamicBean.put("action", action);
            return dynamicBean;
        } catch (Exception e) {
            logger.error("加载class失败:" + jarPath + ":" + className, e);
            throw e;
        }

    }

    public static Map<String, Object> install(DynamicDescribeBean dynamicDescribeBean, ApplicationContext applicationContext) throws Exception {
        List<DynamicDescribeBean> resources = dynamicDescribeBean.getNeededResource();
        if (resources != null && resources.size() > 0) { // 外层
            for (int i = 0; i < resources.size(); i++) {
                install(resources.get(i), applicationContext);
            }
        }
        Map<String, Object> f = SpringDynamicService.loadJarToObject(dynamicDescribeBean.getJarPath(), dynamicDescribeBean.getClassName()
                , dynamicDescribeBean.getBeanId(), (URLClassLoader) ClassLoader.getSystemClassLoader());
        List<String> list = new ArrayList<>();
        for (DynamicDescribeBean s : resources) {
            list.add(s.getBeanId());
        }
        if (!f.isEmpty()) {
            SpringDynamicService.loadBean(f, list, applicationContext);
        }
        return f;
    }

    public static void unInstall(DynamicDescribeBean dynamicDescribeBean, ApplicationContext applicationContext) {
        List<DynamicDescribeBean> resources = dynamicDescribeBean.getNeededResource();
        if (resources != null && resources.size() > 0) { // 外层
            for (int i = 0; i < resources.size(); i++) {
                unInstall(resources.get(i), applicationContext);
            }
        }
        SpringDynamicService.unLoadBean(dynamicDescribeBean.getBeanId(), applicationContext);
    }

    public static synchronized void loadBean(Map<String, Object> dynamicBean, List<String> properties, ApplicationContext applicationContext) {
        Logger log = LoggerFactory.getLogger(SpringDynamicService.class);
        log.info("开始装载 bean : " + dynamicBean.get("beanId"));
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        if (defaultListableBeanFactory.containsBean(dynamicBean.get("beanId").toString())) {
            log.info("已存在 bean : " + dynamicBean.get("beanId") + "取消装载操作");
        } else {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(dynamicBean.get("action").getClass());
            for (int i = 0; i < properties.size(); i++) {
                beanDefinitionBuilder.addPropertyValue(properties.get(i), applicationContext.getBean(properties.get(i)));
            }
            defaultListableBeanFactory.registerBeanDefinition(dynamicBean.get("beanId").toString(), beanDefinitionBuilder.getBeanDefinition());
            log.info("bean : " + dynamicBean.get("beanId") + "装载成功");
        }
    }

    public static synchronized void unLoadBean(String beanName, ApplicationContext applicationContext) {
        Logger log = LoggerFactory.getLogger(SpringDynamicService.class);
        log.info("开始卸载 bean : " + beanName);
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        if (defaultListableBeanFactory.containsBean(beanName) && defaultListableBeanFactory.containsBeanDefinition(beanName)) {
            defaultListableBeanFactory.removeBeanDefinition(beanName);
            defaultListableBeanFactory.destroySingleton(beanName);
            defaultListableBeanFactory.destroyBean(beanName);
            log.info("bean : " + beanName + "已卸载");
        } else {
            log.info("未发现 bean : " + beanName + "取消卸载操作");
        }
    }

    public static void doMethod(String beanName, String methodName, Class[] classes, Object[] objects, ApplicationContext applicationContext) throws Exception {
        if (classes != null && objects != null && classes.length != objects.length) {
            throw new Exception("ERROR:反射加载方法参数匹配错误");
        }
        Object object = applicationContext.getBean(beanName);
        Class clazz = object.getClass();
        Method m = clazz.getDeclaredMethod(methodName, classes);
        Object o = m.invoke(object, objects);
    }

    public static void doDubboMethod(String className, String methodName, Class[] classes, Object[] objects, String address, String protocol) throws Exception {
        // 创建一个容器
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("fuze-bcp-server-provider");
        applicationConfig.setOwner("developer");
        applicationConfig.setOrganization("fuze");
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(address);
        registryConfig.setProtocol(protocol);
        // 创建一个dubbo 服务的引用
        ReferenceBean referenceBean = new ReferenceBean();
        referenceBean.setApplication(applicationConfig);
        referenceBean.setInterface(className);
        referenceBean.setRegistry(registryConfig);
        referenceBean.setTimeout(15000);
        referenceBean.afterPropertiesSet();
        Object object = referenceBean.get();
        // 执行methodName方法
        Method m = object.getClass().getDeclaredMethod(methodName, classes);
        Object o = m.invoke(object, objects);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
