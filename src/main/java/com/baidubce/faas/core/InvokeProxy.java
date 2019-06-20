package com.baidubce.faas.core;

import java.lang.reflect.Method;

abstract class InvokeProxy {
    String handlerName;
    Class handlerClass;
    Method handlerMethod;
    Object handlerObject;

    InvokeProxy(Class hdrcls) {
        handlerClass = hdrcls;
    }

    public static InvokeProxy create(String handler) throws Exception {
        Class handlerClass = Class.forName(handler);
        Class<?>[] interfaces = handlerClass.getInterfaces();

        InvokeProxy proxy = null;
        for (Class<?> cls : interfaces) {
            if (cls.getName().equals(StreamHandler.class.getName())) {
                proxy = new StreamInvokeProxy(handlerClass);
                break;
            } else if (cls.getName().equals(RequestHandler.class.getName())) {
                cls.getGenericInterfaces();
                proxy = new RequestInvokeProxy(handlerClass);
                break;
            }
        }
        if (proxy == null) {
            throw new Exception(String.format("%s should implement StreamHandler or RequestHandler"));
        }
        proxy.newInstance();
        proxy.setHandlerName(handler);
        return proxy;
    }

    private void newInstance() throws Exception {
        handlerObject = handlerClass.newInstance();
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public abstract String invoke(String input, FaasContext context) throws Exception;
}
