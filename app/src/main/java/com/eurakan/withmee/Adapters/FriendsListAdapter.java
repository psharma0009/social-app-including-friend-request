package com.eurakan.withmee.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eurakan.withmee.Activity.ChatDetail;
import com.eurakan.withmee.Models.ChatModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 1/26/2019.
 */

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.MyViewHolder> {
    List<UserModel> verticalList = Collections.emptyList();
    Context context;

    public FriendsListAdapter(List<UserModel> horizontalList, Context context) {
        this.verticalList = horizontalList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView parent;
        public TextView profileFriendName;
        public ImageView profileFriendView;
        public int friend_Id;
        public MyViewHolder(View root) {
            super(root);
            profileFriendView = root.findViewById(R.id.profileFriendView);
            profileFriendName = root.findViewById(R.id.profileFriendName);
            parent = root.findViewById(R.id.parent);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        UserModel movie = verticalList.get(position);
        holder.profileFriendName.setText(movie.GetFullName());
        Picasso.get().load(movie.getProfileImageUrl()).placeholder(R.drawable.placeholder).into(holder.profileFriendView);
        holder.friend_Id = movie.getId();
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ChatDetail.class);
                i.putExtra("friend_id", holder.friend_Id);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return verticalList.size();
    }



}
