package com.baidubce.faas.core;

import java.io.InputStream;
import java.io.OutputStream;

public interface StreamHandler {
    void handler(InputStream input, OutputStream output, FaasContext context) throws Exception;
}
