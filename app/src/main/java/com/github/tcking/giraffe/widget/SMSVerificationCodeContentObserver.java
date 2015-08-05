package com.github.tcking.giraffe.widget;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.github.tcking.giraffe.core.CoreApp;
import com.github.tcking.giraffe.core.Log;
import com.github.tcking.giraffe.event.SMSVerificationCodeEvent;
import com.github.tcking.giraffe.manager.AppSMSManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

/**
 * 直接读取短信数据库（能保证读取到，在有的系统里会弹出读取短信的授权对话框）
 * 需要权限    <uses-permission android:name="android.permission.READ_SMS" />
 * <pre>
 *     1. SMSVerificationCodeContentObserver co=new SMSVerificationCodeContentObserver();
 *     2. co.register();
 *     3. 处理SMSVerificationCodeEvent事件
 *     4. co.unregister();
 * </pre>
 * Created by tc(mytcking@gmail.com) on 15/7/12.
 */
public class SMSVerificationCodeContentObserver extends ContentObserver implements AppSMSManager.SMSReceiver {
    public static final String SMS_URI_INBOX = "content://sms/inbox";

    public SMSVerificationCodeContentObserver() {
        super(null);
    }
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        try {
            Cursor cursor = null;// 光标
            // 读取收件箱中指定号码的短信
            cursor = CoreApp.getInstance().getContentResolver().query(Uri.parse(SMS_URI_INBOX), new String[]{"_id", "address", "body", "read"}, "read=?",
                    new String[]{"0"}, "_id desc");
            if (cursor != null) {// 如果短信为未读模式
                if (cursor.moveToFirst()) {
                    String msgBody = cursor.getString(cursor.getColumnIndex("body"));
                    Log.d("SMSVerificationCodeContentObserver.onChange {}", msgBody);
                    String code=parseVerificationCode(msgBody);
                    if (!TextUtils.isEmpty(code)) {
                        EventBus.getDefault().post(new SMSVerificationCodeEvent(code));
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("SMSVerificationCodeContentObserver.onChange error", e);
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
        CoreApp.getInstance().getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, this);
    }

    /**
     * 反注册
     */
    public void unregister() {
        CoreApp.getInstance().getContentResolver().unregisterContentObserver(this);
    }
}
