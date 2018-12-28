package com.cse437.alarmapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {
    Button TurnAlarmOn;

    static AlarmManager alarmManager;
    TimePicker timePicker;

    PendingIntent pendingIntent;

    boolean openedActivityForEdit = false;
    int PositionToEdit = 0;

    int requestCode=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //Get the request Code from a sharedPreference
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        requestCode = sp.getInt("req_code", -1);
        //if nothing was saved in shared preference set request code to 0
        if(requestCode==-1){
            Log.i(MainActivity.TAG,"request code is -1");
            requestCode=1;
        }
        else{
            Log.i(MainActivity.TAG,"request code is "+requestCode);
        }

        // if this activity was called for editing an
        // existing alarm, get the position of the item to edit
        if(getIntent().getIntExtra("EDIT_ALARM",-1)!=-1){
            Log.i(MainActivity.TAG,"EDIT ALARM");
            openedActivityForEdit=true;
            PositionToEdit =getIntent().getIntExtra("EDIT_ALARM",-1);
            if(PositionToEdit==-1){
                Toast.makeText(this, "PositionToEdit was found to be equal -1", Toast.LENGTH_SHORT).show();
            }
        }

        final Calendar calendar=Calendar.getInstance();

        timePicker=(TimePicker)findViewById(R.id.time_picker);
        TurnAlarmOn=(Button)findViewById(R.id.turnOnBtn);
//        TurnAlarmOff=(Button)findViewById(R.id.turnOffBtn);

        alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);

        final Intent IntentForAlarmReceiver = new Intent(this,AlarmReceiver.class);

        TurnAlarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minute);

                String hourString=""+hour;
                String minString="";
                //Just for formatting
                if(minute<10){
                    minString="0"+minute;
                }
                else{
                    minString=""+minute;
                }


                if(openedActivityForEdit){
                    int id = MainActivity.pointerToDbID.get(PositionToEdit);

                    //changes to database and RecyclerView
                    MainActivity.myDb.updateHour(id,hourString);
                    MainActivity.myDb.updateMinute(id,minString);
                    MainActivity.replaceItemInList(PositionToEdit);
                    MainActivity.recyclerAdapter.notifyDataSetChanged();

                    //get the corrsponding request code
                    MainActivity.cursor=MainActivity.myDb.getRow(id);
                    String RequestCode = MainActivity.cursor.getString(DBAdapter.ALARM_CODE);
                    int req_code = Integer.parseInt(RequestCode.substring(3));

                    //changes to the alarm manager
                    alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    IntentForAlarmReceiver.putExtra("extra", "alarm on");
                    IntentForAlarmReceiver.putExtra("request_code", req_code);
                    //create a pending intent that delays the intent untill the specified calendar time
                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), req_code, IntentForAlarmReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }

                // CREATING A  NEW ALARM
                else {
                    Log.i(MainActivity.TAG,"Creating a new Alarm");
                    Log.i(MainActivity.TAG,"Alarm Code in AlarmActivity is equal to "+requestCode);
                    //if adding another alarm
                    IntentForAlarmReceiver.putExtra("request_code", requestCode);
                    IntentForAlarmReceiver.putExtra("extra", "alarm on");

                    pendingIntent=PendingIntent.getBroadcast(getApplicationContext(), requestCode, IntentForAlarmReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pendingIntent);

                    MainActivity.myDb.insertRow("" + hourString, "" + minString, "ringtone", "enabled","req"+requestCode);
                    requestCode++;
                    saveRequestCodeInSP();
                }
                //Return to the previous main activity
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void saveRequestCodeInSP(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("req_code", requestCode);
        editor.commit();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
