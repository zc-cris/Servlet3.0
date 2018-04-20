package com.zc.cris.servlet;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.annotation.HandlesTypes;

import com.zc.cris.service.HelloService;

//容器启动的时候会将@HandlesTypes指定的这个类型下面的子类（实现类，子接口等）传递过来；
//传入感兴趣的类型；
@HandlesTypes(value = {HelloService.class})
public class MyServletContainerInitializer implements ServletContainerInitializer{

    
    /**
     * 应用启动的时候，会运行onStartup方法；
     * 
     * Set<Class<?>> arg0：感兴趣的类型的所有子类型；
     * ServletContext arg1:代表当前Web应用的ServletContext；一个Web应用一个ServletContext；
     * 
     * 1）、使用ServletContext注册Web组件（Servlet、Filter、Listener）
     * 2）、使用编码的方式，在项目启动的时候给ServletContext里面添加组件；
     *      必须在项目启动的时候来添加；
     *      1）、ServletContainerInitializer得到的ServletContext；
     *      2）、ServletContextListener得到的ServletContext；
     */
    @Override
    public void onStartup(Set<Class<?>> arg0, ServletContext context) throws ServletException {
        /*
         * 注入的class：class com.zc.cris.service.HelloServiceImpl 
         * 注入的class：class com.zc.cris.service.AbsHelloService 
         * 注入的class：interface com.zc.cris.service.HelloServiceExtension
         */
        for (Class<?> class1 : arg0) {
            System.out.println("注入的class："+class1); 
            
        }
        
        // 注册servlet ServletRegistration
        ServletRegistration.Dynamic userServlet = context.addServlet("userServlet", new UserServlet());
        // 配置servlet的映射信息
        userServlet.addMapping("/user");
        
        // 注册listener
        context.addListener(UserListener.class);
        
        // 注册filter FilterRegistration
        FilterRegistration.Dynamic userFilter = context.addFilter("userFilter", UserFilter.class);
        // 配置filter 的映射信息
        userFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        
        
    }
    
    
}
