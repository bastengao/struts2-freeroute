package org.example.action;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.example.service.MoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author bastengao
 * @date 13-3-10 下午5:03
 */
@Component("myInterceptor2")
@Scope("prototype")
public class My2Interceptor implements Interceptor {
    @Autowired
    private MoneyService moneyService;

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        moneyService.giveMeMoney(Integer.MAX_VALUE);
        System.out.println("hehe");
        return invocation.invoke();
    }
}
