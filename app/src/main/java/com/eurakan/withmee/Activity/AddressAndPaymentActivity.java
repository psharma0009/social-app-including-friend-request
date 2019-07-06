package com.eurakan.withmee.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.eurakan.withmee.Models.ProductsDataModel;
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

public class AddressAndPaymentActivity extends AppCompatActivity {

    UserModel userModel;
    Toolbar toolbar;
    Button placeorder;
            Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = AddressAndPaymentActivity.this;
        setContentView(R.layout.activity_address_and_payment);

        userModel = AppPreferences.getInstance(getApplicationContext()).getUser();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        placeorder = findViewById(R.id.placeorder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Confirm Order");

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOrder();
            }
        });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void confirmOrder() {

        //if it passes all the validations

        class ChatList extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(context);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userModel.getId()));
                //returing the response
                return requestHandler.sendPostRequest(Utilities.URL_CONFIRMCART, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Placing order....");
                //show dialog
                asyncDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                asyncDialog.dismiss();
                try {
                    ArrayList<Integer> productidList = new ArrayList<Integer>();
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if (obj.getString("status").equalsIgnoreCase("Success")) {
                        Toast.makeText(context, obj.getString("response"), Toast.LENGTH_SHORT).show();
                        showCustomDialog(obj.getString("order_id"));
                    }
                    else if (obj.getString("status").equalsIgnoreCase("Warning")) {
                        CardView cardView = findViewById(R.id.noProductsCard);
                        cardView.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "No products Found", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        ChatList ru = new ChatList();
        ru.execute();
    }

    private void showCustomDialog(final String orderId) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        Button buttonOk = dialogView.findViewById(R.id.buttonOk);
        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        alertDialog.show();
    }
}
