package com.eurakan.withmee.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eurakan.withmee.Activity.ChangePassword;
import com.eurakan.withmee.Activity.ChatDetail;
import com.eurakan.withmee.Activity.PostDetailsActivity;
import com.eurakan.withmee.Models.ChatModel;
import com.eurakan.withmee.Models.PostsModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.ImageLoader;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 1/22/2019.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    List<PostsModel> verticalList = Collections.emptyList();
    Context context;

    public PostsAdapter(List<PostsModel> horizontalList, Context context) {
        this.verticalList = horizontalList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parent;
        public TextView user_profile_name, like_post, comment_post, share_post, post_comment_text;
        public ImageView profile_image, post_image;
        public ImageButton more_button;
        public MyViewHolder(View root) {
            super(root);
            profile_image = root.findViewById(R.id.profile_image);
            user_profile_name = root.findViewById(R.id.user_profile_name);
            more_button = root.findViewById(R.id.more_button);
            like_post = root.findViewById(R.id.like_post);
            comment_post = root.findViewById(R.id.comment_post);
            share_post = root.findViewById(R.id.share_post);
            post_image = root.findViewById(R.id.post_image);
            parent = root.findViewById(R.id.parent);
            post_comment_text = root.findViewById(R.id.post_comment_text);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PostsModel movie = verticalList.get(position);
        Picasso.get().load(movie.getUserImagePath()).placeholder(R.drawable.placeholder)
                .into(holder.profile_image);

        Glide.with(context)
                .load(movie.getPostImagePath())
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .override(300, 200)
                .into(holder.post_image);
//
//        Picasso.get().load(movie.getPostImagePath())
//                .placeholder(R.drawable.placeholder).
//                into(holder.post_image);
//        holder.comment_post.setText(movie.getComment());
        int totalLikes = movie.getTotal_like();
        String like = "Like";
        if(totalLikes > 0)
            like = totalLikes  + " Likes";
        holder.like_post.setText(like);
        holder.like_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.like_post.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.like_post.setTypeface(null, Typeface.BOLD);
                likePost(movie.getPostId());

            }
        });
        holder.share_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShareItem(holder.post_image);
            }
        });
        holder.post_comment_text.setText(movie.getComment());
        holder.user_profile_name.setText(movie.getUserName());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, PostDetailsActivity.class);
                in.putExtra("postId", movie.getPostId());
                in.putExtra("post_image_url", movie.getPostImagePath());
                in.putExtra("post_user_url", movie.getUserImagePath());
                in.putExtra("post_user_name", movie.getUserName());
                in.putExtra("post_like", movie.getTotal_like());
                context.startActivity(in);
            }
        });
//        Picasso.with(context).load(imageUri).fit().centerCrop()
//                .placeholder(R.drawable.user_placeholder)
//                .error(R.drawable.user_placeholder_error)
//                .into(imageView);
//        holder.chatFriendCity.setText(String.valueOf(movie.getFullAddress()));
//        holder.friend_Id = movie.getId();
//        holder.parent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(context, ChatDetail.class);
//                i.putExtra("friend_id", holder.friend_Id);
//                context.startActivity(i);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return verticalList.size();
    }

    private void likePost(final int post_id) {

        //if it passes all the validations

        class ChangePasswordData extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("post_id", String.valueOf(post_id));
                UserModel user= AppPreferences.getInstance(context).getUser();
                params.put("user_id", String.valueOf(user.getId()));

                //returing the response
                return requestHandler.sendPostRequest(Utilities.URL_LIKEPOST, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if (obj.getString("status").equalsIgnoreCase("Success")) {
                        Toast.makeText(context, obj.getString("response"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "You have already Liked the Post", Toast.LENGTH_SHORT).show();
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

    public void onShareItem(ImageView v) {
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(v);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            // ...sharing failed, handle error
        }
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
