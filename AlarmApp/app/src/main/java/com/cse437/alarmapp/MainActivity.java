package com.cse437.alarmapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
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
    public static List<String> list;
    Switch sw;


    public static ArrayList<Integer>pointerToDbID;
    static int recordsArrayIndex;
    int[] images;
    Button addButton;
    public static String TAG = "MINA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkFirstRun();
        openDB();
        SetListArray();

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        // For the recycler view we need three things : Layout manager, View Holder, Adapter
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter= new RecyclerAdapter(list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);

        //to put the divisors between recyclerView Item
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);

        addButton=(Button)findViewById(R.id.AddAlarm);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AlarmActivity.class);
                intent.putExtra("Create_Alarm",1);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        closeDB();
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
        pointerToDbID=new ArrayList<Integer>();
        list=new ArrayList<String>();
        if(cursor.moveToFirst()){
            do {
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String hour = cursor.getString(DBAdapter.COL_HOUR);
                String minute = cursor.getString(DBAdapter.COL_MINUTE);
                String ringtone = cursor.getString(DBAdapter.COL_RINGTONE);
                String enabled = cursor.getString(DBAdapter.COL_Enabled);

                // Append data to the message:
                String r = hour +":" + minute;
                pointerToDbID.add(recordsArrayIndex,id);
                Log.i("MINA","R is "+r);
                list.add(recordsArrayIndex,r);
                recordsArrayIndex++;
            } while(cursor.moveToNext());
        }
        else{
            list=new ArrayList<>();
            pointerToDbID=new ArrayList<>();
        }
    }
    public static void replaceItemInList(int position) {
        int idd = pointerToDbID.get(position);
        cursor = myDb.getRow(idd);

        int id = cursor.getInt(DBAdapter.COL_ROWID);
        String hour = cursor.getString(DBAdapter.COL_HOUR);
        String minute = cursor.getString(DBAdapter.COL_MINUTE);
        String ringtone = cursor.getString(DBAdapter.COL_RINGTONE);
        String enabled = cursor.getString(DBAdapter.COL_Enabled);

        String r = hour +":" + minute;

        list.set(position,r);
    }
    public static void deleteItem(int position){
        list.remove(position);
        pointerToDbID.remove(position);
    }

    public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){
            // Place your dialog code here to display the dialog

            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
            new AlertDialog.Builder(this).setTitle("Welcome to the Awesome App :D !").setMessage("If using Android 6.0 or higher, Please disable the battery optimization feature for the app    ").setNeutralButton("OK", null).show();
        }
    }
}