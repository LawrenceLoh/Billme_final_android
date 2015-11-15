package com.example.loh.billme_final_android;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.loh.billme_final_android.Parse_subclass.Follow;
import com.example.loh.billme_final_android.Parse_subclass.Group;
import com.example.loh.billme_final_android.Parse_subclass.Invite;
import com.example.loh.billme_final_android.Parse_subclass.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Activity_group extends AppCompatActivity {

    private String groupname;
    @Bind(R.id.group_members_list)ListView group_members_list;
    final List<User> member_list= new ArrayList<User>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_group);
        groupname= getIntent().getStringExtra("groupname");

        SpannableString s = new SpannableString(groupname);
        s.setSpan(new com.example.loh.billme_final_android.TypefaceSpan(this, "nord.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle(s);
        ButterKnife.bind(this);
        onLoadingGroupMember();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_group, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_back) {
            Intent intent = new Intent(Activity_group.this,Activity_group_list.class);
            startActivity(intent);
            finish();
            return true;
        }
        else if(id == R.id.menu_exit){
            ParseQuery<Group> group = ParseQuery.getQuery(Group.class);
            group.whereEqualTo("groupName", groupname);
            group.whereEqualTo("member",ParseUser.getCurrentUser());
            group.findInBackground(new FindCallback<Group>() {
                @Override
                public void done(List<Group> groupList, ParseException e) {
                    if (e == null) {
                        for (Group group : groupList) {
                            group.deleteEventually();
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });

            Intent intent = new Intent(Activity_group.this,Activity_main.class);
            startActivity(intent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    public void onLoadingGroupMember(){

        ParseQuery<Group> group_list = ParseQuery.getQuery(Group.class);
        group_list.whereEqualTo("groupName", groupname);
        group_list.include("member");
        group_list.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(final List<Group> list, ParseException e) {
                for(final Group member :list){
                    member_list.add(member.getMember());
                }
                group_members_list.setAdapter(new Adapter_group(getApplicationContext(), member_list));
            }
        });
    }
}
