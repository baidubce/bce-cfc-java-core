package com.baidubce.cfc.core;

public interface RequestHandler<REQ, RSP> {
    RSP handler(REQ request, CfcContext context) throws Exception;
}
