package com.huang.stuabc;

import java.io.File;

import com.huang.stuabc.adapter.GridAdapter;
import com.huang.stuabc.constants.Constants;
import com.huang.stuabc.constants.Preferences;
import com.huang.stuabc.utils.MyUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import cn.waps.AppConnect;

/**
 * @author user
 * 
 */
public class TestFragment extends Fragment implements OnItemClickListener,
		OnClickListener {
	public static final int GRID_THREAD = 0;
	public static final int LIST_THREAD = 1;
	public static final int GALLERY_THREAD = 5;
	private int num;
	private String mModule;
	private View view;
	private Context mContext;

	private GridView gridView_math, gridVIew_chinese, girdView_english;

	private GridAdapter adapter_math, adapter_chinese, adapter_english;

	private int[] imgs_math = new int[] { R.drawable.math_1, R.drawable.math_2,
			R.drawable.math_3, R.drawable.math_4, R.drawable.math_5,
			R.drawable.math_6 };
	private int[] imgs_chinese = new int[] { R.drawable.chinese_1,
			R.drawable.chinese_2, R.drawable.chinese_3, R.drawable.chinese_4,
			R.drawable.chinese_5, R.drawable.chinese_6 };
	private int[] imgs_english = new int[] { R.drawable.english_1,
			R.drawable.english_2, R.drawable.english_3, R.drawable.english_4,
			R.drawable.english_5, R.drawable.english_6 };

	static TestFragment newInstance(int s, String module) {
		TestFragment newFragment = new TestFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.EXTRA_FRAGMENTNO, s);
		bundle.putString(Constants.EXTRA_MODULE, module);
		newFragment.setArguments(bundle);
		return newFragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this.getActivity().getApplicationContext();
		Bundle args = getArguments();
		num = args.getInt(Constants.EXTRA_FRAGMENTNO);
		mModule = args.getString(Constants.EXTRA_MODULE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		switch (num) {
		case MainActivity.CASE_CHINESE:
			view = inflater.inflate(R.layout.lay1, container, false);
			gridVIew_chinese = (GridView) view.findViewById(R.id.gridView);
			adapter_chinese = new GridAdapter(this.getActivity()
					.getApplicationContext(), imgs_chinese, "chinese");
			gridVIew_chinese.setAdapter(adapter_chinese);
			gridVIew_chinese.setOnItemClickListener(this);
			break;

		case MainActivity.CASE_MATH:
			view = inflater.inflate(R.layout.lay1, container, false);
			gridView_math = (GridView) view.findViewById(R.id.gridView);
			adapter_math = new GridAdapter(this.getActivity()
					.getApplicationContext(), imgs_math, "math");
			gridView_math.setAdapter(adapter_math);
			gridView_math.setOnItemClickListener(this);
			break;

		case MainActivity.CASE_ENGLISH:
			view = inflater.inflate(R.layout.lay1, container, false);
			girdView_english = (GridView) view.findViewById(R.id.gridView);
			adapter_english = new GridAdapter(this.getActivity()
					.getApplicationContext(), imgs_english, "english");
			girdView_english.setAdapter(adapter_english);
			girdView_english.setOnItemClickListener(this);
			break;
		}
		return view;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
		switch (num) {
		case MainActivity.CASE_CHINESE:
			start("chinese", adapter_chinese, pos);
			break;
		case MainActivity.CASE_MATH:
			start("math", adapter_math, pos);
			break;
		case MainActivity.CASE_ENGLISH:
			Resources resources = mContext.getResources();
			if (pos == 0)
				Toast.makeText(
						mContext,
						String.format(resources.getString(R.string.nocontent),
								resources.getString(R.string.grade1)),
						Toast.LENGTH_LONG).show();
			else if (pos == 1)
				Toast.makeText(
						mContext,
						String.format(resources.getString(R.string.nocontent),
								resources.getString(R.string.grade2)),
						Toast.LENGTH_LONG).show();
			else
				start("english", adapter_english, pos);
			break;
		}
	}

	private void start(String module, GridAdapter adapter, int pos) {
		String dbFilePath = MyUtils.getDBFilePathBYGrade(module, pos + 1);
		File file = new File(dbFilePath);
		if (file.exists()) {
			Intent intent = new Intent(TestFragment.this.getActivity(),
					SubjectMainActivity.class);
			intent.putExtra(Constants.EXTRA_DBFILEPATH, dbFilePath);
			intent.putExtra(Constants.EXTRA_MODULE, mModule);
			intent.putExtra(Constants.EXTRA_GRADE,
					MyUtils.getGradeCHName(pos + 1));
			startActivity(intent);
		} else {
			if (Preferences.getGradeFlag(pos + 1)&&Preferences.getCoinCount() < 100) {
				showCoinDialog();
			} else {
				adapter.setDownPos(pos);
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onClick(View arg0) {

	}

	private void showCoinDialog() {
		new AlertDialog.Builder(this.getActivity())
				.setTitle("提示")
				.setMessage(
						"对不起，每年级仅免费提供一个科目的下载，获取其他科目内容请您需花费100金币。金币余额:"
								+ Preferences.getCoinCount())
				.setIcon(R.drawable.alerticon)
				.setPositiveButton("赚取", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						AppConnect.getInstance(mContext).showOffers(mContext);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create().show();
	}
}
