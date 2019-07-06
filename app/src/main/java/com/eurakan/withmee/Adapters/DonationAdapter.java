package com.eurakan.withmee.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eurakan.withmee.Fragment.DonationDialogFrag;
import com.eurakan.withmee.Models.ChatModel;
import com.eurakan.withmee.Models.DonationModel;
import com.eurakan.withmee.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 1/25/2019.
 */

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.MyViewHolder> {
    List<DonationModel> verticalList = Collections.emptyList();
    Context context;
    public DonationAdapter(List<DonationModel> horizontalList, Context context) {
        this.verticalList = horizontalList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView requestedAmount,description, totalAmount,currentAmount, user_profile_name ;
        public ImageView donationImage, profile_image;
        public Button submitDonation ;
        public MyViewHolder(View root) {
            super(root);
            requestedAmount=(TextView)root.findViewById(R.id.requestedAmount);
            description=root.findViewById(R.id.description);
            totalAmount=root.findViewById(R.id.totalAmount);
            currentAmount=root.findViewById(R.id.currentAmount);
            donationImage=root.findViewById(R.id.donationImage);
            submitDonation=root.findViewById(R.id.submitDonation);
            profile_image=root.findViewById(R.id.profile_image);
            user_profile_name=root.findViewById(R.id.user_profile_name);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        DonationModel donationModel = verticalList.get(position);
        holder.requestedAmount.setText(String.valueOf(donationModel.getRequestAmount()));
        holder.description.setText(donationModel.getDescription());
        holder.totalAmount.setText(String.valueOf(donationModel.getRequestAmount()));
        holder.currentAmount.setText(String.valueOf(donationModel.getTotalBalance()));
        holder.user_profile_name.setText(donationModel.getUserName());
        Picasso.get().load(donationModel.getDonationImage()).placeholder(R.drawable.placeholder).into(holder.donationImage);
        Picasso.get().load(donationModel.getProfileImage()).placeholder(R.drawable.placeholder).into(holder.profile_image);
        holder.submitDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                DonationDialogFrag custom = new DonationDialogFrag();
                custom.show(fm,"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return verticalList.size();
    }
}
