package com.cse437.alarmapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {

    private static final String CHANNEL_ID = "1";
    public static  MediaPlayer mediaPlayer;
    int start_id;
    boolean isRunning=false;
    NotificationManager notifyManager;
    int AlarmCode=0;
    String state;
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Intent i = getIn
//        AlarmCode=getInten
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        AlarmCode=intent.getIntExtra("request_code",-1);
        if(AlarmCode==-1){
            Log.i(MainActivity.TAG,"Alarm Code in MyService is equal to 1");
            AlarmCode=0;
        }
        else{
            Log.i(MainActivity.TAG,"Alarm Code in MyService is equal to "+AlarmCode);
        }
        state = intent.getExtras().getString("extra");
        switch (state) {
            case "alarm on":
                start_id = 1;
                break;
            case "alarm off":
                start_id = 0;
                break;
            default:
                start_id = 0;
                break;
        }

        if(!isRunning&&start_id==1){
            mediaPlayer=MediaPlayer.create(this,R.raw.ringtone);
            mediaPlayer.start();
            this.isRunning=true;
            start_id=0;
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            //Notifications
            //set up the notification service
            Intent RingIntent = new Intent(this, RingActivity.class);
            RingIntent.putExtra("request_code",AlarmCode);
            RingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent_RingActivity = PendingIntent.getActivity(this, AlarmCode, RingIntent, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle("My notification")
                    .setContentText("Hello World!")
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent_RingActivity)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, mBuilder.build());
        }else if(!isRunning&&start_id==0){
            this.isRunning=false;
            this.start_id=0;

        }else if(isRunning&&start_id==1){
            this.isRunning=true;
            this.start_id=1;

        }
        else if(isRunning&&start_id==0){
            mediaPlayer.stop();
            mediaPlayer.reset();
            this.isRunning=false;
            start_id=0;
        }
//        mediaPlayer=MediaPlayer.create(this,R.raw.ringtone);
        return Service.START_STICKY;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notifyManager = getSystemService(NotificationManager.class);
            notifyManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
