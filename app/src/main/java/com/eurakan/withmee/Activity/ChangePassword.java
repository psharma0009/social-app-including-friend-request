package com.eurakan.withmee.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class ChangePassword extends AppCompatActivity {
    Toolbar toolbar;

    String oldPassword, newPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Change Password");
        final EditText oldPasswordEdit, newPasswordEdit;
        Button submitButton;
        oldPasswordEdit =findViewById(R.id.oldPassword);
        newPasswordEdit = findViewById(R.id.confirmPassword);

        submitButton = findViewById(R.id.changePasswordSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Utilities.checkNetworkConnection(ChangePassword.this)){
                    View contextView = findViewById(R.id.changePasswordSubmit);
                    Snackbar.make(contextView, "Please check the Internet", Snackbar.LENGTH_SHORT)
                            .show();
                }
else {
                    oldPassword = oldPasswordEdit.getText().toString();
                    newPassword = newPasswordEdit.getText().toString();

                    if (oldPassword.isEmpty()) {
                        oldPasswordEdit.setError("Please enter the old password");
                        oldPasswordEdit.requestFocus();
                    } else if (newPassword.isEmpty()) {
                        newPasswordEdit.setError("Please confirm password");
                        newPasswordEdit.requestFocus();
                    } else {
                        changePassword();
                    }
                }
            }
        });
    }

    private void changePassword() {

        //if it passes all the validations

        class ChangePasswordData extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;
            ProgressDialog asyncDialog = new ProgressDialog(ChangePassword.this);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("oldpassword", oldPassword);
                UserModel user= AppPreferences.getInstance(getApplicationContext()).getUser();

                params.put("user_id", String.valueOf(user.getId()));
                params.put("oldpassword", oldPassword);
                params.put("newpassword", newPassword);

                //returing the response
                return requestHandler.sendPostRequest(Utilities.URL_CHANGE_PASSWORD, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Changing Password....");
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
                        View contextView = findViewById(R.id.changePasswordSubmit);
                        Toast.makeText(getApplicationContext(), obj.getString("response"), Toast.LENGTH_SHORT).show();
                        Snackbar.make(contextView, "Password Changed Successfully", Snackbar.LENGTH_SHORT)
                                .show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
