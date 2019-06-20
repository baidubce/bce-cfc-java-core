package com.baidubce.faas.core;

public interface RequestHandler<REQ, RSP> {
    RSP handler(REQ request, FaasContext context) throws Exception;
}
