package org.nyanya.android.traditionalt9;

import android.content.Context;
import android.content.SharedPreferences;

import org.nyanya.android.traditionalt9.LangHelper.LANGUAGE;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * Remembers which built-in dictionaries were loaded into the database, by the file
 * size listed in dict.properties. A dictionary is "loaded" only if the stored size is
 * the same as the bundled one, so an updated dictionary (or a newly enabled language)
 * is detected, while a new app version with the same dictionaries is not.
 */
public final class DictState {
	private static final String PREFS = "dict_state";

	private DictState() { }

	private static String dictFile(LANGUAGE lang) {
		return String.format("%s-utf8.txt", lang.name().toLowerCase(Locale.ENGLISH));
	}

	private static String bundledSize(Context context, LANGUAGE lang) {
		InputStream input = null;
		try {
			Properties props = new Properties();
			input = context.getAssets().open("dict.properties");
			props.load(input);
			return props.getProperty("size." + dictFile(lang));
		} catch (IOException e) {
			return null;
		} finally {
			if (input != null) {
				try { input.close(); } catch (IOException ignored) { }
			}
		}
	}

	public static boolean isLoaded(Context context, LANGUAGE lang) {
		String size = bundledSize(context, lang);
		if (size == null) {
			return true; // no bundled dictionary for this language, nothing to load
		}
		SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		return size.equals(sp.getString("loaded_" + lang.name(), null));
	}

	public static boolean allLoaded(Context context, LANGUAGE[] langs) {
		for (LANGUAGE lang : langs) {
			if (!isLoaded(context, lang)) {
				return false;
			}
		}
		return true;
	}

	public static void markLoaded(Context context, LANGUAGE[] langs) {
		SharedPreferences.Editor ed = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit();
		for (LANGUAGE lang : langs) {
			String size = bundledSize(context, lang);
			if (size != null) {
				ed.putString("loaded_" + lang.name(), size);
			}
		}
		ed.apply();
	}
}
