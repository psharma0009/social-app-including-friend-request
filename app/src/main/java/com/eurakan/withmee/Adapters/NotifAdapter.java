package com.eurakan.withmee.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eurakan.withmee.Models.NotifModel;
import com.eurakan.withmee.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 1/22/2019.
 */

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.MyViewHolder> {

    List<NotifModel> verticalList = Collections.emptyList();
    Context context;

    public NotifAdapter(List<NotifModel> horizontalList, Context context) {
        this.verticalList = horizontalList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView package_image;

        public MyViewHolder(View root) {
            super(root);

            //actual_price = (TextView) root.findViewById(R.id.actual_price);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        //holder.actual_price.setPaintFlags(holder.actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }

    @Override
    public int getItemCount() {
        return verticalList.size();
    }
}
