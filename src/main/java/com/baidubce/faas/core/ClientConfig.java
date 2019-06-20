package com.baidubce.faas.core;

class ClientConfig {
    private int invokePipefd;
    private int responsePipefd;
    private String functionBrn;
    private String functionName;
    private int functionMemory;
    private String functionVersion;
    private String functionHandler;
    private String userCodeRoot;

    public int getInvokePipefd() {
        return invokePipefd;
    }

    public int getResponsePipefd() {
        return responsePipefd;
    }

    public String getFunctionBrn() {
        return functionBrn;
    }

    public String getFunctionName() {
        return functionName;
    }

    public int getFunctionMemory() {
        return functionMemory;
    }

    public String getFunctionVersion() {
        return functionVersion;
    }

    public String getFunctionHandler() {
        return functionHandler;
    }

    public String getUserCodeRoot() {
        return userCodeRoot;
    }

    private ClientConfig() {
        invokePipefd = Integer.parseInt(System.getenv("BCE_CFC_INVOKE_PIPE"));
        String responsePipe = System.getenv("BCE_CFC_RESPONSE_PIPE");
        if (responsePipe != null) {
            responsePipefd = Integer.parseInt(responsePipe);
        } else {
            responsePipefd = invokePipefd;
        }
        functionBrn = System.getenv("BCE_USER_FUNCTION_BRN");
        functionName = System.getenv("BCE_USER_FUNCTION_NAME");
        functionMemory = Integer.parseInt(System.getenv("BCE_USER_FUNCTION_MEMSIZE"));
        functionVersion = System.getenv("BCE_USER_FUNCTION_VERSION");
        functionHandler = System.getenv("BCE_USER_FUNCTION_HANDLER");
        userCodeRoot = System.getenv("BCE_USER_CODE_ROOT");
    }

    public static ClientConfig getInstance() {
        return instance;
    }

    private static ClientConfig instance;

    static {
        instance = new ClientConfig();
    }
}
