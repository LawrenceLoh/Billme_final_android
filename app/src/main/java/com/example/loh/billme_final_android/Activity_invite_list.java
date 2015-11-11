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

import com.example.loh.billme_final_android.Parse_subclass.Invite;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Activity_invite_list extends AppCompatActivity {


    @Bind(R.id.invite_list)ListView invite_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_list);

        ButterKnife.bind(this);
        loadInvitation();
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
            Intent intent = new Intent(Activity_invite_list.this,Activity_main.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void loadInvitation(){
        ParseQuery<Invite> group_invitation = ParseQuery.getQuery(Invite.class);
        group_invitation.whereEqualTo("GroupToUser", ParseUser.getCurrentUser());
        group_invitation.include("GroupFromUser");
        group_invitation.findInBackground(new FindCallback<Invite>() {
            @Override
            public void done(List<Invite> inviteInvite, ParseException e) {
                invite_list.setAdapter(new Adapter_invite_list(getApplicationContext(), inviteInvite, inviteInvite.size()));
            }
        });

    }
}
