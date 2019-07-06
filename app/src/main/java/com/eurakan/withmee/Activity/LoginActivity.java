package com.eurakan.withmee.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    ProgressDialog progressDialog;
    EditText email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button forgotPassword = (Button) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        Button signUpButton = (Button) findViewById(R.id.signUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        Button logInButton = (Button) findViewById(R.id.loginButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText, passwordText;
                 email = (EditText) findViewById(R.id.emailAddress);
                emailText = email.getText().toString();
                password = findViewById(R.id.password);
                passwordText = password.getText().toString();
                if (emailText.isEmpty()) {
                    email.setError("Please enter Email address");
                } else if (passwordText.isEmpty()) {
                    password.setError("Please enter Password");
                } else if(Utilities.isEmailValid(emailText)) {
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    userLogin();
                } else{
                    View contextView = findViewById(R.id.loginButton);
                    Snackbar.make(contextView, "Please enter valid Email", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }


    private void userLogin() {
        class UserLogin extends AsyncTask<Void, Void, String> {
            final String username = email.getText().toString();
            final String passwordText = password.getText().toString();
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
                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("data");
                        //creating a new user object
                        UserModel user = new UserModel(
                                userJson.getInt("id"),
                                userJson.getString("mobileno"),
                                userJson.getString("email"),
                                userJson.getString("name"),
                                userJson.getString("aadhar_no"),
                                "",
                                userJson.getString("address_user"),
                                "",
                                "",
                                userJson.getString("address_user"),
                                "",
                                ""
                        );
                        user.setProfileImageUrl(userJson.getString("user_photo"));
                            //storing the user in shared preferences
                        AppPreferences.getInstance(getApplicationContext()).userLogin(user);
                        //starting the drawer activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), Drawer.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
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
                params.put("email", username);
                params.put("password", passwordText);
                //returing the response

                return requestHandler.sendPostRequest(Utilities.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}
