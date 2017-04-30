package com.fame.plumbum.lithicsin.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fame.plumbum.lithicsin.R;
import com.fame.plumbum.lithicsin.Singleton;
import com.fame.plumbum.lithicsin.fragments.Home;
import com.fame.plumbum.lithicsin.fragments.Inventory;
import com.fame.plumbum.lithicsin.fragments.MyAccount;
import com.fame.plumbum.lithicsin.fragments.MyOrders;
import com.fame.plumbum.lithicsin.fragments.SchedulePickup;
import com.fame.plumbum.lithicsin.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    LinearLayout analytics_home;
    FrameLayout view;
    TextView connect;
    ImageButton slide;
    TextView fb, blog, twitter;
    Button cancel;
    String fb_link, blog_link, twitter_link;
    boolean onHome = true, socialClicked = false;
    DrawerLayout drawer;
    ImageView org_image;

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
            onHome = true;
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        onHome = false;
        if (id == R.id.home) {
            fragment = new Home();
            onHome = true;
        } else if (id == R.id.my_account) {
            fragment = new MyAccount();
        } else if (id == R.id.inventory) {
            fragment = new Inventory();
        } else if (id == R.id.my_orders) {
            fragment = new MyOrders();
        } else if (id == R.id.schedule_pickup) {
            fragment = new SchedulePickup();
        } else if (id == R.id.nav_email) {

        } else if (id == R.id.chat) {
            final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            Log.e(getClass().getName(), sp.getString("token", "xcf"));
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL_DEFAULT + "checkIfAdmin.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(getClass().getName(), response + " " + sp.getString("id", "asd"));
                            if (response.contentEquals("1")){
                                Intent intent = new Intent(MainActivity.this, com.fame.plumbum.lithicsin.activities.MessageExchangeList.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent = new Intent(MainActivity.this, com.fame.plumbum.lithicsin.activities.MessageExchange.class);
                                intent.putExtra("chat_id", sp.getString("id", "1"));
                                intent.putExtra("name", "Lithics.in");
                                startActivity(intent);
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Error receiving data!", Toast.LENGTH_SHORT).show();
                }
            }){
                protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    params.put("id", sp.getString("id", ""));
                    return params;
                }};
            Singleton.getInstance().addToRequestQueue(stringRequest);
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

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void init() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
//        org_image = (ImageView) findViewById(R.id.org_image);
//        getImage();

//        drawer.openDrawer(GravityCompat.START);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView name_org = (TextView) headerView.findViewById(R.id.name_organization);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (sp.contains("name"))
            name_org.setText(sp.getString("name", "Lithics.in"));
        navigationView.setNavigationItemSelectedListener(this);
        slide = (ImageButton) findViewById(R.id.slide);
        view = (FrameLayout) findViewById(R.id.content_frame);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        analytics_home = (LinearLayout) findViewById(R.id.header_main);
        ft.replace(R.id.content_frame, new Home());
        view.setVisibility(View.VISIBLE);
        ft.commit();
        fb = (TextView) findViewById(R.id.fb);
        blog = (TextView) findViewById(R.id.blog);
        twitter = (TextView) findViewById(R.id.twitter);
        connect = (TextView) findViewById(R.id.connect);
        connect.setOnClickListener(this);
        slide.setOnClickListener(this);
        fb.setOnClickListener(this);
        blog.setOnClickListener(this);
        twitter.setOnClickListener(this);
    }

    private void getImage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL_DEFAULT+"getImage.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(getClass().getName(), response);
                        Picasso.with(MainActivity.this).load("http://www.lithics.in/media/profile/"+response).resize(120, 120).into(org_image);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error receiving data!", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                params.put("seller_id", sp.getString("id", ""));
                return params;
            }};
        Singleton.getInstance().addToRequestQueue(stringRequest);
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