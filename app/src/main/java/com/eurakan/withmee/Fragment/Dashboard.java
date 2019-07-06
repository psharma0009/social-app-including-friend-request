package com.eurakan.withmee.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.eurakan.withmee.Activity.AddNewPost;
import com.eurakan.withmee.Activity.Drawer;
import com.eurakan.withmee.Activity.Notifications;
import com.eurakan.withmee.Adapters.FriendSearchAdapter;
import com.eurakan.withmee.Adapters.NotifAdapter;
import com.eurakan.withmee.Adapters.NotificationAdapter;
import com.eurakan.withmee.Adapters.PostsAdapter;
import com.eurakan.withmee.Adapters.SuggFrndAdapter;
import com.eurakan.withmee.Models.FriendModel;
import com.eurakan.withmee.Models.NotifModel;
import com.eurakan.withmee.Models.NotificationModel;
import com.eurakan.withmee.Models.PostsModel;
import com.eurakan.withmee.Models.SugFrndeModel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    Context context;
    RecyclerView sugg_frnds_recycler_view, posts_recycler_view;
    private List<NotificationModel> list_notif;
    private List<SugFrndeModel> list_sugg_frnds;
    private List<PostsModel> list_posts;
    SuggFrndAdapter suggFrndAdapter;
    PostsAdapter postsAdapter;
    EditText postComment;
    public Dashboard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = getActivity();

        sugg_frnds_recycler_view = (RecyclerView) view.findViewById(R.id.sugg_frnds_recycler_view);
        posts_recycler_view = (RecyclerView) view.findViewById(R.id.posts_recycler_view);

        list_notif = new ArrayList<>();
        list_sugg_frnds = new ArrayList<>();
        list_posts = new ArrayList<>();


        sugg_frnds_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        posts_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        postComment = view.findViewById(R.id.postComment);

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, AddNewPost.class);
                startActivity(in);
            }
        });
        getPostsList();
        getFriendsSearchList();

        return view;
    }

    private void setSuggFrnds() {
        suggFrndAdapter = new SuggFrndAdapter(list_sugg_frnds, context);
        suggFrndAdapter.notifyDataSetChanged();
        sugg_frnds_recycler_view.setAdapter(suggFrndAdapter);
    }

    private void setPosts() {
        postsAdapter = new PostsAdapter(list_posts, context);
        postsAdapter.notifyDataSetChanged();
        posts_recycler_view.setAdapter(postsAdapter);
    }

    private void getFriendsSearchList() {

        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(context);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                UserModel user= AppPreferences.getInstance(context).getUser();

                params.put("name", "a");

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_SEARCHFRIEND, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Fetching Friends....");
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
                        Toast.makeText(context, obj.getString("response"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONArray userJson = obj.getJSONArray("data");
                        for (int i = 0, size = userJson.length(); i < size; i++) {
                            JSONObject objectInArray = userJson.getJSONObject(i);
                            SugFrndeModel friendItem = new SugFrndeModel(
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
                            friendItem.setProfileImageUrl(objectInArray.getString("friend_image"));
                            UserModel user= AppPreferences.getInstance(getContext()).getUser();

                            if(friendItem.getId() != user.getId())
                            list_sugg_frnds.add(friendItem);

                        }
                        setSuggFrnds();

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

    private void getPostsList() {
        //if it passes all the validations
        class RegisterUser extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                UserModel user= AppPreferences.getInstance(context).getUser();

                params.put("userid", String.valueOf(user.getId()));

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_GLOBALLISTPOSTS, params);
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
}
