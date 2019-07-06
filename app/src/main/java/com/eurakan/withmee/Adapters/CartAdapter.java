package com.eurakan.withmee.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eurakan.withmee.Models.NotifModel;
import com.eurakan.withmee.Models.NotificationModel;
import com.eurakan.withmee.Models.ProductsDataModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 1/22/2019.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    List<ProductsDataModel> verticalList = Collections.emptyList();
    Context context;

    public CartAdapter(List<ProductsDataModel> horizontalList, Context context) {
        this.verticalList = horizontalList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView, delete_icon;
        public TextView price, productName, type, quantity;
        public int productId;

        public MyViewHolder(View root) {
            super(root);
            imageView =  root.findViewById(R.id.imageView);
            delete_icon =  root.findViewById(R.id.delete_icon);
            price =  root.findViewById(R.id.price);
            productName =  root.findViewById(R.id.productName);
//            type =  root.findViewById(R.id.type);
            quantity =  root.findViewById(R.id.quantity);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ProductsDataModel movie = verticalList.get(position);
        holder.productId = movie.getId();
        holder.price.setText("\u20B9" + movie.getPrice());
        Picasso.get().load(movie.getProduct_image()).placeholder(R.drawable.placeholder).into(holder.imageView);
        holder.productName.setText(String.valueOf(movie.getProductName()));
        holder.quantity.setText(String.valueOf(movie.getQuantity()));

        final int productId, quantity;
        productId = movie.getId();
        quantity = movie.getQuantity();
        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProductFromCart(productId,quantity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return verticalList.size();
    }

    private void deleteProductFromCart(final int productId,final int quantity) {

        //if it passes all the validations

        class ChatList extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(context);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                UserModel user = AppPreferences.getInstance(context).getUser();

                HashMap<String, String> params = new HashMap<>();
                params.put("product_id", String.valueOf(productId));
                params.put("user_id", String.valueOf(user.getId()));
                params.put("quantity", String.valueOf(quantity));

                //returing the response
                return requestHandler.sendPostRequest(Utilities.URL_DELETEPRODUCTTOCART, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Deleting Product from Cart....");
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
