package com.example.loh.billme_final_android;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loh.billme_final_android.Parse_subclass.Invite;
import com.example.loh.billme_final_android.Parse_subclass.User;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Loh on 10/11/2015.
 */
public class Adapter_group_invite extends BaseAdapter {

    private Context mContext;
    private List<User> users= new ArrayList<User>();
    private Map<User, Boolean> usersTicked = new HashMap<>();
    private LayoutInflater inflater;
    private int numberOfFollowingInSearchResult;
    private boolean buttonSwitch= true;

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
        public TextView textView_user_email;
        public Button btn_follow;
        public ViewHolder(View view) {
            img_user_profilePicture=(ImageView)view.findViewById(R.id.img_user_profilePicture);
            txtview_user_fullName=(TextView)view.findViewById(R.id.txt_user_fullName);
            textView_user_email = (TextView) view.findViewById(R.id.txt_user_email);
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

        Typeface mTypeface = Typeface.createFromAsset(mContext.getAssets(), "nord.ttf");

        holder.txtview_user_fullName.setText(Activity_main.sort_name(users.get(position).getFullName()));
        holder.textView_user_email.setText(Activity_main.sort_name(users.get(position).getEmail()));
        Picasso.with(mContext).load(users.get(position).getProfile().getUrl()).into(holder.img_user_profilePicture);

        holder.txtview_user_fullName.setTypeface(mTypeface);
        holder.textView_user_email.setTypeface(mTypeface);

        if(position<numberOfFollowingInSearchResult){
            holder.btn_follow.setSelected(false);
        }else{
            holder.btn_follow.setSelected(true);
        }

        Boolean previousState = usersTicked.get(users.get(position));
        holder.btn_follow.setSelected(previousState == null ? false : previousState);

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btn_follow.setSelected(!holder.btn_follow.isSelected());
                if (holder.btn_follow.isSelected()) {
                    //Handle selected state change
                    ParseACL aCL = new ParseACL(ParseUser.getCurrentUser());
                    aCL.setPublicReadAccess(true);
                    aCL.setPublicWriteAccess(true);
                    //Add new follow relation
                    Invite invite = new Invite();
                    invite.setACL(aCL);
                    invite.setGroupFromUser((User) ParseUser.getCurrentUser());
                    invite.setGroupToUser(users.get(position));
                    invite.saveEventually();
                    usersTicked.put(users.get(position), true);

                } else {
                    ParseQuery<Invite> group = ParseQuery.getQuery(Invite.class);
                    group.whereEqualTo("GroupFromUser", ParseUser.getCurrentUser());
                    group.whereEqualTo("GroupToUser", users.get(position));

                    group.findInBackground(new FindCallback<Invite>() {
                        @Override
                        public void done(List<Invite> invites, ParseException e) {
                            if (e == null) {
                                for(Invite invite : invites){
                                    invite.deleteEventually();
                                }
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });

                    usersTicked.put(users.get(position), false);


                }
            }
        });
        return v;
    }
}
