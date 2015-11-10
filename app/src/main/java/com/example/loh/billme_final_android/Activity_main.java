package com.example.loh.billme_final_android;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.loh.billme_final_android.Parse_subclass.User;
import com.parse.Parse;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private PullRefreshLayout refresh;


    @Bind(R.id.content_profile_pic)CircleImageView content_profile_pic;
    @Bind(R.id.content_fullname)TextView content_fullname;
    @Bind(R.id.content_email)TextView content_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refresh.setRefreshing(false);
                Intent intent = new Intent(Activity_main.this, Activity_add_bill.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SpannableString s = new SpannableString("Home");
        s.setSpan(new com.example.loh.billme_final_android.TypefaceSpan(this, "nord.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle(s);
        //Bind all,professionally
        ButterKnife.bind(this);

        Typeface mTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "nord.ttf");

        User user = (User) ParseUser.getCurrentUser();
        Picasso.with(this).load(user.getProfile().getUrl()).into(content_profile_pic);
        content_fullname.setText(sort_name(user.getFullName()));
        content_email.setText(user.getUsername());
        content_email.setTypeface(mTypeface);
        content_fullname.setTypeface(mTypeface);

        refresh = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        refresh.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        refresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(Activity_main.this, "refreshing", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_multiple) {
            Intent intent = new Intent(Activity_main.this,Activity_group_invite.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_friend) {
            Intent intent = new Intent(Activity_main.this,Activity_friend_list.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_group) {

        } else if (id == R.id.nav_invite) {
            Intent intent= new Intent(Activity_main.this,Activity_invite_list.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_logout) {
            ParseUser.getCurrentUser().logOut();
            Intent intent = new Intent(Activity_main.this, Activity_login.class);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public StringBuilder sort_name(String fullname){
        String myFullname =fullname.toUpperCase();

        StringBuilder myName = new StringBuilder(fullname);
        myName.setCharAt(0, myFullname.charAt(0));

        return myName;
    }
}
