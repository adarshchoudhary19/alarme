package com.inheritance.coc.alarmwithpuzzle;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class Alarm /*implements Serializable*/ {
    private static final String TAG = "Alarm";
    private final String[] DetTAGS = {"_id", "hour", "min", "switch_on", "msg", "repeat_days", "snooze_time_secs", "ringtone", "puzzle_type", "difficulty"};
    private final String[] ON_Status = {"on", "off"};
    //    private final String[] RepeatDay = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}; // null if empty
//    private final String[] Puzzle_types = {"random", "maths", "gk"};
    private final int[] Difficulty = {1, 2, 3, 4, 5, 6, 7, 8};

    String SP_Difficulty = "Puzzle_Difficulty";
    String SP_Puzzle_type = "Puzzle_type";
    String SP_Ringtone = "Tone_Number";
    String SP_Snooze_Duration = "Snooze_Duration";

    public int _id = -1, hour, min, snooze_time_secs, difficulty, ringtone, puzzle_type;
    public String switch_on, msg;
    public Repeat_Days repeat_days;

    Alarm(Context context, int hour, int min) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.hour = hour; // 24 hrs only //0-23
        this.min = min; //0-59
        //todo
        snooze_time_secs = sharedPreferences.getInt(SP_Snooze_Duration, 120); // update if user tells so
        difficulty = sharedPreferences.getInt(SP_Difficulty, 4); // update if user tells so
        switch_on = ON_Status[0];
        msg = "Wake Up!";
        ringtone = sharedPreferences.getInt(SP_Ringtone, 0); // update if user tells so
        puzzle_type = sharedPreferences.getInt(SP_Puzzle_type, 1); // update if user tells so
        repeat_days = new Repeat_Days();
    }

    void log() {
        Log.e("Alarm: ", String.format("_id=%d, hour=%d, min=%d, msg=%s, snooze_time=%d, diff=%d, on=%s, tone=%d, type=%d",
                _id, hour, min, msg, snooze_time_secs, difficulty, switch_on, ringtone, puzzle_type));  //test
        repeat_days.log();
    }

    void set(Context context) {
        Log.e(TAG, "set: setting alarm");
        final Intent intent = new Intent(context, Alarm_Receiver.class);
        intent.putExtra("_id", _id);
        intent.putExtra("ringtone", ringtone);
        intent.putExtra("extra", "Alarm on");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, _id,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
        final Calendar calendar = Calendar.getInstance();
        Calendar rightNow = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        Log.e(TAG, "set: "+_id +" "+ringtone+" "+hour+" "+min );
        //todo update according to repeat // what if its not on same day?
        Log.e(TAG, "set: Alarm set: "+calendar.getTimeInMillis());
        Log.e(TAG, "set: Time diff in milliseconds = "+ (calendar.getTimeInMillis() - System.currentTimeMillis()));
        //todo check for year and update if its 31st dec and time has already passed
        if(hour<rightNow.get(Calendar.HOUR_OF_DAY)) {
            calendar.set(Calendar.DAY_OF_YEAR, rightNow.get(Calendar.DAY_OF_YEAR)+1);
        } else if(hour == rightNow.get(Calendar.HOUR_OF_DAY)) {
            if(min<=rightNow.get(Calendar.MINUTE)) {
                calendar.set(Calendar.DAY_OF_YEAR, rightNow.get(Calendar.DAY_OF_YEAR)+1);
            }
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);
//        context.sendBroadcast(intent);
    }

    void cancel(Context context, boolean goToMainScreen) {
        final Intent intent = new Intent(context, Alarm_Receiver.class);
        intent.putExtra("_id", _id);
        intent.putExtra("ringtone", ringtone);
        intent.putExtra("extra", "Alarm off");
        if(!goToMainScreen) {
            intent.putExtra("goToMainScreen", false);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, _id,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
        context.sendBroadcast(intent);
    }
    void cancel(Context context) {
        cancel(context, true);
    }

    public void set_repeat_days(String repeat_days_string) {
        Log.e(TAG, "set_repeat_days: " + repeat_days_string);  //test
        this.repeat_days = new Repeat_Days();
        if (!(repeat_days_string == null || repeat_days_string.trim().equalsIgnoreCase("null") || repeat_days_string.trim().equalsIgnoreCase(""))) {
            int a = 0;
            int b = repeat_days_string.indexOf(' ', a);
            while (b != -1) {
                String day = repeat_days_string.substring(a, b).trim();
                switch (day) {
                    case "Sunday":
                        this.repeat_days.sun = true;
                        break;
                    case "Monday":
                        this.repeat_days.mon = true;
                        break;
                    case "Tuesday":
                        this.repeat_days.tue = true;
                        break;
                    case "Wednesday":
                        this.repeat_days.wed = true;
                        break;
                    case "Thursday":
                        this.repeat_days.thurs = true;
                        break;
                    case "Friday":
                        this.repeat_days.fri = true;
                        break;
                    case "Saturday":
                        this.repeat_days.sat = true;
                        break;
                }
                a = b + 1;
                b = repeat_days_string.indexOf(' ', a);
            }
        }
        Log.e(TAG, "set_repeat_days:" + " Sunday=" + repeat_days.sun + " Monday=" + repeat_days.mon + " Tuesday=" + repeat_days.tue
                + " Wednesday=" + repeat_days.wed + " Thursday=" + repeat_days.thurs + " Friday=" + repeat_days.fri + " Saturday=" + repeat_days.sat);  //test
    }

    public String get_repeat_day_string() {
        if (repeat_days != null) {
            String repeat_string = "";
            if (repeat_days.sun) {
                repeat_string = repeat_string.concat("Sunday ");
            }
            if (repeat_days.mon) {
                repeat_string = repeat_string.concat("Monday ");
            }
            if (repeat_days.tue) {
                repeat_string = repeat_string.concat("Tuesday ");
            }
            if (repeat_days.wed) {
                repeat_string = repeat_string.concat("Wednesday ");
            }
            if (repeat_days.thurs) {
                repeat_string = repeat_string.concat("Thursday ");
            }
            if (repeat_days.fri) {
                repeat_string = repeat_string.concat("Friday ");
            }
            if (repeat_days.sat) {
                repeat_string = repeat_string.concat("Saturday ");
            }
            return repeat_string.trim();
        } else {
            return "";
        }
    }

    class Repeat_Days /*implements Serializable*/ {
        public boolean sun = false, mon = false, tue = false, wed = false, thurs = false, fri = false, sat = false;

        void log() {
            Log.e(TAG, "log: Repeat_Days" + String.valueOf(sun) + String.valueOf(mon) + String.valueOf(tue) +
                    String.valueOf(wed) + String.valueOf(thurs) + String.valueOf(fri) + String.valueOf(sat)); //test
        }
    }
    // update db while exiting app or saving alarm
}
