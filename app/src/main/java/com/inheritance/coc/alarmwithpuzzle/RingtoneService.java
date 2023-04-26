package com.inheritance.coc.alarmwithpuzzle;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

public class RingtoneService extends Service /*implements MediaPlayer.OnPreparedListener*/ {
    // todo must play on alarm volume
    // todo must play even on home pressed
    // todo must play even when app is killed
    MediaPlayer mediaPlayer;
    private static final String TAG = "RingtoneService";
    int startId;
    boolean playing;
    Vibrator vibrator;
    long[] pattern = {0, 500, 500, 750, 750};
    int media_resources[] = {R.raw.tone_1, R.raw.tone_2, R.raw.tone_3, R.raw.tone_4, R.raw.tone_5};

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    @TargetApi(19)
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: in");
        String state = null;
        int ringtone = 0;
        if (intent.getExtras() != null) {
            state = intent.getExtras().getString("extra");
            try {
                ringtone = intent.getExtras().getInt("ringtone", 0);
            } catch (Exception e) {}
        }
        assert state != null;
        startId = (state.equalsIgnoreCase("Alarm on")) ? 1 : 0;
        Log.e(TAG, "onStartCommand: playing = "+playing+" startId = "+startId);
//
//        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        assert audioManager != null;
//        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),0);

//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
//        mediaPlayer.setAudioStreamType(AudioAttributes.USAGE_ALARM);

//        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        assert vibrator != null;

        ringtone = media_resources[ringtone];
        if (!this.playing && startId == 1) {
            Log.e(TAG, "onStartCommand: playing");
//            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
//            mediaPlayer.setAudioStreamType(AudioAttributes.USAGE_ALARM);
//            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer = MediaPlayer.create(this, ringtone);
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
//            mediaPlayer.setAudioStreamType(AudioAttributes.USAGE_ALARM);
//            mediaPlayer.setOnPreparedListener(this);
//            mediaPlayer.prepareAsync(); // prepare async to not block main thread
//            mediaPlayer.setAudioStreamType();
            mediaPlayer.start();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 1));
            } else {
                vibrator.vibrate(pattern, 1);
            }
            mediaPlayer.setLooping(true);
            this.playing = true;
            this.startId = 0;
        } else if (this.playing && startId == 0) {
            Log.e(TAG, "onStartCommand: stopping");
            vibrator.cancel();
            mediaPlayer.setLooping(false);
            this.playing = false;
            this.startId = 0;
            try {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e){
                Log.e(TAG, "onDestroy: error", e);
            }
        }
        return START_REDELIVER_INTENT;
    }

//    public void onPrepared(MediaPlayer player) {
//        player.start();
//        player.setLooping(true);
//        this.playing = true;
//        this.startId = 0;
//    }

    @Override
    public void onDestroy() {
        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        } catch (Exception e){
            Log.e(TAG, "onDestroy: error", e);
        }
        super.onDestroy();
    }
}