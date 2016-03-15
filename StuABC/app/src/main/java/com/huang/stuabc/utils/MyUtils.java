package com.huang.stuabc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.huang.stuabc.constants.Constants;

public class MyUtils {
	public static final String AUTHORITYURI = "content://com.aragon.providers.EContentProvider";

	public static String getDBFilePathBYGrade(String module, int grade) {

		return Constants.DATA_FOLDER + module + "/" + grade + "/" + module
				+ grade + ".db";
	}

	public static String getFilePathByGrade(String module, int grade) {

		return Constants.DATA_FOLDER + module + "/" + grade + "/";
	}

	public static String getBookInfo(String module, int grade) {
		String ch_module = "";
		if ("math".endsWith(module))
			ch_module = "数学";
		else if ("chinese".endsWith(module))
			ch_module = "语文";
		else if ("english".endsWith(module))
			ch_module = "英语";
		switch (grade) {
		case 1:
			ch_module += "一年级";
			break;
		case 2:
			ch_module += "二年级";
			break;
		case 3:
			ch_module += "三年级";
			break;
		case 4:
			ch_module += "四年级";
			break;
		case 5:
			ch_module += "五年级";
			break;
		case 6:
			ch_module += "六年级";
			break;
		}
		return ch_module;
	}

	public static String getTemplateFile(String templatePath)
			throws IOException {
		BufferedReader bufread;
		String readStr = "";
		String read;
		FileReader fileread;
		File filename = new File(templatePath);
		if (filename.exists()) {
			try {
				fileread = new FileReader(filename);
				bufread = new BufferedReader(fileread);
				while ((read = bufread.readLine()) != null) {
					readStr = readStr + "\n" + read;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return readStr;
	}

	public static void writeToFile(String content, String FilePath) {
		try {
			File file = new File(FilePath);
			File parentFile = file.getParentFile();
			if (!parentFile.exists())
				parentFile.mkdirs();
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fop = new FileOutputStream(file);
			fop.write(content.getBytes());
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 是否连接到网络
	 * 
	 * @param context
	 *            上下文
	 * @return boolean
	 */
	public static boolean isConnect(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static String getModuleCHName(String en) {
		if ("math".endsWith(en))
			return "数学";
		else if ("chinese".endsWith(en))
			return "语文";
		else if ("english".endsWith(en))
			return "英语";
		return "";
	}

	public static String getGradeCHName(int grade) {
		String gradeName = "";
		switch (grade) {
		case 1:
			gradeName = "一年级";
			break;
		case 2:
			gradeName = "二年级";
			break;
		case 3:
			gradeName = "三年级";
			break;
		case 4:
			gradeName = "四年级";
			break;
		case 5:
			gradeName = "五年级";
			break;
		case 6:
			gradeName = "六年级";
			break;
		}
		return gradeName;
	}
	public static HashMap<String,ArrayList<String>> getUrlMap(){
		HashMap<String,ArrayList<String>> mp = new HashMap<String,ArrayList<String>>();
		ArrayList<String> mathList = new ArrayList<String>();
		mathList.add(Constants.REMOTEFILEPATH_MATH1);
		mathList.add(Constants.REMOTEFILEPATH_MATH2);
		mathList.add(Constants.REMOTEFILEPATH_MATH3);
		mathList.add(Constants.REMOTEFILEPATH_MATH4);
		mathList.add(Constants.REMOTEFILEPATH_MATH5);
		mathList.add(Constants.REMOTEFILEPATH_MATH6);
		mp.put("math", mathList);
		ArrayList<String> chineseList = new ArrayList<String>();
		chineseList.add(Constants.REMOTEFILEPATH_CHINESE1);
		chineseList.add(Constants.REMOTEFILEPATH_CHINESE2);
		chineseList.add(Constants.REMOTEFILEPATH_CHINESE3);
		chineseList.add(Constants.REMOTEFILEPATH_CHINESE4);
		chineseList.add(Constants.REMOTEFILEPATH_CHINESE5);
		chineseList.add(Constants.REMOTEFILEPATH_CHINESE6);
		mp.put("chinese", chineseList);
		ArrayList<String> englishList = new ArrayList<String>();
		englishList.add("");
		englishList.add("");
		englishList.add(Constants.REMOTEFILEPATH_ENGLISH3);
		englishList.add(Constants.REMOTEFILEPATH_ENGLISH4);
		englishList.add(Constants.REMOTEFILEPATH_ENGLISH5);
		englishList.add(Constants.REMOTEFILEPATH_ENGLISH6);
		mp.put("english", englishList);
		return mp;
	}
}
