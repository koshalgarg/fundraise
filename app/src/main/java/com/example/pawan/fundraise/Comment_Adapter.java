package com.example.pawan.fundraise;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by pawan on 10/27/2016.
 */

public class Comment_Adapter extends RecyclerView.Adapter<Comment_Holder> {

    ArrayList<Comments> list;
    Context context;



    public Comment_Adapter(Context mainActivity, ArrayList<Comments> list) {
        this.context=mainActivity;
        this.list=list;
    }

    @Override
    public Comment_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        //layoutView.setMinimumHeight(parent.getMeasuredHeight() / 2);
        Comment_Holder rcv = new Comment_Holder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final Comment_Holder holder, final int position) {

        holder.uname.setText(list.get(position).getUname());

        holder.time.setText(list.get(position).getTime());
        holder.comment.setText(list.get(position).getComment());


        holder.uname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(context,Profile.class);
                i.putExtra("id",list.get(position).getUid());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
