package com.inheritance.coc.alarmwithpuzzle;

//import android.app.AlarmManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

//import android.app.PendingIntent;

public class AddAlarm extends Activity {
    AlarmDB alarmDB;
    private int alarm_id = -1;
    private Alarm alarm;
    String[] listItems;
    boolean[] checkedItems;
    private static final String TAG = "AddAlarm";
//    TimePicker alarm_timePicker;
    MediaPlayer mp = null;
    SharedPreferences sharedPreferences;
    int media_resources[] = {R.raw.tone_1, R.raw.tone_2, R.raw.tone_3, R.raw.tone_4, R.raw.tone_5};
    Spinner puzzle_difficulty_spinner, puzzle_type_spinner, ringtone_spinner;
    EditText snooze_duration_et;
    //    final Calendar calendar = Calendar.getInstance();
//    public static AlarmManager alarmManager;
//    PendingIntent pendingIntent;
//no_repeat    Button repeat_button;
//no_repeat    TextView repeat_tv;
    TextView time_tv;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    Alarm dummyAlarm;
    EditText message_et;
    String HOUR, MIN; // 2 digit integers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        dummyAlarm = new Alarm(AddAlarm.this, 0, 0);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        sharedPreferences = getSharedPreferences("SharedPref", Context.MODE_PRIVATE);

//no_repeat        repeat_button = (Button) findViewById(R.id.repeat_button);
//no_repeat        repeat_tv = (TextView) findViewById(R.id.repeat_tv);
        time_tv = (TextView) findViewById(R.id.time_tv);
//        alarm_timePicker = (TimePicker) findViewById(R.id.timePicker);
        message_et = (EditText) findViewById(R.id.message_et);
        listItems = getResources().getStringArray(R.array.Repeat_Days);
        checkedItems = new boolean[listItems.length]; //boolean array of length 7

        puzzle_difficulty_spinner = findViewById(R.id.puzzle_difficulty_spinner);
        puzzle_type_spinner = findViewById(R.id.puzzle_type_spinner);
        ringtone_spinner = findViewById(R.id.ringtone_spinner);
        snooze_duration_et = (EditText) findViewById(R.id.snooze_duration_et);

//        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        assert alarmManager != null;
//        alarmManager.cancel(pendingIntent);

