package com.eurakan.withmee.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.eurakan.withmee.CameraUtils;
import com.eurakan.withmee.ImagePath_MarshMallow;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AndroidMultiPartEntity;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class AddPhotoActivity extends AppCompatActivity {
    Toolbar toolbar;
    String filePathFinal ="";
    private Uri imageUri;
    ImageView addPhoto, addedImage;
    String photo_image_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Photo");
        addPhoto = findViewById(R.id.addPhoto);
        addedImage = findViewById(R.id.addedImage);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddPhotoActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddPhotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 20);
                } else {
                    final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddPhotoActivity.this);
                    builder.setTitle("Add Photo");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Take Photo")) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                imageUri = CameraUtils.getOutputMediaFileUri(AddPhotoActivity.this);
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
    }

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                launchMediaScanIntent();
                String getImageUrl;
                if (Build.VERSION.SDK_INT > 22)
                    getImageUrl = ImagePath_MarshMallow.getPath(AddPhotoActivity.this, imageUri);
                else
                    //else we will get path directly
                    getImageUrl = imageUri.getPath();

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(getImageUrl, bmOptions);//Decode image path
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                addedImage.setImageBitmap(bitmap);
                addNewPhoto();
            } else if (requestCode == 200) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                addedImage.setImageBitmap(thumbnail);
//            dialog = ProgressDialog.show(EditUserProfile.this, "", "Uploading file...", true);
                filePathFinal= picturePath;
                addNewPhoto();
            }
        }
    }


    private void addNewPhoto() {
        class UploadFileToServer extends AsyncTask<Void, Integer, String> {
            ProgressDialog asyncDialog = new ProgressDialog(AddPhotoActivity.this);

            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage("Uploading Photo....");
                //show dialog
                asyncDialog.show();
                // setting progress bar to zero
                super.onPreExecute();
            }

            @Override
            protected void onProgressUpdate(Integer... progress) {
                // Making progress bar visible

                // updating progress bar value
            }

            @Override
            protected String doInBackground(Void... params) {
                return uploadFile();
            }

            @SuppressWarnings("deprecation")
            private String uploadFile() {
                String responseString = null;

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Utilities.URL_UPLOADPHOTO);

                try {
                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                            new AndroidMultiPartEntity.ProgressListener() {

                                @Override
                                public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
                                }
                            });
                    File sourceFile = new File(filePathFinal);

                    // Adding file data to http body
                    entity.addPart("image", new FileBody(sourceFile));
                    UserModel user= AppPreferences.getInstance(getApplicationContext()).getUser();
                    // Extra parameters if you want to pass to server
                    entity.addPart("userid",
                            new StringBody(String.valueOf(user.getId())));

//                totalSize = entity.getContentLength();
                    httppost.setEntity(entity);

                    // Making server call
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        addedImage.setImageURI(imageUri);
                        // Server response
                        responseString = EntityUtils.toString(r_entity);
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }
                    asyncDialog.dismiss();

                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                } catch (IOException e) {
                    responseString = e.toString();
                }

                return responseString;
            }

            @Override
            protected void onPostExecute(String result) {

                // showing the server response in an alert dialog
                showAlert(result);

                super.onPostExecute(result);
            }
            /**
             * Method to show alert dialog
             * */
            private void showAlert (String message){
                AlertDialog.Builder builder = new AlertDialog.Builder(AddPhotoActivity.this);
                builder.setMessage("Your photo has been successfully Updated.").setTitle("Successfully updated")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        UploadFileToServer ul = new UploadFileToServer();
        ul.execute();
    }
}
