package com.eurakan.withmee.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eurakan.withmee.Activity.ProductDetail;
import com.eurakan.withmee.Models.ProductsDataModel;
import com.eurakan.withmee.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 1/27/2019.
 */

public class ProductAdapter extends BaseAdapter {

    Context c;
    ArrayList<ProductsDataModel> list;
    private static LayoutInflater inflater = null;

    public ProductAdapter(Context context, ArrayList<ProductsDataModel> list1) {
        c = context;
        list = list1;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView;
        Button buy_now;
        TextView price, productName;
        LinearLayout productParent;
        inflater = (LayoutInflater) this.c.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.product_item, null);
        final ProductsDataModel productsDataModel = list.get(position);
        buy_now = (Button) rowView.findViewById(R.id.buy_now);
        productParent = rowView.findViewById(R.id.productParent);
        price =  rowView.findViewById(R.id.sellingPrice);
        productName = rowView.findViewById(R.id.productName);
        try {
            price.setText("\u20B9" + " " +String.valueOf(productsDataModel.getPrice()));
        }
        catch (Exception ex){
            Log.e("Abc", "abcc");
        }
        productName.setText(productsDataModel.getProductName());
        ImageView image = rowView.findViewById(R.id.productImage);
        Picasso.get().load(productsDataModel.getProduct_image()).placeholder(R.drawable.placeholder).into(image);

        productParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c, ProductDetail.class);
                i.putExtra("productid", productsDataModel.id);
                c.startActivity(i);
            }
        });

        return rowView;
    }
}
