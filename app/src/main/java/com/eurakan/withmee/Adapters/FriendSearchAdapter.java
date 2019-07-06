package com.eurakan.withmee.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eurakan.withmee.Activity.FriendActivity;
import com.eurakan.withmee.Models.FriendModel;
import com.eurakan.withmee.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 2/5/2019.
 */

public class FriendSearchAdapter extends RecyclerView.Adapter<FriendSearchAdapter.MyViewHolder>  {

    List<FriendModel> verticalList = Collections.emptyList();
    Context context;
    private static LayoutInflater inflater = null;
    public RelativeLayout viewForeground;


    public FriendSearchAdapter(List<FriendModel> horizontalList, Context context) {
        this.verticalList = horizontalList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //LinearLayout parent;
        public TextView friendName, friendCity, friendStudy, addButton;
        public int userid;
        public ImageView friendImage;
        public String status, dateofbirth, email,mobileno,address_user, worksat, profilePhotourl, friendN, studiesAt;
        public MyViewHolder(View root) {
            super(root);
            friendImage = root.findViewById(R.id.friendImage);
            friendName = root.findViewById(R.id.friendsName);
            friendCity = root.findViewById(R.id.friendDetailsStudy);
            friendStudy = root.findViewById(R.id.friendDetailsCity);
            viewForeground = root.findViewById(R.id.view_foreground);
            addButton = root.findViewById(R.id.addButton);
           // parent = (LinearLayout) root.findViewById(R.id.parent);
            addButton.setOnClickListener(new View.OnClickListener() {
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        FriendModel movie = verticalList.get(position);
        holder.friendName.setText(movie.GetFullName());
        holder.friendCity.setText("Lives In:"+ String.valueOf(movie.getCity()));
        holder.friendStudy.setText("Studies At:" + String.valueOf(movie.getStudyIn()));
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
        Picasso.get().load(movie.getProfileImageUrl()).placeholder(R.drawable.placeholder).into(holder.friendImage);
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

    public void restoreItem(FriendModel item, int position) {
        verticalList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

//    @Override
//    public int getCount() {
//        return 10;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return position;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View rowView;
//        Button buy_now;
//
//        inflater = (LayoutInflater) this.context.
//                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        rowView = inflater.inflate(R.layout.friend_item, null);
//        buy_now = (Button) rowView.findViewById(R.id.buy_now);
//        FriendModel movie = verticalList.get(position);
//        friendName = rowView.findViewById(R.id.friendsName);
//        friendCity = rowView.findViewById(R.id.friendDetailsStudy);
//        friendStudy = rowView.findViewById(R.id.friendDetailsCity);
//        viewForeground = rowView.findViewById(R.id.view_foreground);
//
//        friendName.setText(movie.GetFullName());
//        friendCity.setText(String.valueOf(movie.getCity()));
//        friendStudy.setText(String.valueOf(movie.getStudyIn()));
//
//        viewForeground.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(context, ProductDetail.class);
//                context.startActivity(i);
//            }
//        });
//
//        return rowView;
//    }
}
