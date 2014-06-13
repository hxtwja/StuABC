package com.huang.test.constants;

import android.content.Context;
import android.content.SharedPreferences;

import com.huang.test.constants.Constants;

public class Preferences {
	private static SharedPreferences prefs;
	public static final String PREF_COIN_COUNT = "coincount";
	public static final String PREF_GRADE1_FLAG = "grade1";
	public static final String PREF_GRADE2_FLAG = "grade2";
	public static final String PREF_GRADE3_FLAG = "grade3";
	public static final String PREF_GRADE4_FLAG = "grade4";
	public static final String PREF_GRADE5_FLAG = "grade5";
	public static final String PREF_GRADE6_FLAG = "grade6";

	public static void init(Context context) {
		prefs = context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
	}

	public static long getCoinCount() {
		return prefs.getLong(Preferences.PREF_COIN_COUNT, 0);
	}
	public static void putCoinCount(long count) {
		put(PREF_COIN_COUNT, count);
	}
	public static boolean getGradeFlag(int greade) {
		switch (greade) {
		case 1:
			return prefs.getBoolean(Preferences.PREF_GRADE1_FLAG, false);
		case 2:
			return prefs.getBoolean(Preferences.PREF_GRADE2_FLAG, false);
		case 3:
			return prefs.getBoolean(Preferences.PREF_GRADE3_FLAG, false);
		case 4:
			return prefs.getBoolean(Preferences.PREF_GRADE4_FLAG, false);
		case 5:
			return prefs.getBoolean(Preferences.PREF_GRADE5_FLAG, false);
		case 6:
			return prefs.getBoolean(Preferences.PREF_GRADE6_FLAG, false);
		}
		return false;
	}

	public static void putGradeFlag(boolean flag, int greade) {
		switch (greade) {
		case 1:
			put(PREF_GRADE1_FLAG, flag);
			break;
		case 2:
			put(PREF_GRADE2_FLAG, flag);
			break;
		case 3:
			put(PREF_GRADE3_FLAG, flag);
			break;
		case 4:
			put(PREF_GRADE4_FLAG, flag);
			break;
		case 5:
			put(PREF_GRADE5_FLAG, flag);
			break;
		case 6:
			put(PREF_GRADE6_FLAG, flag);
			break;
		}

	}

	private static void put(String name, Object value) {
		SharedPreferences.Editor editor = prefs.edit();
		if (value.getClass() == Boolean.class) {
			editor.putBoolean(name, (Boolean) value);
		}
		if (value.getClass() == String.class) {
			editor.putString(name, (String) value);
		}
		if (value.getClass() == Integer.class) {
			editor.putInt(name, ((Integer) value).intValue());
		}
		if (value.getClass() == Long.class) {
			editor.putLong(name, ((Long) value).longValue());
		}

		editor.commit();

	}
}
