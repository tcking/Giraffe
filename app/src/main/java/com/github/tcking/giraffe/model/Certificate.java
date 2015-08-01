package com.github.tcking.giraffe.model;

import java.io.Serializable;

/**
 * Created by tc(mytcking@gmail.com) on 15/7/10.
 */
public interface Certificate extends Serializable {
    public String getUserId();
    public void setUserId(String userId);
}
