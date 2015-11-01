package com.example.loh.billme_final_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loh.billme_final_android.Parse_subclass.User;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Activity_sign_up extends AppCompatActivity {

    private Button signUp;
    private EditText name;
    private EditText email;
    private EditText password;
    public String txt_name,txt_email,txt_password;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUp = (Button)findViewById(R.id.sign_up_button);
        name = (EditText)findViewById(R.id.sign_up_name);
        email= (EditText)findViewById(R.id.sign_up_email);
        password= (EditText) findViewById(R.id.sign_up_password);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_name = name.getText().toString();
                txt_email = email.getText().toString();
                txt_password = password.getText().toString();

                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                Boolean isInternetPresent = cd.isConnectingToInternet();

                if (isInternetPresent){
                    if (txt_email.equals("") || txt_password.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please complete the sign up form", Toast.LENGTH_LONG).show();
                    } else {
                        pd = new ProgressDialog(Activity_sign_up.this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        pd.setIndeterminate(true);
                        pd.setMessage("Signing up");
                        pd.setCancelable(false);
                        pd.show();
                        try {
                            ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
                            parseInstallation.put("email", txt_email);
                            ParseACL acl = new ParseACL();
                            acl.setWriteAccess(ParseUser.getCurrentUser(), true);

                            //Create new user with username(email is set to username
                            // because Parse loginInBackground checks username and password), email, fullname, password
                            User user = new User();
                            user.setUsername(txt_email);
                            user.setEmail(txt_email);
                            user.setFullname(txt_name);
                            user.setPassword(txt_password);

                            parseInstallation.saveEventually();
                            user.signUpInBackground(new SignUpCallback() {
                                public void done(ParseException e) {
                                    pd.dismiss();
                                    if (e == null) {
                                        Toast.makeText(getApplicationContext(), "Successfully signed up", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Activity_sign_up.this, Activity_login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Sign up Error", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            }else{
                    Toast.makeText(Activity_sign_up.this, "Please check your network connection and try again", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
