package com.example.dyson.a2_t2;

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
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private List<Bitmap> datas;
    private OnItemClickListener onItemClickListener;

    public MyAdapter(Context context, List<Bitmap> datas) {
        this.context = context;
        this.datas = datas;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void addItem(int index, Bitmap s) {
        datas.add(index, s);
        int a_size=datas.size();
        if (a_size>0){
            Log.d("system out","mdatas oversize again");

            int m=(a_size-1)/2;
            Iterator i1=datas.iterator();
            int count=0;
            List<Bitmap>md;
            md=new ArrayList<>();
            while (count<m+1){
                if (count==m){
                    md.add(datas.get(a_size-1));
                }
                else{
                    md.add(datas.get(count));
                }

                count=count+1;
            }
            datas=md;


        }
        Log.d("ststem out","size of data is:"+datas.size()+"in adapter");
        notifyItemInserted(index);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.rv_item, parent,
                false));
        return holder;
    }
    private byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv.setImageBitmap(datas.get(position));
        int adapterPosition = holder.getAdapterPosition();
        if (onItemClickListener != null) {
            //holder.itemView.setOnClickListener(new MyOnClickListener(position, datas.get(adapterPosition)));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ione=new Intent(context,FourthActivity.class);
                    byte[] bk=Bitmap2Bytes(datas.get(position));

                    ione.putExtra("photo",bk);
                    //ione.putExtra("content",datas[datas.get(position)]);
                    context.startActivity(ione);

                }
            });
            Log.d("clicked","execute"+position);
            //Intent ione=new Intent(ThirdActivity.class,FourthActivity.class);


            //Toast.makeText(,"shit",Toast.LENGTH_SHORT).show();
        }
    }
    private class MyOnClickListener implements View.OnClickListener {
        private int position;
        private Bitmap data;

        public MyOnClickListener(int position, Bitmap data) {
            this.position = position;
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v,position,data);


        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, Bitmap data);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (ImageView) view.findViewById(R.id.imageView2);
        }
    }
}
