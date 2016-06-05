package com.app.liaotianr.adapter;

import java.util.ArrayList;

import com.app.liaotianr.ApplicationSettings;
import com.app.liaotianr.R;
import com.app.liaotianr.model.Message;
import com.app.liaotianr.utilities.Utility;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<Message> mMessages;



	public MessageAdapter(Context context, ArrayList<Message> messages) {
		super();
		this.mContext = context;
		this.mMessages = messages;
	}
	@Override
	public int getCount() {
		return mMessages.size();
	}
	@Override
	public Object getItem(int position) {		
		return mMessages.get(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message message = (Message) this.getItem(position);

		ViewHolder holder; 
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.chat_row, parent, false);
			holder.message = (TextView) convertView.findViewById(R.id.message_text);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		holder.message.setText(message.getMessageBody() + Utility.convertDateTime(message.getTimeStamp()));
		
		LayoutParams lp = (LayoutParams) holder.message.getLayoutParams();
		//android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) holder.layout.getLayoutParams();
		
		
		if(message.getMessageFrom().equals(ApplicationSettings.MY_JID)){
			holder.message.setBackgroundResource(R.drawable.blue_bubble);
			lp.gravity = Gravity.RIGHT;
			
		}
		else{
			holder.message.setBackgroundResource(R.drawable.green_bubble);
			lp.gravity = Gravity.LEFT;
		}
		
		holder.message.setLayoutParams(lp);
		holder.message.setTextColor(Color.WHITE);	
		return convertView;
	}
	private static class ViewHolder
	{
		TextView message;
	}

	@Override
	public long getItemId(int position) {
		//Unimplemented, because we aren't using Sqlite.
		return 0;
	}

}

