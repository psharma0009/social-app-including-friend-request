package com.eurakan.withmee.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText forgotemailEditText;
    String forgotemailEditTextValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Button signUpButton = (Button) findViewById(R.id.forgotPAsswordSubmit);
        forgotemailEditText = (EditText) findViewById(R.id.forgotemailEditText);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotemailEditTextValue =forgotemailEditText.getText().toString();
                if (!forgotemailEditTextValue.isEmpty()) {
                    ForgotPassword();
                } else
                    forgotemailEditText.setError("Please enter the Email Address");

            }
        });
    }

    private void ForgotPassword() {
        class UserLogin extends AsyncTask<Void, Void, String> {
            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (obj.getString("status").equalsIgnoreCase("Success")) {
                        Toast.makeText(getApplicationContext(), obj.getString("response"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Please check your Registered Email Id for Password.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some Error occurred please try after sometime.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("email", forgotemailEditTextValue);
                //returing the response

                return requestHandler.sendPostRequest(Utilities.URL_FORGOT_PASSWORD, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}
