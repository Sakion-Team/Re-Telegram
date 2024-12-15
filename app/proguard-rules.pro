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
-keep class nep.timeline.re_telegram.HookInit {*;}
#-keep class nep.timeline.re_telegram.language.** {*;}
#-keep class nep.timeline.re_telegram.obfuscate.** {*;}
#-keep class nep.timeline.re_telegram.virtuals.** {*;}
#-keep class nep.timeline.re_telegram.configs.** {*;}
#-keep class nep.timeline.re_telegram.application.** {*;}
#-keep class nep.timeline.re_telegram.base.** {*;}
#-keep class nep.timeline.re_telegram.ClientChecker {*;}
#-keep class nep.timeline.re_telegram.HookUtils {*;}
#-keep class nep.timeline.re_telegram.Utils {*;}
#-keep class nep.timeline.re_telegram.** {*;}