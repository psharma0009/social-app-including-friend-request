package com.eurakan.withmee.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
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

public class SignupActivity extends AppCompatActivity {

    EditText nameEdittext, emailEditText, mobileEditText, passwordEditText;
    Button signUpButton;
    String nameEdittextValue, emailEditTextValue, mobileEditTextValue, passwordEditTextValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameEdittext = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        mobileEditText = findViewById(R.id.mobile);
        passwordEditText = findViewById(R.id.signuppassword);

        signUpButton = findViewById(R.id.signupButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEdittextValue = nameEdittext.getText().toString();
                emailEditTextValue = emailEditText.getText().toString();
                mobileEditTextValue = mobileEditText.getText().toString();
                passwordEditTextValue = passwordEditText.getText().toString();
                if (nameEdittextValue.isEmpty()) {
                    nameEdittext.setError("Please enter the full name");
                    nameEdittext.requestFocus();
                } else if (emailEditTextValue.isEmpty()) {
                    emailEditText.setError("Please enter email address");
                    emailEditText.requestFocus();
                } else if (mobileEditTextValue.isEmpty()) {
                    mobileEditText.setError("Please enter mobile number");
                    mobileEditText.requestFocus();
                } else if (passwordEditTextValue.isEmpty()) {
                    passwordEditText.setError("Please enter password");
                    passwordEditText.requestFocus();
                } else if (!Patterns.PHONE.matcher(mobileEditText.getText().toString()).matches()) {
                    View contextView = findViewById(R.id.signupButton);
                    Snackbar.make(contextView, "Please enter valid Mobile Number", Snackbar.LENGTH_SHORT)
                            .show();
                } else if (!Utilities.isEmailValid(emailEditText.getText().toString())) {
                    View contextView = findViewById(R.id.signupButton);
                    Snackbar.make(contextView, "Please enter valid Email Address", Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    registerUser();
                }
            }
        });
    }

    private void registerUser() {

        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("name", nameEdittextValue);
                params.put("email", emailEditTextValue);
                params.put("mobileno", mobileEditTextValue);
                params.put("password", passwordEditTextValue);
                params.put("roll", "1");

                //returing the response
                return requestHandler.sendPostRequest(Utilities.URL_REGISTER, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
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

                        //storing the user in shared preferences
                        AppPreferences.getInstance(getApplicationContext()).userLogin(user);
                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), Drawer.class));
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
