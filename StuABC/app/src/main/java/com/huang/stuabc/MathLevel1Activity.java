package com.huang.stuabc;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huang.stuabc.constants.Constants;
import com.huang.stuabc.db.DBAdapter;
import com.huang.stuabc.model.QuestionModel;
import com.huang.stuabc.view.CustomProgressDialog;
import com.huang.stuabc.view.NewInputwebview;

//测试时间40分钟
public class MathLevel1Activity extends Activity implements OnTouchListener,OnClickListener {
	/** Called when the activity is first created. */

	LinearLayout lin;
	WebView web;
	CustomProgressDialog mDialog = null;
	private InputMethodManager imm;
	ScrollView scroll;
	List<QuestionModel> datas;
	
	private String mKey3;
	private String mKey4;
	private String mDbFilePath;
	private Button commit,next;
	private ArrayList<NewInputwebview> allViews = new ArrayList<NewInputwebview>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_view_main);
	
		init();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mDialog = CustomProgressDialog.createDialog(this);
		mDialog.show();
		LoadDataThread lThread = new LoadDataThread();
		lThread.start();
		
	}

	public void init() {
		commit = (Button) findViewById(R.id.commit);
		commit.getPaint().setFakeBoldText(true);
		commit.setOnClickListener(this);
		next  = (Button) findViewById(R.id.next);
		next.getPaint().setFakeBoldText(true);
		next.setOnClickListener(this);
		lin = (LinearLayout) findViewById(R.id.slider_layout);
		scroll = (ScrollView) findViewById(R.id.scroll);

		Intent i = this.getIntent();
		mDbFilePath = i.getStringExtra(Constants.EXTRA_DBFILEPATH);
		mKey3 = i.getStringExtra(Constants.EXTRA_KEY3);
		mKey4 = i.getStringExtra(Constants.EXTRA_KEY4);
		
		TextView mTextViewtitle = (TextView) this.findViewById(R.id.title);
		TextPaint tp = mTextViewtitle.getPaint();
		tp.setFakeBoldText(true);
		if("".equals(mKey4))
			mTextViewtitle.setText(mKey3);
		else
			mTextViewtitle.setText(mKey4);
		
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

			DBAdapter dataAdapter = new DBAdapter(mDbFilePath);
			datas = dataAdapter.getTestByKnowledge(mKey3,mKey4);

			runOnUiThread(new Runnable() {
		        @Override
		     	public void run() {
		        	dealDiffTingxing();
		        	mDialog.cancel();
			    }
		   });
		}
	}

	public void dealDiffTingxing() {
		for (int i = 0; i < datas.size(); i++) {
			QuestionModel model = datas.get(i);
			NewInputwebview newWebview = new NewInputwebview(this);
			newWebview.initWebView(model.TIMU, i+1 ,model.DAAN,mDbFilePath);
			LinearLayout.LayoutParams mPhotoLP3 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			//mPhotoLP3.setMargins(0, 5, 0, 5);
		    lin.addView(newWebview, mPhotoLP3);
		    allViews.add(newWebview);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (imm != null) {
			if (imm.isActive()) {

				imm.hideSoftInputFromWindow(v.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.commit:
			commit();
			break;
		case R.id.next:
			next();
			break;
		}
	}

	public void commit(){
		for(int i=0;i<allViews.size();i++){
			NewInputwebview webView = allViews.get(i);
			webView.setFlag(true);
			webView.postInvalidate();
		}
		scroll.scrollTo(0, 0);
	}
	
	public void next(){
		mDialog.show();
		allViews.clear();
		lin.removeAllViews();
		LoadDataThread lThread = new LoadDataThread();
		lThread.start();
	}
}