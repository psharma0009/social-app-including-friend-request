package com.eurakan.withmee.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.eurakan.withmee.Models.ProductsDataModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProductDetail extends AppCompatActivity {

    int productId;
    Toolbar toolbar;
    ElegantNumberButton elegantBt;
    Context context;
    ProductsDataModel product;
    TextView productName,productNumber, price, description;
    ImageView productImageView;
    Button addToCart, gotoCart;
    @Override   
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        context = ProductDetail.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Product Detail");

        Bundle extras = getIntent().getExtras();
        productId = extras.getInt("productid");
            // and get whatever type user account id is

        productName = findViewById(R.id.productName);
        description = findViewById(R.id.description);
        productNumber = findViewById(R.id.productNumber);
        price = findViewById(R.id.price);
        productImageView = findViewById(R.id.productImageView);

        addToCart = findViewById(R.id.addToCart);
        gotoCart = findViewById(R.id.gotoCart);
        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ProductDetail.this, PostViewActivity.class);
                in.putExtra("postUrl", product.getProduct_image());
                startActivity(in);
            }
        });
        getProductsDetails();
        elegantBt=(ElegantNumberButton)findViewById(R.id.elegantBt);
        elegantBt.setRange(0,5);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(elegantBt.getNumber());
                addProductToCart(quantity);
            }
        });

        gotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ProductDetail.this, CartActivity.class);
                startActivity(in);
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setValuesToUi(ProductsDataModel movie){
        price.setText(String.valueOf(movie.getPrice()));
        description.setText(String.valueOf(movie.getDesc()));
        productName.setText(String.valueOf(movie.getProductName()));
        productNumber.setText("Item no:" + String.valueOf(movie.getMarchent_id()*1000)+ String.valueOf(movie.getId()));
        Picasso.get().load(movie.getProduct_image()).placeholder(R.drawable.placeholder).into(productImageView);
        price.setText("\u20B9" + String.valueOf(movie.getPrice()));
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
                params.put("productid", String.valueOf(productId));

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_GETPRODUCTDETAILS, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Fetching Product Details....");
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
                        Toast.makeText(context, obj.getString("response"), Toast.LENGTH_SHORT).show();
                        //getting the user from the response
                        JSONArray userJson = obj.getJSONArray("data");
                        for (int i = 0, size = userJson.length(); i < size; i++) {
                            JSONObject objectInArray = userJson.getJSONObject(i);
                            product = new ProductsDataModel(
                                    objectInArray.getInt("id"),
                                    objectInArray.getInt("price"),
                                    objectInArray.getInt("purchased_price"),
                                    objectInArray.getString("productname"),
                                    objectInArray.getString("weight_type"),
                                    objectInArray.getString("status"),
                                    objectInArray.getString("active"),
                                    objectInArray.getString("product_image"),
                                    objectInArray.getString("category_name"),
                                    objectInArray.getString("date_and_time")
                            );

                            setValuesToUi(product);
                        }
                    }
                    else if (obj.getString("status").equalsIgnoreCase("Warning")) {
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

    private void addProductToCart(final int quantity) {

        //if it passes all the validations

        class ChatList extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(context);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                UserModel user = AppPreferences.getInstance(getApplicationContext()).getUser();

                HashMap<String, String> params = new HashMap<>();
                params.put("product_id", String.valueOf(productId));
                params.put("user_id", String.valueOf(user.getId()));
                params.put("marchent_id", String.valueOf(product.getMarchent_id()));
                params.put("quantity", String.valueOf(quantity));

                //returing the response
                return requestHandler.sendPostRequest(Utilities.URL_ADDPRODUCTTOCART, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Adding Product to Cart....");
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
                        Toast.makeText(context, obj.getString("response"), Toast.LENGTH_SHORT).show();
                        //getting the user from the response
                    }
                    else if (obj.getString("status").equalsIgnoreCase("Warning")) {
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


}
