package com.eurakan.withmee.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eurakan.withmee.Models.CommentsModel;
import com.eurakan.withmee.Models.NotificationModel;
import com.eurakan.withmee.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 2/5/2019.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    List<CommentsModel> verticalList = Collections.emptyList();
    Context context;
    public CommentsAdapter(List<CommentsModel> horizontalList, Context context) {
        this.verticalList = horizontalList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView notific_title, notif_time;
        ImageView user_profile;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View root) {
            super(root);
            user_profile = root.findViewById(R.id.user_profile);
            notific_title = root.findViewById(R.id.notific_title);
            notif_time = root.findViewById(R.id.notif_time);
            viewBackground = root.findViewById(R.id.view_background);
            viewForeground = root.findViewById(R.id.view_foreground);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CommentsModel movie = verticalList.get(position);
        holder.notific_title.setText(movie.getUserName());
        holder.notif_time.setText(movie.getComment());
        Picasso.get().load(movie.getProfileImageUrl())
                .into(holder.user_profile);
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

    public void restoreItem(CommentsModel item, int position) {
        verticalList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
