package com.eurakan.withmee.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.eurakan.withmee.Adapters.NotificationAdapter;
import com.eurakan.withmee.Adapters.PendingFriendRequestsAdapter;
import com.eurakan.withmee.Models.FriendModel;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PendingFriendRequests extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recycler_view;
    RecyclerView recycler_viewnew;
    List<PendingFriendRequestModel> sentList;
    List<PendingFriendRequestModel> receivedList;
    PendingFriendRequestsAdapter sentAdapter;
    PendingFriendRequestsAdapter receivedAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Pending Requests");


        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        receivedList = new ArrayList<>();
        getReceivedRequestList();
        recycler_view.setLayoutManager(new LinearLayoutManager(PendingFriendRequests.this, LinearLayoutManager.VERTICAL, false));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        recycler_viewnew = (RecyclerView) findViewById(R.id.recycler_viewnew);
        sentList= new ArrayList<>();
        getSentRequestList();
        recycler_viewnew.setLayoutManager(new LinearLayoutManager(PendingFriendRequests.this, LinearLayoutManager.VERTICAL, false));
        recycler_viewnew.setItemAnimator(new DefaultItemAnimator());
        recycler_viewnew.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    private void setReceivedList() {
        receivedAdapter = new PendingFriendRequestsAdapter(receivedList, PendingFriendRequests.this);
        receivedAdapter.notifyDataSetChanged();
        recycler_view.setAdapter(receivedAdapter);
    }

    private void setSentList() {
        sentAdapter = new PendingFriendRequestsAdapter(sentList, PendingFriendRequests.this);
        sentAdapter.notifyDataSetChanged();
        recycler_viewnew.setAdapter(sentAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getSentRequestList() {

        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(PendingFriendRequests.this);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                UserModel user= AppPreferences.getInstance(getApplicationContext()).getUser();

                params.put("user_id", String.valueOf(user.getId()));
                params.put("roll", "1");

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_PENDINGSENTFRIENDREQUEST, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Fetching Notifications....");
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

                    //if no error in response
                    if (obj.getString("status").equalsIgnoreCase("Success")) {
                        Toast.makeText(getApplicationContext(), obj.getString("response"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONArray notificationJson = obj.getJSONArray("data");
                        for (int i = 0, size = notificationJson.length(); i < size; i++) {
                            JSONObject objectInArray = notificationJson.getJSONObject(i);
                            PendingFriendRequestModel friendItem = new PendingFriendRequestModel(
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

                            sentList.add(friendItem);
                        }
                        setSentList();

                    }
                    else if (obj.getString("status").equalsIgnoreCase("Warning")) {
                        Toast.makeText(getApplicationContext(), "No Friends Requests found", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }


    private void getReceivedRequestList() {

        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(PendingFriendRequests.this);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                UserModel user= AppPreferences.getInstance(getApplicationContext()).getUser();

                params.put("user_id", String.valueOf(user.getId()));
                params.put("roll", "1");

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_PENDINGRECEIVEDFRIENDREQUEST, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Fetching Notifications....");
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

                    //if no error in response
                    if (obj.getString("status").equalsIgnoreCase("Success")) {
                        Toast.makeText(getApplicationContext(), obj.getString("response"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONArray notificationJson = obj.getJSONArray("data");
                        for (int i = 0, size = notificationJson.length(); i < size; i++) {
                            JSONObject objectInArray = notificationJson.getJSONObject(i);
                            PendingFriendRequestModel friendItem = new PendingFriendRequestModel(
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
                            friendItem.isReceived = true;
                            receivedList.add(friendItem);
                        }
                        setReceivedList();

                    }else if (obj.getString("status").equalsIgnoreCase("Warning")) {
                        Toast.makeText(getApplicationContext(), "No Friends Requests found", Toast.LENGTH_SHORT).show();
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
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }

}
