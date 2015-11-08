package com.example.loh.billme_final_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loh.billme_final_android.Parse_subclass.User;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.soundcloud.android.crop.Crop;
import com.victor.loading.newton.NewtonCradleLoading;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Activity_sign_up extends AppCompatActivity {

    private Button signUp;
    private EditText name;
    private EditText email;
    private EditText password;
    public String txt_name,txt_email,txt_password;
    private  byte[] profile;
    private Uri uri;
    private NewtonCradleLoading newtonCradleLoading;
    private LinearLayout sign_up_detail;
    public User user = new User();


    @Bind(R.id.sign_up_button)Button button;
    @Bind(R.id.sign_up_content)CardView sign_up_content;
    @Bind(R.id.sign_up_login_button) TextView txt_login;
    @Bind(R.id.sign_up_layout)RelativeLayout sign_up_layout;
    @Bind(R.id.sign_up_uploadPhoto)ImageView Img_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Bind all,professionally
        ButterKnife.bind(this);

        signUp = (Button)findViewById(R.id.sign_up_button);
        name = (EditText)findViewById(R.id.sign_up_name);
        email= (EditText)findViewById(R.id.sign_up_email);
        password= (EditText) findViewById(R.id.sign_up_password);
        newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);
        sign_up_detail= (LinearLayout) findViewById(R.id.sign_up_detail);
        sign_up_content.setBackgroundColor(getResources().getColor(R.color.white));




        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check the current network connection
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                Boolean isInternetPresent = cd.isConnectingToInternet();
                //if network available
                if (isInternetPresent){

                sign_up();
                }else{
                    Toast.makeText(Activity_sign_up.this, "Please check your network connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


        }



    @OnClick(R.id.sign_up_button)
    public void onCreateGuest(){}

    @OnClick(R.id.sign_up_login_button)
    public void onClick_login_btn(){

        Intent intent = new Intent(Activity_sign_up.this,Activity_login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    public void sign_up(){
             //retrieve signUp detail from user
            txt_name = name.getText().toString();
            txt_email = email.getText().toString();
            txt_password = password.getText().toString();
            //if all the detial are filled
            if (txt_email.equals("") || txt_password.equals("")||txt_name.equals("")) {
                Toast.makeText(getApplicationContext(), "Please complete the sign up form", Toast.LENGTH_LONG).show();
            } else {
                try {
                    newtonCradleLoading.setVisibility(View.VISIBLE);
                    newtonCradleLoading.start();
                    sign_up_detail.setVisibility(View.GONE);
                    txt_login.setVisibility(View.GONE);
                    sign_up_layout.setBackgroundColor(getResources().getColor(R.color.light_green));


                    ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
                    parseInstallation.put("email", txt_email);
                    ParseACL acl = new ParseACL();
                    acl.setWriteAccess(ParseUser.getCurrentUser(), true);

                    //Create new user with username(email is set to username
                    // because Parse loginInBackground checks username and password), email, fullname, password

                    user.setUsername(txt_email);
                    user.setEmail(txt_email);
                    user.setFullname(txt_name);
                    user.setPassword(txt_password);

                    parseInstallation.saveEventually();
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            newtonCradleLoading.start();
                            if (e == null) {

                                String picName = txt_name + ".jpg";
                                if (profile == null) {
                                } else {
                                    user.setProfile(new ParseFile(picName, profile));
                                    user.saveEventually();
                                }
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

    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    public void set_profile(){
        try {
            InputStream iStream = getContentResolver().openInputStream(uri);
            profile = getBytes(iStream);
            set_smaller_Bytes(profile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void set_smaller_Bytes(byte[] uri){
        Bitmap bitmap = BitmapFactory.decodeByteArray(uri , 0, uri .length);
        bitmap= Bitmap.createScaledBitmap(bitmap, 250, 250, false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        profile=bos.toByteArray();

        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onChooseProfilePicture(View view) {
        Crop.pickImage(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            uri = Crop.getOutput(result);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }

        if(uri!=null) {
            Img_upload.setImageURI(uri);
            set_profile();
        }
    }

}
