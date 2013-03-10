package org.example.action;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * @author bastengao
 * @date 13-3-10 下午5:03
 */
public class MyInterceptor implements Interceptor {

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        System.out.println("haha");
        return invocation.invoke();
    }
}
