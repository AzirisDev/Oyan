package com.example.oyan.ui.alarm.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.example.oyan.MainActivity;
import com.example.oyan.R;
import com.example.oyan.ui.alarm.RingActivity;

import static com.example.oyan.ui.alarm.application.App.CHANNEL_ID;
import static com.example.oyan.ui.alarm.broadcastreceiver.AlarmBroadcastReceiver.TITLE;

public class AlarmService extends Service {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String melody = sharedPreferences.getString("alarm_melody","oyan");

        if(melody.equals("oyan")){
            mediaPlayer = MediaPlayer.create(this, R.raw.oyan);
        } else if(melody.equals("wake_up")){
            mediaPlayer = MediaPlayer.create(this, R.raw.wake_up);
        } else if(melody.equals("trumpet")){
            mediaPlayer = MediaPlayer.create(this, R.raw.trumpet);
        } else if(melody.equals("gamelan")){
            mediaPlayer = MediaPlayer.create(this, R.raw.gamelan_hit_soft);
        }

        mediaPlayer.setLooping(true);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, RingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        String alarmTitle = String.format("%s Alarm", intent.getStringExtra(TITLE));

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText("Oyan oyan ... oyan Oyan")
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        if(sharedPreferences.getBoolean("enable_sound",true)){
            mediaPlayer.start();
        }

        long[] pattern = { 0, 100, 1000 };
        vibrator.vibrate(pattern, 0);

        startForeground(1, notification);

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
