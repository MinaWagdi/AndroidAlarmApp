package com.cse437.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import hari.floatingtoast.FloatingToast;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<String> list;
    RecyclerView mRecyclerView;

    public RecyclerAdapter(List<String>list){
        this.list=list;
    }

    //The layout manager will call this method to create each object of the MyViewHolder class
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.clock_item_row,viewGroup,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    // From this method we can assign data to the view components in the myViewHolder object
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, int i) {
        final int position = i;
        String enabled= MainActivity.myDb.getRow(MainActivity.pointerToDbID.get(i)).getString(DBAdapter.COL_Enabled);
        viewHolder.AlarmInfo.setText(list.get(i));
        if(enabled.contains("notenabled")){
            Log.i(MainActivity.TAG,"Set checked to false ");
            viewHolder.s.setChecked(false);
        }
        else{
            viewHolder.s.setChecked(true);
        }

        viewHolder.s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = MainActivity.pointerToDbID.get(position);
                Log.i("MINA","id is "+id);
                if(viewHolder.s.isChecked()){
                    Log.i(MainActivity.TAG,"check swtich ");
                    MainActivity.myDb.updateEnabled(id,"enabled");
                    FloatingToast toast = FloatingToast.makeToast(viewHolder.s, "ALARM ENABLED", FloatingToast.LENGTH_MEDIUM);
                    toast.show();


                    final Calendar calendar=Calendar.getInstance();
                    int hour =  Integer.parseInt(MainActivity.myDb.getRow(MainActivity.pointerToDbID.get(position)).getString(DBAdapter.COL_HOUR));
                    int minute =  Integer.parseInt(MainActivity.myDb.getRow(MainActivity.pointerToDbID.get(position)).getString(DBAdapter.COL_MINUTE));
                    calendar.set(Calendar.HOUR_OF_DAY,hour);
                    calendar.set(Calendar.MINUTE,minute);

                    int req = Integer.parseInt(MainActivity.myDb.getRow(MainActivity.pointerToDbID.get(position)).getString(DBAdapter.ALARM_CODE).substring(3));
                    Intent intent = new Intent(v.getContext(),AlarmReceiver.class);
                    PendingIntent pendingIntent=PendingIntent.getBroadcast(v.getContext(), req, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmActivity.alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }else{
                    Log.i(MainActivity.TAG,"uncheck switch");
                    MainActivity.myDb.updateEnabled(id,"notenabled");
                    FloatingToast toast = FloatingToast.makeToast(viewHolder.s, "ALARM DISABLED", FloatingToast.LENGTH_MEDIUM);
                    toast.show();

                    int req = Integer.parseInt(MainActivity.myDb.getRow(MainActivity.pointerToDbID.get(position)).getString(DBAdapter.ALARM_CODE).substring(3));
                    Log.i(MainActivity.TAG,"Request code to uncheck is "+req);
                    Intent intent = new Intent(v.getContext(),AlarmReceiver.class);
                    PendingIntent pendingIntent=PendingIntent.getBroadcast(v.getContext(), req, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmActivity.alarmManager.cancel(pendingIntent);

//                    FloatingToast.makeToast(viewHolder.s, "Alarm Disabled", FloatingToast.LENGTH_LONG)
//                            .setGravity(FloatingToast.GRAVITY_MID_TOP)
//                            .setFadeOutDuration(FloatingToast.FADE_DURATION_LONG)
//                            .setTextSizeInDp(12)
//                            .setBackgroundBlur(true)
//                            .setFloatDistance(FloatingToast.DISTANCE_SHORT)
//                            .setTextColor(Color.parseColor("#ffffff"))
//                            .setShadowLayer(5, 1, 1, Color.parseColor("#000000"))
//                            .setTextTypeface(customFont)
//                            .show();

                }
                MainActivity.replaceItemInList(position);
                notifyDataSetChanged();
            }
        });
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick){

                    Toast.makeText(view.getContext(), "Alarm Deleted", Toast.LENGTH_SHORT).show();

                    MainActivity.myDb.deleteRow(MainActivity.pointerToDbID.get(position));
                    MainActivity.deleteItem(position);

                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position,MainActivity.list.size());

                }
                else{
                    Intent intent = new Intent(view.getContext(),AlarmActivity.class);
                    intent.putExtra("EDIT_ALARM",position);
                    view.getContext().startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;

    }

    //this class represents the view holder of the recycler view
    //responsible of each item in the list (each item is a toggle button and text view)
    // Each item is an object of the view holder class
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{

        TextView AlarmInfo;
        Switch s;
        ItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            AlarmInfo= itemView.findViewById(R.id.alarm_info);
            s=itemView.findViewById(R.id.switch1);

            AlarmInfo.setOnClickListener(this);
            AlarmInfo.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
        }
    }
}
