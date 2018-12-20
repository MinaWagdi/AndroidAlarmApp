package com.cse437.alarmapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter recyclerAdapter;
    public static DBAdapter myDb;
    Cursor cursor;
    private List<String> list;
    Switch sw;

    ArrayList<String> Hour;
    ArrayList<String>Minute;
    ArrayList<String>Enabled;
    ArrayList<String>Ringtone;
    int recordsArrayIndex=0;

    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDB();
        myDb.deleteAll();

        list=new ArrayList<String>();


        addDummyData();
        SetListArray();


        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        // For the recycler view we need three things : Layout manager, View Holder, Adapter
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter= new RecyclerAdapter(list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);





        AccessDBAndQuery();
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
    public void SetListArray(){
        Cursor cursor = MainActivity.myDb.getAllRows();
        displayRecordSet(cursor);
    }

    private void displayRecordSet(Cursor cursor) {
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
                String r = id
                        +", " + hour
                        +", " + minute
                        +", " + ringtone
                        +", " + enabled
                        +"\n";
//
//                Hour.add(recordsArrayIndex,hour);
//                Minute.add(recordsArrayIndex,minute);
//                Ringtone.add(recordsArrayIndex,ringtone);
//                Enabled.add(recordsArrayIndex,enabled);
//                Log.i("MINA","Passed2");

                list.add(recordsArrayIndex,r);
                Log.i("MINA","Passed3");
                // [TO_DO_B6]
                // Create arraylist(s)? and use it(them) in the list view
                recordsArrayIndex++;
            } while(cursor.moveToNext());
        }
    }
}