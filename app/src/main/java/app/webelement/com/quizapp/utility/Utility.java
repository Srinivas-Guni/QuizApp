package app.webelement.com.quizapp.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.webelement.com.quizapp.Model.User;
import app.webelement.com.quizapp.R;
import app.webelement.com.quizapp.activities.NavDrawerActivity;

/**
 * Created by ADMIN on 2/6/16.
 */
public class Utility {


    public static boolean checkNetworkConnection(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }

    public static void showSnackBar(Context context, String message) {


        AppCompatActivity activity = (AppCompatActivity) context;
        View rootView = activity.getWindow().getDecorView().findViewById(R.id.rootView);

        Snackbar snackbar = Snackbar
                .make((CoordinatorLayout) rootView, message, Snackbar.LENGTH_LONG);

        snackbar.show();

    }

    public static void storeUser(Context context, User user) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogged", true);


        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("me", json);
        editor.commit();


    }

    public static boolean checkIsLoggedIn(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", 0);
        boolean isLogged = false;

        if (sharedPreferences != null && sharedPreferences.contains("isLogged")) {

            isLogged = sharedPreferences.getBoolean("isLogged", false);

        }

        return isLogged;
    }

    public static User getUser(Context context) {


        SharedPreferences sharedPreferences = context.getSharedPreferences("user", 0);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("me", "");
        Log.d("user", json);

        User user = gson.fromJson(json, User.class);
        return user;

    }

    public static void logout(Context context) {


        SharedPreferences sharedPreferences = context.getSharedPreferences("user", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogged", false);

        editor.putString("me", "");
        editor.commit();


    }

    public static String formatDate(String dateInString) {

        SimpleDateFormat initialDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = initialDateFormat.parse(dateInString);

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            String formattedDate = formatter.format(date);
            System.out.println(formattedDate);
            return formattedDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }


    public static String formatToYesterdayOrToday(Context context, String date) {


        Date dateTime = null;
        try {
            dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("hh:mm a");

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            String time_string = DateUtils.getRelativeTimeSpanString(dateTime.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            return "Today " + time_string;
            //return "Today " + timeFormatter.format(dateTime);
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday " + timeFormatter.format(dateTime);
        } else {

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            String formattedDate = formatter.format(dateTime);
            System.out.println(formattedDate);


            return formattedDate;


        }
    }

}
