package com.health.scan.entity.response;

import org.springframework.http.HttpStatus;

public class ObjectResponse {
    private HttpStatus status;
    private int code;
    private String message;
    private Object result;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.setCode(status.value());
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    private void setCode(int code) {
        this.code = code;
    }
}
