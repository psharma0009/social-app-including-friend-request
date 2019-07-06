package com.eurakan.withmee.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eurakan.withmee.Activity.RedeemEarningsActivity;
import com.eurakan.withmee.Activity.UserProfile;
import com.eurakan.withmee.Models.EarningModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EarningsFragment extends Fragment {
    Context context;
    EarningModel earningModel;
    Button submitCashback, RedeemButton;
    EditText etManifacturingDate,etBatchCode,likesEarnings,likesRealEarnings, commentsEarnings, commentsRealEarnings, chatEarnings, chatRealEarnings, cashBackEarnings, cashBackRealEarnings, bonusEarnings, bonusRealEarnings, totalEarning,
            totalRealEarning;
    public EarningsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_earnings, container, false);
        context=getActivity();
        try {
            userProfile();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        etManifacturingDate = view.findViewById(R.id.etManifacturingDate);
        etBatchCode = view.findViewById(R.id.etBatchCode);
        likesEarnings = view.findViewById(R.id.likesEarnings);
        likesRealEarnings = view.findViewById(R.id.likesRealEarnings);
        commentsEarnings = view.findViewById(R.id.commentsEarnings);
        chatEarnings = view.findViewById(R.id.chatEarnings);
        commentsRealEarnings = view.findViewById(R.id.commentsRealEarnings);
        chatRealEarnings = view.findViewById(R.id.chatRealEarnings);
        cashBackEarnings = view.findViewById(R.id.cashBackEarnings);
        cashBackRealEarnings = view.findViewById(R.id.cashBackRealEarnings);
        bonusEarnings = view.findViewById(R.id.bonusEarnings);
        bonusRealEarnings = view.findViewById(R.id.bonusRealEarnings);
        totalEarning = view.findViewById(R.id.totalEarning);
        totalRealEarning = view.findViewById(R.id.totalRealEarning);

        submitCashback = view.findViewById(R.id.submitCashback);
        RedeemButton = view.findViewById(R.id.RedeemButton);

        RedeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, RedeemEarningsActivity.class);
                startActivity(in);
            }
        });

        return view;
    }


    private void userProfile() throws InterruptedException {
        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(context);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                asyncDialog.setMessage("Fetching Earnings....");
                //show dialog
                asyncDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    asyncDialog.dismiss();
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if (obj.getString("status").equalsIgnoreCase("Success")) {
                        Toast.makeText(context, obj.getString("response"), Toast.LENGTH_SHORT).show();
                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("data");
                        //creating a new user object
                        earningModel = new EarningModel(
                                userJson.getInt("id"),
                                userJson.getInt("likes_point"),
                                userJson.getInt("comments_point"),
                                userJson.getInt("chat_points"),
                                userJson.getInt("cashback_points")
                                );
                        //starting the drawer activity
                        earningModel.setBonus_points(200);
                        if(earningModel != null) {
                            likesEarnings.setText(String.valueOf(earningModel.getLikes_point()));
                            commentsEarnings.setText(String.valueOf(earningModel.getComments_point()));
                            chatEarnings.setText(String.valueOf(earningModel.getChat_points()));
                            cashBackEarnings.setText(String.valueOf(earningModel.getCashback_points()));
                            bonusEarnings.setText(String.valueOf(earningModel.getBonus_points()));
                            totalEarning.setText(String.valueOf(earningModel.getTotalPoints()));
                            likesRealEarnings.setText(String.valueOf(earningModel.getLikes_point() / 2));
                            commentsRealEarnings.setText(String.valueOf(earningModel.getComments_point() / 2));
                            chatRealEarnings.setText(String.valueOf(earningModel.getChat_points() / 2));
                            cashBackRealEarnings.setText(String.valueOf(earningModel.getCashback_points() / 2));
                            bonusRealEarnings.setText(String.valueOf(earningModel.getBonus_points() / 2));
                            totalRealEarning.setText(String.valueOf(earningModel.getTotalPoints() / 2));
                        }
                    } else {
                        Toast.makeText(context, "Error Fetching the Earnings", Toast.LENGTH_SHORT).show();
                    }
                }   catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                UserModel user= AppPreferences.getInstance(context).getUser();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user.getId()));
                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_USEREARNINGS, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

}
