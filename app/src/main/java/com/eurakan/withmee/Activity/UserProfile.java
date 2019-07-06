package com.eurakan.withmee.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eurakan.withmee.Adapters.ChatAdapter;
import com.eurakan.withmee.Adapters.FriendsListAdapter;
import com.eurakan.withmee.Adapters.PhotosAdapter;
import com.eurakan.withmee.Adapters.PostsAdapter;
import com.eurakan.withmee.CameraUtils;
import com.eurakan.withmee.ImagePath_MarshMallow;
import com.eurakan.withmee.Models.ChatModel;
import com.eurakan.withmee.Models.PhotoModel;
import com.eurakan.withmee.Models.PostsModel;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AndroidMultiPartEntity;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserProfile extends AppCompatActivity {
    private List<UserModel> friendList;
    private List<PhotoModel> photosList;
    Context context;
    RecyclerView friends_recycler_view, photos_recycler_view, posts_recycler_view;
    private Uri imageUri;
    PostsAdapter postsAdapter;
    private List<PostsModel> list_posts;
    Toolbar toolbar;
    UserModel user;
    String filePathFinal ="";
    ImageView profilePic, coverPhoto;
    Button editCoverPhoto;
    PhotosAdapter photosAdapter;
    TextView userProfileAddPhoto;
    FriendsListAdapter friendsListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Bundle extras = getIntent().getExtras();
        context = UserProfile.this;
        photosList = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user= AppPreferences.getInstance(getApplicationContext()).getUser();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Profile");
        profilePic = findViewById(R.id.user_profile);
        coverPhoto = findViewById(R.id.coverPhoto);
        userProfileAddPhoto = findViewById(R.id.userProfileAddPhoto);
        editCoverPhoto = findViewById(R.id.editCoverPhoto);


        coverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(UserProfile.this, PostViewActivity.class);
                in.putExtra("postUrl", user.getProfileCoverImageUrl());
                startActivity(in);
            }
        });

        userProfileAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(UserProfile.this, AddPhotoActivity.class);
                startActivity(in);
            }
        });

        editCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(UserProfile.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserProfile.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 20);
                } else {
                    final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
                    builder.setTitle("Add Photo");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Take Photo")) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                imageUri = CameraUtils.getOutputMediaFileUri(UserProfile.this);
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

        posts_recycler_view = (RecyclerView) findViewById(R.id.posts_recycler_view);
        list_posts = new ArrayList<>();

        posts_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        photos_recycler_view = (RecyclerView) findViewById(R.id.photos_grid);

        photos_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        friends_recycler_view = (RecyclerView) findViewById(R.id.friends_grid);
        friendList = new ArrayList<>();

        friends_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));



        try {
            userProfile();
            getPhotosList();
            getPostsList();
            getFriendsList();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Button editProfile = findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(UserProfile.this, EditUserProfile.class);
                startActivity(in);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void setFriendsList() {
        friendsListAdapter = new FriendsListAdapter(friendList, UserProfile.this);
        friendsListAdapter.notifyDataSetChanged();
        friends_recycler_view.setAdapter(friendsListAdapter);
    }

    private void setPhotosList() {
        photosAdapter = new PhotosAdapter(photosList, UserProfile.this);
        photosAdapter.notifyDataSetChanged();
        photos_recycler_view.setAdapter(photosAdapter);
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
                    getImageUrl = ImagePath_MarshMallow.getPath(UserProfile.this, imageUri);
                else
                    //else we will get path directly
                    getImageUrl = imageUri.getPath();

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(getImageUrl, bmOptions);//Decode image path
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                coverPhoto.setImageBitmap(bitmap);

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
                coverPhoto.setImageBitmap(thumbnail);
//            dialog = ProgressDialog.show(EditUserProfile.this, "", "Uploading file...", true);
                filePathFinal= picturePath;
                userCoverPhotoUpload();

            }
        }
    }

    private void userProfile() throws InterruptedException {
        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(UserProfile.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                asyncDialog.setMessage("Fetching Profile....");
                //show dialog
                asyncDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                asyncDialog.dismiss();
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (obj.getString("status").equalsIgnoreCase("Success")) {
                        Toast.makeText(getApplicationContext(), obj.getString("response"), Toast.LENGTH_SHORT).show();
                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("data");
                        //creating a new user object
                         user = new UserModel(
                                userJson.getInt("id"),
                                userJson.getString("mobileno"),
                                userJson.getString("email"),
                                userJson.getString("name"),
                                userJson.getString("aadhar_no"),
                                "",
                                userJson.getString("address_user"),
                                userJson.getString("worksat"),
                                userJson.getString("address_shop"),
                                userJson.getString("address_user"),
                                userJson.getString("studyin"),
                                userJson.getString("dateofbirth")
                        );
                        user.setProfileImageUrl(userJson.getString("user_photo"));
                        user.setProfileCoverImageUrl(userJson.getString("common_photo"));
                        //storing the user in shared preferences
                        Picasso.get().load(userJson.getString("user_photo")).placeholder(R.drawable.placeholder).into(profilePic);
                        Picasso.get().load(userJson.getString("common_photo")).placeholder(R.drawable.placeholder).into(coverPhoto);
                        AppPreferences.getInstance(getApplicationContext()).userLogin(user);
                        //starting the drawer activity
                        TextView userProfile = findViewById(R.id.userProfileName);
                        TextView userEmail = findViewById(R.id.userProfileEmail);
                        TextView userCity = findViewById(R.id.userProfileCity);
                        TextView userStatus = findViewById(R.id.userProfileStatus);
                        TextView userStudy = findViewById(R.id.userProfileStudy);
                        TextView userWork = findViewById(R.id.userProfileWork);
                        TextView userDateOfBirth = findViewById(R.id.userProfileDOB);
                        if(user != null) {
                            String name = user.GetFullName();
                            userProfile.setText(name);
                            userEmail.setText("Email: " + user.getEmail());
                            userCity.setText("Lives In: " + user.getFullAddress());
                            userStatus.setText("Living Status: " + user.getStatus());
                            userStudy.setText("Studys In: " + user.getStudyIn());
                            userWork.setText("Works At: " + user.getWork());
                            userDateOfBirth.setText("DOB: " + user.getDateOfBirth());
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }   catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                UserModel user= AppPreferences.getInstance(getApplicationContext()).getUser();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("userid", String.valueOf(user.getId()));
                params.put("roll", "1");
                //returing the response

                return requestHandler.sendGetRequest(Utilities.URL_GETUSERPROFILE, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }


    private void setPosts() {
        postsAdapter = new PostsAdapter(list_posts, context);
        postsAdapter.notifyDataSetChanged();
        posts_recycler_view.setAdapter(postsAdapter);
    }


    private void getPostsList() {

        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                UserModel user= AppPreferences.getInstance(context).getUser();

                params.put("userid", String.valueOf(user.getId()));

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_LISTPOSTS, params);
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

                        //getting the user from the response
                        JSONArray userJson = obj.getJSONArray("data");
                        for (int i = 0, size = userJson.length(); i < size; i++) {
                            JSONObject objectInArray = userJson.getJSONObject(i);
                            PostsModel postModel = new PostsModel(objectInArray.getInt("id"),objectInArray.getString("userimage"),objectInArray.getString("image"),objectInArray.getString("comment"),objectInArray.getInt("total_like"),objectInArray.getInt("userid"),objectInArray.getString("username"));
                            list_posts.add(postModel);

                        }
                        setPosts();

                    } else {
                        Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }

    private void userCoverPhotoUpload() {
        class UploadFileToServer extends AsyncTask<Void, Integer, String> {
            @Override
            protected void onPreExecute() {
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
                HttpPost httppost = new HttpPost(Utilities.URL_UPLOADCOVERPROFILE);

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
                        coverPhoto.setImageURI(imageUri);
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

                // showing the server response in an alert dialog
                showAlert(result);

                super.onPostExecute(result);
            }
            /**
             * Method to show alert dialog
             * */
            private void showAlert (String message){
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
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

    private void getFriendsList() {

        //if it passes all the validations

        class ChatList extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(UserProfile.this);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                UserModel user= AppPreferences.getInstance(UserProfile.this).getUser();
                params.put("user_id", String.valueOf(user.getId()));

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_GETCHATFRIENDSLIST, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Fetching Friends List....");
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
                    List<Integer> a = new ArrayList();
                    //if no error in response
                    if (obj.getString("status").equalsIgnoreCase("Success")) {
                        Toast.makeText(getApplicationContext(), obj.getString("response"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONArray userJson = obj.getJSONArray("data");
                        for (int i = 0, size = userJson.length() -1; i < size; i++) {
                            JSONObject objectInArray = userJson.getJSONObject(i);
                            UserModel user = new UserModel(
                                    objectInArray.getInt("friend_id"),
                                    objectInArray.getString("friend_mobileno"),
                                    objectInArray.getString("friend_email"),
                                    objectInArray.getString("friend_name"),
                                    "",
                                    "",
                                    objectInArray.getString("friend_address_user"),
                                    objectInArray.getString("friend_worksat"),
                                    objectInArray.getString("friend_status"),
                                    objectInArray.getString("friend_address_user"),
                                    objectInArray.getString("friend_studyin"),
                                    objectInArray.getString("friend_dateofbirth")
                            );
                            user.setProfileCoverImageUrl(objectInArray.getString("common_photo"));
                            user.setProfileImageUrl(objectInArray.getString("user_photo"));
                            if(a.contains(user.getId()) == false) {
                                friendList.add(user);
                                a.add(user.getId());
                            }
                        }
                        setFriendsList();
                    }
                    else if (obj.getString("status").equalsIgnoreCase("Warning")) {
                        Toast.makeText(getApplicationContext(), "No Friends found", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        ChatList ru = new ChatList();
        ru.execute();
    }

    private void getPhotosList() {

        //if it passes all the validations

        class ChatList extends AsyncTask<Void, Void, String> {

            ProgressDialog asyncDialog = new ProgressDialog(UserProfile.this);
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                UserModel user= AppPreferences.getInstance(UserProfile.this).getUser();
                params.put("user_id", String.valueOf(user.getId()));

                //returing the response
                return requestHandler.sendGetRequest(Utilities.URL_GETALLPHOTO, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                asyncDialog.setMessage("Fetching All Photos....");
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
                    List<Integer> a = new ArrayList();
                    //if no error in response
                    if (obj.getString("status").equalsIgnoreCase("Success")) {
                        Toast.makeText(getApplicationContext(), obj.getString("response"), Toast.LENGTH_SHORT).show();
                        //getting the user from the response
                        JSONArray userJson = obj.getJSONArray("data");
                        for (int i = 0, size = userJson.length(); i < size; i++) {
                            JSONObject objectInArray = userJson.getJSONObject(i);
                            PhotoModel user = new PhotoModel(
                                    objectInArray.getInt("id"),
                                    objectInArray.getString("pic"),
                                    objectInArray.getString("createdon")
                            );
                            photosList.add(user);
                        }
                        setPhotosList();
                    }
                    else if (obj.getString("status").equalsIgnoreCase("Warning")) {
                        Toast.makeText(getApplicationContext(), "No Photos found", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        ChatList ru = new ChatList();
        ru.execute();
    }
}
