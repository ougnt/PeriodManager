package com.ougnt.period_manager.activity.helper;

import android.content.res.Configuration;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import java.util.Locale;

/**
 * Created by wacharint on 1/27/2017 AD.
 * For collect a util methods
 */

public class UtilHelper {

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String text) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }

    @SuppressWarnings("deprecation")
    public static void setConfigurationLocale(Configuration config, Locale locale) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
    }

//    public static void updateConfiguration(Context context, Configuration config) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            context = context.createConfigurationContext(config);
//        } else {
//            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
//        }
//    }
}
