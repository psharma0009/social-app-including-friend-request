package com.eurakan.withmee.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eurakan.withmee.Adapters.FriendSearchAdapter;
import com.eurakan.withmee.Models.FriendModel;
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


public class SearchFriendsFragment extends Fragment {
    private Context mContext;
    private RecyclerView recyclerView;
     ArrayList<FriendModel> list = new ArrayList<FriendModel>();
    FriendSearchAdapter adapter;
EditText et;
    public SearchFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_friends, container, false);

        mContext = getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.usersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        TextView searchFriendButton = view.findViewById(R.id.searchFriendButton);
        et = view.findViewById(R.id.searchFriendTextBox);

        searchFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFriendsSearchList();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void getFriendsSearchList() {

        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(mContext);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                list.clear();
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                UserModel user= AppPreferences.getInstance(mContext).getUser();

                params.put("name", et.getText().toString());

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_SEARCHFRIEND, params);
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
                        Toast.makeText(mContext, obj.getString("response"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONArray userJson = obj.getJSONArray("data");
                        for (int i = 0, size = userJson.length(); i < size; i++) {
                            JSONObject objectInArray = userJson.getJSONObject(i);
//                            FriendModel friendItem = new FriendModel(objectInArray.getInt("friend_id"),objectInArray.getString("friend_name"),objectInArray.getString("friend_address_user"),objectInArray.getString("friend_worksat"),objectInArray.getString("image"));

                            FriendModel friendItem = new FriendModel(
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
                            list.add(friendItem);

                        }
                        adapter = new FriendSearchAdapter(list, mContext);
                        recyclerView.setAdapter(adapter);

                    } else {
                        Toast.makeText(mContext, "No records found", Toast.LENGTH_SHORT).show();
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
//
//    private void setNotificationList() {
//        adapter = new FriendSearchAdapter(list, mContext);
//        adapter.notifyDataSetChanged();
//        recyclerView.setAdapter(adapter);
//    }
}
