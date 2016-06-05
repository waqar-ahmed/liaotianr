package com.app.liaotianr.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.liaotianr.FriendMessage;
import com.app.liaotianr.R;
import com.app.liaotianr.xmpp.PresenceStatus;
import com.app.liaotianr.xmpp.XMPPFriend;


public class FriendListAdapter extends ArrayAdapter<FriendMessage>{

	Context mContext;
	int layoutId;
	List<FriendMessage> list ;
	
	public FriendListAdapter(Context context, int textViewResourceId, List<FriendMessage> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		mContext = context;
		layoutId=textViewResourceId;
		list=objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		  
		View row = convertView;
	    FriendHolder holder = null;
	    
	    if(row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(layoutId, parent, false);
           
            holder = new FriendHolder();
            holder.imgUser = (ImageView)row.findViewById(R.id.user_image);
            holder.txtName = (TextView)row.findViewById(R.id.user_name);
            holder.txtPresence = (TextView)row.findViewById(R.id.secondLine);
            holder.txtCount = (TextView)row.findViewById(R.id.message_count);
            row.setTag(holder);
        }
        else
        {
            holder = (FriendHolder)row.getTag();
        }
	    
	    FriendMessage mMessage = list.get(position);
	    XMPPFriend friend = mMessage.getFriend();
        holder.txtName.setText(friend.getName());
        
        if(friend.getPresenceStatus() == PresenceStatus.ONLINE)
        	holder.txtPresence.setText("Online");
        else
        	holder.txtPresence.setText("Offline");
        
        if(mMessage.getMessageCount() > 0)
        {
        	holder.txtCount.setVisibility(View.VISIBLE);
        	holder.txtCount.setText(String.valueOf(mMessage.getMessageCount()));
        }
        else
        {
        	holder.txtCount.setVisibility(View.GONE);
        }
        
        if(friend.getImage()==null)
        	holder.imgUser.setImageResource(R.drawable.ic_launcher);
        else{
        		//holder.imgUser.setImageBitmap(getRoundedCornerBitmap(friend.getImage(),"#ffffff"));
        	holder.imgUser.setImageBitmap(friend.getImage());
        }
       
        return row;
	}

	
    static class FriendHolder
    {
        ImageView imgUser;
        TextView txtName;
        TextView txtPresence;
        TextView txtCount;
    } 
}
