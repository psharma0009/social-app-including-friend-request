package com.eurakan.withmee.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eurakan.withmee.Activity.ChatDetail;
import com.eurakan.withmee.Activity.LoginActivity;
import com.eurakan.withmee.Activity.PendingFriendRequests;
import com.eurakan.withmee.Activity.SplashActivity;
import com.eurakan.withmee.Models.ChatModel;
import com.eurakan.withmee.Models.NotificationModel;
import com.eurakan.withmee.Models.PendingFriendRequestModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 1/26/2019.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    List<ChatModel> verticalList = Collections.emptyList();
    Context context;

    public ChatAdapter(List<ChatModel> horizontalList, Context context) {
        this.verticalList = horizontalList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parent;
        public TextView chatFriendName, chatFriendCity;
        public int friend_Id;
        public MyViewHolder(View root) {
            super(root);
            chatFriendName = root.findViewById(R.id.chatFriendName);
            chatFriendCity = root.findViewById(R.id.chatFriendCity);
            parent = (LinearLayout) root.findViewById(R.id.parent);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ChatModel movie = verticalList.get(position);
        holder.chatFriendName.setText(movie.GetFullName());
        holder.chatFriendCity.setText(String.valueOf(movie.getFullAddress()));
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
