![image](https://raw.githubusercontent.com/AshiVered/support-israel-banner/main/assets/support-israel-banner.jpg)

# QinBoard T9 keypad IME for Android.
(fork of https://github.com/Clam-/TraditionalT9/.)

This is a Input Method Editor for Android that implements predictive text using the hardware keypad on the device. **Useless without a hardware numerical keypad.**

This fork has been specially adapted for Qin devices, but it works for any device where the backspace key is in the "back" key.
Also, this fork is with a keymap similar to that of Nokia devices, and not like the original project - where it is a Sony Ericsson keymap.

See Wiki for more information&setup guide: [QinBoard T9 keypad IME for Android](https://github.com/AshiVered/QinBoard-T9/wiki)

# About this fork (YairDaniel11/QinBoard-T9)

This repository is a fork of [AshiVered/QinBoard-T9](https://github.com/AshiVered/QinBoard-T9) by A.I.V, with improvements mainly for Hebrew and everyday use on keypad phones. All credit for the original application goes to its developer. The license terms below apply to this fork as well, and the application name, icon and the "Developed By A.I.V" line in the settings are unchanged.

## What is new in this fork

Dictionaries and prediction
- Larger dictionaries: Hebrew grew from about 470 thousand to 684 thousand words, English from about 96 thousand to 200 thousand words.
- Suggestions are ranked by how common a word is, so useful words are offered first (for example key sequence 333 offers the Hebrew words "אבא" and "אגב" before rare ones).
- Data sources for words and frequencies: [wordfreq](https://github.com/rspeer/wordfreq), [FrequencyWords](https://github.com/hermitdave/FrequencyWords) (OpenSubtitles) and [hebrew-words-db](https://github.com/roni5604/hebrew-words-db). Please check the licenses of these projects.
- The default languages are Hebrew and English, the default mode is predictive, and the keyboard starts in Hebrew. Existing installs that still had the old defaults are migrated once.
- The app offers to load the dictionary on first run, when a built-in dictionary changes, or when a new language is enabled. The loading dialog shows real word counts (loaded / total).

Keys and language switching
- The hash key cycles Hebrew, English, numbers, and back to Hebrew.
- Suggestions are shown right to left, with the first one pinned to the right. DPAD left moves to the next suggestion and DPAD right to the previous one.
- Optional setting: a double press on the star key opens the special characters window (a single press switches case once, and long press is disabled).
- The OK (center) key is passed to the foreground app when there is no text being composed.
- Selecting "Add" in the suggestions opens the add word window instead of typing the label. A short message confirms that the word was added.

Look and feel
- Three color themes for the suggestions bar (classic white, gray and blue, gold and black), a font size setting (small, normal, large, extra large) and better text contrast.
- Redesigned settings, add word, setup and user guide windows, with text aligned to the right for Hebrew. The setup window only offers the step that is still needed.
- The GitHub item in the settings opens this repository and sits at the bottom of the list.

Build
- Built automatically by GitHub Actions (minimum Android 4.4). Two files are published for every build: a full version with all languages, and a lite version with Hebrew and English only (much smaller, easier to transfer over Bluetooth).
- The package name `aiv.ashivered.qinboard.t9.hebrow` is unchanged. The files are signed with a fixed debug key of this fork, so updates of this fork install over each other. If the original app is installed, uninstall it first.

## Download

Latest build: [releases page](https://github.com/YairDaniel11/QinBoard-T9/releases/latest)
- `QinBoard-T9-vX.Y.apk` - full version, all languages.
- `QinBoard-T9-lite-vX.Y.apk` - lite version, Hebrew and English only.

Original project releases: [AshiVered/QinBoard-T9 releases](https://github.com/AshiVered/QinBoard-T9/releases)

## בעברית

פורק של QinBoard T9 של A.I.V, עם שיפורים בעיקר לעברית ולשימוש יומיומי במכשירי מקשים. הקרדיט על האפליקציה המקורית שייך למפתח שלה, ותנאי הרישיון שלמטה חלים גם על הפורק.

מה חדש:
- מילון עברית של 684 אלף מילים ומילון אנגלית של 200 אלף מילים, עם הצעות המדורגות לפי נפיצות.
- ברירת מחדל: עברית ואנגלית, מצב חיזוי, והמקלדת מתחילה בעברית. טעינת המילון מוצעת אוטומטית, והסרגל מציג מספרי מילים אמיתיים.
- מקש סולמית מחליף עברית, אנגלית, מספרים. ההצעות מוצגות מימין לשמאל, והחץ שמאלה עובר להצעה הבאה.
- אפשרות לפתוח סימנים מיוחדים בלחיצה כפולה על כוכבית.
- מקש האישור עובר לאפליקציה כשאין טקסט מוקלד, והפריט "הוסף" פותח את חלון הוספת המילה.
- שלוש ערכות צבעים להצעות, גודל פונט לבחירה, וחלונות מעוצבים מחדש.
- גרסה מלאה וגרסה קלה (עברית ואנגלית בלבד, כ-9MB), הנבנות אוטומטית ב-GitHub Actions.

הורדה: [עמוד השחרורים](https://github.com/YairDaniel11/QinBoard-T9/releases/latest). אם מותקנת הגרסה המקורית יש להסירה לפני ההתקנה.

# To-do list
1. Fix [this issue](https://github.com/sspanak/tt9/issues/182) in QinBoard T9.
2. ~~add symbol & smiley by long click on star~~ DONE! V3 and higher.
3. ~~Add help screen~~ DONE! V5.2-beta and higher
4. ~~Add setup option (to make the keyboard easier to operate and set it to a default input method)~~ DONE! V6 and higher.
5. ~~Setting classic typing (without text prediction) by default, so that it is possible to use the keyboard even before loading the dictionary.~~ DONE! V5.1-beta and higher
6. Add more languages.
7. A more modern UI. - *Some of the changes have already been made in V5.1-beta*
8. ~~navigate through the words using the dial key (KEYCODE_CALL).~~ DONE! V4 and higher. (in v7 i replace the action of call key. (see item 11 in this list)).
9. Add option to new line in Qin1s+.
10. Fix load user option in Android 12 and higher.
11. ~~Change input modes (Dictionary, text, numbers) by one click~~ DONE! V7 and higher. *by preesing dial key (KEYCODE_CALL)*
12. Fix some scroll up/scroll down issues (for example, in Tfilon app).
# Download
QinBoard-T9 (original fork by A.I.V):
from the [releases page](https://github.com/AshiVered/QinBoard-T9/releases).
QinBoard-T9 (this fork, with the improvements above):
from the [releases page](https://github.com/YairDaniel11/QinBoard-T9/releases/latest).
TraditionalT9 (orginal project):
available on Google Play: [Traditional T9 Keypad IME](https://play.google.com/store/apps/details?id=org.nyanya.android.traditionalt9)
# License
Do what you want, but keep the following in mind:
1. **Commercial use is prohibited.** If you want to make commercial use, contact me.
2. The application icon.
3. Name of the application (QinBoard T9).
4. "Developed By A.I.V" in the application settings.
5. Do not change the terms of the license

# Support me
I develop this project voluntarily, to benefit the community. But it's not an easy job with a lot of investment...
If you want to support me, [buy me a coffee ☕](https://www.ko-fi.com/ashivered)



I LOVE ISRAEL.
Enjoy!


