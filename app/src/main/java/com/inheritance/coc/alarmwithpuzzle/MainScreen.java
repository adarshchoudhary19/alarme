package com.inheritance.coc.alarmwithpuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {
    private static final String TAG = "MainScreen";
    private ArrayList<Alarm> alarms = new ArrayList<>();
    private AlarmRVAdapter alarmRVAdapter;
    private RecyclerView rv_alarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        // toolbar
        rv_alarms = (RecyclerView) findViewById(R.id.rv_alarms);

        AlarmDB database = new AlarmDB(this);
        alarms = database.getAllAlarms(this);
        Log.e(TAG, "onCreate: alarmArrayList = " + alarms); //test
        Log.e(TAG, "onCreate: alarmArrayList size = " + alarms.size()); //test
        if (alarms != null && alarms.size() > 0) {
            alarmRVAdapter = new AlarmRVAdapter(alarms);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rv_alarms.setLayoutManager(layoutManager);
            rv_alarms.setHasFixedSize(true); // this allows the UI to be optimised
            rv_alarms.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            rv_alarms.setItemAnimator(new DefaultItemAnimator());
            rv_alarms.setAdapter(alarmRVAdapter);
            RecyclerView.ItemDecoration itemDecoration =
                    new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
            rv_alarms.addItemDecoration(itemDecoration);
            alarmRVAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "You have not set any alarms as yet!\nPlease start by creating a new alarm!", Toast.LENGTH_LONG).show();
//            add_alarm_clicked(findViewById(R.id.add_alarm_fab));
        }
        ConstraintLayout constraintLayout = findViewById(R.id.const_layout);
        rv_alarms.setMinimumWidth(constraintLayout.getWidth());
    }

    public void add_alarm_clicked(View view) {
        Intent intent = new Intent(this, AddAlarm.class);
        intent.putExtra("_id", -1);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onClickSettings(View view) {
        Log.e(TAG, "onClickSettings: in" );
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
