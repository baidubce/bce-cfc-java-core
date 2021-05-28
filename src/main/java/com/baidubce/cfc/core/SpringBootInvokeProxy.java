package com.baidubce.cfc.core;

import com.fasterxml.jackson.databind.ObjectMapper;

class SpringBootInvokeProxy extends InvokeProxy {
    SpringBootInvokeProxy(Class hdrcls) throws Exception {
        super(hdrcls);
        handlerMethod = hdrcls.getMethod("handler", APIGatewayProxyRequestEvent.class);
    }

    @Override
    public String invoke(String input, CfcContext context) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        APIGatewayProxyRequestEvent request;
        String result = null;
        try {
            request = mapper.readValue(input.getBytes("UTF-8"), APIGatewayProxyRequestEvent.class);
            result = (String) handlerMethod.invoke(handlerObject, request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
