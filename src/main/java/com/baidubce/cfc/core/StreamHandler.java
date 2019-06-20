package com.baidubce.cfc.core;

import java.io.InputStream;
import java.io.OutputStream;

public interface StreamHandler {
    void handler(InputStream input, OutputStream output, CfcContext context) throws Exception;
}
