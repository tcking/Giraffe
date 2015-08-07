
# Giraffe
---------

android开发是件体力活，我们都需要花大量的时间去堆砌跟业务无关的代码，例如拍照，获取照片，处理照片大小，各线程之间的交互等，这些问题都有优秀的方案来解决，Giraffe是将这些优秀的东西集合和组织起来，成为一个快速搭建android app的脚手架，让“卓码”将更多的精力放到业务的处理上。
Giraffe是这样来组织app的：

1. app以事件驱动，从app的加载配置，完成启动，到各业务，例如获取定位等都是以事件的方式发出。
2. app在启动的时候按次序实例化manager，并注册到事件总线中。manager是一个独立的逻辑处理类，或者业务处理类，例如处理定位信息的AppLocationManager，这些Manager一般情况下应该都是单例。


## 依赖关系
<hr>
原则上app/libs下不放置任何jar包，所有的第三方jar统一放到:commonlib中，其他module则在依赖commonlib

## 如何使用
1. 在Grraffe工程的基础上开始一个新的project，或者自己创建一个新的project，然后import app之外的module，再将app下的src目录拷贝到新工程的src目录。
2. 创建自己的Application类，继承自CoreApp，实现getRegisterManager（返回注册了的manager数组资源，app在启动的时候按次序实例化manager），并注册到事件总线中.例如：
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string-array name="managers" translatable="false">
        <item>com.github.tcking.example.manager.AppConfigManager</item>
        <item>com.github.tcking.giraffe.manager.DeviceManager</item>
        <item>com.github.tcking.giraffe.manager.CoreSecurityManager</item>
    </string-array>
</resources>
```

## 配置
<hr>
约定优于配置，giraffe在启动的时候会去加载`assets/config.properties`中的配置,配置采用[__PlaceHolderProperties__](https://github.com/tcking/PlaceHolderProperties)支持placeHolder,例如:<br>
<code>images=${app_home}/images</code>

* **model分隔不同环境的配置**
配置项model用于指定当前的配置的环境是什么，例如dev，test或production，其他的配置项通过model前缀来区别不同环境的配置，例如:
<pre>
dev.openAPIServer=http://dev.myhost.com #表示dev的配置
production.openAPIServer=http://wwww.myhost.com #表示生产的环境配置
</pre>
而这些配置项可以写在一个文件中，通过修改mode来快速切换配置

* **日志配置**
日志参考sl4j的思想，支持参数化，例如：<code>Log.e("get user detail error:{}",userId,exception)</code> 日志记录采用[**Microlog**](http://microlog.sourceforge.net/site/),支持选择输出到logcat或文件，日志记录格式可配置

<pre>
%P 日志级别
%t 当前线程名称
%d 当前时间
%m 日志消息
%T Throwable对象
</pre>

## 基础组件
<hr>

* **1.日志**
支持参数化（只有在日志打印的时候字符串才进行拼接，性能更好）
``` java
Log.d("hello {},hello {}","world","giraffe");
Log.e("parameter a={},b={},c={}","a","b","c",exception);
```

* **2.DAO**
DAO采用[__greenDAO__](https://github.com/greenrobot/greenDAO)，修改module dao下的<code>DAOGenerator</code>，再运行DAOGenerator的main方法在模块app下生产对应的POJO和DAO文件
	* 获取DAO对象，<br><code>DBManager.getInstance("tc").getDaoSession().getXDAO()</code> , DBManager根据数据库名称（一般以用户来区别）来创建或打开数据库并获取DAO对象
	* 数据库结构和数据的升级，需要编写实现逻辑并注册到升级管理器，DBManager在打开数据库的时候将根据版本号一次执行升级：
		1. 编写实现了DBUpgrader接口的实现类，完成数据库的升级逻辑
		2. 修改<code>DBUpgradeManager.registerUpgraders</code>方法，将第一步的实现类注册到升级管理器

* **3.DeviceManager** 获取设备相关的信息，例如网络状态，类型，IMEI，屏幕像素，分辨率，px和dp的相互转换等。

* **4.AppConfigManager**
从ConfigLoadEvent事件中获取`Properties`对象（来自配置文件/assets/config.properties）,并初始化应用自己的配置文件；`CoreAppConfig`中的配置在app启动时已经配置完成，应用自己的配置类一般继承`CoreAppConfig`

* **5.AppLocationManager** 
获取定位（目前只支持单次定位，获取到位置信息后停止定位）

1.要获取定位时，调用`AppLocationManager.getInstance().tryLocation(GPSFirst,15000);//GPS优先，定位超时15s`
2.获取到定位消息时发送Event：LocationEvent
3.获取定位超时时发送Event：LocationTimeoutEvent

* **6.AppSMSManager**
读取短信验证码，有两个实现类

 * 实现类：SMSVerificationCodeReceiver:通过receiver读取，如果系统装有其他第三方app管理短信，可能导致读取不到，读取时可能不会弹授权框( )
 * 实现类：SMSVerificationCodeContentObserver:直接读取短信数据库，肯定能读取到，但是会弹出授权框（有些系统里）

```
//1.获取接受短信的Receiver实例
AppSMSManager.SMSReceiver smsReceiver= new SMSVerificationCodeReceiver(){
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
//2.注册
sms.register();
//3. 处理SMSVerificationCodeEvent事件,通过SMSVerificationCodeEvent.getCode()获取验证码
//4.反注册 
sms.unregister();
```

## 事件
1. ConfigLoadEvent:配置文件加载完成后驱动的事件(stick事件，在AppInitEvent时remove)
2. AppInitEvent:app完成初始化时发布的事件
3. DeviceNetworkChangeEvent: 网络状态发送变化时
4. LocationEvent：获取到网络定位时发布的事件（通过AppLocationManager.getInstance().tryLocation()开始定位）
5. LocationTimeoutEvent：定位超时时的事件
6. SMSVerificationCodeEvent：获取到短信验证码时发布的事件
