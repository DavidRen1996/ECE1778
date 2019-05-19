package com.example.a3_t1;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import com.squareup.picasso.Picasso;

import java.util.List;

public class display_adapter extends RecyclerView.Adapter<display_adapter.Display_viewholder> {
    private Context context;
    private List<String>URL;
    private List<String>User;
    private List<String>Comment;

//context is 'this' from activity class
    public display_adapter(Context c,List<String>URL,List<String>User,List<String>Comment){
        this.URL=URL;
        this.User=User;
        this.Comment=Comment;
        this.context=c;
    }

    public void addItem(int index, String s,String t,String u) {
        //index assign the place where the new item will be placed, do not use a constant!!
        //use the length of the existing elements
        Log.d("ststem out","Added uri:"+u);
        URL.add(u);
        Log.d("ststem out","Added name:"+s);
        User.add(s);
        Log.d("ststem out","Added comment:"+t);
        Comment.add(t);

        //Log.d("ststem out","size of data is:"+datas.size()+"in adapter");
        notifyItemInserted(index);
    }
    @Override
    public Display_viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        //view holder is the placeholder for the view
        display_adapter.Display_viewholder holder = new display_adapter.Display_viewholder(LayoutInflater.from(
                context).inflate(R.layout.comments, parent,
                false));
        return holder;
    }


    @Override
    public void onBindViewHolder(Display_viewholder holder, final int position) {
        //all modification on the view should be done within this function by viewholder holder
        //you can consider viewholder as the view itself
        Log.d("system.out","load image uri:"+URL.get(position));
        Log.d("system.out","load comment:"+Comment.get(position));
        Picasso.get().load(URL.get(position)).into(holder.tv);
        holder.user.setText(User.get(position));
        holder.comment.setText(Comment.get(position));
        int adapterPosition = holder.getAdapterPosition();

    }

    @Override
    public int getItemCount() {
        return User.size();
    }

    class Display_viewholder extends RecyclerView.ViewHolder{
        //define the view holder for the recycler view,below are the contents included in that view
        ImageView tv;
        TextView user;
        TextView comment;
        public Display_viewholder(View view) {
            super(view);
            tv = (ImageView) view.findViewById(R.id.imageView6);
            user=(TextView)view.findViewById(R.id.textView7);
            comment=(TextView)view.findViewById(R.id.textView8);
        }
    }








}
