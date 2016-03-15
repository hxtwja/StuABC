package com.huang.stuabc;

import java.lang.ref.WeakReference;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;

public class StartActivity extends Activity{

	private ImageView mStartImage;
	private static final int ENTER_APPLICATION = 0xAAA1;
	private Handler mCloseAnimationActivityHandler = new HandlerExtension(
			StartActivity.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.animation);
		mStartImage = (ImageView) this.findViewById(R.id.start_img);
		mStartImage.setBackgroundResource(R.drawable.bg1);

		mCloseAnimationActivityHandler.sendEmptyMessageDelayed(
				ENTER_APPLICATION, 1000 * 2);
	}

	static final class HandlerExtension extends Handler {

		WeakReference<StartActivity> outerClass;

		HandlerExtension(StartActivity activity) {
			outerClass = new WeakReference<StartActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			StartActivity theClass = outerClass.get();
			switch (msg.what) {
			case ENTER_APPLICATION:
				theClass.toMainActivty();
				break;
			}
			super.handleMessage(msg);
		}
	}

	public void toMainActivty() {
		Intent intent = new Intent(StartActivity.this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.animenter, R.anim.animexit);
		onBackPressed();
	}
	
}
