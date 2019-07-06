package com.eurakan.withmee.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.eurakan.withmee.R;

/**
 * Created by Admin on 1/25/2019.
 */

public class DonationDialogFrag extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.donation_dialog, container, false);
        getDialog().setTitle("Make Donation");
        Button submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(doneAction);
        return view;
    }

    View.OnClickListener doneAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           dismiss();
        }
    };

}