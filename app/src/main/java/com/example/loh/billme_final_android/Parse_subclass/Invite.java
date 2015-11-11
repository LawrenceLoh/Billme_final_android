package com.example.loh.billme_final_android.Parse_subclass;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Loh on 10/11/2015.
 */
@ParseClassName("Invite")
public class Invite extends ParseObject {
    public Invite() {
        // A default constructor is required.
    }

    public User getGroupFromUser(){
        return (User)getParseUser("GroupFromUser");
    }

    public void setGroupFromUser(User user){
        put("GroupFromUser", user);
    }

    public User getGroupToUser(){
        return (User)getParseUser("GroupToUser");
    }

    public void setGroupToUser(User user){
        put("GroupToUser", user);
    }

    public void setGroupName(String groupName){
        put("groupName", groupName);
    }
    public String getGroupName(){
        return getString("groupName");
    }


}
