package app.webelement.com.quizapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.webelement.com.quizapp.MainActivity;
import app.webelement.com.quizapp.R;
import app.webelement.com.quizapp.utility.Utility;

/**
 * Created by ADMIN on 3/26/16.
 */
public class Splash extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.splash);
        final ImageView logo = (ImageView) findViewById(R.id.logo);
        YoYo.with(Techniques.ZoomIn).duration(2500).playOn(logo);

        setData();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (Utility.checkIsLoggedIn((Context) context)) {


                    Intent i = new Intent(Splash.this, NavDrawerActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                } else {

                    Intent i = new Intent(Splash.this, Login.class);
                    startActivity(i);

                    // close this activity
                    finish();

                }

            }
        }, 3000);
    }

    private void setData() {


        String dateInString = "07/06/2013";


        Date date = new Date(dateInString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(dateInString));

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        String format = formatter.format(date);
        System.out.println(format);


    }
}
