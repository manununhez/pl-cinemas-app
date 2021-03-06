# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


#https://stackoverflow.com/questions/50378810/proguard-causing-runtime-exception-with-android-navigation-component/50378828#50378828
-keep class * extends androidx.fragment.app.Fragment{}
#https://stackoverflow.com/questions/42782328/retrofit-2-returns-null-in-release-apk-when-minifyenable-but-ok-in-debug-apk
-keep class today.kinema.data.api.model.* {*;}

-keepnames class * extends android.os.Parcelable
-keepnames class * extends java.io.Serializable
