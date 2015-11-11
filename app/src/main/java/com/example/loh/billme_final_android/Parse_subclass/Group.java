package com.example.loh.billme_final_android.Parse_subclass;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Loh on 11/11/2015.
 */
@ParseClassName("Group")
public class Group extends ParseObject {

    public void setGroupName(String groupName){
        put("groupName", groupName);
    }
    public String getGroupName(){
        return getString("groupName");
    }

    public User getMember(){
        return (User)getParseUser("member");
    }

    public void setMember(User user){
        put("member", user);
    }
}
