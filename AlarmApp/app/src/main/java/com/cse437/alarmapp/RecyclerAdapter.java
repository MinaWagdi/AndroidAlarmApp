package com.cse437.alarmapp;

import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.List;

import hari.floatingtoast.FloatingToast;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<String> list;

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
        viewHolder.AlarmInfo.setText(list.get(i));
        if(list.get(i).contains("notenabled")){
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
                    MainActivity.myDb.updateRow(id,"enabled");
                    FloatingToast toast = FloatingToast.makeToast(viewHolder.s, "ALARM ENABLED", FloatingToast.LENGTH_MEDIUM);
                    toast.show();
                }else{
                    MainActivity.myDb.updateRow(id,"notenabled");
                    FloatingToast toast = FloatingToast.makeToast(viewHolder.s, "ALARM DISABLED", FloatingToast.LENGTH_MEDIUM);
                    toast.show();

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
                MainActivity.replaceItem(position);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //this class represents the view holder of the recycler view
    //responsible of each item in the list (each item is a toggle button and text view)
    // Each item is an object of the view holder class
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView AlarmInfo;
        Switch s;

        public MyViewHolder(View itemView) {
            super(itemView);
            AlarmInfo= itemView.findViewById(R.id.alarm_info);
            s=itemView.findViewById(R.id.switch1);
        }
    }
}
