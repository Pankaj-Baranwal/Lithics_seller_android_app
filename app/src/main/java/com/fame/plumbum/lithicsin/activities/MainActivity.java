package com.fame.plumbum.lithicsin.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.fragments.Home;
import com.fame.plumbum.lithicsin.fragments.Inventory;
import com.fame.plumbum.lithicsin.fragments.MyAccount;
import com.fame.plumbum.lithicsin.fragments.MyOrders;
import com.fame.plumbum.lithicsin.fragments.SchedulePickup;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    LinearLayout analytics_home;
    FrameLayout view;
    TextView connect;
    ImageButton slide, fb, blog, twitter;
    Button cancel;
    String fb_link, blog_link, twitter_link;
    boolean onHome = true, socialClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_main);
        init();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!onHome){
            Fragment fragment = new Home();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            analytics_home.setVisibility(GONE);
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            fragment = new Home();
        } else if (id == R.id.my_account) {
            fragment = new MyAccount();
        } else if (id == R.id.inventory) {
            fragment = new Inventory();
        } else if (id == R.id.my_orders) {
            fragment = new MyOrders();
        } else if (id == R.id.schedule_pickup) {
            fragment = new SchedulePickup();
        } else if (id == R.id.settings) {

        } else if (id == R.id.nav_email) {

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (id != R.id.home)
                analytics_home.setVisibility(GONE);
            else
                analytics_home.setVisibility(View.VISIBLE);
            ft.replace(R.id.content_frame, fragment);
            view.setVisibility(View.VISIBLE);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void init() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                slide.setVisibility(GONE);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                slide.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        /*
         Main Address
         Landmark
         City
         State
         Pincode

         */

//        drawer.openDrawer(GravityCompat.START);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        slide = (ImageButton) findViewById(R.id.slide);
        view = (FrameLayout) findViewById(R.id.content_frame);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        analytics_home = (LinearLayout) findViewById(R.id.header_main);
        ft.replace(R.id.content_frame, new Home());
        view.setVisibility(View.VISIBLE);
        ft.commit();
        fb = (ImageButton) findViewById(R.id.fb);
        blog = (ImageButton) findViewById(R.id.blog);
        twitter = (ImageButton) findViewById(R.id.twitter);
        connect = (TextView) findViewById(R.id.connect);
        connect.setOnClickListener(this);
        slide.setOnClickListener(this);
        fb.setOnClickListener(this);
        blog.setOnClickListener(this);
        twitter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        LinearLayout social = (LinearLayout) findViewById(R.id.social_ll);
        if (socialClicked){

        }
        switch (view.getId()){
            case R.id.connect:
                social.setVisibility(View.VISIBLE);
                socialClicked = true;
                break;
            case R.id.slide:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.cancel:
                social.setVisibility(View.GONE);
                break;
            case R.id.fb:
                openBrowser(fb_link);
                break;
            case R.id.twitter:
                openBrowser(twitter_link);
                break;
            case R.id.blog:
                openBrowser(blog_link);
                break;
        }
    }

    void openBrowser(String text){

    }
}