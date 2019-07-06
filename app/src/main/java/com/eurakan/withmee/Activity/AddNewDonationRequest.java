package com.eurakan.withmee.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.eurakan.withmee.CameraUtils;
import com.eurakan.withmee.Models.DonationModel;
import com.eurakan.withmee.R;

public class AddNewDonationRequest extends AppCompatActivity {

    Toolbar toolbar;
    private Uri imageUri;
    Button uploadPhotoforDonation, updateProfileSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_donation_request);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add New Donation");

        uploadPhotoforDonation = findViewById(R.id.uploadPhotoforDonation);
        updateProfileSubmit = findViewById(R.id.updateProfileSubmit);

        uploadPhotoforDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(AddNewDonationRequest.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddNewDonationRequest.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 20);

                } else {
                    final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddNewDonationRequest.this);
                    builder.setTitle("Add Photo");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Take Photo")) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                imageUri = CameraUtils.getOutputMediaFileUri(AddNewDonationRequest.this);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, 100);
                            } else if (options[item].equals("Choose from Gallery")) {
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 200 );
                            }
                        }
                    });
                    builder.show();
                }
            }
        });

        updateProfileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddNewDonationRequest.this,"Your Request has been submitted.", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
