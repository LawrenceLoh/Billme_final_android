package com.example.loh.billme_final_android;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class Activity_friend_list extends AppCompatActivity {

    final List<User> followedFriend= new ArrayList<User>();
    @Bind(R.id.friend_list_newton_cradle_loading)NewtonCradleLoading friend_list_newton_cradle_loading;
    @Bind(R.id.friend_list)ListView friendList;
    @Bind(R.id.friend_list_layout)RelativeLayout friend_list_layout;
    @Bind(R.id.friend_list_swipeRefreshLayout)PullRefreshLayout friend_list_swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_friend_list);

        ButterKnife.bind(this);
        onLoadingStart();
        loadFriendsList();


        friend_list_swipeRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        friend_list_swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                followedFriend.clear();
                loadFriendsList();
                friend_list_swipeRefreshLayout.setRefreshing(false);
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

                Adapter_search_user adapter = new Adapter_search_user(getApplicationContext(), followedFriend, followedFriend.size());
                adapter.notifyDataSetChanged();
                friendList.setAdapter(adapter);
                friend_list_newton_cradle_loading.stop();
                friend_list_newton_cradle_loading.setVisibility(View.GONE);
                friend_list_layout.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

    }

    public void onLoadingStart(){
        friend_list_newton_cradle_loading.setVisibility(View.VISIBLE);
        friend_list_layout.setBackgroundColor(getResources().getColor(R.color.logo_green));
        friend_list_newton_cradle_loading.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
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
            Intent intent = new Intent(Activity_friend_list.this,Activity_main.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
