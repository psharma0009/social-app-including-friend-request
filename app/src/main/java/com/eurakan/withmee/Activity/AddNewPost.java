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
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

public class AddNewPost extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Uri imageUri;
    TextView image;
    Toolbar toolbar;
    String str_profile="";
    String filePathFinal ="";
    String post_comment = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add New Post");

        image = findViewById(R.id.uploadImage);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etpost_Comment = findViewById(R.id.etPost_Comment);
                post_comment = etpost_Comment.getText().toString();
                if (ContextCompat.checkSelfPermission(AddNewPost.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddNewPost.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 20);
                } else {
                    final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddNewPost.this);
                    builder.setTitle("Add Photo");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Take Photo")) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                imageUri = CameraUtils.getOutputMediaFileUri(AddNewPost.this);
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

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
                    getImageUrl = ImagePath_MarshMallow.getPath(AddNewPost.this, imageUri);
                else
                    //else we will get path directly
                    getImageUrl = imageUri.getPath();

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(getImageUrl, bmOptions);//Decode image path
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                str_profile = Base64.encodeToString(b, Base64.DEFAULT);
                filePathFinal = getImageUrl;
                ImageView postImage = findViewById(R.id.postImage);
                postImage.setImageBitmap(bitmap);
                userLogin();

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
                long lengthbmp = b.length;
//                if(lengthbmp < 3000) {
                    str_profile = Base64.encodeToString(b, Base64.DEFAULT);
//            dialog = ProgressDialog.show(EditUserProfile.this, "", "Uploading file...", true);
                    filePathFinal = picturePath;
                    ImageView postImage = findViewById(R.id.postImage);
                    postImage.setImageBitmap(thumbnail);
                    userLogin();
//                }else{
//                    new AlertDialog.Builder(AddNewPost.this)
//                            .setTitle("Size exceeds")
//                            .setMessage("Size exceeds 3 mb")
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
//                }
            }
        }
    }

    private void userLogin() {
        class UploadFileToServer extends AsyncTask<Void, Integer, String> {
            ProgressDialog asyncDialog = new ProgressDialog(AddNewPost.this);

            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage("Uploading Image....");
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
                HttpPost httppost = new HttpPost(Utilities.URL_POSTIMAGE);
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
                    entity.addPart("imagepath", new FileBody(sourceFile));

                    UserModel user = AppPreferences.getInstance(getApplicationContext()).getUser();
                    // Extra parameters if you want to pass to server
                    entity.addPart("userid",
                            new StringBody(String.valueOf(user.getId())));
                    entity.addPart("comment",
                            new StringBody(post_comment));

                    radioGroup = (RadioGroup) findViewById(R.id.radioPostMode);
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radioButton = (RadioButton) findViewById(selectedId);
                    String post_mode = "2";
                    if(radioButton.getText().toString().equalsIgnoreCase("Private"))
                        post_mode = "1";

                    entity.addPart("post_mode",
                            new StringBody(post_mode));

//                totalSize = entity.getContentLength();
                    httppost.setEntity(entity);

                    // Making server call
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        // Server response
                        responseString = EntityUtils.toString(r_entity);
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }

                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                } catch (IOException e) {
                    responseString = e.toString();
                }

                return responseString;

            }

            @Override
            protected void onPostExecute(String result) {
                asyncDialog.dismiss();
                // showing the server response in an alert dialog
                showAlert(result);

                super.onPostExecute(result);
            }
            /**
             * Method to show alert dialog
             * */
            private void showAlert (String message){
                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewPost.this);
                builder.setMessage("Your Post has been successfully added.").setTitle("Image Uploaded Successfully")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
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
