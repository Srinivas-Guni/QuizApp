package app.webelement.com.quizapp.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import app.webelement.com.quizapp.Model.User;
import app.webelement.com.quizapp.R;
import app.webelement.com.quizapp.fragment.ContentFragment;
import app.webelement.com.quizapp.fragment.MyExams;
import app.webelement.com.quizapp.utility.Utility;

/**
 * Created by ADMIN on 3/26/16.
 */
public class NavDrawerActivity extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView email, username;
    private NavDrawerActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        context = this;
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);


        getHeader();
        setUserProfile();

        //set up home fragment
        setUpHomeFragment();


        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();
                android.support.v4.app.Fragment fragment = null;

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.quiz:
                       // Toast.makeText(getApplicationContext(), "Inbox Selected", Toast.LENGTH_SHORT).show();
                        fragment = new ContentFragment();

                        break;

                    // For rest of the options we just show a toast on click

                    case R.id.myexam:
                     //   Toast.makeText(getApplicationContext(), "My Exam Selected", Toast.LENGTH_SHORT).show();
                        fragment = new MyExams();

                        break;
                    case R.id.sent_mail:
                      //  Toast.makeText(getApplicationContext(), "Send Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.drafts:
                    //    Toast.makeText(getApplicationContext(), "Drafts Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.allmail:
                     //   Toast.makeText(getApplicationContext(), "All Mail Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.trash:
                    //    Toast.makeText(getApplicationContext(), "Trash Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:

                        Utility.logout(context);
                        finish();


                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        break;


                }

                if (fragment != null) {
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.commit();

                }
                return true;
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


    }

    private void setUserProfile() {

        if (Utility.checkIsLoggedIn(context)) {
            User user = Utility.getUser(context);

            String s[] = user.getName().split("@");
            username.setText(s[0]);
            email.setText(user.getName());

        }


    }

    private void getHeader() {

        View view = navigationView.getHeaderView(0);
        Log.d("view", view.toString());

        username = (TextView) view.findViewById(R.id.username);
        email = (TextView) view.findViewById(R.id.email);


    }

    private void setUpHomeFragment() {


        ContentFragment fragment = new ContentFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
