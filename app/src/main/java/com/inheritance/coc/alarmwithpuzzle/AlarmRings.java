package com.inheritance.coc.alarmwithpuzzle;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
//import android.os.VibrationEffect;
//import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

public class AlarmRings extends AppCompatActivity {
    private static final String TAG = "AlarmRings";
    private final String[] Puzzle_types = {"random", "maths", "gk"};
    private String puzzle_type = Puzzle_types[1];
    private Alarm alarm;
    AlarmDB alarmDB;
    MCQ_Layout mcq_view_group;
    Maths_Layout maths_view_group;
//    Vibrator vibrator;

    //    private ConstraintLayout button_layout; // should be a dialog box
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_rings);

        mcq_view_group = (MCQ_Layout) findViewById(R.id.mcq_view_group);
        mcq_view_group.setVisibility(View.INVISIBLE);
        maths_view_group = (Maths_Layout) findViewById(R.id.maths_layout);
        maths_view_group.setVisibility(View.INVISIBLE);

        alarmDB = new AlarmDB(this);
        int difficulty=4;
        Bundle data = getIntent().getExtras();
        if (data != null) {
            try {
                alarm = alarmDB.getAlarmObj(AlarmRings.this, data.getInt("_id"));
                puzzle_type = Puzzle_types[alarm.puzzle_type];
            } catch (Exception e) {
                e.printStackTrace();
            }
            difficulty = data.getInt("difficulty", alarm.difficulty);
            Log.e(TAG, "onCreate: intent extra puzzle_type = " + puzzle_type);  //test
            Log.e(TAG, "onCreate: intent extra _id = " + data.getInt("_id"));  //test
            Log.e(TAG, "onCreate: intent extra difficulty = " + difficulty);  //test
        }

//        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        assert vibrator != null;
//        long[] pattern = {0, 500, 500, 750, 750};
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 1));
//        } else {
//            vibrator.vibrate(pattern, 1);
//        }
//        Log.e(TAG, "onCreate: Vibrating");

        if (puzzle_type.equalsIgnoreCase(Puzzle_types[0])) {
            Random rand = new Random();
            puzzle_type = Puzzle_types[rand.nextInt(Puzzle_types.length - 1) + 1];  // 4 -> 1-4
        }
        if (puzzle_type.equalsIgnoreCase(Puzzle_types[2])) {
            mcq_view_group.setVisibility(View.VISIBLE);
            mcq_view_group.bringToFront();
//            maths_view_group.setVisibility(View.INVISIBLE);
            maths_view_group.setVisibility(View.GONE);
            MCQ_Layout.OnChangeListener listener = new MCQ_Layout.OnChangeListener() {
                @Override
                public void onChange(boolean correct_ans) {
                    if (correct_ans) {
                        Toast.makeText(AlarmRings.this, "Your answer is correct!", Toast.LENGTH_SHORT).show();
                        got_correct_answer();
                    } else {
                        Toast.makeText(AlarmRings.this, "Your answer is incorrect!", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            mcq_view_group.setOnChangeListener(listener);
        } else if (puzzle_type.equalsIgnoreCase(Puzzle_types[1])) {
            try {
                maths_view_group.set_difficulty(difficulty);
            } catch (Exception e) {
                Log.e(TAG, "onCreate: maths_view_group.set_difficulty error = ", e);
            }
            maths_view_group.setVisibility(View.VISIBLE);
            maths_view_group.bringToFront();
//            mcq_view_group.setVisibility(View.INVISIBLE);
            mcq_view_group.setVisibility(View.GONE);
            Maths_Layout.OnChangeListener listener = new Maths_Layout.OnChangeListener() {
                @Override
                public void onChange(boolean correct_ans) {
                    if (correct_ans) {
                        Toast.makeText(AlarmRings.this, "Your answer is correct!", Toast.LENGTH_SHORT).show();
                        got_correct_answer();
                    } else {
                        Toast.makeText(AlarmRings.this, "Your answer is incorrect!", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            maths_view_group.setOnChangeListener(listener);
        }
    }

    private void got_correct_answer() {
//        vibrator.cancel();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alarm.msg);
        builder.setMessage("Do you want to snooze or stop the alarm?");
        builder.setCancelable(false);
        builder.setPositiveButton("Snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                final Intent intent1 = new Intent(AlarmRings.this, Alarm_Receiver.class);
                intent1.putExtra("_id", alarm._id);
                intent1.putExtra("ringtone", alarm.ringtone);
                intent1.putExtra("extra", "Alarm off");
                sendBroadcast(intent1);
                Toast.makeText(getApplicationContext(), "Alarm Snoozed for " + alarm.snooze_time_secs + " seconds", Toast.LENGTH_LONG).show();

                final Intent intent = new Intent(AlarmRings.this, Alarm_Receiver.class);
                intent.putExtra("_id", alarm._id);
                intent.putExtra("ringtone", alarm.ringtone);
                intent.putExtra("extra", "Alarm on");
                intent.putExtra("difficulty", alarm.difficulty + 1); // we do not continuously keep on increasing difficulty
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmRings.this, alarm._id,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) AlarmRings.this.getSystemService(ALARM_SERVICE);
                assert alarmManager != null;
                alarmManager.cancel(pendingIntent);
                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, alarm.hour);
                calendar.set(Calendar.MINUTE, alarm.min);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + alarm.snooze_time_secs * 1000,
                        pendingIntent);
//                sendBroadcast(intent);
                Intent intent2 = new Intent(AlarmRings.this, MainScreen.class);
                startActivity(intent2);
            }
        });

        builder.setNegativeButton("Stop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(getApplicationContext(), "Alarm Stopped", Toast.LENGTH_LONG).show();
                if ((alarm.get_repeat_day_string() == null || alarm.get_repeat_day_string().trim().equalsIgnoreCase("null")
                        || alarm.get_repeat_day_string().trim().equalsIgnoreCase(""))) {
                    alarm.switch_on = "off";
                    alarmDB.storeAlarm(alarm);
                } else {
                    alarm.set(AlarmRings.this);
                }
                final Intent intent = new Intent(AlarmRings.this, Alarm_Receiver.class);
                intent.putExtra("_id", alarm._id);
                intent.putExtra("ringtone", alarm.ringtone);
                intent.putExtra("extra", "Alarm off");
//                intent.putExtra("difficulty", alarm.difficulty + 1); // we do not continuously keep on increasing difficulty
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmRings.this, alarm._id,
//                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                AlarmManager alarmManager = (AlarmManager) AlarmRings.this.getSystemService(ALARM_SERVICE);
//                assert alarmManager != null;
//                alarmManager.cancel(pendingIntent);
//                final Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR_OF_DAY, alarm.hour);
//                calendar.set(Calendar.MINUTE, alarm.min);
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + alarm.snooze_time_secs * 1000,
//                        pendingIntent);
                sendBroadcast(intent);


                Intent intent1 = new Intent(AlarmRings.this, MainScreen.class);
                startActivity(intent1);
                //Repeat alarm?? // I don't think it is required
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Toast.makeText(this, "Please solve the puzzle first!", Toast.LENGTH_SHORT).show();
    }
}
