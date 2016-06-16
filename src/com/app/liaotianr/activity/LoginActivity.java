package com.app.liaotianr.activity;

import org.jivesoftware.smack.XMPPException;

import com.app.liaotianr.MyPreference;
import com.app.liaotianr.R;
import com.app.liaotianr.xmpp.ServerConfiguration;
import com.app.liaotianr.xmpp.XMPPManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity{
	
	private EditText etUsername, etPassword;
	private Button btnLogin;
	private String username;
	private String password;
	private ProgressDialog dialog;
	private XMPPManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		etUsername = (EditText)findViewById(R.id.login_et_username);
		etPassword = (EditText)findViewById(R.id.login_et_password);
		btnLogin = (Button)findViewById(R.id.login_btn_login);
		
		manager = XMPPManager.getInstance(getApplicationContext());
	}
	
	public void loginMe(View view){
		username = etUsername.getText().toString();
		password = etPassword.getText().toString();
		
		if(validateInput(username, password)){
			showDialog();
			try {
				manager.loginMe(username, password);
				MyPreference.saveBoolean(getApplicationContext(), MyPreference.Keys.IS_USER_REGISTERED, true);
				MyPreference.saveString(getApplicationContext(), MyPreference.Keys.USERNAME, username);
				MyPreference.saveString(getApplicationContext(), MyPreference.Keys.PASSWORD, password);
				
				startActivity(new Intent(LoginActivity.this, FriendListActivity.class));
				LoginActivity.this.finish();
				
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showToast(e.getMessage());
			}
		}
		else{
			showToast("Please filled all the fields!");
		}
	}

	private boolean validateInput(String username, String password) {
		if(username.equals("") || password.equals("")){
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
