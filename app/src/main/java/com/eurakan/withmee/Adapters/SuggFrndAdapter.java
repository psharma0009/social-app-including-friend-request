package com.eurakan.withmee.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eurakan.withmee.Activity.FriendActivity;
import com.eurakan.withmee.Models.FriendModel;
import com.eurakan.withmee.Models.SugFrndeModel;
import com.eurakan.withmee.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 1/22/2019.
 */

public class SuggFrndAdapter extends RecyclerView.Adapter<SuggFrndAdapter.MyViewHolder> {

    List<SugFrndeModel> verticalList = Collections.emptyList();
    Context context;

    public SuggFrndAdapter(List<SugFrndeModel> horizontalList, Context context) {
        this.verticalList = horizontalList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //LinearLayout parent;
        public TextView dashboardSugFriendName;
        public int userid, viewposition;
        public ImageView close, suggestedFriendView;
        public String status, dateofbirth, email,mobileno,address_user, worksat, profilePhotourl, friendN, studiesAt;
        public MyViewHolder(View root) {
            super(root);
            dashboardSugFriendName = root.findViewById(R.id.dashboardSugFriendName);
            suggestedFriendView = root.findViewById(R.id.suggestedFriendView);
            close = root.findViewById(R.id.dashboardSugFriendCancel);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(viewposition);
                }
            });

            // parent = (LinearLayout) root.findViewById(R.id.parent);
            suggestedFriendView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(context, FriendActivity.class);
                    in.putExtra("user_id", userid);
                    in.putExtra("name", friendN);
                    in.putExtra("email", email);
                    in.putExtra("status", status);
                    in.putExtra("dateofbirth", dateofbirth);
                    in.putExtra("mobileno", mobileno);
                    in.putExtra("worksat", worksat);
                    in.putExtra("profilePhotourl", profilePhotourl);
                    in.putExtra("address_user", address_user);
                    in.putExtra("studiesAt", studiesAt);
                    context.startActivity(in);
                }
            });
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggested_friends_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        SugFrndeModel movie = verticalList.get(position);
        holder.viewposition = position;
        holder.dashboardSugFriendName.setText(movie.GetFullName());
        holder.userid = movie.getId();
        holder.status = movie.getStatus();
        holder.dateofbirth = movie.getDateOfBirth();
        holder.email = movie.getEmail();
        holder.mobileno = movie.getMobile();
        holder.address_user = movie.getFullAddress();
        holder.worksat = movie.getWork();
        holder.profilePhotourl = movie.getProfileImageUrl();
        holder.friendN = movie.GetFullName();
        holder.studiesAt = movie.getStudyIn();
        Picasso.get().load(movie.getProfileImageUrl()).placeholder(R.drawable.placeholder)
                .into(holder.suggestedFriendView);
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

    public void restoreItem(SugFrndeModel item, int position) {
        verticalList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
