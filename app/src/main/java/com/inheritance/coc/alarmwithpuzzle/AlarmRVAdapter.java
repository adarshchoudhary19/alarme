package com.inheritance.coc.alarmwithpuzzle;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class AlarmRVAdapter extends RecyclerView.Adapter<AlarmRVAdapter.AlarmHolder> {
    private Context context;
    private AlarmDB alarmDB;
    private ArrayList<Alarm> alarms;
    private static final String TAG = "AlarmRVAdapter";

    public AlarmRVAdapter(ArrayList<Alarm> inputList) {
        this.alarms = inputList;
    }

    @NonNull
    @Override
    public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        alarmDB = new AlarmDB(context);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.alarm_holder, parent, false);
        return new AlarmHolder(view);
    }

    public void add(int position, Alarm alarm) {
        alarms.add(position, alarm);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        alarms.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(AlarmHolder holder, int position) {
        final Alarm alarm = alarms.get(position);
        holder.bind_alarm(alarm);
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    class AlarmHolder extends RecyclerView.ViewHolder {
        TextView time_tv, message_tv;
        Switch on_switch;
        ConstraintLayout alarm_holder_layout;

        AlarmHolder(View itemView) {
            super(itemView);
            time_tv = (TextView) itemView.findViewById(R.id.time_tv);
            message_tv = (TextView) itemView.findViewById(R.id.message_tv);
            on_switch = (Switch) itemView.findViewById(R.id.on_switch);
            alarm_holder_layout = (ConstraintLayout) itemView.findViewById(R.id.alarm_holder_layout);
        }

        void bind_alarm(final Alarm alarm) {
//            alarm_details = {_id, hour, min, switch_on, msg};
            String minute_string = String.valueOf(alarm.min);
            if (alarm.min < 10) {
                minute_string = "0" + String.valueOf(alarm.min);
            }
            time_tv.setText(String.format(Locale.ENGLISH, "%d:%s", alarm.hour, minute_string));
            message_tv.setText(alarm.msg.trim());
            if (alarm.switch_on.equalsIgnoreCase("on")) {
                on_switch.setChecked(true);
            } else { //"off"
                on_switch.setChecked(false);
            }
            on_switch.bringToFront();
            on_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.e(TAG, "onCheckedChanged: "+on_switch.isChecked() );
                    if(isChecked) {
                        alarm.switch_on = "on";
                    } else {
                        alarm.switch_on = "off";
                    }
                    alarmDB.storeAlarm(alarm);
                    Log.e(TAG, "onCheckedChanged: alarm stored" );
                    if(isChecked) {
                        alarm.set(context);
                    } else {
                        alarm.cancel(context, false);
                    }
                }
            });
//            on_switch.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.e(TAG, "onClick: "+ on_switch.isChecked() );
////                    on_switch.toggle();
//                    if (on_switch.isChecked()) {
//                        alarm.switch_on = "on";
//                        alarm.set(context);
//                        on_switch.setChecked(true);
//                    } else {
//                        alarm.switch_on = "off";
//                        alarm.cancel(context);
//                        on_switch.setChecked(false);
//                    }
//                    alarmDB.storeAlarm(alarm);
//                }
//            });
            alarm_holder_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, AddAlarm.class);
                    i.putExtra("_id", alarm._id);
                    context.startActivity(i);
                }
            });
        }
    }
}
