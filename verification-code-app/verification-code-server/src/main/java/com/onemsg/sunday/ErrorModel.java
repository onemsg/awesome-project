package com.onemsg.sunday;

public class ErrorModel {

    public final int code;
    public final String message;
    public final String detail;

    public ErrorModel(int code, String message, String detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    public static ErrorModel of(int code, String message, String detail) {
        return new ErrorModel(code, message, detail);
    }

}
