package com.example.loh.billme_final_android;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

public class SearchResultsActivity extends ActionBarActivity {


    @Bind(R.id.search_newton_cradle_loading)NewtonCradleLoading search_newton_cradle_loading;
    @Bind(R.id.list_search_result)ListView list_search_result;
    @Bind(R.id.search_layout)RelativeLayout search_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        handleIntent(getIntent());

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
            Intent intent = new Intent(SearchResultsActivity.this,Activity_main.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String fullname = intent.getStringExtra(SearchManager.QUERY);
            search_newton_cradle_loading.setVisibility(View.VISIBLE);
            search_layout.setBackgroundColor(getResources().getColor(R.color.logo_green));
            search_newton_cradle_loading.start();

            final List<User> searchedUser_Following=new ArrayList<User>();
            final List<User> searchedUser_Not_Following=new ArrayList<User>();

            final ParseQuery<User> searchUserQuery = ParseQuery.getQuery(User.class);
            searchUserQuery.whereMatches("fullname", "(" + fullname + ")", "i");
            searchUserQuery.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
            searchUserQuery.findInBackground(new FindCallback<User>() {
                @Override
                public void done(final List<User> searchUserResult, ParseException e) {
                    if (e == null) {
                        ParseQuery<Follow> searchUserQuery_Following = ParseQuery.getQuery(Follow.class);
                        searchUserQuery_Following.whereEqualTo("fromUser", ParseUser.getCurrentUser());
                        searchUserQuery_Following.whereMatchesQuery("toUser", searchUserQuery);
                        searchUserQuery_Following.include("toUser");

                        searchUserQuery_Following.findInBackground(new FindCallback<Follow>() {
                            @Override
                            public void done(List<Follow> searchUserResult_Following, ParseException e) {
                                if (e == null) {
                                    if(searchUserResult_Following.size()!=0){
                                        for (User user : searchUserResult) {
                                            boolean following_found=false;
                                            for (Follow following : searchUserResult_Following) {
                                                if (user.getObjectId().equals(following.getToUser().getObjectId())) {
                                                    following_found=true;
                                                    break;
                                                }
                                            }
                                            if(following_found==true){
                                                searchedUser_Following.add(user);
                                            }else{
                                                searchedUser_Not_Following.add(user);
                                            }
                                        }
                                    }

                                    int numberOfFollowingInSearchResult = searchedUser_Following.size();
                                    searchedUser_Following.addAll(searchedUser_Not_Following);

                                    if(searchedUser_Following.size()!=0){
                                        list_search_result.setAdapter(new Adapter_searchUser(getApplicationContext(), searchedUser_Following, numberOfFollowingInSearchResult));
                                    }else{
                                        list_search_result.setAdapter(new Adapter_searchUser(getApplicationContext(), searchUserResult, numberOfFollowingInSearchResult));
                                    }
                                    search_layout.setBackgroundColor(getResources().getColor(R.color.white));
                                    search_newton_cradle_loading.stop();
                                    search_newton_cradle_loading.setVisibility(View.GONE);
                                    list_search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                int position, long id) {
                                            Log.e("Activity_Groupnom", "onClick");
                                        }
                                    });
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else{
                        e.printStackTrace();
                    }
                }
            });



        }
    }
}