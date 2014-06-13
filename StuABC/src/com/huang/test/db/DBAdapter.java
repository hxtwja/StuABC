package com.huang.test.db;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.huang.test.model.QuestionModel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
	public static final String LOG_TAG = "DBAdapter";
	public final static String TABLENAME = "test";
	public static SQLiteDatabase db;
	private String dbFilePath;

	public DBAdapter(String dbPath) {
		this.dbFilePath = dbPath;
	}

	private boolean checkDataBase() {

		try {
			File f = new File(dbFilePath);
			if (f.exists()) {
				int fileSize = new FileInputStream(f).available();
				if (fileSize > 0) {
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

	private void openDatabase() {
		try {

			if (checkDataBase()) {

				db = SQLiteDatabase.openDatabase(dbFilePath, null,
						SQLiteDatabase.NO_LOCALIZED_COLLATORS);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/* 关闭数据库 */
	public void closeDatabase() {

		db.close();

	}


	public ArrayList<QuestionModel> getTestByKnowledge(String key3,
			String key4) {
		openDatabase();
		ArrayList<QuestionModel> modules = new ArrayList<QuestionModel>();
		String sql = "";
		Cursor c = null;
		try {

			if ("".equals(key4)) {
				sql = "select  *  from  test where key3 = ? order by RANDOM() limit 10";
				c = db.rawQuery(sql, new String[] { key3 });
			} else {
				sql = "select  *  from  test where key3 = ? and key4 = ? order by RANDOM() limit 10";
				c = db.rawQuery(sql, new String[] { key3, key4 });
			}
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						QuestionModel module = new QuestionModel();
						module.ID = c.getInt(c.getColumnIndex("_id"));
						module.JIEXI = c.getString(c.getColumnIndex("jiexi"));
						module.KEY3 = c.getString(c.getColumnIndex("key3"));
						module.KEY4 = c.getString(c.getColumnIndex("key4"));
						module.TIXING = c.getString(c.getColumnIndex("tixing"));
						module.DIFFICULTY = c.getString(c
								.getColumnIndex("difficulty"));
						module.LEVEL = c.getString(c.getColumnIndex("level"));
						module.NIANJI = c.getString(c.getColumnIndex("nianji"));
						module.XUEKE = c.getString(c.getColumnIndex("xueke"));
						module.DAAN = c.getString(c.getColumnIndex("daan"));
						module.TIMU = c.getString(c.getColumnIndex("timu"));
						module.ZITIMU = c.getString(c.getColumnIndex("zitimu"));
						module.ZITIHAO = c.getString(
								c.getColumnIndex("zitihao")).trim();
						if (Integer.valueOf(module.ZITIHAO) > 0) {
							module.TIMU += "<br>" + module.ZITIMU;
						}
						if (c.getInt(c.getColumnIndex("tixing")) == 0) {
							String A = c.getString(c
									.getColumnIndexOrThrow("itemA"));
							String B = c.getString(c
									.getColumnIndexOrThrow("itemB"));
							String C = c.getString(c
									.getColumnIndexOrThrow("itemC"));
							String D = c.getString(c
									.getColumnIndexOrThrow("itemD"));
							String before = "<ol class=\"options\">";
							String a1 = "<li>" + "A:" + A + "</li>";
							String b1 = "<li>" + "B:" + B + "</li>";
							String c1 = "<li>" + "C:" + C + "</li>";
							String d1 = "<li>" + "D:" + D + "</li>";
							String after = "</ol>";
							String label = "";
							label = before + a1 + b1 + after;
							if (!"".equals(C)) {
								label = before + a1 + b1 + c1 + after;
							}
							if (!"".equals(D)) {
								label = before + a1 + b1 + c1 + d1 + after;
							}
							module.TIMU += label;
						}
						modules.add(module);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!c.isClosed())
				c.close();
		}
		closeDatabase();
		return modules;
	}

	public HashMap<String, ArrayList<String>> getMathKnowledge() {
		HashMap<String, ArrayList<String>> mathMap = new HashMap<String, ArrayList<String>>();
		openDatabase();
		Cursor c = null;
		try {
			String sql = "select key3,key4 from test order by _id;";
			c = db.rawQuery(sql, null);
			int count = c.getCount();
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						String key3 = c.getString(0);
						String key4 = c.getString(1);
						if (!mathMap.containsKey(key3)) {
							ArrayList<String> itemsList = new ArrayList<String>();
							mathMap.put(key3, itemsList);
						}
						if (!mathMap.get(key3).contains(key4)) {
							mathMap.get(key3).add(key4);
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		closeDatabase();
		return mathMap;
	}


	public void closeDB() {

		if (db != null) {
			db.close();
		}
	}
}
