package com.baidubce.cfc.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Class that represents an APIGatewayProxyResponseEvent object
 */
public class APIGatewayProxyResponseEvent implements Serializable, Cloneable {

    // private static final long serialVersionUID = 4290727616926418509L;
    private Integer statusCode;

    private Map<String, String> headers;
    private String body;

    private Boolean isBase64Encoded;

    /**
     * default constructor
     */
    public APIGatewayProxyResponseEvent() {
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getIsBase64Encoded() {
        return isBase64Encoded;
    }

    public void setIsBase64Encoded(Boolean isBase64Encoded) {
        this.isBase64Encoded = isBase64Encoded;
    }

    /**
     * Returns a string representation of this object; useful for testing and debugging.
     *
     * @return A string representation of this object.
     * @see Object#toString()
     */
    @Override
    public String toString() {

        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("isBase64Encoded", getIsBase64Encoded());
        obj.put("statusCode", getStatusCode());
        obj.put("headers", getHeaders());
        obj.put("body", getBody());
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return "json error";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof APIGatewayProxyResponseEvent == false) {
            return false;
        }
        APIGatewayProxyResponseEvent other = (APIGatewayProxyResponseEvent) obj;
        if (other.getStatusCode() == null ^ this.getStatusCode() == null) {
            return false;
        }

        if (other.getStatusCode() != null && other.getStatusCode().equals(this.getStatusCode()) == false) {
            return false;
        }

        if (other.getHeaders() == null ^ this.getHeaders() == null) {
            return false;
        }

        if (other.getHeaders() != null && other.getHeaders().equals(this.getHeaders()) == false){
            return false;
        }

        if (other.getBody() == null ^ this.getBody() == null) {
            return false;
        }

        if (other.getBody() != null && other.getBody().equals(this.getBody()) == false) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;

        hashCode = prime * hashCode + ((getStatusCode() == null) ? 0 : getStatusCode().hashCode());
        hashCode = prime * hashCode + ((getHeaders() == null) ? 0 : getHeaders().hashCode());
        hashCode = prime * hashCode + ((getIsBase64Encoded() == null) ? 0 : getIsBase64Encoded().hashCode());
        hashCode = prime * hashCode + ((getBody() == null) ? 0 : getBody().hashCode());
        return hashCode;
    }

    @Override
    public APIGatewayProxyResponseEvent clone() {
        try {
            return (APIGatewayProxyResponseEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);

        }
    }

}