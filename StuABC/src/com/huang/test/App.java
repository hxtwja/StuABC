package com.huang.test;

import com.huang.test.constants.Preferences;

import android.app.Application;

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Preferences.init(this);
	}
}
