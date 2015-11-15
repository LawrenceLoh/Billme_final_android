package com.example.loh.billme_final_android.Parse_subclass;

import com.parse.ParseClassName;
import com.parse.ParseObject;


/**
 * Created by Loh on 14/11/2015.
 */
@ParseClassName("Bill")
public class Bill extends ParseObject {


    public User getFromUserBill(){
        return (User)getParseUser("fromUserBill");
    }

    public void setFromUserBill(User user){
        put("fromUserBill", user);
    }

    public User getToUserBill(){
        return (User)getParseUser("toUserBill");
    }

    public void setToUserBill(User user){
        put("toUserBill", user);
    }

    public void setGroup(String groupName){
        put("group", groupName);
    }
    public String getGroup(){
        return getString("group");
    }

    public void setBillName(String groupName){
        put("billName", groupName);
    }
    public String getBillName(){
        return getString("billName");
    }
    public void setBillAmount(String groupName){
        put("billAmount", groupName);
    }
    public String getBillAmount(){
        return getString("billAmount");
    }

    public void setBillDateFrom(String billDate){
        put("billDateFrom", billDate);
    }
    public String getBillDateFrom(){
        return getString("billDateFrom");
    }

    public void setBillDateTo(String billDate){
        put("billDateTo", billDate);
    }
    public String getBillDateTo(){
        return getString("billDateTo");
    }
}
