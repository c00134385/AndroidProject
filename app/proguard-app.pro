# 忽略警告
#-ignorewarning

# 混淆保护自己项目的部分代码以及引用的第三方jar包
#-libraryjars libs/umeng-analytics-v5.2.4.jar

-keep class com.github.chrisbanes.photoview.** {*;}

# 标题栏框架
-keep class com.hjq.bar.** {*;}

# 吐司框架
-keep class com.hjq.toast.** {*;}

# 权限请求框架
-keep class com.hjq.permissions.** {*;}

#移除log 测试了下没有用还是建议自己定义一个开关控制是否输出日志
#-assumenosideeffects class android.util.Log {
#    public static boolean isLoggable(java.lang.String, int);
#    public static int v(...);
#    public static int i(...);
#    public static int w(...);
#    public static int d(...);
#    public static int e(...);
#}

# webview + js
-keepattributes *JavascriptInterface*
# keep 使用 webview 的类
-keepclassmembers class  com.veidy.activity.WebViewActivity {
   public *;
}
# keep 使用 webview 的类的所有的内部类
-keepclassmembers  class  com.veidy.activity.WebViewActivity$*{
    *;
}

# 不混淆WebChromeClient中的openFileChooser方法
-keepclassmembers class * extends android.webkit.WebChromeClient{
   public void openFileChooser(...);
}

# EventBus3
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


# 3party
-keep class com.geekmaker.** {*;}
-keep class com.hoho.** {*;}
-keep class android.app.** {*;}
-keep class com.ys.** {*;}

# scan gun
-keep class com.chice.** {*;}

-keep class com.lvrenyang.** {*;}
-keep class android_serialport_api.** {*;}
-keep class compress_lib_api.** {*;}
-keep class com.hjimi.api.iminect.** {*;}
-keep class com.hjq.demo.serial.** {*;}


# retrofit + okhttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**


-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule


