package com.onemsg.sunday;

public class HttpBadParamException extends RuntimeException {
    
    public HttpBadParamException() {
        super();
    }

    public HttpBadParamException(String message) {
        super(message);
    }

    public HttpBadParamException(String message, Throwable t){
        super(message, t);
    }

    public HttpBadParamException(Throwable cause) {
        super(cause);
    }
}
