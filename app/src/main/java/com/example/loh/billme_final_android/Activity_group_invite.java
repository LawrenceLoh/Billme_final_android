package com.example.loh.billme_final_android;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.baoyz.widget.PullRefreshLayout;
import com.example.loh.billme_final_android.Parse_subclass.Follow;
import com.example.loh.billme_final_android.Parse_subclass.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Activity_group_invite extends AppCompatActivity {

    final List<User> followedFriend= new ArrayList<User>();
    @Bind(R.id.group_invite_newton_cradle_loading)NewtonCradleLoading group_invite_newton_cradle_loading;
    @Bind(R.id.group_invite_list)ListView group_invite_list;
    @Bind(R.id.group_invite_layout)RelativeLayout group_invite_layout;
    @Bind(R.id.group_invite_swipeRefreshLayout)PullRefreshLayout group_invite_swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invite);

        ButterKnife.bind(this);
        onLoadingStart();
        loadFriendsList();


        group_invite_swipeRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        group_invite_swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                followedFriend.clear();
                loadFriendsList();
                group_invite_swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void loadFriendsList(){
        ParseQuery<Follow> searchUserQuery_Following = ParseQuery.getQuery(Follow.class);
        searchUserQuery_Following.whereEqualTo("fromUser", ParseUser.getCurrentUser());
        searchUserQuery_Following.include("toUser");
        searchUserQuery_Following.findInBackground(new FindCallback<Follow>() {
            @Override
            public void done(List<Follow> List, ParseException e) {
                for (Follow friend : List) {
                    followedFriend.add(friend.getToUser());
                }

                Adapter_group_invite adapter = new Adapter_group_invite(getApplicationContext(), followedFriend, followedFriend.size());
                adapter.notifyDataSetChanged();
                group_invite_list.setAdapter(adapter);
                group_invite_newton_cradle_loading.stop();
                group_invite_newton_cradle_loading.setVisibility(View.GONE);
                group_invite_layout.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

    }
    public void onLoadingStart(){
        group_invite_newton_cradle_loading.setVisibility(View.VISIBLE);
        group_invite_layout.setBackgroundColor(getResources().getColor(R.color.logo_green));
        group_invite_newton_cradle_loading.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_group_invite, menu);
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
            Intent intent = new Intent(Activity_group_invite.this,Activity_main.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
