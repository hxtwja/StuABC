package com.huang.test;

import java.util.ArrayList;
import java.util.HashMap;

import com.huang.test.adapter.MainKnowledgeListAdapter;
import com.huang.test.constants.Constants;
import com.huang.test.db.DBAdapter;
import com.huang.test.view.CustomProgressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SubjectMainActivity extends Activity {
	private static Context mContext;
	private String dbFilePath;
	private String mGrade;
	private String mModule;
	CustomProgressDialog loadDataDialog;
	public static ExpandableListView exListView;
	HashMap<String, ArrayList<String>> ch12Map = new HashMap<String, ArrayList<String>>();

	ArrayList<String> groupList = new ArrayList<String>();
	ArrayList<ArrayList<String>> childrenList = new ArrayList<ArrayList<String>>();
	MainKnowledgeListAdapter adapter;
	public static ProgressDialog composePageDialog;
	public LinearLayout llKnowledgeList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		mContext = this;
		
		Intent i = this.getIntent();
		dbFilePath = i.getStringExtra(Constants.EXTRA_DBFILEPATH);
		mGrade = i.getStringExtra(Constants.EXTRA_GRADE);
		mModule = i.getStringExtra(Constants.EXTRA_MODULE);
		initView();
		loadDataDialog = CustomProgressDialog.createDialog(this);
		loadDataDialog.show();
		LoadDataThread loadth = new LoadDataThread();
		loadth.start();
	}


	public void initView() {
		exListView = (ExpandableListView) findViewById(R.id.exListView);
		TextView mTextViewtitle = (TextView) this.findViewById(R.id.title);
		TextPaint tp = mTextViewtitle.getPaint();
		tp.setFakeBoldText(true);
		mTextViewtitle.setText(mModule + mGrade);
		
		Button mButtonBack = (Button) this.findViewById(R.id.back);
		mButtonBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
			
		});
	}

	class LoadDataThread extends Thread {
		public void run() {
			groupList.clear();
			childrenList.clear();
			DBAdapter dbAdapter = new DBAdapter(dbFilePath);
			HashMap<String,ArrayList<String>> data = dbAdapter.getMathKnowledge();
			for (String key : data.keySet()) {
				groupList.add(key);
				childrenList.add(data.get(key));
			}
			loadDataHandler.sendMessage(Message.obtain());
		}
	}

	Handler loadDataHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			adapter = new MainKnowledgeListAdapter(mContext, groupList,
					childrenList, dbFilePath, mModule, mGrade);
			exListView.setAdapter(adapter);
			if (loadDataDialog.isShowing()) {
				loadDataDialog.dismiss();
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
