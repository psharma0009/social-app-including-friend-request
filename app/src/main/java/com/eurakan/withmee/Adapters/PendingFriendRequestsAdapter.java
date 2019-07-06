package com.eurakan.withmee.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eurakan.withmee.Activity.ChangePassword;
import com.eurakan.withmee.Models.NotificationModel;
import com.eurakan.withmee.Models.PendingFriendRequestModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 2/5/2019.
 */

public class PendingFriendRequestsAdapter extends RecyclerView.Adapter<PendingFriendRequestsAdapter.MyViewHolder> {

    List<PendingFriendRequestModel> verticalList = Collections.emptyList();
    Context context;
    public PendingFriendRequestsAdapter(List<PendingFriendRequestModel> horizontalList, Context context) {
        this.verticalList = horizontalList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, studyat;
        public ImageView delete, confirm;
        public int friend_id;
        //LinearLayout parent;

        public MyViewHolder(View root) {
            super(root);
            name = root.findViewById(R.id.pendingFriendRequestName);
            delete = root.findViewById(R.id.delete_icon);
            confirm = root.findViewById(R.id.confirm);
            name = root.findViewById(R.id.pendingFriendRequestName);
            studyat = root.findViewById(R.id.pendingFriendRequesInfo);
           // parent = (LinearLayout) root.findViewById(R.id.parent);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteFriendRequest(friend_id, getAdapterPosition());
                }
            });

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptFriendRequest(friend_id, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_friend_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        PendingFriendRequestModel movie = verticalList.get(position);
        holder.name.setText(movie.GetFullName());
        holder.studyat.setText(String.valueOf("Studies at: "+movie.getStudyIn()));
        if(movie.isReceived)
            holder.confirm.setVisibility(View.VISIBLE);
        else
            holder.delete.setVisibility(View.VISIBLE);

        holder.friend_id = movie.getId();
    }

    @Override
    public int getItemCount() {
        return verticalList.size();
    }

    public void removeItem(int position) {
        verticalList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(PendingFriendRequestModel item, int position) {
        verticalList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }


    private void acceptFriendRequest(final int friend_id, final int position) {

        //if it passes all the validations

        class ChangePasswordData extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("friend_id", String.valueOf(friend_id));
                UserModel user= AppPreferences.getInstance(context).getUser();
                params.put("user_id", String.valueOf(user.getId()));

                //returing the response
                return requestHandler.sendPostRequest(Utilities.URL_ACCEPTFRIENDREQUEST, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                //show dialog
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
                        removeItem(position);
                    } else {
                        Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
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

    private void deleteFriendRequest(final int friend_id, final int position) {

        //if it passes all the validations

        class ChangePasswordData extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;
            ProgressDialog asyncDialog = new ProgressDialog(context);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("friend_id", String.valueOf(friend_id));
                UserModel user= AppPreferences.getInstance(context).getUser();
                params.put("user_id", String.valueOf(user.getId()));

                //returing the response
                return requestHandler.sendPostRequest(Utilities.URL_DELETEFRIENDREQUEST, params);
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
                        Toast.makeText(context, obj.getString("response"), Toast.LENGTH_SHORT).show();
                        removeItem(position);
                    } else {
                        Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
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
}
