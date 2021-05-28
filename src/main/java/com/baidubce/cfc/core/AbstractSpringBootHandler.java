package com.baidubce.cfc.core;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;

public abstract class AbstractSpringBootHandler implements SpringBootHandlerInterface {
    private static final String HOST_IP = "http://127.0.0.1:8080";
    private static volatile boolean coldLaunch;

    // initialize phase, initialize cold_launch
    static {
        coldLaunch = true;
    }

    // Start spring application
    public abstract void startApp();
    // Entry Function
    public String handler(APIGatewayProxyRequestEvent req) {
        Thread t = new Thread() {
            public void run() {
                startApp();
            }
        };
        if (coldLaunch) {
            t.start();
            coldLaunch = false;
        }
        if (isSocketAlive("localhost", 8080)) {
            ResponseEntity<String> response = springHttpRequset(req);
            // 等待 spring 业务返回处理结构 -> apiGW response
            System.out.println("start trans response to event resp");
            APIGatewayProxyResponseEvent resp = composeApiGWResponse(response);
            System.out.println("response body: " + response.getBody());
            return resp.toString();

        } else {
            APIGatewayProxyResponseEvent resp = new APIGatewayProxyResponseEvent();
            resp.setStatusCode(504);
            resp.setBody(" SpringBoot web server don't alive");
            resp.setIsBase64Encoded(false);
            return resp.toString();
        }

    }

    protected APIGatewayProxyResponseEvent composeApiGWResponse(ResponseEntity<String> response) {
        APIGatewayProxyResponseEvent resp = new APIGatewayProxyResponseEvent();
        resp.setIsBase64Encoded(false);
        resp.setStatusCode(response.getStatusCodeValue());
        HttpHeaders orgHeaders = response.getHeaders();
        Map<String, String> headerObj = new HashMap<String, String>();
        for (String key : orgHeaders.keySet()) {
            if (orgHeaders.getValuesAsList(key).size() > 1) {
                headerObj.put(key, orgHeaders.getValuesAsList(key).toString());
            } else {
                headerObj.put(key, orgHeaders.getValuesAsList(key).get(0));
            }
        }
        resp.setHeaders(headerObj);
        resp.setBody(response.getBody());
        return resp;
    }

    protected ResponseEntity<String> springHttpRequset(APIGatewayProxyRequestEvent req) {
        // apiGW event -> spring request
        String path = req.getPath();
        String method = req.getHttpMethod();
        String body = req.getBody();
        Map<String, String> hdrs = req.getHeaders();
        HttpMethod m = HttpMethod.resolve(method);
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(hdrs);
        RestTemplate client = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
        String url = HOST_IP + path;
        return client.exchange(url, m, entity, String.class);
    }

    protected boolean isSocketAlive(String hostName, int port) {
        boolean isAlive = false;

        int retryCnt = 100;
        while (retryCnt > 0) {
            try (Socket s = new Socket(hostName, port)){
                isAlive = true;
                break;
            } catch (IOException e) {
                System.out.println("socket ioexception by e:" + e);
                retryCnt = retryCnt - 1;
                try {
                    TimeUnit.MILLISECONDS.sleep(600);
                } catch (InterruptedException ex) {
                    System.out.println("interupt exception by ex:" + ex);
                    return false;
                }

            }
        }
        return isAlive;
    }
}




