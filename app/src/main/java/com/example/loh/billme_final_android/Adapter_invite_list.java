package com.example.loh.billme_final_android;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loh.billme_final_android.Parse_subclass.Group;
import com.example.loh.billme_final_android.Parse_subclass.Invite;
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
 * Created by Loh on 11/11/2015.
 */
public class Adapter_invite_list extends BaseAdapter {
    private Context mContext;
    private List<Invite> inviteInvitation = new ArrayList<Invite>();
    private List<User> hostList = new ArrayList<User>();
    private LayoutInflater inflater;
    private int numberOfFollowingInSearchResult;

    public Adapter_invite_list(Context c, List<Invite> inviteInvitation,List<User> hostList, int numberOfFollowingInSearchResult) {
        mContext = c;
        this.inviteInvitation = inviteInvitation;
        this.hostList=hostList;
        inflater = LayoutInflater.from(c);
        this.numberOfFollowingInSearchResult=numberOfFollowingInSearchResult;
    }

    @Override
    public int getCount() {
        return inviteInvitation.size();
    }

    @Override
    public Object getItem(int position) {
        return inviteInvitation.get(position);
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
            v = inflater.inflate(R.layout.adapter_invite_list, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.txtview_user_fullName.setText(inviteInvitation.get(position).getGroupFromUser().getFullName() +"\ninvites you to "+ inviteInvitation.get(position).getGroupName());
        Picasso.with(mContext).load(inviteInvitation.get(position).getGroupFromUser().getProfile().getUrl()).into(holder.img_user_profilePicture);
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
                    Group groupinvite = new Group();
                    groupinvite.setACL(aCL);
                    groupinvite.setMember(inviteInvitation.get(position).getGroupToUser());
                    groupinvite.setGroupName(inviteInvitation.get(position).getGroupName());
                    groupinvite.saveEventually();

                    ParseQuery<Invite> invitation = ParseQuery.getQuery(Invite.class);
                    invitation.whereEqualTo("GroupFromUser", hostList.get(position));
                    invitation.whereEqualTo("GroupToUser", ParseUser.getCurrentUser());
                    invitation.findInBackground(new FindCallback<Invite>() {
                        @Override
                        public void done(List<Invite> invites, ParseException e) {
                            if (e == null) {
                                for (Invite invite : invites) {
                                    invite.deleteEventually();
                                }
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });

                } else {

                }
            }
        });
        return v;
    }
}
