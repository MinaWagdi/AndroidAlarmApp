package com.cse437.alarmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MINA","Receiver works");
        Toast.makeText(context, "Receiver works", Toast.LENGTH_SHORT).show();

        String message = intent.getExtras().getString("extra");

        Intent service_intent = new Intent(context,MyService.class);
        service_intent.putExtra("extra",message);
        context.startService(service_intent);
        Toast.makeText(context, "start Service called", Toast.LENGTH_SHORT).show();

    }
}
