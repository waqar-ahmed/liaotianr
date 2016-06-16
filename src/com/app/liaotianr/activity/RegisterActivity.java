package com.app.liaotianr.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.liaotianr.MyPreference;
import com.app.liaotianr.R;
import com.app.liaotianr.utilities.Utility;
import com.app.liaotianr.xmpp.ServerConfiguration;
import com.app.liaotianr.xmpp.XMPPConstants;
import com.app.liaotianr.xmpp.XMPPManager;

public class RegisterActivity extends Activity{
	
	EditText etUsername, etName, etPassword;
	Button btnRegister;
	TextView tvloginLink;
	String username, name, password;
	XMPPManager manager;
	Map<String, String> values;
	public static Handler handler;
	int code;
	String TAG = "RegisterActivity";
	ProgressDialog dialog ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		etUsername = (EditText)findViewById(R.id.register_et_username);
		etName = (EditText)findViewById(R.id.register_et_name);
		etPassword = (EditText)findViewById(R.id.register_et_password);
		btnRegister = (Button)findViewById(R.id.register_btn_register);
		
		tvloginLink = (TextView)findViewById(R.id.register_tv_loginLink);
		tvloginLink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Go to login screen
				startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
			}
		});
		
		etUsername.setText("waqar");
		etName.setText("waqar");
		etPassword.setText("waqar");
		
		values = new HashMap<String, String>();
		manager = XMPPManager.getInstance(getApplicationContext());
		
		handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				dialog.dismiss();
				Bundle bundle = msg.getData();
				processResponse(bundle);
			};
		};
	}

	protected void processResponse(Bundle bundle) {
		// TODO Auto-generated method stub
		Intent arg1 = new Intent();
		arg1.putExtras(bundle);
		code = arg1.getIntExtra(XMPPConstants.IntentKeys.XmppAccountCreationKey.XMPP_ACCOUNT_CREATION_CODE, -1);
		if(code == XMPPConstants.XMPP_ACCOUNT_CREATION_CONFLICT){
			Utility.Log(TAG, "Sorry!, User already exist with this username");
			showToast("Sorry!, User already exist with this username");
		}
		else if(code == XMPPConstants.XMPP_ACCOUNT_CREATION_SUCCESSFULL){
			Utility.Log(TAG, "Account created Successfully");
			MyPreference.saveBoolean(getApplicationContext(), MyPreference.Keys.IS_USER_REGISTERED, true);
			MyPreference.saveString(getApplicationContext(), MyPreference.Keys.USERNAME, username);
			MyPreference.saveString(getApplicationContext(), MyPreference.Keys.PASSWORD, password);
//			try {
//				manager.loginMe(username, password);
//			} catch (XMPPException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			startActivity(new Intent(RegisterActivity.this, FriendListActivity.class));
			RegisterActivity.this.finish();
		}
		else if(code == XMPPConstants.XMPP_ACCOUNT_CREATION_ERROR){
			Utility.Log(TAG, "Sorry!, Error occured during creation of account");
			showToast("Sorry!, Error occured during creation of account");
		}
		else{
			Utility.Log(TAG, "Some Internal error occured");
		}
	}

	public void registerMe(View view){
		username = etUsername.getText().toString();
		name = etName.getText().toString();
		password = etPassword.getText().toString();
		
		if(validateInput(username, name, password)){
			values.put("email", username + "@" + ServerConfiguration.DOMAIN);
			values.put("name", name);
			showDialog();
			manager.createAccount(username, password, values);
		}
		else{
			showToast("Please filled all the fields!");
		}
	}

	private boolean validateInput(String username, String name, String password) {
		if(username.equals("") || name.equals("") || password.equals("")){
			return false;
		}
		return true;
	}
	
	void showDialog(){
		dialog = new ProgressDialog(this);
		dialog.setTitle("Wait");
		dialog.setMessage("Creating account...");
		dialog.show();
	}
	
	void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

}
