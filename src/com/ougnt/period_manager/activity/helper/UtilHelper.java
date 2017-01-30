package com.ougnt.period_manager.activity.helper;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

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
}
