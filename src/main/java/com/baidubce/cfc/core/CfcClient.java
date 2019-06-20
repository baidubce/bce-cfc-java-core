package com.baidubce.cfc.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileDescriptor;
import java.io.ByteArrayOutputStream;

public class CfcClient {

    private FileReader invokePipe;
    private FileWriter responsePipe;
    private ClientConfig clientConfig;
    private InvokeProxy invokeProxy;

    public CfcClient() {
        clientConfig = ClientConfig.getInstance();
        FileDescriptor fd = new FileDescriptor();
        sun.misc.SharedSecrets.getJavaIOFileDescriptorAccess().set(fd, clientConfig.getInvokePipefd());
        invokePipe = new FileReader(fd);
        if (clientConfig.getInvokePipefd() != clientConfig.getResponsePipefd()) {
            fd = new FileDescriptor();
            sun.misc.SharedSecrets.getJavaIOFileDescriptorAccess().set(fd, clientConfig.getResponsePipefd());
            responsePipe = new FileWriter(fd);
        } else {
            responsePipe = new FileWriter(fd);
        }
        invokeProxy = null;
    }

    public static void main(String[] args) {
        CfcClient client = new CfcClient();
        client.waitInvoke();
    }

    private InvokeProxy getHandlerProxy(String name) throws Exception {
        if (invokeProxy == null) {
            invokeProxy = InvokeProxy.create(name);
        }
        return invokeProxy;
    }

    public void waitInvoke() {
        BufferedReader reader = new BufferedReader(invokePipe);
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String requestID = "";
                boolean success = false;
                String result = null;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                output.reset();
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    InvokeRequest request = mapper.readValue(line.getBytes("UTF-8"), InvokeRequest.class);

                    StsCredential credential = new StsCredential();
                    credential.setAccessKeyId(request.getAccessKey());
                    credential.setSecretAccessKey(request.getSecretKey());
                    credential.setSessionToken(request.getSecurityToken());

                    ContextImpl context = new ContextImpl();
                    context.setCredential(credential);
                    context.setFunctionBrn(clientConfig.getFunctionBrn());
                    context.setFunctionName(clientConfig.getFunctionName());
                    context.setFunctionVersion(clientConfig.getFunctionVersion());
                    context.setMemoryLimit(clientConfig.getFunctionMemory());
                    context.setRequestId(request.getRequestID());
                    requestID = request.getRequestID();

                    InvokeProxy handler = getHandlerProxy(clientConfig.getFunctionHandler());
                    assert handler != null;
                    result = handler.invoke(request.getEventObject(), context);
                    success = true;
                } catch (Throwable t) {
                    StackDumper.dumpStackTrace(t, output);
                }

                this.flushStd();
                InvokeResponse response = new InvokeResponse();
                response.setRequestID(requestID);
                response.setSuccess(success);
                if (success) {
                    response.setResult(result);
                } else {
                    response.setError(output.toString());
                }

                output.reset();
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(output, response);
                output.write('\n');
                responsePipe.write(output.toString());
                responsePipe.flush();
                output.close();
            }
        } catch (Exception ex) {
            // 程序自动退出
            ex.printStackTrace();
        }
    }

    private void flushStd() {
        System.out.append('\0');
        System.err.append('\0');
        System.out.flush();
        System.err.flush();
    }
}
