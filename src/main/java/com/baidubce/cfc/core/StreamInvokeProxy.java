package com.baidubce.cfc.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

class StreamInvokeProxy extends InvokeProxy {
    StreamInvokeProxy(Class hdrcls) throws Exception {
        super(hdrcls);
        handlerMethod = hdrcls.getMethod("handler", InputStream.class, OutputStream.class, CfcContext.class);
    }

    @Override
    public String invoke(String input, CfcContext context) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String result = null;
        try {
            // handlerMethod.invoke(handlerObject, inputStream, outputStream, context);
            ((StreamHandler) handlerObject).handler(inputStream, outputStream, context);
            result = outputStream.toString("UTF-8");
        } finally {
            inputStream.close();
            outputStream.close();
        }
        return result;
    }
}
