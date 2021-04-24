package com.technerd.easyblog.exception;

/**
 * @author technerd
 */

public class MyBlogException extends RuntimeException {

    private Integer code;

    private String message;


    public MyBlogException() {
        super();
    }

    public MyBlogException(String message) {
        this.code = 500;
        this.message = message;
    }

    public MyBlogException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
