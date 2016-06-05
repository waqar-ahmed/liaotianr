package com.app.liaotianr.activity;

import com.app.liaotianr.R;
import com.app.liaotianr.R.id;
import com.app.liaotianr.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.TextView;

public class ExceptionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exception);
		Intent i = getIntent();
		TextView tv = (TextView)findViewById(R.id.tvException);
		tv.setText(i.getStringExtra("exception"));
		tv.setMovementMethod(new ScrollingMovementMethod());
	}


}
