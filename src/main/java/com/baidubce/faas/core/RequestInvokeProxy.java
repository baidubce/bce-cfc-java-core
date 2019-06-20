package com.baidubce.faas.core;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class RequestInvokeProxy extends InvokeProxy {
    JavaType reqJavaType;
    Type requestType;

    RequestInvokeProxy(Class hdrcls) throws Exception {
        super(hdrcls);
        Type[] interfaces = hdrcls.getGenericInterfaces();
        for (Type t : interfaces) {
            if (t.getTypeName().startsWith(RequestHandler.class.getName())) {
                ParameterizedType parameterizedType = (ParameterizedType)t;
                Type[] typeArgs = parameterizedType.getActualTypeArguments();
                requestType = typeArgs[0];
                if (typeArgs[0] instanceof ParameterizedType) {
                    ParameterizedType arg = (ParameterizedType)(typeArgs[0]);
                    Type[] params = arg.getActualTypeArguments();
                    Class<?>[] clsParams = new Class<?>[params.length];
                    for (int i = 0; i < params.length; i++) {
                        clsParams[i] = (Class<?>)params[i];
                    }
                    reqJavaType = TypeFactory.defaultInstance()
                            .constructParametricType((Class<?>)arg.getRawType(), clsParams);
                    handlerMethod = handlerClass.getMethod("handler",
                            (Class<?>) arg.getRawType(), FaasContext.class);
                } else {
                    reqJavaType = TypeFactory.defaultInstance().constructType(typeArgs[0]);
                    handlerMethod = handlerClass.getMethod("handler",
                            (Class<?>)(typeArgs[0]), FaasContext.class);
                }
                break;
            }
        }
    }

    @Override
    public String invoke(String input, FaasContext context) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.INDENT_OUTPUT);
        Object request = null;
        if (requestType.getTypeName().equals(String.class.getTypeName())) {
            request = input;
        } else if (requestType.getTypeName().equals(Integer.class.getTypeName())) {
            request = Integer.parseInt(input);
        } else if (requestType.getTypeName().equals(Boolean.class.getTypeName())) {
            request = Boolean.parseBoolean(input);
        } else {
            request = mapper.readValue(input, reqJavaType);
        }
        Object response = handlerMethod.invoke(handlerObject, request, context);
        if (response.getClass().equals(String.class) ||
                response.getClass().equals(Integer.class) ||
                response.getClass().equals(Boolean.class)) {
            return response.toString();
        }
        return mapper.writeValueAsString(response);
    }
}
