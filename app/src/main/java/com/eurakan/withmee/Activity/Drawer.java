package com.eurakan.withmee.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eurakan.withmee.Fragment.Dashboard;
import com.eurakan.withmee.Fragment.Donation;
import com.eurakan.withmee.Fragment.EarningsFragment;
import com.eurakan.withmee.Fragment.ProductsFragment;
import com.eurakan.withmee.Fragment.SearchFriendsFragment;
import com.eurakan.withmee.Models.UserModel;
import com.eurakan.withmee.Preferences.AppPreferences;
import com.eurakan.withmee.Preferences.BottomNavigationViewHelper;
import com.eurakan.withmee.Preferences.RequestHandler;
import com.eurakan.withmee.Preferences.Utilities;
import com.eurakan.withmee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Drawer extends AppCompatActivity {

    //private static final String TAG = MemberDrawer.class.getSimpleName();
    private FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private Fragment fragment = null;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    protected AppPreferences appPrefs;
    NavigationView navigationView;
    public static Toolbar toolbar;
    private BottomNavigationView bottomNavigation;
    boolean doubleBackToExitPressedOnce = false;

    private String firebaseToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Dashboard");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
//To do//
                            return;
                        }
                    }
                });
        appPrefs = new AppPreferences(Drawer.this);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.inflateMenu(R.menu.bottom_nav);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fragmentManager = getSupportFragmentManager();
        if (Utilities.checkNetworkConnection(Drawer.this)) {
            bottomNavigation.setSelectedItemId(R.id.home);
            fragmentTransaction = fragmentManager.beginTransaction();
            fragment = new Dashboard();
            fragmentTransaction.replace(R.id.main_container, fragment);
            fragmentTransaction.commit();
        } else {
            Toast.makeText(Drawer.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            finish();
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        //View header = navigationView.inflateHeaderView(R.layout.nav_header);
        //TextView name = (TextView) header.findViewById(R.id.name);
        //name.setText("Welcome " + appPrefs.getProviderName());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                navigationView.clearFocus();
                navigationView.requestLayout();
                Intent in;
                switch (id) {

                       case R.id.profile:
                        startActivity(new Intent(Drawer.this, UserProfile.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.changePassword:
                        startActivity(new Intent(Drawer.this, ChangePassword.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.pendingFriends:
                        startActivity(new Intent(Drawer.this, PendingFriendRequests.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.aboutUs:
                        in = new Intent(Drawer.this, AboutUsActivity.class);
                        in.putExtra("url", "http://retailgrow.in/retailgrow/aboutus.html");
                        in.putExtra("type", "aboutus");
                        startActivity(in);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.terms:
                        in = new Intent(Drawer.this, AboutUsActivity.class);
                        in.putExtra("url", "http://retailgrow.in/retailgrow/terms-conditions.html");
                        in.putExtra("type", "terms");
                        startActivity(in);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.privacyPolicy:
                        in = new Intent(Drawer.this, AboutUsActivity.class);
                        in.putExtra("url", "http://retailgrow.in/retailgrow/privacy-policy.html");
                        in.putExtra("type", "privacy");
                        startActivity(in);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.faqs:
                        in = new Intent(Drawer.this, AboutUsActivity.class);
                        in.putExtra("url", "http://retailgrow.in/retailgrow/faqs.html");
                        in.putExtra("type", "faqs");
                        startActivity(in);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.contactus:
                        in = new Intent(Drawer.this, AboutUsActivity.class);
                        in.putExtra("url", "http://retailgrow.in/retailgrow/contact_us.html");
                        in.putExtra("type", "contactus");
                        startActivity(in);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.logout:
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(Drawer.this, R.style.AppCompatAlertDialogStyle);
                        builder.setTitle(Html.fromHtml("<b>Logout</b>"));
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AppPreferences.getInstance(getApplicationContext()).logout();
                                Toast.makeText(Drawer.this, "Logout successfully", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(Drawer.this, LoginActivity.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.show();
                        break;


                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                assert drawer != null;
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //BottomNavigationViewHelper.disableShiftMode(bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {

                    case R.id.home:
                        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStack();
                        }
                        fragment = new Dashboard();
                        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);
                        break;

                    case R.id.earnings:
                        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStack();
                        }
                        fragment = new EarningsFragment();
                        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);
                        break;

                    case R.id.cart:
                        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStack();
                        }
                        fragment = new ProductsFragment();
                        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);
                        break;

                    case R.id.donation:
                        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStack();
                        }
                        fragment = new Donation();
                        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);
                        break;

                    case R.id.search_friends:
                        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStack();
                        }
                        fragment = new SearchFriendsFragment();
                        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);
                        break;

                }

                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notification_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.chat:
                Intent in = new Intent(Drawer.this, Chats.class);
                startActivity(in);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;

            case R.id.notifications:
                Intent in1 = new Intent(Drawer.this, Notifications.class);
                startActivity(in1);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;

            case R.id.usercart:
                Intent intent = new Intent(Drawer.this, CartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
