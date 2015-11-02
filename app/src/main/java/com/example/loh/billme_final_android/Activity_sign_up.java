package com.example.loh.billme_final_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.loh.billme_final_android.Parse_subclass.User;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_sign_up extends Activity {

    private Button signUp;
    private EditText name;
    private EditText email;
    private EditText password;
    public String txt_name,txt_email,txt_password;
    public ProgressDialog pd;
    private CircleImageView Img_upload;
    public Bitmap bitmap=null;
    protected static final int GALLERY_PICTURE = 1;
    protected static final int CAMERA_REQUEST = 0;

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
                sign_up();
            }
        });

        Img_upload= (CircleImageView) findViewById(R.id.uploadPhoto);
        Img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(Activity_sign_up.this,Activity_crop_profile.class);
                startActivity(intent);
            }
        });

    }

    public void sign_up(){
        //retrieve signUp detail from user
        txt_name = name.getText().toString();
        txt_email = email.getText().toString();
        txt_password = password.getText().toString();

        //check the current network connection
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();

        //if network available
        if (isInternetPresent){
            //if all the detial are filled
            if (txt_email.equals("") || txt_password.equals("")||txt_name.equals("")) {
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

//    public void startDialog(){
//        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(Activity_sign_up.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//        myAlertDialog.setTitle("Upload");
//        myAlertDialog.setMessage("How do you want to set your profile picture?");
//
//        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent();
//                // Show only images, no videos or anything else
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                // Always show the chooser (if there are multiple options available)
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_PICTURE);
//
//
//            }
//        });
//
//        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//
//                startActivityForResult(intent, CAMERA_REQUEST);
//            }
//        });
//        myAlertDialog.show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE && data != null && data.getData() != null) {
//
//            Uri uri = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                Log.d("image", String.valueOf(bitmap));
//                Img_upload.setImageBitmap(bitmap);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE){
//
//        }
//
//    }
}
