package com.example.loh.billme_final_android;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loh.billme_final_android.Parse_subclass.Bill;
import com.example.loh.billme_final_android.Parse_subclass.Follow;
import com.example.loh.billme_final_android.Parse_subclass.Group;
import com.example.loh.billme_final_android.Parse_subclass.User;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loh on 9/11/2015.
 */
public class Adapter_main extends BaseAdapter {

    private Context mContext;
    private List<Bill> bills= new ArrayList<>();
    private LayoutInflater inflater;
    private int members;

    public Adapter_main(Context c, List<Bill> bills,int members) {
        mContext = c;
        this.bills = bills;
        this.members= members;
        inflater = LayoutInflater.from(c);
    }


    @Override
    public int getCount() {
        return bills.size();
    }

    @Override
    public Object getItem(int position) {
        return bills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        public ImageView main_profile;
        public TextView main_from_user ;
        public TextView main_amount;
        public TextView main_title;
        public TextView main_date_from;
        public TextView main_date_to;
        public TextView main_toGroup;
        public TextView main_personal_amount;

        public ViewHolder(View view) {

            main_profile=(ImageView)view.findViewById(R.id.main_profile);
            main_from_user=(TextView)view.findViewById(R.id.main_from_user);
            main_amount=(TextView)view.findViewById(R.id.main_amount);
            main_title=(TextView)view.findViewById(R.id.main_title);
            main_date_from=(TextView)view.findViewById(R.id.main_date_from);
            main_date_to=(TextView)view.findViewById(R.id.main_date_to);
            main_toGroup=(TextView)view.findViewById(R.id.main_toGroup);
            main_personal_amount=(TextView)view.findViewById(R.id.main_personal_amount);

        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.adapter_main, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        Typeface mTypeface = Typeface.createFromAsset(mContext.getAssets(), "nord.ttf");

        Picasso.with(mContext).load(bills.get(position).getFromUserBill().getProfile().getUrl()).into(holder.main_profile);
        holder.main_from_user.setText("From : "+Activity_main.sort_name(bills.get(position).getFromUserBill().getFullName()));
        holder.main_amount.setText("Total : "+bills.get(position).getBillAmount());
        holder.main_title.setText("Title : "+Activity_main.sort_name(bills.get(position).getBillName()));
        holder.main_date_from.setText("From : "+bills.get(position).getBillDateFrom());
        holder.main_date_to.setText("To : "+bills.get(position).getBillDateTo());
        holder.main_toGroup.setText("To : "+Activity_main.sort_name(bills.get(position).getGroup()));

        double result = Integer.parseInt(bills.get(position).getBillAmount())/members;
        holder.main_personal_amount.setText("You need to pay RM "+result);

        holder.main_from_user.setTypeface(mTypeface);
        holder.main_amount.setTypeface(mTypeface);
        holder.main_title.setTypeface(mTypeface);
        holder.main_date_from.setTypeface(mTypeface);
        holder.main_date_to.setTypeface(mTypeface);
        holder.main_toGroup.setTypeface(mTypeface);
        holder.main_personal_amount.setTypeface(mTypeface);

        return v;
    }

}
