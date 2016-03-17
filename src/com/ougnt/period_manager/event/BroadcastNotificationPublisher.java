package com.ougnt.period_manager.event;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.widget.Toast;
import com.ougnt.period_manager.activity.InitialActivity;
import com.ougnt.period_manager.*;
import org.joda.time.DateTime;

/**
 * * # Created by wacharint on 3/15/16.
 */
public class BroadcastNotificationPublisher extends BroadcastReceiver {

    public static String ExtraContentTitle = "ExtraContentTitle";
    public static String ExtraContentText = "ExtraContentText";

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(intent.getExtras().getString(ExtraContentTitle));
        builder.setContentText(intent.getExtras().getString(ExtraContentText));
        builder.setSmallIcon(R.drawable.icon);
        Notification notification = builder.getNotification();
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, notification);
    }

    public void setNotification(Context context, DateTime timeToNotify, String contentTitle, String contentText){

        Intent initialIntent = new Intent(context, BroadcastNotificationPublisher.class);
        initialIntent.putExtra(ExtraContentTitle, contentTitle);
        initialIntent.putExtra(ExtraContentText, contentText);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, initialIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int dateFormatFlag = android.text.format.DateUtils.FORMAT_SHOW_DATE | android.text.format.DateUtils.FORMAT_ABBREV_MONTH | android.text.format.DateUtils.FORMAT_SHOW_YEAR;
        am.set(AlarmManager.RTC_WAKEUP, timeToNotify.getMillis(), pIntent );
        String notifyWhen = context.getResources().getString(R.string.notify_when);
        Toast.makeText(context,notifyWhen + DateUtils.formatDateTime(context, timeToNotify.getMillis(), dateFormatFlag) , Toast.LENGTH_LONG).show();
    }
}