        alarmDB = new AlarmDB(this);
        Bundle registerData = getIntent().getExtras();
        if (registerData != null) {
            try {
                alarm_id = registerData.getInt("_id", -1);
                if (alarm_id != -1) {
                    alarm = alarmDB.getAlarmObj(AddAlarm.this, alarm_id);
                    message_et.setText(alarm.msg);
//no_repeat                    repeat_tv.setText(alarm.get_repeat_day_string());
                    HOUR = to_string_double_digit(alarm.hour);
                    MIN = to_string_double_digit(alarm.min);
                    String time = HOUR+":"+MIN;
                    time_tv.setText(time);
                    puzzle_difficulty_spinner.setSelection(alarm.difficulty);
                    puzzle_type_spinner.setSelection(alarm.puzzle_type);
                    ringtone_spinner.setSelection(alarm.ringtone);
                    Log.e(TAG, "onCreate: snooze_time_secs = "+alarm.snooze_time_secs );
                    snooze_duration_et.setText(Integer.toString(alarm.snooze_time_secs));
                } else {
                    alarm = new Alarm(AddAlarm.this, 0, 0);
                    puzzle_difficulty_spinner.setSelection(sharedPreferences.getInt(getResources().getString(R.string.SP_Difficulty), 4));
                    puzzle_type_spinner.setSelection(sharedPreferences.getInt(getResources().getString(R.string.SP_Puzzle_type), 1));
                    int ringtone_no = sharedPreferences.getInt(getResources().getString(R.string.SP_Ringtone), -1);
                    if(ringtone_no!=-1) {
                        ringtone_spinner.setSelection(ringtone_no);
                    }
                    final Calendar calendar = Calendar.getInstance();
                    Calendar rightNow = Calendar.getInstance();
                    HOUR = to_string_double_digit(rightNow.get(Calendar.HOUR_OF_DAY));
                    MIN = to_string_double_digit(rightNow.get(Calendar.MINUTE));
                    String time = HOUR+":"+MIN;
                    time_tv.setText(time);
                    snooze_duration_et.setText(String.format(Locale.ENGLISH, "%d", sharedPreferences.getInt(getResources().getString(R.string.SP_Snooze_Duration), 120)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e(TAG, "onCreate: intent extra alarm_id = " + alarm_id);  //test
        }
        mp = new MediaPlayer();
        ringtone_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean first = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!first) {
                    play_tone(false, -1);
                    try {
                        play_tone(true, position);
                    } catch (Exception e) {
                        Log.e("Settings", "onCreate: ", e);
                    }
                }
                first = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//no_repeat
//        repeat_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddAlarm.this);
//                mBuilder.setTitle(R.string.Repeat);
//                dummyAlarm.repeat_days.sun = alarm.repeat_days.sun;
//                dummyAlarm.repeat_days.mon = alarm.repeat_days.mon;
//                dummyAlarm.repeat_days.tue = alarm.repeat_days.tue;
//                dummyAlarm.repeat_days.wed = alarm.repeat_days.wed;
//                dummyAlarm.repeat_days.thurs = alarm.repeat_days.thurs;
//                dummyAlarm.repeat_days.fri = alarm.repeat_days.fri;
//                dummyAlarm.repeat_days.sat = alarm.repeat_days.sat;
//                checkedItems[0] = dummyAlarm.repeat_days.sun;
//                checkedItems[1] = dummyAlarm.repeat_days.mon;
//                checkedItems[2] = dummyAlarm.repeat_days.tue;
//                checkedItems[3] = dummyAlarm.repeat_days.wed;
//                checkedItems[4] = dummyAlarm.repeat_days.thurs;
//                checkedItems[5] = dummyAlarm.repeat_days.fri;
//                checkedItems[6] = dummyAlarm.repeat_days.sat;
//                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
//                        if (isChecked) {
//                            if (!mUserItems.contains(position)) {
//                                mUserItems.add(position);
//                            } else {
//                                mUserItems.remove(position);
//                            }
//                        }
//                    }
//                });
//                mBuilder.setCancelable(false);
//                mBuilder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int which) {
////                        String item = "";
//                        for (int i = 0; i < mUserItems.size(); i++) {
//                            String day = listItems[mUserItems.get(i)];
////                            item = item + day;
//                            switch (day) {
//                                case "Sunday":
//                                    dummyAlarm.repeat_days.sun = true;
//                                    checkedItems[0] = true;
//                                    if (!mUserItems.contains(0)) {
//                                        mUserItems.add(0);
//                                    }
//                                    break;
//                                case "Monday":
//                                    dummyAlarm.repeat_days.mon = true;
//                                    checkedItems[1] = true;
//                                    if (!mUserItems.contains(1)) {
//                                        mUserItems.add(1);
//                                    }
//                                    break;
//                                case "Tuesday":
//                                    dummyAlarm.repeat_days.tue = true;
//                                    checkedItems[2] = true;
//                                    if (!mUserItems.contains(2)) {
//                                        mUserItems.add(2);
//                                    }
//                                    break;
//                                case "Wednesday":
//                                    dummyAlarm.repeat_days.wed = true;
//                                    checkedItems[3] = true;
//                                    if (!mUserItems.contains(3)) {
//                                        mUserItems.add(3);
//                                    }
//                                    break;
//                                case "Thursday":
//                                    dummyAlarm.repeat_days.thurs = true;
//                                    checkedItems[4] = true;
//                                    if (!mUserItems.contains(4)) {
//                                        mUserItems.add(4);
//                                    }
//                                    break;
//                                case "Friday":
//                                    dummyAlarm.repeat_days.fri = true;
//                                    checkedItems[5] = true;
//                                    if (!mUserItems.contains(5)) {
//                                        mUserItems.add(5);
//                                    }
//                                    break;
//                                case "Saturday":
//                                    dummyAlarm.repeat_days.sat = true;
//                                    checkedItems[6] = true;
//                                    if (!mUserItems.contains(6)) {
//                                        mUserItems.add(6);
//                                    }
//                                    break;
//                            }
////                            if (i != mUserItems.size() - 1) {
////                                item = item + " ";
////                            }
//                        }
//                        dummyAlarm.repeat_days.log();
//                        repeat_tv.setText(dummyAlarm.get_repeat_day_string());
//                    }
//                });
//
//                mBuilder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });
//
//                mBuilder.setNeutralButton(R.string.Clear_All, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int which) {
//                        for (int i = 0; i < checkedItems.length; i++) {
//                            checkedItems[i] = false;
//                        }
//                        dummyAlarm.set_repeat_days("");
//                        mUserItems.clear();
//                        repeat_tv.setText("");
//                    }
//                });
//                AlertDialog mDialog = mBuilder.create();
//                mDialog.show();
//            }
//        });
//no_repeat
        time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddAlarm.this);
                mBuilder.setTitle("Alarm Time");
                mBuilder.setMessage("Please select the alarm time:");
                TimePicker timePicker = new TimePicker(AddAlarm.this);
                mBuilder.setView(timePicker);
                if (Build.VERSION.SDK_INT < 23) {
                    timePicker.setCurrentHour(Integer.parseInt(HOUR));
                    timePicker.setCurrentMinute(Integer.parseInt(MIN));
                } else {
                    timePicker.setHour(Integer.parseInt(HOUR));
                    timePicker.setMinute(Integer.parseInt(MIN));
                }
//                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (Build.VERSION.SDK_INT < 23) {
                            HOUR = to_string_double_digit(timePicker.getCurrentHour());
                            MIN = to_string_double_digit(timePicker.getCurrentMinute());
                        } else {
                            HOUR = to_string_double_digit(timePicker.getHour());
                            MIN = to_string_double_digit(timePicker.getMinute());
                        }
                        String time = HOUR+":"+MIN;
                        time_tv.setText(time);
                        dialogInterface.cancel();
                    }
                });
                mBuilder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }
    String to_string_double_digit(int i) {
        String s = Integer.toString(i);
        if(i<10) {
            s = "0"+s;
        }
        return s;
    }
    void play_tone(boolean play, int ringtone_index) {
        try{
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = null;
            }
        } catch (Exception e) {}
        if(play) {
            mp = MediaPlayer.create(AddAlarm.this, media_resources[ringtone_index]);
            mp.start();
        }
    }
    public void onClickSave(View view) {
        Log.e(TAG, "onClickSave: in" );
        play_tone(false, -1);
//        if (alarm_id == -1) {
            alarm.switch_on = "on";
//        }
        alarm.hour = Integer.parseInt(HOUR);
        alarm.min = Integer.parseInt(MIN);
        alarm.msg = message_et.getText().toString();
        alarm.set_repeat_days(dummyAlarm.get_repeat_day_string());
        alarm.difficulty = puzzle_difficulty_spinner.getSelectedItemPosition();
        alarm.puzzle_type = puzzle_type_spinner.getSelectedItemPosition();
        alarm.ringtone = ringtone_spinner.getSelectedItemPosition();
        alarm.snooze_time_secs = Integer.parseInt(snooze_duration_et.getText().toString());
        alarm.repeat_days.sun = dummyAlarm.repeat_days.sun;
        alarm.repeat_days.mon = dummyAlarm.repeat_days.mon;
        alarm.repeat_days.tue = dummyAlarm.repeat_days.tue;
        alarm.repeat_days.wed = dummyAlarm.repeat_days.wed;
        alarm.repeat_days.thurs = dummyAlarm.repeat_days.thurs;
        alarm.repeat_days.fri = dummyAlarm.repeat_days.fri;
        alarm.repeat_days.sat = dummyAlarm.repeat_days.sat;
        alarm.log();
        alarm._id = alarmDB.storeAlarm(alarm);
        Log.e(TAG, "onClickSave: id = "+ alarm._id );
        alarm.log();
        if (alarm.switch_on.equalsIgnoreCase("on"))
            alarm.set(this);
//        else // not needed, I think
//            alarm.cancel(this);
        Intent intent2 = new Intent(this, MainScreen.class);
        startActivity(intent2);
        finish();
    }

    @Override
    public void onBackPressed() {
        play_tone(false, -1);
        Intent intent = new Intent(this, MainScreen.class);
        Toast.makeText(this, "All changes have been discarded", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void onClickDelete(View view) {
        play_tone(false, -1);
        alarm.cancel(this);
        alarmDB.deleteAlarm(alarm);
        Toast.makeText(this, "Alarm Deleted!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }
}

/*
 ** on stop alarm switch off only if no repeat
 ** pendig intent add aalrm id as requesst code
 * ringtone options
 * proper camcel and save of intetns and alarm
 ** adv options media player stop if playing ringrtone
 */
