package com.example.loh.billme_final_android;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.example.loh.billme_final_android.Parse_subclass.Bill;
import com.example.loh.billme_final_android.Parse_subclass.Follow;
import com.example.loh.billme_final_android.Parse_subclass.Group;
import com.example.loh.billme_final_android.Parse_subclass.Invite;
import com.example.loh.billme_final_android.Parse_subclass.User;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_add_bill extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {

    private List<String> groupNames = new ArrayList<>();
    private RadioGroup add_bill_radio_group;
    private String billDateFrom;
    private String billDateTo;
    private String billDate;
    private String billName;
    private String billAmount;
    private String billGroup;


    @Bind(R.id.add_bill_bill_name)EditText add_bill_bill_name;
    @Bind(R.id.add_bill_bill_amount)EditText add_bill_bill_amount;
    @Bind(R.id.all_bill_scrollView) ScrollView all_bill_scrollView;
    @Bind(R.id.add_bill_from)TextView add_bill_from;
    @Bind(R.id.add_bill_to) TextView all_bill_to;
    @Bind(R.id.add_bill_clock)ImageView add_bill_clock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_add_bill);

        SpannableString s = new SpannableString("Add Bill");
        s.setSpan(new com.example.loh.billme_final_android.TypefaceSpan(this, "nord.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle(s);
        ButterKnife.bind(this);

        Typeface mTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "nord.ttf");

        add_bill_bill_name.setTypeface(mTypeface);
        add_bill_bill_amount.setTypeface(mTypeface);
        add_bill_from.setTypeface(mTypeface);
        all_bill_to.setTypeface(mTypeface);



        onLoadingRadioGroup();


        add_bill_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        Activity_add_bill.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {



        String from = dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        String to = dayOfMonthEnd+"/"+(++monthOfYearEnd)+"/"+yearEnd;
        add_bill_from.setText("From : "+from);
        all_bill_to.setText("To : "+to);
        billDateFrom =from;
        billDateTo = to;
        billDate=from + to;
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
            Intent intent = new Intent(Activity_add_bill.this,Activity_main.class);
            startActivity(intent);
            finish();
            return true;
        }
        else if(id== R.id.menu_confirm){
            onCreateBill();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLoadingRadioGroup(){
        ParseQuery<Group> group_list = ParseQuery.getQuery(Group.class);
        group_list.whereEqualTo("member", ParseUser.getCurrentUser());
        group_list.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> list, ParseException e) {
                for (Group member : list) {
                    groupNames.add(member.getGroupName());
                }
                Typeface mTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "nord.ttf");
                //add radio buttons
                final RadioButton[] rb = new RadioButton[groupNames.size()];
                RadioGroup rg = new RadioGroup(getApplicationContext()); //create the RadioGroup
                rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
                rg.setId(R.id.add_bill_radio_group);
                for (int i = 0; i < groupNames.size(); i++) {
                    rb[i] = new RadioButton(getApplicationContext());
                    rb[i].setText(Activity_main.sort_name(groupNames.get(i)));
                    rb[i].setId(i);
                    rb[i].setTextColor(getResources().getColor(R.color.logo_green));
                    rb[i].setTypeface(mTypeface);
                    rg.addView(rb[i]); //the RadioButtons are added to the radioGroup instead of the layout

                }
                all_bill_scrollView.addView(rg);//you add the whole RadioGroup to the layout
                add_bill_radio_group = (RadioGroup) findViewById(R.id.add_bill_radio_group);
                add_bill_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        billGroup = groupNames.get(checkedId);
                    }
                });

            }
        });
    }
    public void onCreateBill(){
        billName = add_bill_bill_name.getText().toString();
        billAmount = add_bill_bill_amount.getText().toString();

            if ((billName.length() > 0) && (billAmount.length() > 0)) {
                try {
                    if (billGroup.length() > 0 && billDate.length() > 0) {
                        ParseQuery<Group> load_group_member = ParseQuery.getQuery(Group.class);
                        load_group_member.whereEqualTo("groupName", billGroup);
                        load_group_member.include("member");
                        load_group_member.findInBackground(new FindCallback<Group>() {
                            @Override
                            public void done(List<Group> List, ParseException e) {
                                for (Group member : List) {
                                    ParseACL aCL = new ParseACL(ParseUser.getCurrentUser());
                                    aCL.setPublicReadAccess(true);
                                    aCL.setPublicWriteAccess(true);
                                    //Add new follow relation
                                    Bill bill = new Bill();
                                    bill.setACL(aCL);
                                    bill.setBillDateFrom(billDateFrom);
                                    bill.setBillDateTo(billDateTo);
                                    bill.setBillAmount(billAmount);
                                    bill.setBillName(billName);
                                    bill.setGroup(billGroup);
                                    bill.setFromUserBill((User) ParseUser.getCurrentUser());
                                    bill.setToUserBill(member.getMember());
                                    bill.saveEventually();
                                }
                                Intent intent = new Intent(Activity_add_bill.this, Activity_main.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                    } else {
                        Toast.makeText(Activity_add_bill.this, "Please fill in all the details!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(Activity_add_bill.this, "Please fill in all the details!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Activity_add_bill.this, "Please fill in all the details!", Toast.LENGTH_SHORT).show();
            }
    }
}
