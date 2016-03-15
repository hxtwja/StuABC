package com.huang.stuabc.adapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import com.huang.stuabc.R;
import com.huang.stuabc.constants.Constants;
import com.huang.stuabc.constants.Preferences;
import com.huang.stuabc.utils.MyUtils;
import com.huang.stuabc.utils.ZipUtil;

import cn.itcast.net.download.DownloadProgressListener;
import cn.itcast.net.download.FileDownloader;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class GridAdapter extends BaseAdapter implements UpdatePointsNotifier {
	public static final String ACTION_INTENT_TEST = "com.terry.broadcast.test";
	private LayoutInflater layoutInflater;
	private int[] data;
	private int downButtonShowPos = -1;
	private Context mContext;
	private String mModule;
	private ViewHolder mViewholder;
	private static final int MESSAGE_FLAG_NORESPONSE = -1;
	private static final int MESSAGE_FLAG_SUCCESS = 1;
	private static final int MESSAGE_FLAG_UNZIPSUCCESS = 2;
	private Handler handler = new HandlerExtension(GridAdapter.this);
	private HashMap<Integer, Integer> hashMapPos = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> hashMapFileSize = new HashMap<Integer, Integer>();

	private HashMap<String,ArrayList<String>> mUrlMap;
	public GridAdapter(Context context, int[] data, String module) {
		this.data = data;
		this.mContext = context;
		this.mModule = module;
		this.layoutInflater = LayoutInflater.from(context);
		mUrlMap = MyUtils.getUrlMap();
	}


	public void setDownPos(int pos) {
		if (this.downButtonShowPos == pos || hashMapPos.containsKey(pos) || hashMapPos.size() >= 1){
			this.downButtonShowPos = -1;
			if(hashMapPos.size() >= 1){
				Toast.makeText(mContext,
						mContext.getResources().getString(R.string.notstartdownload),
						Toast.LENGTH_SHORT).show();
			}
		}else 
			this.downButtonShowPos = pos;
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		ImageView imageView;
		ImageView imageViewNotDown;
		ProgressBar pb;
		ProgressBar pb1;
		Button buttonDown;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.lay1_item,
					null);
			mViewholder = new ViewHolder();
			mViewholder.imageView = (ImageView) convertView
					.findViewById(R.id.grid_imageView);
			mViewholder.imageViewNotDown = (ImageView) convertView
					.findViewById(R.id.imagedownload);
			mViewholder.imageViewNotDown.setAlpha(111);
			mViewholder.buttonDown = (Button) convertView
					.findViewById(R.id.buttondownload);
			mViewholder.pb = (ProgressBar) convertView.findViewById(R.id.pb);
			mViewholder.pb1 = (ProgressBar) convertView.findViewById(R.id.pb1);
			convertView.setTag(mViewholder);
		} else {
			mViewholder = (ViewHolder) convertView.getTag();
		}

		int beauty = data[position];
		mViewholder.imageView.setImageResource(beauty);
		mViewholder.imageViewNotDown.setImageResource(R.drawable.tem);
		mViewholder.imageViewNotDown.setVisibility(View.VISIBLE);
		mViewholder.pb1.setVisibility(View.GONE);
		mViewholder.pb.setVisibility(View.GONE);
		
		if (downButtonShowPos == position){
			mViewholder.buttonDown.setVisibility(View.VISIBLE);
		}else{
			mViewholder.buttonDown.setVisibility(View.GONE);
		}
		if (hashMapPos.containsKey(position)
				&& hashMapFileSize.containsKey(position)) {
			int size = hashMapPos.get(position);
			int total = hashMapFileSize.get(position);
			mViewholder.pb.setVisibility(View.VISIBLE);
			mViewholder.pb.setMax(total);
			mViewholder.pb.setProgress(size);
			if (size == hashMapFileSize.get(position)) {
				hashMapPos.remove(position);
				hashMapFileSize.remove(position);
				mViewholder.pb1.setVisibility(View.VISIBLE);
				mViewholder.pb.setVisibility(View.GONE);
			}
		}
		//Preferences.putGradeFlag(false, position + 1);
		if (new File(MyUtils.getDBFilePathBYGrade(mModule, position + 1))
				.exists()) {
			mViewholder.imageViewNotDown.setVisibility(View.GONE);
			mViewholder.pb.setVisibility(View.GONE);
			mViewholder.pb1.setVisibility(View.GONE);
			Preferences.putGradeFlag(true, position + 1);
		}

		mViewholder.buttonDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(check()){
					v.setVisibility(View.GONE);
					hashMapPos.put(downButtonShowPos, 0);
					startDownLoad(mUrlMap.get(mModule).get(downButtonShowPos),Constants.DATA_FOLDER,
							downButtonShowPos);
					GridAdapter.this.notifyDataSetChanged();
					downButtonShowPos = -1;
				}
				
			}

		});
		return convertView;
	}

	static class HandlerExtension extends Handler {

		WeakReference<GridAdapter> outerClass;

		HandlerExtension(GridAdapter activity) {
			outerClass = new WeakReference<GridAdapter>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			GridAdapter theClass = outerClass.get();
			switch (msg.what) {
			case MESSAGE_FLAG_NORESPONSE:
				theClass.hashMapPos.remove(msg.arg1);
				Toast.makeText(
						theClass.mContext,
						theClass.mContext.getResources().getString(
								R.string.noresponse), Toast.LENGTH_LONG).show();
				break;
			case MESSAGE_FLAG_SUCCESS:
				int position = msg.arg1;
				int size = msg.arg2;
				if (size == theClass.hashMapFileSize.get(position)) {

					String path = MyUtils.getFilePathByGrade(theClass.mModule,
							position + 1);
					String fileName = Constants.DATA_FOLDER + msg.obj;
					Log.e("filepath", fileName);
					Toast.makeText(
							theClass.mContext,
							theClass.mContext.getResources().getString(
									R.string.downloadstep2), Toast.LENGTH_SHORT)
							.show();
					theClass.unzip(path, fileName);
					if (Preferences.getGradeFlag(position+1)) {
						AppConnect.getInstance(theClass.mContext).spendPoints(
								100, theClass);
					}
				}
				theClass.hashMapPos.put(position, size);
				theClass.notifyDataSetChanged();
				break;
			case MESSAGE_FLAG_UNZIPSUCCESS:
				theClass.notifyDataSetChanged();
				Toast.makeText(
						theClass.mContext,
						theClass.mContext.getResources().getString(
								R.string.downloadstep3), Toast.LENGTH_SHORT)
						.show();

				break;
			}
		}

	}

	/**
	 * 下载文件
	 * 
	 * @param path下载路径
	 * @param saveDir文件保存目录
	 */
	public void download(final String path, final File saveDir, final int pos) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					FileDownloader downer = new FileDownloader(mContext, path,
							saveDir, 3);
					int fileSize = downer.getFileSize();
					hashMapFileSize.put(pos, fileSize);
					downer.download(new DownloadProgressListener() {
						@Override
						public void onDownloadSize(int size) {
							Message msg = new Message();
							msg.what = MESSAGE_FLAG_SUCCESS;
							msg.arg1 = pos;
							msg.arg2 = size;
							msg.obj = path.substring(path.lastIndexOf("/") + 1);
							handler.sendMessage(msg);// 发送消息
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.arg1 = pos;
					msg.what = MESSAGE_FLAG_NORESPONSE;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	private void unzip(final String localPath, final String localFilePath) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					File unzipDir = new File(localPath);
					ZipUtil.unzipAll(new File(localFilePath), unzipDir);
					handler.sendEmptyMessage(MESSAGE_FLAG_UNZIPSUCCESS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void startDownLoad(String url, String localPath, int pos) {

		download(url, new File(localPath), pos);
		
		Toast.makeText(mContext,
				mContext.getResources().getString(R.string.downloadstep1),
				Toast.LENGTH_LONG).show();
	}

	private boolean check(){
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.nosdcard),
					Toast.LENGTH_LONG).show();
			return false;
		}

		if (!MyUtils.isConnect(mContext)) {
			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.downfail),
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	@Override
	public void getUpdatePoints(String arg0, int arg1) {
		Preferences.putCoinCount(arg1);
		Log.e("金币余额", arg1+"");
	}

	@Override
	public void getUpdatePointsFailed(String arg0) {

	}

}
