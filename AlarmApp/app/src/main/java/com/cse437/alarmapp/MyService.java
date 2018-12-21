package com.cse437.alarmapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {

    MediaPlayer mediaPlayer;
    int start_id;
    boolean isRunning=false;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String state = intent.getExtras().getString("extra");
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
        Log.i("MINA","state is "+state+" and start id is "+start_id);
        if(!isRunning&&start_id==1){
            mediaPlayer=MediaPlayer.create(this,R.raw.ringtone);
            mediaPlayer.start();
            this.isRunning=true;
            start_id=0;
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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
