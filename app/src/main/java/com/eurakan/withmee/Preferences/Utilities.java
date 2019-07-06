package com.eurakan.withmee.Preferences;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities extends Activity {

    public static String URL = "";
    public static final String ROOT_URL = "http://retailgrow.in/newServices/";
    public static final String PROFILE_IMAGE_ROOT_URL = "http://retailgrow.in/newServices/";

    public static final String URL_REGISTER = ROOT_URL + "userresister.php";
    public static final String URL_LOGIN= ROOT_URL + "user_login_with_me.php";
    public static final String URL_FORGOT_PASSWORD= ROOT_URL + "forgetpassword.php";
    public static final String URL_CHANGE_PASSWORD= ROOT_URL + "user_change_password.php";
    public static final String URL_UPDATE_PROFILE= ROOT_URL + "update_user_profile.php";
    public static final String URL_NOTIFICATIONLIST = ROOT_URL + "get_all_notification.php";
    public static final String URL_GETUSERPROFILE = ROOT_URL + "get_user_profile.php";
    public static final String URL_SEARCHFRIEND = ROOT_URL + "withme/friends_list.php";
    public static final String URL_SENDFRIENDREQUEST = ROOT_URL + "withme/friends_request.php";
    public static final String URL_PENDINGSENTFRIENDREQUEST = ROOT_URL + "withme/pending_sent_friend_request.php";
    public static final String URL_PENDINGRECEIVEDFRIENDREQUEST = ROOT_URL + "withme/pending_friend_request.php";
    public static final String URL_ACCEPTFRIENDREQUEST = ROOT_URL + "withme/friends_request_accept.php";
    public static final String URL_DELETEFRIENDREQUEST = ROOT_URL + "withme/friends_request_delete.php";
    public static final String URL_GETCHATFRIENDSLIST = ROOT_URL + "withme/friends_list_outer_list.php";
    public static final String URL_GETCHATFRIENDSMESSAGES = ROOT_URL + "withme/friends_list_for_chat.php";
    public static final String URL_GETCHATSENDMESSAGES = ROOT_URL + "withme/send_message.php";
    public static final String URL_UPLOADPROFILE = ROOT_URL + "us_wm_pro.php";
    public static final String URL_LISTPOSTS = ROOT_URL + "wtm_pst_dir_user_data_list.php";
    public static final String URL_GETCOMMENTSFORPOST = ROOT_URL + "withme/wtme_post_comment_list.php";
    public static final String URL_POSTCOMMENT = ROOT_URL + "withme/wtme_post_comment.php";
    public static final String URL_POSTIMAGE = ROOT_URL + "withme/image_upload.php";
    public static final String URL_GLOBALLISTPOSTS = ROOT_URL + "wtm_pst_dir_user_data_list_global_posts.php";

    public static final String URL_UPLOADCOVERPROFILE = ROOT_URL + "us_wm_coverphoto.php";
    public static final String URL_UPLOADPHOTO = ROOT_URL + "us_wm_photos_upload.php";
    public static final String URL_GETALLPHOTO = ROOT_URL + "get_all_photos.php";
    public static final String URL_GETALLDONATIONS = ROOT_URL + "withme/user_donation_manager.php";
    public static final String URL_UPDATEFIREBASE = ROOT_URL + "noti/register.php";
    public static final String URL_LIKEPOST = ROOT_URL + "user_like.php";

    public static final String URL_GETALLPRODUCTS = ROOT_URL + "rtgrow/user_product_search.php";
    public static final String URL_GETPRODUCTDETAILS = ROOT_URL + "rtgrow/product_details.php";
    public static final String URL_ADDPRODUCTTOCART = ROOT_URL + "rtgrow/cart_insert_data.php";
    public static final String URL_DELETEPRODUCTTOCART = ROOT_URL + "rtgrow/delete_cart_product.php";
    public static final String URL_GETCART = ROOT_URL + "rtgrow/get_all_cart_product_by_user_id.php";
    public static final String URL_CONFIRMCART = ROOT_URL + "rtgrow/user_order_confirm.php";
    public static final String URL_USEREARNINGS = ROOT_URL + "withme/user_earnings_details.php";


    public static void goToPage(Context paramContext, Class paramClass, Bundle paramBundle) {
        Intent localIntent = new Intent(paramContext, paramClass);
        if (paramBundle != null)
            localIntent.putExtra("android.intent.extra.INTENT", paramBundle);
        paramContext.startActivity(localIntent);
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            System.out.println("Exc=" + e);
            return null;
        }
    }

    public static boolean checkNetworkConnection(Context paramContext) {
        int i = 1;
        boolean flag = true;
        ConnectivityManager connectivity = (ConnectivityManager) paramContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo localNetworkInfo1 = connectivity.getNetworkInfo(i);
            NetworkInfo localNetworkInfo2 = connectivity.getActiveNetworkInfo();
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            System.out.println("wifi" + localNetworkInfo1.isAvailable());
            System.out.println("info" + localNetworkInfo2);

            if (((localNetworkInfo2 == null) || (!localNetworkInfo2
                    .isConnected())) && (!localNetworkInfo1.isAvailable()))
                i = 0;
            if (info != null) {
                for (int j = 0; j < info.length; j++)
                    if (info[j].getState() == NetworkInfo.State.CONNECTED) {
                        i = 1;
                        break;
                    } else
                        i = 0;
            }

        } else
            i = 0;

        if (i == 0)
            flag = false;
        if (i == 1)
            flag = true;

        return flag;
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isTablet(Context paramContext, String tab) {
        //String tab = paramContext.getResources().getString(R.string.isTablet);
        if (tab.equals("0"))
            return false;
        else
            return true;
    }

    public static String convertGMTtoDate(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("MMM dd, yyyy");
        TimeZone tz = TimeZone.getDefault(); //Will return your device current time zone
        dateformat.setTimeZone(tz); //Set the time zone to your simple date formatter
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        Date currenTimeZone = (Date) calendar.getTime();
        return dateformat.format(currenTimeZone);
    }

    public static String convertGMTtoTime(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm a");
        TimeZone tz = TimeZone.getDefault(); //Will return your device current time zone
        dateformat.setTimeZone(tz); //Set the time zone to your simple date formatter
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        Date currenTimeZone = (Date) calendar.getTime();
        return dateformat.format(currenTimeZone);
    }

    public static String getCountOfDays(String createdDateString, String expireDateString) {
        //DateTimeUtils obj = new DateTimeUtils();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String diff = "";
        try {
            //Date date1 = simpleDateFormat.parse("10/10/2013 11:30:10");
            //Date date2 = simpleDateFormat.parse("13/10/2013 20:35:55");

            Date date1 = simpleDateFormat.parse(createdDateString);
            Date date2 = simpleDateFormat.parse(expireDateString);

            diff = printDifference(date1, date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return diff;
    }

    public static String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return ""+elapsedDays+" Days";
    }

    public static String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
