package com.eurakan.withmee.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eurakan.withmee.Models.ChatDetModel;
import com.eurakan.withmee.Models.NotificationModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 1/27/2019.
 */

public class ChatDetAdapter extends RecyclerView.Adapter<ChatDetAdapter.MyViewHolder> {

    List<ChatDetModel> verticalList = Collections.emptyList();
    Context context;

    public ChatDetAdapter(List<ChatDetModel> horizontalList, Context context) {
        this.verticalList = horizontalList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView comment;
        LinearLayout singleMessageContainer;
        ImageView image;

        public MyViewHolder(View root) {
            super(root);
            comment = (TextView) root.findViewById(R.id.comment);
            image=(ImageView)root.findViewById(R.id.image);
            singleMessageContainer = (LinearLayout) root.findViewById(R.id.singleMessageContainer);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_det_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ChatDetModel movie = verticalList.get(position);
        UserModel user= AppPreferences.getInstance(context).getUser();
        if (movie.sender == user.getId()){
            holder.comment.setBackgroundResource(R.drawable.bubble_a);
            holder.comment.setText(movie.message);
            holder.singleMessageContainer.setGravity(Gravity.RIGHT);
            holder.image.setVisibility(View.GONE);
        } else{
            holder.comment.setBackgroundResource(R.drawable.bubble_b);
            holder.comment.setText(movie.message);
            holder.singleMessageContainer.setGravity(Gravity.LEFT);
            holder.image.setVisibility(View.VISIBLE);
        }

        //String name = verticalList.get(position).name;
        //SpannableString s = new SpannableString(name);
        //s.setSpan(new RelativeSizeSpan(0.7f), 0, name.length(), 0);

        //String comm = verticalList.get(position).comment;
        //CharSequence charr = TextUtils.concat(s, "\n", comm);
        //holder.comment.setText(charr);

    }

    @Override
    public int getItemCount() {
        return verticalList.size();
    }
}
