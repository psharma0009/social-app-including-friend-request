package com.eurakan.withmee.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.eurakan.withmee.Adapters.ChatAdapter;
import com.eurakan.withmee.Models.ChatModel;
import com.eurakan.withmee.Models.NotificationModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Chats extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recycler_view;
    List<ChatModel> list;
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Chats");


        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<>();

        recycler_view.setLayoutManager(new LinearLayoutManager(Chats.this, LinearLayoutManager.VERTICAL, false));

        getChatList();

    }

    private void setChatList() {
        adapter = new ChatAdapter(list, Chats.this);
        adapter.notifyDataSetChanged();
        recycler_view.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getChatList() {

        //if it passes all the validations

        class ChatList extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(Chats.this);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                UserModel user= AppPreferences.getInstance(getApplicationContext()).getUser();
                params.put("user_id", String.valueOf(user.getId()));

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_GETCHATFRIENDSLIST, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Fetching Friends Chat List....");
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
                    List<Integer> a = new ArrayList();
                    //if no error in response
                    if (obj.getString("status").equalsIgnoreCase("Success")) {
                        Toast.makeText(getApplicationContext(), obj.getString("response"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONArray userJson = obj.getJSONArray("data");
                        for (int i = 0, size = userJson.length(); i < size; i++) {
                            JSONObject objectInArray = userJson.getJSONObject(i);
                            ChatModel user = new ChatModel(
                                    objectInArray.getInt("friend_id"),
                                    objectInArray.getString("friend_mobileno"),
                                    objectInArray.getString("friend_email"),
                                    objectInArray.getString("friend_name"),
                                    "",
                                    "",
                                    objectInArray.getString("friend_address_user"),
                                    objectInArray.getString("friend_worksat"),
                                    objectInArray.getString("friend_status"),
                                    objectInArray.getString("friend_address_user"),
                                    objectInArray.getString("friend_studyin"),
                                    objectInArray.getString("friend_dateofbirth")
                            );

                            if(a.contains(user.getId()) == false) {
                                list.add(user);
                                a.add(user.getId());
                            }
                        }
                        setChatList();
                    }
                    else if (obj.getString("status").equalsIgnoreCase("Warning")) {
                        Toast.makeText(getApplicationContext(), "No Friends found", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        ChatList ru = new ChatList();
        ru.execute();
    }

}
