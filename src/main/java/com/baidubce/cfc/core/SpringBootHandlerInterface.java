package com.baidubce.cfc.core;


/***
 * Interface for SpringBootHandlerInterface
 */
public interface SpringBootHandlerInterface {
    String handler(APIGatewayProxyRequestEvent req);
}