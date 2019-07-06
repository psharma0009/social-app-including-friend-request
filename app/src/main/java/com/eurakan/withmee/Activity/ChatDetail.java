package com.eurakan.withmee.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eurakan.withmee.Adapters.ChatDetAdapter;
import com.eurakan.withmee.Models.ChatDetModel;
import com.eurakan.withmee.Models.ChatModel;
import com.eurakan.withmee.Models.PendingFriendRequestModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatDetail extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar;
    RecyclerView recycler_view;
    List<ChatDetModel> list;
    ChatDetAdapter adapter;
    int friendUserId;
    LinearLayoutManager LayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String comments_string;
    EditText comments;
    TextView send;
    int chat_room_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Chat");

        Bundle extras = getIntent().getExtras();
        comments = (EditText) findViewById(R.id.textmessage);
        send = (TextView) findViewById(R.id.send);
        if (extras != null) {
            friendUserId = extras.getInt("friend_id");
            // and get whatever type user account id is
        }
        LayoutManager = new LinearLayoutManager(ChatDetail.this, LinearLayoutManager.VERTICAL, false);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comments.getText().toString().trim().isEmpty()) {

                } else {
                    comments_string=comments.getText().toString().trim();
                    comments.setText("");
                    sendComment();
                }
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<>();

        recycler_view.setLayoutManager(new LinearLayoutManager(ChatDetail.this, LinearLayoutManager.VERTICAL, false));

        getMessagesList();
    }

    private void setChatList() {
        adapter = new ChatDetAdapter(list, ChatDetail.this);
        adapter.notifyDataSetChanged();
        recycler_view.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void sendComment() {

        //if it passes all the validations

        class ChangePasswordData extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("Message", String.valueOf(comments_string));
                UserModel user= AppPreferences.getInstance(getApplicationContext()).getUser();
                params.put("sender_id", String.valueOf(user.getId()));
                params.put("chat_room_id", String.valueOf(chat_room_id));

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_GETCHATSENDMESSAGES, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if (obj.getString("status").equalsIgnoreCase("Success")) {
//                        View contextView = findViewById(R.id.changePasswordSubmit);
//                        Toast.makeText(getApplicationContext(), obj.getString("response"), Toast.LENGTH_SHORT).show();
//                        Snackbar.make(contextView, "Password Changed Successfully", Snackbar.LENGTH_SHORT)
//                                .show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                    onRefresh();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        ChangePasswordData ru = new ChangePasswordData();
        ru.execute();
    }

    private void getMessagesList() {

        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(ChatDetail.this);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                UserModel user= AppPreferences.getInstance(getApplicationContext()).getUser();

                params.put("user_id", String.valueOf(user.getId()));
                params.put("friend_id", String.valueOf(friendUserId));
                params.put("roll", "1");

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_GETCHATFRIENDSMESSAGES, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Fetching Chat....");
                //show dialog
                asyncDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                //hiding the progressbar after completion
                asyncDialog.dismiss();

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    mSwipeRefreshLayout.setRefreshing(false);
                    //if no error in response
                    if (obj.getString("status").equalsIgnoreCase("Success")) {
                        Toast.makeText(getApplicationContext(), obj.getString("response"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONArray notificationJson = obj.getJSONArray("data");
                        for (int i = 0, size = notificationJson.length(); i < size; i++) {
                            JSONObject objectInArray = notificationJson.getJSONObject(i);
                            ChatDetModel friendItem = null;
                            try {
                                friendItem = new ChatDetModel(
                                        objectInArray.getInt("chat_room_id"),
                                        objectInArray.getString("Message"),
                                        objectInArray.getString("createdAt"),
                                        objectInArray.getInt("sender_id")
                                );
                            }
                            catch (ParseException e) {
                                mSwipeRefreshLayout.setRefreshing(false);
                                e.printStackTrace();
                            }
                            if(friendItem != null) {
                                chat_room_id = objectInArray.getInt("chat_room_id");
                                friendItem.chatroomid  = objectInArray.getInt("chat_room_id");
                                list.add(friendItem);
                            }
                        }
                        setChatList();

                    }else if (obj.getString("status").equalsIgnoreCase("Warning")) {
                        chat_room_id = obj.getInt("chat_room_id");
                        Toast.makeText(getApplicationContext(), "No Chat to Show", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }

    @Override
    public void onRefresh() {
        list.clear();
        getMessagesList();
    }
}
