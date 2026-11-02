package org.nyanya.android.traditionalt9;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Colour theme of the suggestions bar. Stored in SharedPreferences (not in the
 * T9 database) so that no database upgrade is needed.
 */
public final class CandidateTheme {
	public static final int CLASSIC = 0;
	public static final int GRAY_BLUE = 1;
	public static final int GOLD_BLACK = 2;

	private static final String PREFS = "ui_prefs";
	private static final String KEY = "candidate_theme";

	private static final String KEY_FONT = "candidate_font_size";
	// 0 small, 1 normal (default - same size as before), 2 large, 3 extra large
	private static final float[] FONT_SCALES = { 0.8f, 1.0f, 1.25f, 1.5f };
	public static final int FONT_DEFAULT = 1;

	private CandidateTheme() { }

	public static int getFontIndex(Context context) {
		try {
			int v = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_FONT, FONT_DEFAULT);
			return (v >= 0 && v < FONT_SCALES.length) ? v : FONT_DEFAULT;
		} catch (Exception e) {
			return FONT_DEFAULT;
		}
	}

	public static float getFontScale(Context context) {
		return FONT_SCALES[getFontIndex(context)];
	}

	public static void setFontIndex(Context context, int index) {
		SharedPreferences.Editor ed = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit();
		ed.putInt(KEY_FONT, index);
		ed.apply();
	}

	public static int get(Context context) {
		try {
			int v = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY, CLASSIC);
			return (v >= CLASSIC && v <= GOLD_BLACK) ? v : CLASSIC;
		} catch (Exception e) {
			return CLASSIC;
		}
	}

	public static void set(Context context, int theme) {
		SharedPreferences.Editor ed = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit();
		ed.putInt(KEY, theme);
		ed.apply();
	}
}
