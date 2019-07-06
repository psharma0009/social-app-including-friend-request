package com.eurakan.withmee.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.eurakan.withmee.Adapters.ProductAdapter;
import com.eurakan.withmee.Models.ProductsDataModel;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {

    final ArrayList<ProductsDataModel> productsList = new ArrayList<ProductsDataModel>();
    Context context;
    FlowLayout flowLayout;
    GridView products;

    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        context = getActivity();

        flowLayout = (FlowLayout) view.findViewById(R.id.flowLayout);
        products = (GridView) view.findViewById(R.id.products);

        setFilters();
        getProductsList();

        return view;
    }

    private void setFilters() {

        if (flowLayout != null) {
                flowLayout.removeAllViews();
                    FlowLayout.LayoutParams lparams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                    TextView rowTextView = (TextView) getLayoutInflater().inflate(R.layout.tag, null);
                    rowTextView.setText("Low to High Price");
                    rowTextView.setLayoutParams(lparams);
            rowTextView.setTextColor(Color.WHITE);
            TextView rowTextView1 = (TextView) getLayoutInflater().inflate(R.layout.tag, null);
            rowTextView1.setText("High to Low Price");
            rowTextView1.setLayoutParams(lparams);
            rowTextView1.setTextColor(Color.WHITE);
            flowLayout.addView(rowTextView1);
            flowLayout.addView(rowTextView);
        }
//
//
//        TextView textView = buildLabel("Low to High Price");
//        textView.setPadding(8,0,8,0);
//        flowLayout.addView(textView);
//        TextView textView1 = buildLabel("High to Low Price");
//        textView.setPadding(8,0,8,0);
//        flowLayout.addView(textView);

    }


    private TextView buildLabel(String title) {
        TextView textView = new TextView(context);
        textView.setText(title);
        textView.setBackgroundResource(R.drawable.label_bg);
        textView.setPadding(8, 8, 8, 8);
        textView.setGravity(Gravity.CENTER);
        return textView;

    }

    private void getProducts() {
//        for (int i = 0; i < 10; i++) {
//            list.add(new ProductsDataModel(""+i, "", "PRODUCT NAME","PRODUCT PRICE"));
//        }
        products.setAdapter(new ProductAdapter(context,productsList));
    }

    private void getProductsList() {

        //if it passes all the validations

        class ChatList extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(context);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_GETALLPRODUCTS, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Fetching Products List....");
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
                        for (int i = 0, size = userJson.length() -1; i < size; i++) {
                            JSONObject objectInArray = userJson.getJSONObject(i);
                            ProductsDataModel products = new ProductsDataModel(
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
                                productsList.add(products);
                        }
                        getProducts();
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
