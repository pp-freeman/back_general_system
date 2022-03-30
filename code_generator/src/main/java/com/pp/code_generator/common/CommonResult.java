package com.pp.code_generator.common;

public class CommonResult<T> {
    private T data;
    private String message;
    private Integer code;
    private Integer number;

    public CommonResult() {
    }

    public CommonResult(T data, String message, Integer code) {
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public CommonResult(T data, String message, Integer code, Integer number) {
        this.data = data;
        this.message = message;
        this.code = code;
        this.number = number;
    }

    public CommonResult(T data, Integer number) {
        this.data = data;
        this.message = "操作成功";
        this.code = 200;
        this.number = number;
    }

    public CommonResult(String message, Integer code) {
        this(null, message, code);
    }

    public CommonResult(T data) {
        this(data, "操作成功", 200);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
