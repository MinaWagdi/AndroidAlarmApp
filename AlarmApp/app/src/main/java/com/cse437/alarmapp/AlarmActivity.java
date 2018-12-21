package com.cse437.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {
    Button TurnAlarmOn;
    Button TurnAlarmOff;

    AlarmManager alarmManager;
    AlarmManager alarmManager2;
    TimePicker timePicker;

    TextView update_text;
    PendingIntent pendingIntent;
    PendingIntent pendingIntent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        update_text=(TextView)findViewById(R.id.update_text);
        timePicker=(TimePicker)findViewById(R.id.time_picker);
        alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager2=(AlarmManager)getSystemService(ALARM_SERVICE);

        final Calendar calendar=Calendar.getInstance();

        TurnAlarmOn=(Button)findViewById(R.id.turnOnBtn);
        TurnAlarmOff=(Button)findViewById(R.id.turnOffBtn);

        final Intent IntentForAlarmReceiver = new Intent(this.getApplicationContext(),AlarmReceiver.class);
        final Intent IntentForRingActivity = new Intent(this.getApplicationContext(),RingActivity.class);


        TurnAlarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                String hourString=""+hour;
                String minString="";
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minute);

                if(minute<10){
                    minString="0"+minute;
                    update_text.setText("Alarm ON : "+hour+":0"+minute);
                }
                else{
                    minString=""+minute;
                    update_text.setText("Alarm ON : "+hour+":"+minute);
                }

                IntentForAlarmReceiver.putExtra("extra","alarm on");
                //create a pending intent that delays the intent untill the specified calendar time
                pendingIntent=PendingIntent.getBroadcast(AlarmActivity.this,0,IntentForAlarmReceiver,PendingIntent.FLAG_UPDATE_CURRENT);
                Toast.makeText(AlarmActivity.this, "after initializing pending Intent", Toast.LENGTH_SHORT).show();
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

                pendingIntent2=PendingIntent.getActivity(AlarmActivity.this,0,IntentForRingActivity,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager2.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent2);

                Toast.makeText(AlarmActivity.this, "after alarm manager initialized"+calendar.getTimeInMillis(), Toast.LENGTH_SHORT).show();
                MainActivity.myDb.insertRow(""+hourString,""+minString,"ringtone","enabled");

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        TurnAlarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_text.setText("Alarm OFF");
                IntentForAlarmReceiver.putExtra("extra","alarm off");
                alarmManager.cancel(pendingIntent);
                sendBroadcast(IntentForAlarmReceiver);
            }
        });




    }
}
