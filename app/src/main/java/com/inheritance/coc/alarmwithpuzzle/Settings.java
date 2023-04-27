package com.inheritance.coc.alarmwithpuzzle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

public class Settings extends AppCompatActivity /*implements AdapterView.OnItemSelectedListener*/ {

    SharedPreferences sharedPreferences;
    Spinner puzzle_difficulty_spinner, puzzle_type_spinner, ringtone_spinner;
    EditText snooze_duration_et;
    boolean saved = false;
    MediaPlayer mp = null;
    int media_resources[] = {R.raw.tone_1, R.raw.tone_2, R.raw.tone_3, R.raw.tone_4, R.raw.tone_5};
    private static final String TAG = "Settings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate: in" );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        puzzle_difficulty_spinner = findViewById(R.id.puzzle_difficulty_spinner);
        puzzle_difficulty_spinner.setSelection(sharedPreferences.getInt(getResources().getString(R.string.SP_Difficulty), 4));
//        ArrayAdapter<CharSequence> adapter_a = ArrayAdapter.createFromResource(this,R.array.Puzzle_Difficulty,android.R.layout.simple_spinner_item);
//        adapter_a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_a.setAdapter(adapter_a);
//        spinner_a.setOnItemSelectedListener(this);

        puzzle_type_spinner = findViewById(R.id.puzzle_type_spinner);
        puzzle_type_spinner.setSelection(sharedPreferences.getInt(getResources().getString(R.string.SP_Puzzle_type), 1));
//        ArrayAdapter<CharSequence> adapter_b = ArrayAdapter.createFromResource(this,R.array.Puzzle_type,android.R.layout.simple_spinner_item);
//        adapter_b.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_b.setAdapter(adapter_b);
//        spinner_b.setOnItemSelectedListener(this);

        ringtone_spinner = findViewById(R.id.ringtone_spinner);
        ringtone_spinner.setSelection(sharedPreferences.getInt(getResources().getString(R.string.SP_Ringtone), 0));
//        ArrayAdapter<CharSequence> adapter_c = ArrayAdapter.createFromResource(this,R.array.Ringtone_Select,android.R.layout.simple_spinner_item);
//        adapter_c.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        ringtone_spinner.setAdapter(adapter_c);
        mp = new MediaPlayer();
        ringtone_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean first = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(saved) {
                    play_tone(false, -1);
                }
                else {
                    if(!first) {
                        play_tone(false, -1);
                        try {
                            play_tone(true, position);
                        } catch (Exception e) {
                            Log.e("Settings", "onCreate: ", e);
                        }
                    }
                    first = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        snooze_duration_et = (EditText) findViewById(R.id.snooze_duration_et);
        snooze_duration_et.setText(String.format(Locale.ENGLISH, "%d", sharedPreferences.getInt(getResources().getString(R.string.SP_Snooze_Duration), 120)));
    }

    @Override
    public void onBackPressed() {
        play_tone(false, -1);
        saveSettings((Button) findViewById(R.id.save_button));
//        super.onBackPressed();
        saved = true;
        ringtone_spinner.setSelection(ringtone_spinner.getSelectedItemPosition());
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
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
            mp = MediaPlayer.create(Settings.this, media_resources[ringtone_index]);
            mp.start();
        }
    }

    void saveSettings(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getResources().getString(R.string.SP_Difficulty), puzzle_difficulty_spinner.getSelectedItemPosition());
        editor.putInt(getResources().getString(R.string.SP_Puzzle_type), puzzle_type_spinner.getSelectedItemPosition());
        editor.putInt(getResources().getString(R.string.SP_Ringtone), ringtone_spinner.getSelectedItemPosition());
        editor.putInt(getResources().getString(R.string.SP_Snooze_Duration), Integer.parseInt(snooze_duration_et.getText().toString()));
        editor.apply();
        play_tone(false, -1);
        Toast.makeText(this, "Preferences Saved!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }
}
