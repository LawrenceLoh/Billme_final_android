package com.example.loh.billme_final_android.Parse_subclass;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Loh on 9/11/2015.
 */
@ParseClassName("Follow")
public class Follow extends ParseObject {
    public Follow() {
    // A default constructor is required.
}

    public User getFromUser(){
        return (User)getParseUser("fromUser");
    }

    public void setFromUser(User user){
        put("fromUser", user);
    }

    public User getToUser(){
        return (User)getParseUser("toUser");
    }

    public void setToUser(User user){
        put("toUser", user);
    }
}
