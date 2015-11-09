package com.example.loh.billme_final_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.loh.billme_final_android.Parse_subclass.Follow;
import com.example.loh.billme_final_android.Parse_subclass.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Activity_friend_list extends AppCompatActivity {

    final List<User> followedFriend= new ArrayList<User>();
    @Bind(R.id.friend_list)ListView friendList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_friend_list);

        ButterKnife.bind(this);

        ParseQuery<Follow> searchUserQuery_Following = ParseQuery.getQuery(Follow.class);
        searchUserQuery_Following.whereEqualTo("fromUser", ParseUser.getCurrentUser());
        searchUserQuery_Following.include("toUser");
        searchUserQuery_Following.findInBackground(new FindCallback<Follow>() {
            @Override
            public void done(List<Follow> List, ParseException e) {
                for(Follow friend: List){
                    followedFriend.add(friend.getToUser());
                }
                friendList.setAdapter(new Adapter_search_user(getApplicationContext(),followedFriend,followedFriend.size()));
            }
        });
    }
}
