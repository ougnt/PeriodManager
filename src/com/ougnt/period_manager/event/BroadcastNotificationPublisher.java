package com.ougnt.period_manager.event;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.ougnt.period_manager.activity.InitialActivity;
import com.ougnt.period_manager.*;
import org.joda.time.DateTime;

/**
 * * # Created by wacharint on 3/15/16.
 */
public class BroadcastNotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText("Test Content");
        builder.setSmallIcon(R.drawable.icon);
        Notification notification = builder.getNotification();

        Intent notificationIntent = new Intent(context, BroadcastNotificationPublisher.class);
        notificationIntent.putExtra(BroadcastNotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(BroadcastNotificationPublisher.NOTIFICATION, notification);

        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, notification);
    }

    public void setNotification(Context context, DateTime timeToNotify){

        Intent initialIntent = new Intent(context, BroadcastNotificationPublisher.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, initialIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        am.set(AlarmManager.RTC_WAKEUP, timeToNotify.getMillis(), pIntent );
        Toast.makeText(context,timeToNotify.toString("YYYY MMM dd HH:mm:ss"), Toast.LENGTH_LONG).show();
    }
}
