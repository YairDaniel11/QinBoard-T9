package org.nyanya.android.traditionalt9;

import android.content.Context;

/** Small UI/input preferences kept in SharedPreferences (no database upgrade needed). */
public final class InputPrefs {
	private static final String PREFS = "ui_prefs";
	private static final String KEY_STAR_DOUBLE = "star_double_press";

	private InputPrefs() { }

	/** true: double press on '*' opens the special characters window (and long press is disabled). */
	public static boolean isStarDouble(Context context) {
		try {
			return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY_STAR_DOUBLE, false);
		} catch (Exception e) {
			return false;
		}
	}

	public static void setStarDouble(Context context, boolean value) {
		context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putBoolean(KEY_STAR_DOUBLE, value).apply();
	}
}
