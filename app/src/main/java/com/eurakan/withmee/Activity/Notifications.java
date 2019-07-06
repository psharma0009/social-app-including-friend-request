package com.eurakan.withmee.Activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eurakan.withmee.Adapters.NotificationAdapter;
import com.eurakan.withmee.Models.NotificationModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.RecyclerItemTouchHelper;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notifications extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    Toolbar toolbar;
    RecyclerView recycler_view;
    List<NotificationModel> list;
    NotificationAdapter adapter;
    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notifications");

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        getNotificationList();
        recycler_view.setLayoutManager(new LinearLayoutManager(Notifications.this, LinearLayoutManager.VERTICAL, false));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_view);

    }

    private void setNotificationList() {
        adapter = new NotificationAdapter(list, Notifications.this);
        adapter.notifyDataSetChanged();
        recycler_view.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotificationAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = list.get(viewHolder.getAdapterPosition()).getNotificationTitle();

            // backup of removed item for undo purpose
            final NotificationModel deletedItem = list.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    adapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    private void getNotificationList() {

        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(Notifications.this);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                UserModel user= AppPreferences.getInstance(getApplicationContext()).getUser();

                params.put("userid", String.valueOf(user.getId()));
                params.put("roll", "1");

                //returing the response
                return requestHandler.sendPostRequest(Utilities.URL_NOTIFICATIONLIST, params);
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
                            NotificationModel notificationItem = new NotificationModel(objectInArray.getString("id"), objectInArray.getString("notification_title"), objectInArray.getString("noti_details"), objectInArray.getString("dateandtime"));
                            list.add(notificationItem);
                        }
                        setNotificationList();

                        } else {
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
