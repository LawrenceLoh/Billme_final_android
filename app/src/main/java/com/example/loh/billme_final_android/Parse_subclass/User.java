package com.example.loh.billme_final_android.Parse_subclass;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

/**
 * Created by Loh on 1/11/2015.
 */
@ParseClassName("_User")
public class User extends ParseUser {

    public User(){

    }
    public String getEmail() {
        return getString("email");
    }
    public void setEmail(String email) {
        put("email", email);
    }
    public String getUsername(){
        return getString("username");
    }
    public void setUsername(String username){
        put("username",username);
    }
    public String getFullName() {
        return getString("fullname");
    }
    public void setFullname(String fullname){
        put("fullname",fullname);
    }
    public void setProfile(ParseFile profile) {
        put("profile", profile);
        try {
            profile.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public ParseFile getProfile() {
        return getParseFile("profile");
    }

}
