package com.eurakan.withmee.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eurakan.withmee.Activity.ChatDetail;
import com.eurakan.withmee.Activity.PostViewActivity;
import com.eurakan.withmee.Models.ChatModel;
import com.eurakan.withmee.Models.PhotoModel;
import com.eurakan.withmee.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 1/26/2019.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.MyViewHolder> {

    List<PhotoModel> verticalList = Collections.emptyList();
    Context context;

    public PhotosAdapter(List<PhotoModel> horizontalList, Context context) {
        this.verticalList = horizontalList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView photoView;
        private CardView parent;
        public MyViewHolder(View root) {
            super(root);
            photoView = root.findViewById(R.id.photoView);
            parent = (CardView) root.findViewById(R.id.parent);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        PhotoModel movie = verticalList.get(position);
        final String url = movie.getPic();
        Picasso.get().load(movie.getPic()).placeholder(R.drawable.placeholder).into(holder.photoView);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PostViewActivity.class);
                i.putExtra("postUrl", url);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return verticalList.size();
    }



}
