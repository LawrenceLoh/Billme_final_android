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
import android.widget.ListView;

import com.example.loh.billme_final_android.Parse_subclass.Group;
import com.example.loh.billme_final_android.Parse_subclass.Invite;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Activity_group_list extends AppCompatActivity {

    @Bind(R.id.group_list)ListView listView_group_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_group_list);
        ButterKnife.bind(this);
        onLoadingGroupList();
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
            Intent intent = new Intent(Activity_group_list.this,Activity_main.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLoadingGroupList(){

        ParseQuery<Group> group_list = ParseQuery.getQuery(Group.class);
        group_list.whereEqualTo("member", ParseUser.getCurrentUser());
        group_list.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> list, ParseException e) {
                listView_group_list.setAdapter(new Adapter_group_list(getApplicationContext(),list));
            }
        });
    }
}
