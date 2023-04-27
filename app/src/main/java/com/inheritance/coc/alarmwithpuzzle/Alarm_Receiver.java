package com.inheritance.coc.alarmwithpuzzle;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Calendar;

import javax.security.auth.login.LoginException;

public class Alarm_Receiver extends BroadcastReceiver {
    private static final String TAG = "Alarm_Receiver";

    @Override
    @TargetApi(19)
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive: in"); //test
//        Calendar calendar = Calendar.getInstance();
//        int current_min = calendar.get(Calendar.MINUTE);
//        Log.e(TAG, "onReceive: current_min="+current_min );
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (intent.getExtras() != null) {
//             while (true) {
//                calendar = Calendar.getInstance();
//                if(calendar.get(Calendar.MINUTE) > current_min) {
//                    Log.e(TAG, "onReceive: current_min="+calendar.get(Calendar.MINUTE) );
//                    break;
//                }
//            }
            String extra_string = intent.getExtras().getString("extra");
            Log.e(TAG, "onReceive: extra_string" + extra_string);
            assert extra_string != null;
            Log.e(TAG, "onReceive: Ringtone Service has been executed. Back to Alarm Receiver");

            Intent intent1;
            if (extra_string.equalsIgnoreCase("Alarm off")) {
                Intent intent3 = new Intent(context, RingtoneService.class);
                intent3.putExtra("extra", extra_string);
                context.startService(intent3);
                Log.e(TAG, "onReceive: Ringtone Service has been executed. Back to Alarm Receiver");
                notificationManager.cancel(0);
                intent1 = new Intent(context, MainScreen.class);
                if (intent.getExtras().getBoolean("goToMainScreen", true)) {
                    context.startActivity(intent1);
                }
            } else if (extra_string.equalsIgnoreCase("Alarm on")) {
                Intent intent2 = new Intent(context, RingtoneService.class);
                intent2.putExtra("extra", extra_string);
                intent2.putExtra("ringtone", intent.getExtras().getInt("ringtone"));
                Log.e(TAG, "onReceive: ringtone = " + intent.getExtras().getInt("ringtone"));
                context.startService(intent2);
                intent1 = new Intent(context, AlarmRings.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent1.putExtra("_id", intent.getExtras().getInt("_id"));
                try {
                    int diff = intent.getExtras().getInt("difficulty", -1);
                    if (diff >= 0) {
                        intent1.putExtra("difficulty", diff);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onReceive: difficulty has not been passed");  //test
                }

                String notification_channel_id = "Alarm_Notif_Channel";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "AlarmChannel";
                    String description = "The channel on which alarms are displayed. Deserves maximum priority.";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel channel = new NotificationChannel(notification_channel_id, name, importance);
                    channel.setDescription(description);
                    NotificationManager notificationManager1 = context.getSystemService(NotificationManager.class);
                    assert notificationManager1 != null;
                    notificationManager1.createNotificationChannel(channel);
                }
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

                Calendar rightNow = Calendar.getInstance();
                int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                int min = rightNow.get(Calendar.MINUTE);
                String minutes = Integer.toString(min);
                String hours = Integer.toString(hour);
                if (min < 10)
                    minutes = "0".concat(minutes);
                if (hour < 10)
                    hours = "0".concat(hours);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, notification_channel_id)
                        .setSmallIcon(R.mipmap.icon)
                        .setContentTitle("Its time to wake up!")
                        .setContentText(String.format("The time is:- %s:%s", hours, minutes))
//                        .setContentText(new SimpleDateFormat("HH:mm", Locale.US).format(new Date()))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentIntent(pendingIntent)
                        .setChannelId(notification_channel_id)
                        .setOngoing(true)
                        .setAutoCancel(false);
                notificationManager.notify(0, mBuilder.build());
                context.startActivity(intent1);
            }
        }
    }
}
