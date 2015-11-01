package com.example.loh.billme_final_android;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import java.text.ParseException;

public class Activity_login extends Activity {

    Button btn_login;
    EditText Etxt_email;
    EditText Etxt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login= (Button) findViewById(R.id.login_button);
        Etxt_email = (EditText) findViewById(R.id.login_email);
        Etxt_password= (EditText) findViewById(R.id.login_password);

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
                                } else {
                                    Toast.makeText(getApplicationContext(), "No such user exist, please signup", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }


}
