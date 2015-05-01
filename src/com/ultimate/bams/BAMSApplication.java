package com.ultimate.bams;

import android.app.Application;
import com.parse.Parse;

public class BAMSApplication extends Application {
		
	@Override
	public void onCreate() { 
		super.onCreate();
		 Parse.initialize(this, "EPvwbLQKsRg6sEByBXhTKmwsUYTxYm3OhAWYAZcC", "JHTiUQbXzNw3OBuWPoKa6nRhlQtNsno2HHk2bLY4");	
		
	}
	}