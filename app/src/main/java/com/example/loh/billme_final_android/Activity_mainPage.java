package com.example.loh.billme_final_android;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import android.os.Handler;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class Activity_mainPage extends Activity implements WaveSwipeRefreshLayout.OnRefreshListener {

    private ListView mListview;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_main_page);

        ImageView imageView= new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_action_new);

        //set the main button
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .setBackgroundDrawable(R.drawable.button_action_blue_selector)
                .build();


        //set the sub button
        ImageView subbutton1 = new ImageView(this);
        subbutton1.setImageResource(android.R.drawable.ic_menu_week);
        ImageView subbutton2 = new ImageView(this);
        subbutton2.setImageResource(android.R.drawable.ic_menu_camera);
        ImageView subbutton3 = new ImageView(this);
        subbutton3.setImageResource(android.R.drawable.ic_media_ff);
        ImageView subbutton4 = new ImageView(this);
        subbutton4.setImageResource(android.R.drawable.ic_dialog_alert);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue_selector));

        SubActionButton button1 = itemBuilder.setContentView(subbutton1).build();
        SubActionButton button2 = itemBuilder.setContentView(subbutton2).build();
        SubActionButton button3 = itemBuilder.setContentView(subbutton3).build();
        SubActionButton button4 = itemBuilder.setContentView(subbutton4).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .attachTo(actionButton)
                .build();

        subbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Activity_mainPage.this, "success", Toast.LENGTH_SHORT).show();
            }
        });
        initView();
    }

    private void initView() {
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setWaveColor(Color.BLUE);
        //mWaveSwipeRefreshLayout.setMaxDropHeight(1500);

        mListview = (ListView) findViewById(R.id.mainlist);
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    protected void onResume() {
        refresh();
        super.onResume();
    }

    private void refresh(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mWaveSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}

