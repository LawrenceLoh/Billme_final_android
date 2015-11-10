package com.example.loh.billme_final_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Created by Loh on 10/11/2015.
 */
public class Adapter_group_invite extends BaseAdapter {

    private Context mContext;
    private List<User> users= new ArrayList<User>();
    private LayoutInflater inflater;
    private int numberOfFollowingInSearchResult;

    public Adapter_group_invite(Context c, List<User> users, int numberOfFollowingInSearchResult) {
        mContext = c;
        this.users = users;
        inflater = LayoutInflater.from(c);
        this.numberOfFollowingInSearchResult=numberOfFollowingInSearchResult;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        public ImageView img_user_profilePicture;
        public TextView txtview_user_fullName;
        public Button btn_follow;
        public ViewHolder(View view) {
            img_user_profilePicture=(ImageView)view.findViewById(R.id.img_user_profilePicture);
            txtview_user_fullName=(TextView)view.findViewById(R.id.txt_user_fullName);
            btn_follow=(Button)view.findViewById(R.id.btn_follow);
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.adapter_group_invite, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.txtview_user_fullName.setText(users.get(position).getFullName());
        Picasso.with(mContext).load(users.get(position).getProfile().getUrl()).into(holder.img_user_profilePicture);
        if(position<numberOfFollowingInSearchResult){
            holder.btn_follow.setSelected(false);
        }else{
            holder.btn_follow.setSelected(true);
        }

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btn_follow.setSelected(!holder.btn_follow.isSelected());
                if (holder.btn_follow.isSelected()) {
                    //Handle selected state change
                    ParseACL aCL = new ParseACL(ParseUser.getCurrentUser());
                    aCL.setPublicReadAccess(true);
                    //Add new follow relation
                    Group group = new Group();
                    group.setACL(aCL);
                    group.setGroupFromUser((User) ParseUser.getCurrentUser());
                    group.setGroupToUser(users.get(position));
                    group.saveEventually();
                } else {
                    //Handle de-select state change
                    //Query rows where "fromUser" is current User AND "toUser" is selected User and delete
                    //To remove following relation ship of current User and selected User

                    ParseQuery<Group> group = ParseQuery.getQuery(Group.class);
                    group.whereEqualTo("GroupFromUser", ParseUser.getCurrentUser());
                    group.whereEqualTo("GroupToUser", users.get(position));

                    group.findInBackground(new FindCallback<Group>() {
                        @Override
                        public void done(List<Group> groups, ParseException e) {
                            if (e == null) {
                                for(Group group:groups){
                                    group.deleteEventually();
                                }
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        return v;
    }
}
