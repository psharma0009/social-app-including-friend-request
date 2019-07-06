package com.eurakan.withmee.Adapters;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eurakan.withmee.Models.NotificationModel;
import com.eurakan.withmee.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 2/5/2019.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    List<NotificationModel> verticalList = Collections.emptyList();
    Context context;
    public NotificationAdapter(List<NotificationModel> horizontalList, Context context) {
        this.verticalList = horizontalList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView notific_title, notif_time;
        //LinearLayout parent;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View root) {
            super(root);
            notific_title = root.findViewById(R.id.notific_title);
            notif_time = root.findViewById(R.id.notif_time);
            viewBackground = root.findViewById(R.id.view_background);
            viewForeground = root.findViewById(R.id.view_foreground);
           // parent = (LinearLayout) root.findViewById(R.id.parent);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        NotificationModel movie = verticalList.get(position);
        holder.notific_title.setText(movie.getNotificationTitle());
        holder.notif_time.setText(String.valueOf(movie.getDateTime()));
    }

    @Override
    public int getItemCount() {
        return verticalList.size();
    }

    public void removeItem(int position) {
        verticalList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(NotificationModel item, int position) {
        verticalList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
