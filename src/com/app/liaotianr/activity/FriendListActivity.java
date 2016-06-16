package com.app.liaotianr.activity;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.XMPPException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.liaotianr.FriendMessage;
import com.app.liaotianr.MessageManageHelper;
import com.app.liaotianr.MyPreference;
import com.app.liaotianr.R;
import com.app.liaotianr.SettingsActivity;
import com.app.liaotianr.adapter.FriendListAdapter;
import com.app.liaotianr.database.MessageDbHandler;
import com.app.liaotianr.receiver.NetworkReceiver;
import com.app.liaotianr.receiver.ServerConnectedReceiver;
import com.app.liaotianr.service.XMPPService;
import com.app.liaotianr.utilities.Utility;
import com.app.liaotianr.xmpp.ConnectionStatus;
import com.app.liaotianr.xmpp.XMPPConstants;
import com.app.liaotianr.xmpp.XMPPFriend;
import com.app.liaotianr.xmpp.XMPPManager;

@SuppressLint("HandlerLeak")
public class FriendListActivity extends AppCompatActivity implements OnItemClickListener{

	XMPPManager manager ;
	boolean hasAccount = false;
	ListView friendsList;
	TextView emptyListText;
	FriendListAdapter adapter;
	String currentChatWith = "";
	
	NetworkReceiver networkReceiver = new NetworkReceiver();
	ServerConnectedReceiver receiver;
	List<XMPPFriend> friends;
	List<FriendMessage> friendsMessage = new ArrayList<FriendMessage>();
	MessageManageHelper mMessageManager;
	public static boolean isVisible = false;
	static final String FRIEND_LIST_ACTIVITY_INTENT_ACTIVE = "com.app.liaotianr.friendListActivity_active";
	static final String FRIEND_LIST_ACTIVITY_INTENT_UNACTIVE = "com.app.liaotianr.friendListActivity_unactive";
	
	public static Handler updateView ;
	public static Handler messageReceived;
	public static Handler showRequestDialog;
	
	MessageDbHandler dbMessage;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d("tag", "oncreate called");
		
		setContentView(R.layout.friends_list);
		
		//setupActionBar();
		
		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
	    setSupportActionBar(myToolbar);

		dbMessage = new MessageDbHandler(getApplicationContext());
		
