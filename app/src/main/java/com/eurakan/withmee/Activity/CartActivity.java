package com.eurakan.withmee.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eurakan.withmee.Adapters.CartAdapter;
import com.eurakan.withmee.Adapters.NotificationAdapter;
import com.eurakan.withmee.Models.NotificationModel;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    Toolbar toolbar;
    Context context;
    UserModel userModel;
    RecyclerView recycler_view;
    List<ProductsDataModel> list;
    CartAdapter adapter;
    Button confirmOrder;
    double finalAmount = 0.0d;
    TextView totalAmount, totalDiscount;
    LinearLayout cartDetailsParent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        context = CartActivity.this;
        userModel = AppPreferences.getInstance(getApplicationContext()).getUser();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CardView cardView = findViewById(R.id.noProductsCard);
        cardView.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cart");
        totalAmount = findViewById(R.id.totalAmount);
        totalDiscount = findViewById(R.id.totalDiscount);
        cartDetailsParent = findViewById(R.id.cartDetailsParent);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        getProductsDetails();
        recycler_view.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        confirmOrder = findViewById(R.id.confirmOrder);
        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =  new Intent(CartActivity.this, AddressAndPaymentActivity.class);
                startActivity(in);
            }
        });
    }

    private void setCartList() {
        adapter = new CartAdapter(list, CartActivity.this);
        adapter.notifyDataSetChanged();
        recycler_view.setAdapter(adapter);
        totalAmount.setText("\u20B9" + " " +String.valueOf(finalAmount));
        totalDiscount.setText("\u20B9" + " " +String.valueOf(0));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        getProductsDetails();
    }

    private void getProductsDetails() {

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
                return requestHandler.sendGetRequest(Utilities.URL_GETCART, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Fetching Cart Details....");
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
                        //getting the user from the response
                        JSONArray userJson = obj.getJSONArray("data");
                        for (int i = 0, size = userJson.length(); i < size; i++) {
                            JSONObject objectInArray = userJson.getJSONObject(i);
                            ProductsDataModel product = new ProductsDataModel(
                                    objectInArray.getInt("id"),
                                    objectInArray.getInt("price"),
                                    0,
                                    objectInArray.getString("productname"),
                                    "",
                                    "",
                                    "",
                                    objectInArray.getString("product_image"),
                                    "",
                                    ""
                            );
                            product.setQuantity(objectInArray.getInt("quantity"));
                            product.setMarchent_id(objectInArray.getInt("marchant_id"));
                            list.add(product);
                            finalAmount = finalAmount + objectInArray.getInt("price") * objectInArray.getInt("quantity");
                        }
                        setCartList();
                    }
                    else if (obj.getString("status").equalsIgnoreCase("Warning")) {
                        CardView cardView = findViewById(R.id.noProductsCard);
                        cardView.setVisibility(View.VISIBLE);
                        cartDetailsParent.setVisibility(View.GONE);
                        Toast.makeText(context, "No products Found", Toast.LENGTH_SHORT).show();
                        confirmOrder.setEnabled(false);
                        confirmOrder.setVisibility(View.GONE);
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
}
