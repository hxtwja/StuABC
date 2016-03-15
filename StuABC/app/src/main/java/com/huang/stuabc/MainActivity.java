package com.huang.stuabc;

import java.util.ArrayList;

import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;

import com.huang.stuabc.adapter.MyFragmentPagerAdapter;
import com.huang.stuabc.constants.Constants;
import com.huang.stuabc.constants.Preferences;
import com.rrgame.sdk.RRGDevMode;
import com.rrgame.sdk.RRGScreen;
import com.rrgame.sdk.RRGScreenListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author
 *
 */

public class MainActivity extends FragmentActivity implements
		UpdatePointsNotifier,RRGScreenListener {

	public static ViewPager mPager;
	private ImageView img_ch, img_math, img_en, img_title;
	private ArrayList<Fragment> fragmentsList;
	private ArrayList<ImageView> viewList;
	public static final int CASE_CHINESE = 10;
	public static final int CASE_MATH = 11;
	public static final int CASE_ENGLISH = 12;

	private int mBackEventCount = 1;
	private int[] titles = new int[] { R.drawable.title_ch,
			R.drawable.title_math, R.drawable.title_en };
	/** 插屏广告 */
	private RRGScreen interstitialAd;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppConnect.getInstance("99a31d6e8744a34ec5af70d2bbf5bba0", "gfan", this);
		AppConnect.getInstance(this).setAdViewClassName("com.huang.test.MyAdActivity");
		AppConnect.getInstance(this).setCrashReport(false);
		
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		initViews();
		InitViewPager();

		interstitialAd = new RRGScreen(this,
				"346cbaec7c234b6bb990cf4fb9799b0d", RRGDevMode.RELEASE_MODE);
		interstitialAd.setAdListener(this);
		
		interstitialAd.load();
	}

	/**
	 * 初始化四个TextView，并为TextView设置点击监听事件
	 */
	private void initViews() {
		((LinearLayout) this.findViewById(R.id.linearLayout1)).getBackground()
				.setAlpha(0);
		img_title = (ImageView) this.findViewById(R.id.title);
		img_ch = (ImageView) this.findViewById(R.id.img_ch);
		img_math = (ImageView) this.findViewById(R.id.img_math);
		img_en = (ImageView) this.findViewById(R.id.img_en);
		viewList = new ArrayList<ImageView>();
		viewList.add(img_ch);
		viewList.add(img_math);
		viewList.add(img_en);
	}

	/**
	 * 初始化ViewPager，创建3个Fragment，为ViewPager设置适配器，为ViewPager设置监听器
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		fragmentsList = new ArrayList<Fragment>();

		Fragment activityfragment = TestFragment.newInstance(CASE_CHINESE,
				Constants.CHINESE_ZH);
		Fragment groupFragment = TestFragment.newInstance(CASE_MATH,
				Constants.MATH_ZH);
		Fragment friendsFragment = TestFragment.newInstance(CASE_ENGLISH,
				Constants.ENGLISH_ZH);

		fragmentsList.add(activityfragment);
		fragmentsList.add(groupFragment);
		fragmentsList.add(friendsFragment);

		mPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentsList));
		mPager.setCurrentItem(0);
		mPager.setOffscreenPageLimit(2);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			pageSelected(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private void pageSelected(int pos) {
		for (int i = 0; i < viewList.size(); i++) {
			if (pos == i)
				viewList.get(i).setImageResource(R.drawable.icon_turns_current);
			else
				viewList.get(i).setImageResource(R.drawable.icon_turns);

			img_title.setImageResource(titles[pos]);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mBackEventCount == 1) {
				mBackEventCount += 1;
				Toast.makeText(this, R.string.exit, Toast.LENGTH_LONG).show();
				return true;
			} else if (mBackEventCount == 2) {
				this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		AppConnect.getInstance(this).getPoints(this);
		super.onResume();
	}

	/**
	 * AppConnect.getPoints()方法的实现，必须实现
	 * 
	 * @param currencyName
	 *            虚拟货币名称.
	 * @param pointTotal
	 *            虚拟货币余额.
	 */
	public void getUpdatePoints(String currencyName, int pointTotal) {
		Preferences.putCoinCount(pointTotal);
		Log.e("pointTotal", pointTotal + "");
	}

	/**
	 * AppConnect.getPoints() 方法的实现，必须实现
	 * 
	 * @param error
	 *            请求失败的错误信息
	 */
	public void getUpdatePointsFailed(String error) {
	}
	
	@Override
	protected void onDestroy() {
		AppConnect.getInstance(this).finalize();
		super.onDestroy();
	}

	@Override
	public void onScreenDismiss() {
	}

	@Override
	public void onScreenFailed(int arg0, String arg1) {
	}

	@Override
	public void onScreenLeaveApplication() {
	}

	@Override
	public void onScreenPresent() {
	}

	@Override
	public void onScreenReady() {
		if (interstitialAd != null) {
			interstitialAd.show(this);
		}
	}

}
