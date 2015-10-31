package com.example.loh.billme_final_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class Activity_mainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_main_page);

        ImageView imageView= new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);

        //set the main button
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .build();
        //set the sub button
        ImageView subbutton1 = new ImageView(this);
        subbutton1.setImageResource(android.R.drawable.ic_btn_speak_now);
        ImageView subbutton2 = new ImageView(this);
        subbutton2.setImageResource(android.R.drawable.ic_menu_camera);
        ImageView subbutton3 = new ImageView(this);
        subbutton3.setImageResource(android.R.drawable.ic_media_ff);
        ImageView subbutton4 = new ImageView(this);
        subbutton4.setImageResource(android.R.drawable.ic_dialog_alert);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
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
    }

}
