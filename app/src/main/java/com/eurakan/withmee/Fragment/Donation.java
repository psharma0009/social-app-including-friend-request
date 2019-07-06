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
import android.widget.Button;
import android.widget.Toast;

import com.eurakan.withmee.Activity.AddNewDonationRequest;
import com.eurakan.withmee.Activity.UserProfile;
import com.eurakan.withmee.Adapters.DonationAdapter;
import com.eurakan.withmee.Models.DonationModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Donation extends Fragment {

    Context context;
    RecyclerView recycler_view;
    List<DonationModel> list;
    DonationAdapter adapter;
    Button submitDonation;

    public Donation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation, container, false);
        context=getActivity();

        recycler_view=(RecyclerView)view.findViewById(R.id.recycler_view);
        list=new ArrayList<>();

        recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        submitDonation = view.findViewById(R.id.submitDonation);
        submitDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, AddNewDonationRequest.class);
                context.startActivity(in);
            }
        });
        getDonationList();

        return view;
    }

    private void setDonationlIst() {
        adapter = new DonationAdapter(list, context);
        adapter.notifyDataSetChanged();
        recycler_view.setAdapter(adapter);
    }

    private void getDonationList() {
        //if it passes all the validations

        class ChatList extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(context);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                UserModel user= AppPreferences.getInstance(context).getUser();
                params.put("user_id", String.valueOf(user.getId()));

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_GETALLDONATIONS, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Fetching Donations List....");
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
                        Toast.makeText(context, obj.getString("response"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONArray userJson = obj.getJSONArray("data");
                        for (int i = 0, size = userJson.length() -1; i < size; i++) {
                            JSONObject objectInArray = userJson.getJSONObject(i);

                            DonationModel donation = new DonationModel(
                                    objectInArray.getInt("id"),
                                    objectInArray.getString("reason_for_donation"),
                                    objectInArray.getString("donation_status"),
                                    objectInArray.getString("title"),
                                    objectInArray.getString("desc"),
                                    objectInArray.getInt("user_id"),
                                    objectInArray.getInt("request_amount"),
                                    0,
                                    objectInArray.getString("date")
                            );
                            donation.setProfileImage(objectInArray.getString("user_image"));
                            donation.setUserName(objectInArray.getString("username"));
                            donation.setDonationImage(objectInArray.getString("donation_image"));
                            list.add(donation);
                        }
                        setDonationlIst();
                    }
                    else if (obj.getString("status").equalsIgnoreCase("Warning")) {
                        Toast.makeText(context, "No Donations found", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
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
