package com.github.tcking.giraffe.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.github.tcking.giraffe.core.CoreApp;
import com.github.tcking.giraffe.core.Log;
import com.github.tcking.giraffe.event.SMSVerificationCodeEvent;
import com.github.tcking.giraffe.manager.AppSMSManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

/**
 *  * 需要权限    <uses-permission android:name="android.permission.RECEIVE_SMS" />
 * Created by tc(mytcking@gmail.com) on 15/8/4.
 */
public class SMSVerificationCodeReceiver extends BroadcastReceiver implements AppSMSManager.SMSReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            Object[] smsObj = (Object[]) bundle.get("pdus");
            for (Object object : smsObj) {
                SmsMessage msg = SmsMessage.createFromPdu((byte[]) object);
                String msgBody = msg.getDisplayMessageBody();
                Log.d("SMSVerificationCodeReceiver.onReceive {}", msgBody);
                String code=parseVerificationCode(msgBody);
                if (!TextUtils.isEmpty(code)) {
                    EventBus.getDefault().post(new SMSVerificationCodeEvent(code));
                }
            }
        }
    }

    /**
     * override this to get verification code
     * @param msgBody
     * @return
     */
    public String parseVerificationCode(String msgBody) {
        String regEx = "^(【xxxx】)\\D*([0-9]{6})";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(msgBody);
        if (m.find()) {
            return m.group(2);
        }
        return null;
    }

    /**
     * 注册
     */
    public void register() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        CoreApp.getInstance().registerReceiver(this,intentFilter);
    }

    /**
     * 反注册
     */
    public void unregister() {
        CoreApp.getInstance().unregisterReceiver(this);
    }
}
