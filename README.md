
# Giraffe
---------

android开发是件体力活，我们都需要花大量的时间去堆砌跟业务无关的代码，例如拍照，获取照片，处理照片大小，各线程之间的交互等，这些问题都有优秀的方案来解决，Giraffe是将这些优秀的东西集合和组织起来，成为一个快速搭建android app的脚手架，让“卓码”将更多的精力放到业务的处理上。


## 依赖关系
<hr>
原则上app/libs下不放置任何jar包，所有的第三方jar统一放到:commonlib中，其他module则在依赖commonlib

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
{% highlight java%}
Log.d("hello {},hello {}","world","giraffe");
Log.e("parameter a={},b={},c={}","a","b","c",exception);
{% endhighlight %}

* **2.DAO**
DAO采用[__greenDAO__](https://github.com/greenrobot/greenDAO)，修改module dao下的<code>DAOGenerator</code>，再运行DAOGenerator的main方法在模块app下生产对应的POJO和DAO文件
	* 获取DAO对象，<br><code>DBManager.getInstance("tc").getDaoSession().getXDAO()</code> , DBManager根据数据库名称（一般以用户来区别）来创建或打开数据库并获取DAO对象
	* 数据库结构和数据的升级，需要编写实现逻辑并注册到升级管理器，DBManager在打开数据库的时候将根据版本号一次执行升级：
		1. 编写实现了DBUpgrader接口的实现类，完成数据库的升级逻辑
		2. 修改<code>DBUpgradeManager.registerUpgraders</code>方法，将第一步的实现类注册到升级管理器

* **3.DeviceManager** 获取设备相关的信息，例如网络状态，类型，IMEI，屏幕像素，分辨率，px和dp的相互转换等。



