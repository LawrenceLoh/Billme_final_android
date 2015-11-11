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

import com.example.loh.billme_final_android.Parse_subclass.Follow;
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
public class Adapter_search_user extends BaseAdapter {

    private Context mContext;
    private List<User> users= new ArrayList<User>();
    private LayoutInflater inflater;
    private int numberOfFollowingInSearchResult;

    public Adapter_search_user(Context c, List<User> users, int numberOfFollowingInSearchResult) {
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
        public TextView txtview_user_email;
        public Button btn_follow;
        public ViewHolder(View view) {
            img_user_profilePicture=(ImageView)view.findViewById(R.id.img_user_profilePicture);
            txtview_user_fullName=(TextView)view.findViewById(R.id.txt_user_fullName);
            btn_follow=(Button)view.findViewById(R.id.btn_follow);
            txtview_user_email= (TextView) view.findViewById(R.id.txt_user_email);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ViewHolder holder;

        if (v == null) {
            v = inflater.inflate(R.layout.adapter_search_result, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        Typeface mTypeface = Typeface.createFromAsset(mContext.getAssets(), "nord.ttf");

        holder.txtview_user_fullName.setText(Activity_main.sort_name(users.get(position).getFullName()));
        holder.txtview_user_email.setText(users.get(position).getEmail());

        holder.txtview_user_email.setTypeface(mTypeface);
        holder.txtview_user_fullName.setTypeface(mTypeface);

        Picasso.with(mContext).load(users.get(position).getProfile().getUrl()).into(holder.img_user_profilePicture);
        if(position<numberOfFollowingInSearchResult){
            holder.btn_follow.setSelected(true);
        }else{
            holder.btn_follow.setSelected(false);
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
                    Follow follow = new Follow();
                    follow.setACL(aCL);
                    follow.setFromUser((User) ParseUser.getCurrentUser());
                    follow.setToUser(users.get(position));
                    follow.saveEventually();
                } else {
                    //Handle de-select state change
                    //Query rows where "fromUser" is current User AND "toUser" is selected User and delete
                    //To remove following relation ship of current User and selected User

                    ParseQuery<Follow> follow = ParseQuery.getQuery(Follow.class);
                    follow.whereEqualTo("fromUser", ParseUser.getCurrentUser());
                    follow.whereEqualTo("toUser", users.get(position));

                    follow.findInBackground(new FindCallback<Follow>() {
                        @Override
                        public void done(List<Follow> follows, ParseException e) {
                            if (e == null) {
                                for(Follow follow:follows){
                                    follow.deleteEventually();
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
