package com.example.loh.billme_final_android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Activity_login extends Activity {

    TextView Vtxt_sign_up;

    @Bind(R.id.login_button)Button btn_login;
    @Bind(R.id.login_email)EditText Etxt_email;
    @Bind(R.id.login_password)EditText Etxt_password;
    @Bind(R.id.login_sign_up_button)TextView btn_sign_up;
    @Bind(R.id.login_content)CardView card_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        Typeface mTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "nord.ttf");

        btn_login.setTypeface(mTypeface);
        Etxt_email.setTypeface(mTypeface);
        Etxt_password.setTypeface(mTypeface);
        btn_sign_up.setTypeface(mTypeface);

        card_content.setBackgroundColor(getResources().getColor(R.color.white));


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the text entered from the EditText
                String txt_email = Etxt_email.getText().toString();
                String txt_password = Etxt_password.getText().toString();

                // Send data to Parse.com for verification
                ParseUser.logInInBackground(txt_email, txt_password,
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, com.parse.ParseException e) {
                                if (parseUser != null) {
                                    // If user exist and authenticated, send user to Activity_main.class
                                    Intent intent = new Intent(Activity_login.this, Activity_main.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Successfully Logged in", Toast.LENGTH_LONG).show();
                                    finish();
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                } else {
                                    Toast.makeText(getApplicationContext(), "No such user exist, please signup", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        Vtxt_sign_up = (TextView) findViewById(R.id.login_sign_up_button);
        Vtxt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_login.this,Activity_sign_up.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }


}
