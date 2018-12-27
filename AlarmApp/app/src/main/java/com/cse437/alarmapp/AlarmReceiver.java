package com.cse437.alarmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    int alarmCode;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(MainActivity.TAG,"in onReceive");
        String message = intent.getExtras().getString("extra");
        alarmCode= intent.getIntExtra("request_code",-1);
        if(alarmCode==-1){
            Toast.makeText(context, "alarm Code in AlarmReceiver is -1", Toast.LENGTH_SHORT).show();
            alarmCode=0;
        }
        Intent service_intent = new Intent(context,MyService.class);
        service_intent.putExtra("extra",message);
        service_intent.putExtra("request_code",alarmCode);
        context.startService(service_intent);

    }
}
