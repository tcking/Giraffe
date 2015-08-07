package com.github.tcking.giraffe.manager;

import android.content.Context;

import com.github.tcking.giraffe.model.Certificate;

/**
 * 安全管理
 * Created by tc(mytcking@gmail.com) on 15/7/10.
 */
public abstract class CoreSecurityManager extends BaseManager {

    @Override
    public void onAppStart(Context context) {
        super.onAppStart(context);

    }

    protected static Certificate certificate;

    /**
     * 获取当前登陆app的用户凭证
     * @return the certificate
     */
    public static Certificate getCertificate() {
        return certificate;
    }

    protected static boolean certificated;
    /**
     * 当前用户是否已经授权
     * @return the certificated
     */
    public static boolean isCertificated() {
        return certificated;
    }

    /**
     * 当前用户的Id
     * @return
     */
    public static String currentUserId() {
        return certificate!=null?certificate.getUserId():null;
    }
}
