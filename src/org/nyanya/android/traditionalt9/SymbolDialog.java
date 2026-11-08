package org.nyanya.android.traditionalt9;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * One window with two screens: symbols (punctuation, math, currency...) and icons (emoticons
 * and emoji). Switch between the screens with the arrow keys. Ten entries per page; the ones
 * the user picks most often are moved to the front of each screen.
 */
public class SymbolDialog extends AbsSymDialog {

	private static final String[] SYMBOLS = {
		// page 1-3: everyday punctuation and typing signs
		".", ",", "?", "!", "'", "\"", ":", ";", "-", "_",
		"/", "\\", "*", "+", "=", "(", ")", "[", "]", "{",
		"}", "<", ">", "@", "#", "$", "%", "&", "^", "|",
		"~", "`", "₪", "€", "£", "¥", "¢", "°", "±", "×",
		// page 5+: math, typography, quotes, arrows
		"÷", "²", "³", "½", "¼", "¾", "≠", "≈", "≤", "≥",
		"∞", "√", "π", "µ", "‰", "§", "©", "®", "™", "¶",
		"•", "…", "–", "—", "«", "»", "‹", "›", "“", "”",
		"‘", "’", "„", "‚", "¡", "¿", "←", "→", "↑", "↓",
		"↔", "†", "‡", "¦", "¬", "✓", "✗", "★", "☆", "♥",
		"♪", "●", "○", "■", "□", "▲", "▼", "◀", "▶", "№"
	};

	private static final String[] ICONS = {
		// classic text emoticons
		":)", ":(", ";)", ":D", ":P", ":O", ":/", ":'(", "<3", "^_^",
		// faces
		"😀", "😁", "😂", "😃", "😄", "😅", "😆", "😉", "😊", "😋",
		"😎", "😍", "😘", "😗", "😚", "☺️", "😇", "😐", "😑", "😶",
		"😏", "😣", "😥", "😮", "😯", "😪", "😫", "😴", "😌", "😛",
		"😜", "😝", "😒", "😓", "😔", "😕", "😲", "😷", "😖", "😞",
		"😟", "😤", "😢", "😭", "😦", "😧", "😨", "😩", "😬", "😰",
		"😱", "😳", "😵", "😡", "😠", "😈", "👿", "💀", "👻", "🙈",
		// hands and people
		"👍", "👎", "👌", "✌️", "✊", "✋", "👊", "👏", "🙌", "🙏",
		"💪", "👋", "👆", "👇", "👈", "👉", "🙍", "💁", "🙇", "🏃",
		"👶", "👪", "💏", "💃", "👫", "👀", "👅", "💋", "💂", "💇",
		// hearts and symbols
		"❤️", "💔", "💕", "💖", "💗", "💙", "💚", "💛", "💜", "🔥",
		"⭐", "✨", "💯", "💤", "💣", "💥", "💦", "💩", "✔️", "❌",
		"✅", "⭕", "❗", "❓", "⚠️", "🚫", "🔔", "🔒", "🔑", "💡",
		"✡️", "🕎", "🕯️", "🎉", "🎁", "🎂", "🎈", "🎵", "🎶", "🎮",
		// nature and weather
		"🌞", "🌝", "🌎", "🌙", "☀️", "☁️", "☔", "⚡", "❄️", "🌈",
		"🌹", "🌻", "🌲", "🍀", "🐶", "🐱", "🐭", "🐰", "🐻", "🐼",
		"🐵", "🐔", "🐟", "🐢", "🐍", "🐴", "🐷", "🐮", "🐦", "🐝",
		// food and drink
		"🍎", "🍏", "🍐", "🍊", "🍋", "🍌", "🍉", "🍇", "🍓", "🍅",
		"🍕", "🍔", "🍟", "🍞", "🍰", "🍩", "🍦", "🍫", "☕", "🍷",
		"🍺", "🍻", "🍾", "🍹", "🍼", "🍚", "🍝", "🍗", "🍳", "🍯",
		// places, travel, objects
		"⚽", "🏀", "🏆", "🚗", "🚌", "🚕", "✈️", "🚂", "🚲", "🏠",
		"🏥", "🏫", "⛪", "🕓", "⏰", "⌛", "📞", "📱", "📵", "💻",
		"📖", "📚", "✉️", "📧", "💰", "💳", "📋", "📎", "✏️", "🔍",
		"🌎", "📷", "🎤", "🎧", "📺", "🔋", "🔧", "💊", "💉", "🎓"
	};

	private static final String PREFS = "qb_symbol_usage";

	private boolean icons = false;
	private String[] orderedSymbols = SYMBOLS;
	private String[] orderedIcons = ICONS;

	public SymbolDialog(Context c, View mv) {
		super(c, mv);
	}

	/** Show the window on the symbols screen or on the icons screen. */
	protected void showScreen(View anchor, boolean showIcons) {
		icons = showIcons;
		orderedSymbols = orderByUsage(SYMBOLS, "s:");
		orderedIcons = orderByUsage(ICONS, "i:");
		resetPage();
		doShow(anchor);
	}

	private String[] orderByUsage(String[] base, String prefix) {
		final SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		final String pre = prefix;
		List<String> list = new ArrayList<String>();
		Collections.addAll(list, base);
		// Collections.sort is stable: items with the same count keep the default order
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				return sp.getInt(pre + b, 0) - sp.getInt(pre + a, 0);
			}
		});
		return list.toArray(new String[list.size()]);
	}

	private String[] current() {
		return icons ? orderedIcons : orderedSymbols;
	}

	@Override
	protected void onSymbolChosen(String symbol) {
		try {
			SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
			String key = (icons ? "i:" : "s:") + symbol;
			sp.edit().putInt(key, sp.getInt(key, 0) + 1).commit();
		} catch (RuntimeException ignored) { }
	}

	// arrow keys switch between the symbols screen and the icons screen
	@Override
	protected boolean onSwitchKey() {
		icons = !icons;
		resetPage();
		refreshButtons();
		return true;
	}

	@Override
	String[] getContentDescription() {
		return null;
	}

	@Override
	protected String getSymbol(int index) {
		return current()[index];
	}

	@Override
	protected String getTitleText() {
		return context.getString(icons ? R.string.smiley_insert : R.string.symbol_insert) + " \u25C4\u25BA";
	}

	@Override
	protected int getSymbolSize() {
		return current().length;
	}

	@Override
	protected int getMaxPage() {
		return (int) Math.ceil(current().length / 10.0);
	}

}
