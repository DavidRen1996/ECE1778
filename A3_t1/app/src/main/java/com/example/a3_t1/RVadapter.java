package com.example.a3_t1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RVadapter extends RecyclerView.Adapter<RVadapter.RV_viewholder> {
    private Context context;
    private List<String>datas;
    private List<String>timestamps;
    private OnItemClickListener onItemClickListener;
    private String Email;
    private List<String>owner;
    public RVadapter(Context context,List<String>data,List<String>timestamps,String email,List<String>OWN){
        this.context=context;
        this.datas=data;
        this.timestamps=timestamps;
        this.Email=email;
        //owner is the list that contains the owners of the photoes
        this.owner=OWN;

    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void addItem(int index, String s,String t,String O) {
        datas.add(s);
        timestamps.add(t);
        owner.add(O);
        //Log.d("ststem out","size of data is:"+datas.size()+"in adapter");
        notifyItemInserted(index);
    }
    @Override
    public RV_viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        RV_viewholder holder = new RV_viewholder(LayoutInflater.from(
                context).inflate(R.layout.personal_feed, parent,
                false));
        return holder;
    }


    @Override
    public void onBindViewHolder(RV_viewholder holder, final int position) {
        //holder.tv.setImageBitmap(datas.get(position));
        Picasso.get().load(datas.get(position)).into(holder.tv);
        int adapterPosition = holder.getAdapterPosition();
        if (onItemClickListener != null) {
            //holder.itemView.setOnClickListener(new MyOnClickListener(position, datas.get(adapterPosition)));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("system.out","click");
                    Intent home2display=new Intent(context,Display.class);
                    String uri=datas.get(position);
                    String time_direct=timestamps.get(position);
                    String own=owner.get(position);
                    String e=Email;
                    home2display.putExtra("photouri",uri);
                    Log.d("system.out","time direct is:"+time_direct);
                    home2display.putExtra("time2direct",time_direct);
                    home2display.putExtra("owner",own);
                    home2display.putExtra("e",e);
                    context.startActivity(home2display);

                }
            });
        }
        else{
            Log.d("system.out","lister is null");
        }
    }
    private class MyOnClickListener implements View.OnClickListener {
        private int position;
        private String data;

        public MyOnClickListener(int position, String data) {
            this.position = position;
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v,position,data);


        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, String data);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class RV_viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView tv;

        public RV_viewholder(View view) {
            super(view);
            view.setOnClickListener(this);
            tv = (ImageView) view.findViewById(R.id.imageView3);
        }
        @Override
        public void onClick(View view) {
            Log.d("system.out","click");
            int k=getLayoutPosition();
            Log.d("system.out","click"+k);
            Intent home2display=new Intent(context,Display.class);
            //String uri=datas.get(position);
            //home2display.putExtra("photouri",uri);
            context.startActivity(home2display);
        }
    }
}
