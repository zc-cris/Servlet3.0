package com.zc.cris.asyncServlet;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/async", asyncSupported = true)
public class AsyncServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("主线程开始：" + Thread.currentThread() + "====>" + System.currentTimeMillis());
        
        // 1. 开启异步支持注解属性
        // 2. 开启异步上下文
        AsyncContext asyncContext = req.startAsync();
        
        // 3. 业务逻辑进行异步处理（主线程交给副线程）--》这里使用lambda 表达式
        asyncContext.start(() -> {
            try {
                System.out.println("副线程开始："+Thread.currentThread()+"====>"+System.currentTimeMillis());
                this.test();
                asyncContext.complete();
                asyncContext.getResponse().getWriter().write("hey, cris");
                System.out.println("副线程结束："+Thread.currentThread()+"====>"+System.currentTimeMillis());
                
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });
        
        System.out.println("主线程结束：" + Thread.currentThread() + "====>" + System.currentTimeMillis());
    }

    public void test() throws InterruptedException {
        System.out.println(Thread.currentThread() + "processing.......");
        Thread.sleep(3000);
    }

}
