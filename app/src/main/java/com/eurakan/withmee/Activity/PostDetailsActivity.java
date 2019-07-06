package com.eurakan.withmee.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eurakan.withmee.Adapters.CommentsAdapter;
import com.eurakan.withmee.Adapters.NotificationAdapter;
import com.eurakan.withmee.Models.CommentsModel;
import com.eurakan.withmee.Models.NotificationModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recycler_view;
    List<CommentsModel> list;
    CommentsAdapter adapter;
    int postId, post_like;
    String comments_string;
    EditText comments;
    TextView send;
    int chat_room_id;
    String post_image_url, post_user_url, post_user_name;
    public TextView user_profile_name, like_post, comment_post, share_post;
    public ImageView profile_image, post_image;
    public ImageButton more_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        Bundle extras = getIntent().getExtras();
        postId = extras.getInt("postId");
        post_image_url = extras.getString("post_image_url");
        post_user_url = extras.getString("post_user_url");
        post_user_name = extras.getString("post_user_name");
        post_like = extras.getInt("post_like");

        profile_image = findViewById(R.id.profile_image);
        user_profile_name = findViewById(R.id.user_profile_name);
        more_button = findViewById(R.id.more_button);
        like_post = findViewById(R.id.like_post);
        comment_post = findViewById(R.id.comment_post);
        comment_post = findViewById(R.id.comment_post);
        share_post = findViewById(R.id.share_post);
        post_image = findViewById(R.id.post_image);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Post Details");
        user_profile_name.setText(post_user_name);
        Picasso.get().load(post_user_url).placeholder(R.drawable.placeholder)
                .into(profile_image);
        Picasso.get().load(post_image_url).placeholder(R.drawable.placeholder).
                into(post_image);
        int totalLikes = post_like;
        String like = "Like";
        if(totalLikes > 0)
            like = totalLikes  + " Likes";
        like_post.setText(like);
        like_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like_post.setTextColor(getResources().getColor(R.color.colorPrimary));
                like_post.setTypeface(null, Typeface.BOLD);
            }
        });

        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(PostDetailsActivity.this, PostViewActivity.class);
                in.putExtra("postUrl", post_image_url);
                startActivity(in);
            }
        });

        comments = (EditText) findViewById(R.id.etpost_comment);
        send = (TextView) findViewById(R.id.send);
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

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        getNotificationList();
        recycler_view.setLayoutManager(new LinearLayoutManager(PostDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setNotificationList() {
        if(list.size() > 0) {
            adapter = new CommentsAdapter(list, PostDetailsActivity.this);
            adapter.notifyDataSetChanged();
            recycler_view.setAdapter(adapter);
        }
        else{
            //todo: No Comments Code
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getNotificationList() {
        //if it passes all the validations
        class RegisterUser extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(PostDetailsActivity.this);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                list.clear();
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("post_id", String.valueOf(postId));
                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_GETCOMMENTSFORPOST, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Fetching Comments....");
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
                            CommentsModel commentModel = new CommentsModel(objectInArray.getInt("user_id"),objectInArray.getString("image"), objectInArray.getString("comment"), objectInArray.getString("username"));
                            list.add(commentModel);
                        }
                        setNotificationList();

                    } else {
                        Toast.makeText(getApplicationContext(), "No Comments Found", Toast.LENGTH_SHORT).show();
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

    private void sendComment() {

        //if it passes all the validations

        class SendComment extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("post_id", String.valueOf(postId));
                UserModel user= AppPreferences.getInstance(getApplicationContext()).getUser();
                params.put("user_id", String.valueOf(user.getId()));
                params.put("comment_title", String.valueOf(comments_string));

                //returing the response
                return requestHandler.sendPostRequest(Utilities.URL_POSTCOMMENT, params);
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
                    getNotificationList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        SendComment ru = new SendComment();
        ru.execute();
    }
}
