package com.cse437.alarmapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private static RecyclerView.LayoutManager layoutManager;
    public static RecyclerAdapter recyclerAdapter;
    public static DBAdapter myDb;
    public static Cursor cursor;
    private static List<String> list;
    Switch sw;


    public static ArrayList<Integer>pointerToDbID;
    ArrayList<String> Hour;
    ArrayList<String>Minute;
    ArrayList<String>Enabled;
    ArrayList<String>Ringtone;
    static int recordsArrayIndex;
//    ConstraintLayout cl;
    int[] images;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        images=new int[4];
//        images[0]=R.drawable.morning1;
//        images[1]=R.drawable.morning2;
//        images[2]=R.drawable.morning3;
//        images[3]=R.drawable.morning4;
//        int idx = new Random().nextInt(images.length);
//        int random = images[idx];
//        cl=(ConstraintLayout)findViewById(R.id.ConstraintLayout);
//        cl.setBackgroundResource(random);
        pointerToDbID=new ArrayList<Integer>();
        Toast.makeText(this, "OnCreate", Toast.LENGTH_SHORT).show();

        openDB();
        list=new ArrayList<String>();

        SetListArray();




        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        // For the recycler view we need three things : Layout manager, View Holder, Adapter
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter= new RecyclerAdapter(list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);



        addButton=(Button)findViewById(R.id.AddAlarm);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AlarmActivity.class);
                startActivity(intent);
            }
        });
    }
    private void addDummyData(){
        long newID=myDb.insertRow("5","30","ringtone","enabled");
        long newID1=myDb.insertRow("5","45","ringtone","enabled");
        long newID2=myDb.insertRow("6","30","dsf","notenabled");
    }

    private void AccessDBAndQuery() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }
    private void closeDB() {
        myDb.close();
    }
    public static void SetListArray(){
        Cursor cursor = MainActivity.myDb.getAllRows();
        displayRecordSet(cursor);

    }

    private static void displayRecordSet(Cursor cursor) {
        recordsArrayIndex=0;
        if(cursor.moveToFirst()){
            do {
                // Process the data:

                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String hour = cursor.getString(DBAdapter.COL_HOUR);
                String minute = cursor.getString(DBAdapter.COL_MINUTE);
                String ringtone = cursor.getString(DBAdapter.COL_RINGTONE);
                String enabled = cursor.getString(DBAdapter.COL_Enabled);

                Log.i("MINA","Passed1");

                // Append data to the message:
                String r =id+","+
                        hour +":" + minute
                        +", " + ringtone
                        +", " + enabled
                        +"\n";
                pointerToDbID.add(recordsArrayIndex,id);

                list.add(recordsArrayIndex,r);
                Log.i("MINA","r is "+ r);

                recordsArrayIndex++;
            } while(cursor.moveToNext());
        }
    }

    public static void replaceItem(int position) {
        Log.i("MINA","POSITION IS "+position);
        int idd = pointerToDbID.get(position);
        cursor = myDb.getRow(idd);

        int id = cursor.getInt(DBAdapter.COL_ROWID);
        String hour = cursor.getString(DBAdapter.COL_HOUR);
        String minute = cursor.getString(DBAdapter.COL_MINUTE);
        String ringtone = cursor.getString(DBAdapter.COL_RINGTONE);
        String enabled = cursor.getString(DBAdapter.COL_Enabled);

        String r =id+","+
                hour +":" + minute
                +", " + ringtone
                +", " + enabled
                +"\n";

        list.set(position,r);

    }
}