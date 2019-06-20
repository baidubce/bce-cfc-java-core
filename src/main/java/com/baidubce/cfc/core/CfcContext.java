package com.baidubce.cfc.core;

import java.util.Map;

public interface CfcContext {
    String getRequestId();

    String getFunctionBrn();

    String getFunctionName();

    String getFunctionVersion();

    long getMemoryLimitMB();

    StsCredential getCredential();

    Map<String, String> getEnvironment();
}
