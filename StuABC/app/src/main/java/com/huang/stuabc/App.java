package com.huang.stuabc;

import com.huang.stuabc.constants.Preferences;

import android.app.Application;

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Preferences.init(this);
	}
}
