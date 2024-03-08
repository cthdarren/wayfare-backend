package com.wayfare.backend;

public class ResponseObject {
    public boolean success;
    public Object data;

    public ResponseObject(boolean success, Object data)
    {
        this.success = success;
        this.data = data;
    }
}
