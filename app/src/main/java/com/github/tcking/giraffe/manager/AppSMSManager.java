package com.github.tcking.giraffe.manager;

/**
 * <pre>
 * 读取短信验证码
 * 实现类：SMSVerificationCodeReceiver:通过receiver读取，如果系统装有其他第三方app管理短信，可能导致读取不到，读取时可能不会弹授权框(<uses-permission android:name="android.permission.READ_SMS" />)
 * 实现类：SMSVerificationCodeContentObserver:直接读取短信数据库，肯定能读取到，但是会弹出授权框（有些系统里）(<uses-permission android:name="android.permission.RECEIVE_SMS" />)
 *     1. AppSMSManager.SMSReceiver smsReceiver= new SMSVerificationCodeReceiver(){
        public String parseVerificationCode(String msgBody) {
                String regEx = "^(【天猫】)\\D*([0-9]{6})";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(msgBody);
                if (m.find()) {
                    return m.group(2);
                }
                return null;
            }
        };
 *     2. sms.register();
 *     3. 处理SMSVerificationCodeEvent事件
 *     4. sms.unregister();
 * </pre>
 * Created by tc(mytcking@gmail.com) on 15/8/4.
 */
public class AppSMSManager {
    public interface SMSReceiver {
        void register();
        void unregister();
        String parseVerificationCode(String msgBody);
    }

}
