package com.example.loh.billme_final_android;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.loh.billme_final_android.Parse_subclass.Group;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loh on 11/11/2015.
 */
public class Adapter_group_list extends BaseAdapter {
    private Context mContext;
    private List<Group> groups= new ArrayList<Group>();
    private LayoutInflater inflater;





    public Adapter_group_list(Context c, List<Group> groups) {
        mContext = c;
        this.groups = groups;
        inflater = LayoutInflater.from(c);


    }


    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        public TextView txtview_group_name;
        public TextView txtview_group_list_amount;

        public ViewHolder(View view) {

            txtview_group_name=(TextView)view.findViewById(R.id.txt_group_name);
            txtview_group_list_amount= (TextView) view.findViewById(R.id.group_list_amount);

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.adapter_group_list, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        ParseQuery<Group> group_list = ParseQuery.getQuery(Group.class);
        group_list.whereEqualTo("groupName", groups.get(position).getGroupName());
        group_list.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> list, ParseException e) {
                    int numberOfGroups =list.size();
                    holder.txtview_group_list_amount.setText(Integer.toString(numberOfGroups));

            }
        });
        Typeface mTypeface = Typeface.createFromAsset(mContext.getAssets(), "nord.ttf");
        holder.txtview_group_name.setText(Activity_main.sort_name(groups.get(position).getGroupName()));
        holder.txtview_group_name.setTypeface(mTypeface);



        return v;
    }
}