        updateView = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		// TODO Auto-generated method stub
        		super.handleMessage(msg);
        		refreshList();
        	}
        };
        
        showRequestDialog = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		// TODO Auto-generated method stub
        		super.handleMessage(msg);
        		Bundle bundle = msg.getData();
				Intent i = new Intent();
				i.putExtras(bundle);
				
				String from = i.getStringExtra("from");
				String title = i.getStringExtra("title");
				String toast = i.getStringExtra("toast");
				boolean isFriendRequestReceived = i.getBooleanExtra("isFriendRequestReceived", false);
				
				showReceiveFriendRequestDialog(from, title, toast, isFriendRequestReceived);
        	}
        };
        
		manager = XMPPManager.getInstance(getApplicationContext());
		friendsList = (ListView)findViewById(R.id.chat_list_listview);
		adapter = new FriendListAdapter(FriendListActivity.this, R.layout.friend_list_row, friendsMessage);
		friendsList.setAdapter(adapter);
		
		emptyListText = (TextView)findViewById(R.id.chat_list_listviewText);
		friendsList.setEmptyView(emptyListText);
		
		friendsList.setOnItemClickListener(this);
		
		//you can also wait for server to connect before login
		if(!manager.isUserLogin())
			new AsynLogin().execute();
		

        messageReceived = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		// TODO Auto-generated method stub
        		super.handleMessage(msg);
        		Bundle bundle = msg.getData();
				Intent i = new Intent();
				i.putExtras(bundle);
				
				String from = i.getStringExtra(XMPPConstants.IntentKeys.XmppMessageKey.XMPP_MESSAGE_JID);
				String body = i.getStringExtra(XMPPConstants.IntentKeys.XmppMessageKey.XMPP_MESSAGE_BODY);
				String dateTime = i.getStringExtra(XMPPConstants.IntentKeys.XmppMessageKey.XMPP_MESSAGE_DATE_TIME);
				
				String username = from.split("/")[0];   //splitting resource like abc@sol/spark
				
				if(currentChatWith.equals(username)){
					//send to chat screen and set alreadyRead to 0 --- also set 0 on list item click
					dbMessage.updateAlreadyReadMessage(username);
					//send message to chat Activity
					android.os.Message mMsg = android.os.Message.obtain();
					mMsg.copyFrom(msg);
					ChatActivity.messageHandler.sendMessage(mMsg);
				}
				else
				{
					int count = dbMessage.countUnreadMessages(username);
					for(FriendMessage f : friendsMessage){
						if(f.getFriend().getUsername().equals(username)){
							f.setMessageCount(count);
							adapter.notifyDataSetChanged();
							return;
						}
					}
				}
        	}
        };
        
        
    	
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(
        	    new OnSharedPreferenceChangeListener() {
        	    @Override
        	    public void onSharedPreferenceChanged(
        	        SharedPreferences sharedPreferences, String key) {
        	        Log.i("Tag", "testOnSharedPreferenceChangedWrong key =" + key);
        	        System.out.println("testOnSharedPreferenceChangedWrong key =" + key);
        	        
        	        XMPPService.reConnect();
        	        new Thread(){
        	        	public void run(){
        	        		while(ConnectionStatus.CURRENT_STATUS != ConnectionStatus.CONNECTED){
        	        			try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
        	        		}
        	        		
        	        		FriendListActivity.this.runOnUiThread(new Runnable() {
        	        		    @Override
        	        		    public void run() {
        	        		        new AsynLogin().execute();
        	        		    	//recreate();
        	        		    }
        	        		});
        	        		
//        	        		
//        	        		FriendListActivity.runOnUiThread(new Runnable() {
//        	        	        @Override
//        	        	        public void run() {
//        	        	           //Your code to run in GUI thread here
//        	        	        }//public void run() {
//        	        	});
        	        		
        	        	}
        	        }.start();
        	    }
        	    
        	});
        
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		FriendMessage f = friendsMessage.get(arg2);
		currentChatWith = f.getFriend().getUsername();
		dbMessage.updateAlreadyReadMessage(currentChatWith);
		Intent i = new Intent(FriendListActivity.this, ChatActivity.class);
		i.putExtra("username", f.getFriend().getUsername());
		i.putExtra("name", f.getFriend().getName());
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(item.getItemId()){
		
			case R.id.add_friend:
				showAddFriendDialog("Add Friend", "Friend request sent!", false);
				break;
				
			case R.id.action_settings:
				Intent i = new Intent(FriendListActivity.this, SettingsActivity.class);
                startActivity(i);
				break;
		}
		return super.onOptionsItemSelected(item);
	}


	
	private void showAddFriendDialog(String dialogTitle, final String toastMessage, final boolean isFriendRequestReceived) {
		showDialog("", dialogTitle, toastMessage, isFriendRequestReceived);
	}
	
	
	public void showReceiveFriendRequestDialog(final String from, String dialogTitle, final String toastMessage, final boolean isFriendRequestReceived) {
		showDialog(from, dialogTitle, toastMessage, isFriendRequestReceived);
	}

	private void showDialog(final String from, String dialogTitle,
			final String toastMessage, final boolean isFriendRequestReceived) {
		// TODO Auto-generated method stub
		LayoutInflater li = LayoutInflater.from(this);
		View addFriendView = li.inflate(R.layout.single_prompt_input, null);
		
		String positiveButtonTitle = "Send";
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setView(addFriendView);
		
		final EditText input = (EditText)addFriendView.findViewById(R.id.prompt_input);
		final TextView title = (TextView)addFriendView.findViewById(R.id.prompt_title);
		title.setText(dialogTitle);
		
		if(isFriendRequestReceived){
			input.setVisibility(View.GONE);
			positiveButtonTitle = "Accept";
		}
		
		alertDialog.setPositiveButton(positiveButtonTitle, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				if(isFriendRequestReceived){
					try {
						manager.addFriend(from);
					} catch (XMPPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					String value = input.getText().toString();
					if(!value.equals("")){
						try {
								manager.addFriend(value);
							
							Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
						} catch (XMPPException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		alertDialog.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				if(isFriendRequestReceived)
					manager.sendCancelRequest(from);
				
				dialog.cancel();
			}
		});
		
		alertDialog.create();
		alertDialog.show();
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent intent = new Intent();
        intent.setAction(FRIEND_LIST_ACTIVITY_INTENT_ACTIVE);
        intent.putExtra("state", true);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
		currentChatWith = "";
		if(XMPPService.isLogin()){
			refreshList();
		}
		
	}
	
	private void refreshList(){
		friendsMessage.clear();
		List<FriendMessage> temp = null;
		try{
			temp = XMPPService.getFriendList(); 
		}
		catch(Exception e){
			Utility.Log("FriendListActiivry", e.getMessage());
			Utility.Log("FriendListActiivry", e.getCause().toString());
		}
		if(temp != null){
			friendsMessage.addAll(temp);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		isVisible = false;
		Intent intent = new Intent();
        intent.setAction(FRIEND_LIST_ACTIVITY_INTENT_UNACTIVE);
        intent.putExtra("state", false);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	public class AsynLogin extends AsyncTask<Void, Void, Boolean>{

		ProgressDialog dialog;
		
		public AsynLogin(){
			dialog = new ProgressDialog(FriendListActivity.this); // this = YourActivity
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setMessage("Loading. Please wait...");
			dialog.setIndeterminate(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
		
			while(ConnectionStatus.CURRENT_STATUS != ConnectionStatus.CONNECTED){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return XMPPService.login(MyPreference.getString(getApplicationContext(), MyPreference.Keys.USERNAME), MyPreference.getString(getApplicationContext(), MyPreference.Keys.PASSWORD));
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			if(!result){
				Toast.makeText(FriendListActivity.this, "Error occured during login!", Toast.LENGTH_LONG).show();
				friendsMessage.clear();
				adapter.notifyDataSetChanged();
				return;
			}
			refreshList();
			//XMPPService.addFriend("adnan@sol");
		}
	}


	
}
