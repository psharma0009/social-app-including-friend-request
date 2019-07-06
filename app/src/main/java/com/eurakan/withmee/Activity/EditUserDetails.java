package com.eurakan.withmee.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class EditUserDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Calendar calendar ;
    Toolbar toolbar;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day ;
    EditText dateOfBirth;
    String studyIn, worksAt, status, dateOfBirthString;
    EditText etStudyIn, etworksAt, etStatus;
    String profileName, profileMail, profileMobile, profileAddress, profileCity, profileAadhar;
    UserModel user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);
        user= AppPreferences.getInstance(getApplicationContext()).getUser();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etStudyIn = findViewById(R.id.editStudyIn);
        etworksAt = findViewById(R.id.editWorksAt);
        etStatus = findViewById(R.id.editStatusLife);
        dateOfBirth = findViewById(R.id.editDateOfBirth);
        UserModel user= AppPreferences.getInstance(getApplicationContext()).getUser();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        calendar = Calendar.getInstance();

        etStudyIn.setText(user.getStudyIn());
        etStatus.setText(user.getStatus());
        etworksAt.setText(user.getWork());
        dateOfBirth.setText(user.getDateOfBirth());

        dateOfBirthString = dateOfBirth.getText().toString();
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        dateOfBirth = findViewById(R.id.editDateOfBirth);
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = DatePickerDialog.newInstance(EditUserDetails.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#0072BA"));
                datePickerDialog.setTitle("Select Date of Birth");
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
            }
        });

        Button submitButton = findViewById(R.id.updateProfileSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studyIn = etStudyIn.getText().toString();
                worksAt = etworksAt.getText().toString();
                status = etStatus.getText().toString();
                UserModel user= AppPreferences.getInstance(getApplicationContext()).getUser();
                profileAddress = user.getFullAddress();
                profileCity = user.getFullAddress();
                profileAadhar = user.getAadharNumber();
                saveUserProfile();
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dateOfBirth.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
        dateOfBirthString = year + "/" + monthOfYear + "/" + dayOfMonth;
    }



    public void saveUserProfile() {

        //if it passes all the validations

        class UpdateProfileData extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;
            ProgressDialog asyncDialog = new ProgressDialog(EditUserDetails.this);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters

                HashMap<String, String> params = new HashMap<>();
                params.put("address_user", profileAddress + " " + profileCity);
                params.put("userid", String.valueOf(user.getId()));
                params.put("name", user.getUsername());
                params.put("email", user.getEmail());
                params.put("mobileno", user.getMobile());
                params.put("userid", String.valueOf(user.getId()));
                params.put("aadhar_no", profileAadhar);
                params.put("worksat", worksAt);
                params.put("studyin", studyIn);
                params.put("address_shop", status);
                params.put("dateofbirth", dateOfBirthString);
                //storing the user in shared preferences

                user.setFullAddress( profileAddress + " " + profileCity);
                user.setAadharNumber(profileAadhar);
                user.setStatus(status);
                user.setWorksAt(worksAt);
                user.setDateOfBirth(dateOfBirthString);
                AppPreferences.getInstance(getApplicationContext()).userLogin(user);
                View contextView = findViewById(R.id.updateProfileSubmit);
                Snackbar.make(contextView, "Profile Updated Successfully", Snackbar.LENGTH_SHORT)
                        .show();
                finish();
                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_UPDATE_PROFILE, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Updating Profile....");
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
                        View contextView = findViewById(R.id.updateProfileSubmit);
                        Toast.makeText(getApplicationContext(), obj.getString("response"), Toast.LENGTH_SHORT).show();
                        Snackbar.make(contextView, "Profile Updated Successfully", Snackbar.LENGTH_SHORT)
                                .show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        UpdateProfileData ru = new UpdateProfileData();
        ru.execute();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
