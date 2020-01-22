# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\android_studio\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


#umeng
-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


# sentry
-keepattributes LineNumberTable,SourceFile
-dontwarn org.slf4j.**
-dontwarn javax.**
-keep class io.sentry.event.Event { *; }


# greendao
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties {*;}
# If you do not use SQLCipher:
-dontwarn net.sqlcipher.database.**
# If you do not use RxJava:
-dontwarn rx.**