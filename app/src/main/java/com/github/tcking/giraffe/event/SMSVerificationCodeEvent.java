package com.github.tcking.giraffe.event;

/**
 * 接受到短信验证码
 * Created by tc(mytcking@gmail.com) on 15/8/4.
 */
public class SMSVerificationCodeEvent extends BaseEvent {
    private String code;

    public String getCode() {
        return code;
    }

    public SMSVerificationCodeEvent(String code) {

        this.code = code;
    }
}
