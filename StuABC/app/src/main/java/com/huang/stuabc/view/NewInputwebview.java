package com.huang.stuabc.view;

import java.io.IOException;
import java.util.ArrayList;

import com.huang.stuabc.R;
import com.huang.stuabc.constants.Constants;
import com.huang.stuabc.utils.MyUtils;
import com.huang.stuabc.utils.UUID;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

public class NewInputwebview extends LinearLayout {
	Context context;
	private View view;
	private String mDaan;
	private boolean flag;

	RadioGroup radioGroup;
	String result = "";
	private TextView daanText;
	Drawable d = null;
	TextView txt1;
	WebView web;
	EditText daanText2;
	public boolean yuWen = false;
	String model = "";
	ArrayList<String> datas = new ArrayList<String>();
	PopupWindow selectPopupWindow;
	int allCount = 0;
	public int yuwen1 = 0;
	int size = 18;

	public NewInputwebview(Context context) {
		super(context);
		setWillNotDraw(false);
		this.context = context;
		init(context);
	}

	public NewInputwebview(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		this.context = context;
		init(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (flag) {
			daanText.setVisibility(View.VISIBLE);
			Spanned richText = Html.fromHtml(mDaan);
			daanText.setText("  正确答案：   " + richText);

			daanText.setTextSize(25);
			daanText.setBackgroundResource(R.drawable.daanbg);

			daanText.setTextColor(Color.BLACK);
		}
		super.onDraw(canvas);
	}

	private void init(Context mContext) {
		LayoutInflater vi = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.webview, null);
		daanText = (TextView) view.findViewById(R.id.daan);

		web = (WebView) view.findViewById(R.id.webview);
		web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		web.setBackgroundColor(R.color.transparent);
		web.getSettings().setDefaultTextEncodingName("UTF-8");
		web.getSettings().setPluginState(WebSettings.PluginState.ON);
		//web.getSettings().setPluginsEnabled(true);

		web.getSettings().setJavaScriptEnabled(true);
		WebSettings webSettings = web.getSettings();
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
		webSettings.setAllowFileAccess(true);

		this.addView(view);

	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public NewInputwebview initWebView(String mTimu, int currentId,String daan,String dbFilePath) {
		try {
			String temp;
			mDaan = daan;
			String uuid = UUID.timeUUID();
			String filePath = dbFilePath.substring(0, dbFilePath.lastIndexOf("/")) + "/template.html";
			temp = MyUtils.getTemplateFile(filePath);
			temp = temp.replace("[xuhao]", String.valueOf(currentId));
			temp = temp.replace("[biaoti]", mTimu);
			temp = temp.replaceAll("images", dbFilePath.substring(0, dbFilePath.lastIndexOf("/"))+"/images");
			temp = temp.replace("期末试卷", "");
			MyUtils.writeToFile(temp, Constants.DATA_FOLDER_TEMPLATE + uuid + ".html");

			web.loadUrl("file://" + Constants.DATA_FOLDER_TEMPLATE + uuid + ".html");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	public NewInputwebview initWebView1(String mTimu, int currentId) {
		if (web != null) {

			String hh1 = "";

			hh1 = "<html><head>"
					+ "<style type=\"text/css\">input{background:transparent;border:30;"
					+ "padding:0 2px;color:#527ab7;}</style>"
					+ "<script type=\"text/javascript\""
					+ "src=\"file://mnt/sdcard/ktnw/coolsubjecttest/数学/MathJax/MathJax.js?config=default\">"
					+ "MathJax.Hub.Config({"
					+ "config : [ \"MMLorHTML.js\" ],"
					+ "jax : [ \"input/MathML\", \"output/HTML-CSS\", \"output/NativeMML\" ],"
					+ "extensions : [ \"mml2jax.js\", \"MathMenu.js\", \"MathZoom.js\" ]"
					+ "});" + "</script>"
					+ "</head><body { font-family: 宋体; font-size: sizept;}>"
					+ currentId + "&nbsp" + mTimu + "</body></html>";

			web.loadDataWithBaseURL("file://" + Constants.DATA_FOLDER, hh1,
					"text/html", "utf-8", "");
		}

		return this;
	}

}