package com.yl.acoreui.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by 寻水的鱼 on 2018/12/5.
 */
public class ProxyHandle implements InvocationHandler {
    private Object realObj;
    private Method realMethod;

    public ProxyHandle(Object realObj,Method realMethod){
        this.realObj=realObj;
        this.realMethod=realMethod;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果真实对象跟方法都不为null 则直接返回真是方法(这里真实方法必须跟接口方法参数保存一致)
        if (realObj!=null&&realMethod!=null){
            return realMethod.invoke(realObj,args);
        }else {
            return method.invoke(proxy,args);
        }
    }
}

