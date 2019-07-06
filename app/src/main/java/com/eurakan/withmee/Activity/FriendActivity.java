package com.eurakan.withmee.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eurakan.withmee.Adapters.FriendsListAdapter;
import com.eurakan.withmee.Adapters.PhotosAdapter;
import com.eurakan.withmee.Adapters.PostsAdapter;
import com.eurakan.withmee.Models.PhotoModel;
import com.eurakan.withmee.Models.PostsModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendActivity extends AppCompatActivity {
    UserModel user;
    Toolbar toolbar;
    String name = "", status = "", dateofbirth ="", email = "",mobileno = "",address_user = "", worksat ="", profilePhotourl ="", studiesAt= "", profileCoverurl= "";
    Button friendAdd;
    int friendUserId;
    ImageView profilePic, coverPhoto;
    PhotosAdapter photosAdapter;
    private List<UserModel> friendList;
    private List<PhotoModel> photosList;
    Context context;
    FriendsListAdapter friendsListAdapter;
    RecyclerView friends_recycler_view, photos_recycler_view, posts_recycler_view;
    private Uri imageUri;
    PostsAdapter postsAdapter;
    private List<PostsModel> list_posts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        Bundle extras = getIntent().getExtras();
        context = FriendActivity.this;
        profilePic = findViewById(R.id.user_profile);
        coverPhoto = findViewById(R.id.coverPhoto);
        if (extras != null) {
            friendUserId = extras.getInt("user_id");
            // and get whatever type user account id is
            user = new UserModel(friendUserId);
        }
        coverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(FriendActivity.this, PostViewActivity.class);
                in.putExtra("postUrl", user.getProfileCoverImageUrl());
                startActivity(in);
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(FriendActivity.this, PostViewActivity.class);
                in.putExtra("postUrl", user.getProfileImageUrl());
                startActivity(in);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(name);
        friendAdd = findViewById(R.id.friendAdd);
        friendAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendAdd.setText("Sent");
                friendAdd.setBackgroundColor(getResources().getColor(R.color.grey));
                sendRequest();
            }
        });

        posts_recycler_view = (RecyclerView) findViewById(R.id.posts_recycler_view);
        list_posts = new ArrayList<>();

        posts_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        photosList = new ArrayList<>();
        photos_recycler_view = (RecyclerView) findViewById(R.id.photos_grid);

        photos_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        friends_recycler_view = (RecyclerView) findViewById(R.id.friends_grid);
        friendList = new ArrayList<>();

        friends_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        try {
            userProfile();
            getPhotosList();
            getPostsList();
            getFriendsList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setPosts() {
        postsAdapter = new PostsAdapter(list_posts, context);
        postsAdapter.notifyDataSetChanged();
        posts_recycler_view.setAdapter(postsAdapter);
    }

    private void setFriendsList() {
        friendsListAdapter = new FriendsListAdapter(friendList, FriendActivity.this);
        friendsListAdapter.notifyDataSetChanged();
        friends_recycler_view.setAdapter(friendsListAdapter);
    }

    private void setPhotosList() {
        photosAdapter = new PhotosAdapter(photosList, FriendActivity.this);
        photosAdapter.notifyDataSetChanged();
        photos_recycler_view.setAdapter(photosAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void sendRequest() {

        //if it passes all the validations

        class ChangePasswordData extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;
            ProgressDialog asyncDialog = new ProgressDialog(FriendActivity.this);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(friendUserId));
                UserModel currentuser= AppPreferences.getInstance(getApplicationContext()).getUser();
                params.put("friend_id", String.valueOf(currentuser.getId()));
                //returing the response
                return requestHandler.sendPostRequest(Utilities.URL_SENDFRIENDREQUEST, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Sending Friend Request....");
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
                        View contextView = findViewById(R.id.friendAdd);
                        Toast.makeText(getApplicationContext(), obj.getString("response"), Toast.LENGTH_SHORT).show();
                        Snackbar.make(contextView, "Friend Request Sent Successfully!!", Snackbar.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("response"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        ChangePasswordData ru = new ChangePasswordData();
        ru.execute();
    }

    private void userProfile() throws InterruptedException {
        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(FriendActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                asyncDialog.setMessage("Fetching Profile....");
                //show dialog
                asyncDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                asyncDialog.dismiss();
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (obj.getString("status").equalsIgnoreCase("Success")) {
                        Toast.makeText(getApplicationContext(), obj.getString("response"), Toast.LENGTH_SHORT).show();
                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("data");
                        //creating a new user object
                        user = new UserModel(
                                userJson.getInt("id"),
                                userJson.getString("mobileno"),
                                userJson.getString("email"),
                                userJson.getString("name"),
                                userJson.getString("aadhar_no"),
                                "",
                                userJson.getString("address_user"),
                                userJson.getString("worksat"),
                                userJson.getString("address_shop"),
                                userJson.getString("address_user"),
                                userJson.getString("studyin"),
                                userJson.getString("dateofbirth")
                        );
                        user.setProfileImageUrl(userJson.getString("user_photo"));
                        user.setProfileCoverImageUrl(userJson.getString("common_photo"));
                        //storing the user in shared preferences
                        Picasso.get().load(userJson.getString("user_photo")).placeholder(R.drawable.placeholder).into(profilePic);
                        Picasso.get().load(userJson.getString("common_photo")).placeholder(R.drawable.placeholder).into(coverPhoto);
                        TextView userProfile = findViewById(R.id.friendProfileName);
                        TextView userEmail = findViewById(R.id.friendProfileEmail);
                        TextView userCity = findViewById(R.id.friendProfileCity);
                        TextView userStatus = findViewById(R.id.friendProfileStatus);
                        TextView userStudy = findViewById(R.id.friendProfileStudy);
                        TextView userWork = findViewById(R.id.friendProfileWork);
                        TextView userDateOfBirth = findViewById(R.id.friendProfileDOB);
                        if(user != null) {
                            String name = user.GetFullName();
                            userProfile.setText(name);
                            userEmail.setText("Email: " + user.getEmail());
                            userCity.setText("Lives In: " + user.getFullAddress());
                            userStatus.setText("Living Status: " + user.getStatus());
                            userStudy.setText("Studys In: " + user.getStudyIn());
                            userWork.setText("Works At: " + user.getWork());
                            userDateOfBirth.setText("DOB: " + user.getDateOfBirth());
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }   catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("userid", String.valueOf(user.getId()));
                params.put("roll", "1");
                //returing the response

                return requestHandler.sendGetRequest(Utilities.URL_GETUSERPROFILE, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

    private void getPostsList() {

        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("userid", String.valueOf(user.getId()));

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_LISTPOSTS, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                //show dialog
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
                        Toast.makeText(context, obj.getString("response"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONArray userJson = obj.getJSONArray("data");
                        for (int i = 0, size = userJson.length(); i < size; i++) {
                            JSONObject objectInArray = userJson.getJSONObject(i);
                            PostsModel postModel = new PostsModel(objectInArray.getInt("id"),objectInArray.getString("userimage"),objectInArray.getString("image"),objectInArray.getString("comment"),objectInArray.getInt("total_like"),objectInArray.getInt("userid"),objectInArray.getString("username"));
                            list_posts.add(postModel);

                        }
                        setPosts();

                    } else {
                        Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
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

    private void getFriendsList() {

        //if it passes all the validations

        class ChatList extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(FriendActivity.this);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user.getId()));

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_GETCHATFRIENDSLIST, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Fetching Friends List....");
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
                        for (int i = 0, size = userJson.length() -1; i < size; i++) {
                            JSONObject objectInArray = userJson.getJSONObject(i);
                            UserModel user = new UserModel(
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
                            user.setProfileCoverImageUrl(objectInArray.getString("common_photo"));
                            user.setProfileImageUrl(objectInArray.getString("user_photo"));
                            if(a.contains(user.getId()) == false) {
                                friendList.add(user);
                                a.add(user.getId());
                            }
                        }
                        setFriendsList();
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

    private void getPhotosList() {

        //if it passes all the validations

        class ChatList extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(FriendActivity.this);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user.getId()));

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_GETALLPHOTO, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Fetching All Photos....");
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
                            PhotoModel user = new PhotoModel(
                                    objectInArray.getInt("id"),
                                    objectInArray.getString("pic"),
                                    objectInArray.getString("createdon")
                            );
                            photosList.add(user);
                        }
                        setPhotosList();
                    }
                    else if (obj.getString("status").equalsIgnoreCase("Warning")) {
                        Toast.makeText(getApplicationContext(), "No Photos found", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        ChatList ru = new ChatList();
        ru.execute();
    }
}
