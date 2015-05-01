package com.ultimate.bams;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements
ActionBar.TabListener{
    
	public static final String TAG = MainActivity.class.getSimpleName();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        
        ParseAnalytics.trackAppOpened(getIntent());
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			navigateToLogin();
		}
		else {
			Log.i(TAG, currentUser.getUsername());
		}
		
        /**
         * Creating all buttons instances
         * */
        // Dashboard News feed button
        Button btn_regStudent = (Button) findViewById(R.id.btn_news_feed);
        
        // Dashboard Friends button
        Button btn_editStudents = (Button) findViewById(R.id.btn_friends);
        
        // Dashboard Messages button
        Button btn_takeAtten = (Button) findViewById(R.id.btn_messages);
        
        // Dashboard Places button
        Button btn_showAtten = (Button) findViewById(R.id.btn_places);
        
        // Dashboard Events button
        Button btn_syncData = (Button) findViewById(R.id.btn_events);
        
        // Dashboard Photos button
        Button btn_logout = (Button) findViewById(R.id.btn_photos);
        
        /**
         * Handling all button click events
         * */
        
        // Listening to News Feed button click
        btn_regStudent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching Take_Attendance Screen
				Intent i = new Intent(getApplicationContext(), Register_Student.class);
				startActivity(i);
			}
		});
        
       // Listening Friends button click
        btn_editStudents.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching Edit_Details_Activity Screen
				Intent i = new Intent(getApplicationContext(), Edit_Details_Activity.class);
				startActivity(i);
			}
		});
        
        // Listening Messages button click
        btn_takeAtten.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching Take_Attendance
				Intent i = new Intent(getApplicationContext(), Take_Attendance.class);
				startActivity(i);
			}
		});
        
        // Listening to Places button click
        btn_showAtten.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching News Feed Screen
				Intent i = new Intent(getApplicationContext(), Pickdate.class);
				startActivity(i);
			}
		});
        
        // Listening to Events button click
        btn_syncData.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching News Feed Screen
				Intent i = new Intent(getApplicationContext(), SyncActivity.class);
				startActivity(i);
			}
		});
        
        // Listening to Photos button click
        btn_logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				ParseUser.logOut();
				navigateToLogin();
			}
		});
    }
    private void navigateToLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		
		if (itemId == R.id.action_logout) {
			ParseUser.logOut();
			navigateToLogin();
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}