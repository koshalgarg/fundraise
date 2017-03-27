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

public class Profile_RV_adapter extends RecyclerView.Adapter<Profile_RV_holder> {

    ArrayList<Profile_RV> list;
    Context context;



    public Profile_RV_adapter(Context ProfileActivity, ArrayList<Profile_RV> list) {
        this.context=ProfileActivity;
        this.list=list;
    }

    @Override
    public Profile_RV_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_rv, parent, false);
        //layoutView.setMinimumHeight(parent.getMeasuredHeight() / 2);
        Profile_RV_holder rcv = new Profile_RV_holder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final Profile_RV_holder holder, final int position) {

        holder.name.setText(list.get(position).getName());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,ShowFund.class);
                i.putExtra("id",list.get(position).getFid());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
