package com.app.liaotianr.activity;

import java.util.ArrayList;

import org.jivesoftware.smack.XMPPException;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.liaotianr.ApplicationSettings;
import com.app.liaotianr.R;
import com.app.liaotianr.R.id;
import com.app.liaotianr.R.layout;
import com.app.liaotianr.R.menu;
import com.app.liaotianr.adapter.MessageAdapter;
import com.app.liaotianr.database.MessageDbHandler;
import com.app.liaotianr.model.Message;
import com.app.liaotianr.service.XMPPService;
import com.app.liaotianr.xmpp.XMPPConstants;
import com.app.liaotianr.xmpp.XMPPUtils;

public class ChatActivity extends ActionBarActivity {

	static final String CHAT_ACTIVITY_INTENT_ACTIVE = "com.app.liaotianr.chatActivity_active";
	static final String CHAT_ACTIVITY_INTENT_UNACTIVE = "com.app.liaotianr.chatActivity_unactive";
	
	ListView listMessages;
	Button sendMessage;
	EditText etMessage;
	TextView tvUsername;
	String message;
	
	ArrayList<Message> messages;
	
	MessageDbHandler dbMessage;
	
	MessageAdapter adapter;
	
	Intent intent;
	//getting these values from intent
	String username;
	String name;
	
	public static Handler messageHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_screen);
		
		listMessages = (ListView)findViewById(R.id.chat_screen_lv_chat_messages);
		sendMessage = (Button)findViewById(R.id.chat_screen_btn_send);
		etMessage = (EditText)findViewById(R.id.chat_screen_et_message);
		tvUsername = (TextView)findViewById(R.id.chat_screen_tv_user_name);
		
		intent = this.getIntent();
		username = intent.getStringExtra("username");
		name = intent.getStringExtra("name");
		
		tvUsername.setText(name + "("+username+")");
		

		messages = new ArrayList<Message>();
		dbMessage = new MessageDbHandler(getApplicationContext());
		messages.addAll(dbMessage.getAllMessages(username, 1));
		
		adapter = new MessageAdapter(this, messages);
		listMessages.setAdapter(adapter);
		
		listMessages.setSelection(messages.size());

		messageHandler = new Handler(){
			@Override
			public void handleMessage(android.os.Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				Bundle bundle = msg.getData();
				Intent i = new Intent();
				i.putExtras(bundle);
				
				String from = i.getStringExtra(XMPPConstants.IntentKeys.XmppMessageKey.XMPP_MESSAGE_JID);
				String body = i.getStringExtra(XMPPConstants.IntentKeys.XmppMessageKey.XMPP_MESSAGE_BODY);
				String dateTime = i.getStringExtra(XMPPConstants.IntentKeys.XmppMessageKey.XMPP_MESSAGE_DATE_TIME);
				
				Message message = new Message(from.split("/")[0], from.split("/")[0], ApplicationSettings.MY_JID, body, 0, dateTime);
				messages.add(message);
				adapter.notifyDataSetChanged();
				listMessages.setSelection(messages.size());
			}
		};
		
		Intent intent = new Intent();
        intent.setAction(CHAT_ACTIVITY_INTENT_ACTIVE);
        intent.putExtra("state", true);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
	}
	
	public void sendMessage(View v){
		message = etMessage.getText().toString();
		if(message.equals("")) return;
		
		try {
			XMPPService.sendMessage(username, message);
			Message msg = new Message(username, ApplicationSettings.MY_JID, username, message, 0, XMPPUtils.getCurrentGMTDate());
			messages.add(msg);
			dbMessage.addMessage(msg);
			adapter.notifyDataSetChanged();
			listMessages.setSelection(messages.size());
			etMessage.setText("");
		} catch (NetworkErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "Network error occured!. Make sure you connected to network", Toast.LENGTH_SHORT).show();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "Connection error occured!", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Intent intent = new Intent();
        intent.setAction(CHAT_ACTIVITY_INTENT_UNACTIVE);
        intent.putExtra("state", false);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
