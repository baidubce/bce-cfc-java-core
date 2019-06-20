package com.baidubce.cfc.core;

import java.util.Map;

class ContextImpl implements CfcContext {
    private String requestId;
    private String functionBrn;
    private String functionName;
    private String functionVersion;
    private long memoryLimit;
    private StsCredential credential;
    private Map<String, String> environment;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getFunctionBrn() {
        return functionBrn;
    }

    public void setFunctionBrn(String functionBrn) {
        this.functionBrn = functionBrn;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionVersion() {
        return functionVersion;
    }

    public void setFunctionVersion(String functionVersion) {
        this.functionVersion = functionVersion;
    }

    public long getMemoryLimitMB() {
        return memoryLimit;
    }

    public void setMemoryLimit(long memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public StsCredential getCredential() {
        return credential;
    }

    public void setCredential(StsCredential credential) {
        this.credential = credential;
    }

    public Map<String, String> getEnvironment() {
        return System.getenv();
    }

    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }
}
