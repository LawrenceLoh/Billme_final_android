package com.example.loh.billme_final_android;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;

        import com.parse.ParseAnonymousUtils;
        import com.parse.ParseUser;

/**
 * Created by Loh on 1/11/2015.
 */
public class Activity_status extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        // Determine whether the current user is an anonymous user
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // If user is anonymous, send the user to LoginSignupActivity.class
            Intent intent = new Intent(Activity_status.this, Activity_login.class);
            startActivity(intent);
            finish();
        } else {
            // If current user is NOT anonymous user
            // Get current user data from Parse.com
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                // Send logged in users to Welcome.class
                Intent intent = new Intent(Activity_status.this, Activity_main.class);
                startActivity(intent);
                finish();
            } else {
                // Send user to LoginSignupActivity.class
                Intent intent = new Intent(Activity_status.this, Activity_login.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
