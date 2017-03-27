package com.example.pawan.fundraise;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Profile_RV_holder extends RecyclerView.ViewHolder {

    TextView name;
    LinearLayout ll;

    public Profile_RV_holder(View itemView) {
        super(itemView);

        ll= (LinearLayout) itemView.findViewById(R.id.ll_pro_list);
        name= (TextView) itemView.findViewById(R.id.tv_rv_fundname);

    }
}
