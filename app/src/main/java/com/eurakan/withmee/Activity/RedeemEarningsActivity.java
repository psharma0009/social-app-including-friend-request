package com.eurakan.withmee.Activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.eurakan.withmee.R;

public class RedeemEarningsActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button paytm, bank, recharge, electricityBill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_earnings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Redeem the Earnings");
        paytm = findViewById(R.id.btnPaytm);
        bank = findViewById(R.id.btnbank);
        recharge = findViewById(R.id.btnRecharge);
        electricityBill = findViewById(R.id.btnElectricityBill);


        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(RedeemEarningsActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.redeem_paytm,null);

                // Specify alert dialog is not cancelable/not ignorable
                builder.setCancelable(false);

                // Set the custom layout as alert dialog view
                builder.setView(dialogView);

                // Get the custom alert dialog view widgets reference
                Button btn_negative = (Button) dialogView.findViewById(R.id.btnSubmit);
                final EditText et_name = (EditText) dialogView.findViewById(R.id.et2);
                final EditText et_mobile = (EditText) dialogView.findViewById(R.id.et);
                final EditText et_amount = (EditText) dialogView.findViewById(R.id.et3);

                Button imageView_close = dialogView.findViewById(R.id.imageView_close);
                final AlertDialog dialog = builder.create();

                imageView_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                // Create the alert dialog

                // Set negative/no button click listener
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss/cancel the alert dialog
                        //dialog.cancel();
                        dialog.dismiss();
                        Toast.makeText(getApplication(),
                                "Your request has been Submitted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();
            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(RedeemEarningsActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.redeem_bank,null);

                // Specify alert dialog is not cancelable/not ignorable
                builder.setCancelable(false);

                // Set the custom layout as alert dialog view
                builder.setView(dialogView);

                // Get the custom alert dialog view widgets reference
                Button btn_negative = (Button) dialogView.findViewById(R.id.btnSubmit);
                final EditText et_name = (EditText) dialogView.findViewById(R.id.et2);
                final EditText et_mobile = (EditText) dialogView.findViewById(R.id.et);
                final EditText et_amount = (EditText) dialogView.findViewById(R.id.et3);

                // Create the alert dialog
                Button imageView_close = dialogView.findViewById(R.id.imageView_close);
                final AlertDialog dialog = builder.create();

                imageView_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // Set negative/no button click listener
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss/cancel the alert dialog
                        //dialog.cancel();
                        dialog.dismiss();
                        Toast.makeText(getApplication(),
                                "Your request has been Submitted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();
            }
        });

        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(RedeemEarningsActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.redeem_recharge,null);

                // Specify alert dialog is not cancelable/not ignorable
                builder.setCancelable(false);

                // Set the custom layout as alert dialog view
                builder.setView(dialogView);

                // Get the custom alert dialog view widgets reference
                Button btn_negative = (Button) dialogView.findViewById(R.id.btnSubmit);
                final EditText et_name = (EditText) dialogView.findViewById(R.id.et2);
                final EditText et_mobile = (EditText) dialogView.findViewById(R.id.et);
                final EditText et_amount = (EditText) dialogView.findViewById(R.id.et3);

                // Create the alert dialog
                Button imageView_close = dialogView.findViewById(R.id.imageView_close);
                final AlertDialog dialog = builder.create();

                imageView_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // Set negative/no button click listener
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss/cancel the alert dialog
                        //dialog.cancel();
                        dialog.dismiss();
                        Toast.makeText(getApplication(),
                                "Your request has been Submitted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();
            }
        });

        electricityBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(RedeemEarningsActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.redeem_electricity_bill,null);

                // Specify alert dialog is not cancelable/not ignorable
                builder.setCancelable(false);

                // Set the custom layout as alert dialog view
                builder.setView(dialogView);

                // Get the custom alert dialog view widgets reference
                Button btn_negative = (Button) dialogView.findViewById(R.id.btnSubmit);
                final EditText et_name = (EditText) dialogView.findViewById(R.id.et2);
                final EditText et_mobile = (EditText) dialogView.findViewById(R.id.et);
                final EditText et_amount = (EditText) dialogView.findViewById(R.id.et3);

                // Create the alert dialog
                Button imageView_close = dialogView.findViewById(R.id.imageView_close);
                final AlertDialog dialog = builder.create();

                imageView_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // Set negative/no button click listener
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss/cancel the alert dialog
                        //dialog.cancel();
                        dialog.dismiss();
                        Toast.makeText(getApplication(),
                                "Your request has been Submitted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();
            }
        });

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
