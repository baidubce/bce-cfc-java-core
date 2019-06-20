package com.baidubce.faas.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

class StreamInvokeProxy extends InvokeProxy {
    StreamInvokeProxy(Class hdrcls) throws Exception {
        super(hdrcls);
        handlerMethod = hdrcls.getMethod("handler", InputStream.class, OutputStream.class, FaasContext.class);
    }

    @Override
    public String invoke(String input, FaasContext context) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String result = null;
        try {
            handlerMethod.invoke(handlerObject, inputStream, outputStream, context);
            result = outputStream.toString("UTF-8");
        } finally {
            inputStream.close();
            outputStream.close();
        }
        return result;
    }
}
