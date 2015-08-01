package com.github.tcking.giraffe.exception;

/**
 * 业务异常类
 * Created by zhoujie on 15/7/21.
 */
public class BizException extends Exception {
    private String bizError;

    public String getBizError() {
        return bizError;
    }

    public BizException setBizError(String bizError) {
        this.bizError = bizError;
        return this;
    }

    public BizException() {
        super();
    }

    public BizException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BizException(String detailMessage) {
        super(detailMessage);
    }

    public BizException(Throwable throwable) {
        super(throwable);
    }

}
