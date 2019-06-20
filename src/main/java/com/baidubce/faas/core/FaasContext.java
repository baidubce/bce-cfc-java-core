package com.baidubce.faas.core;

import java.util.Map;

public interface FaasContext {
    String getRequestId();

    String getFunctionBrn();

    String getFunctionName();

    String getFunctionVersion();

    long getMemoryLimitMB();

    StsCredential getCredential();

    Map<String, String> getEnvironment();
}
