package org.nyanya.android.traditionalt9;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class help extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(format(readHelp()));
    }

    private String readHelp() {
        try {
            String file = getResources().getString(R.string.help);
            InputStream in = getAssets().open(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int r;
            while ((r = in.read(buf)) > 0) {
                out.write(buf, 0, r);
            }
            in.close();
            return out.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    // Section titles ("| title |") become large cyan headings, lines that end with ":" are bold,
    // "* " items become bullets, and decorative dash lines are dropped.
    private CharSequence format(String text) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        String[] lines = text.replace("\r", "").split("\n");
        boolean first = true;
        for (String raw : lines) {
            String line = raw.trim();
            if (line.length() > 0 && line.replace("-", "").trim().length() == 0) {
                continue; // dash line
            }
            if (!first) {
                sb.append('\n');
            }
            first = false;
            int start = sb.length();
            if (line.length() > 2 && line.startsWith("|") && line.endsWith("|")) {
                String title = line.substring(1, line.length() - 1).trim();
                if (start > 0) {
                    sb.append('\n');
                    start = sb.length();
                }
                sb.append(title);
                sb.setSpan(new ForegroundColorSpan(Color.parseColor("#4FD8EA")), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.setSpan(new RelativeSizeSpan(1.25f), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                continue;
            }
            if (line.startsWith("* ")) {
                line = "\u2022 " + line.substring(2);
            }
            sb.append(line);
            if (line.endsWith(":") && line.length() > 1) {
                sb.setSpan(new StyleSpan(Typeface.BOLD), start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return sb;
    }
}
