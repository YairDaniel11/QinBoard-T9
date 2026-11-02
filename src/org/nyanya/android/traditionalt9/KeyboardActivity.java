package org.nyanya.android.traditionalt9;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Small window that guides through the two steps: enabling the keyboard in the list of
 * input methods, and then choosing it as the default keyboard. Only the step that is still
 * needed is offered.
 */
public class KeyboardActivity extends Activity {

    private Button keyboardSettingsButton;
    private Button inputMethodSettingsButton;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        keyboardSettingsButton = (Button) findViewById(R.id.keyboard_settings_button);
        inputMethodSettingsButton = (Button) findViewById(R.id.input_method_settings_button);
        statusText = (TextView) findViewById(R.id.keyboard_status);

        // step 1: the list of input methods, to enable the keyboard
        keyboardSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));
            }
        });

        // step 2: the picker, to choose the keyboard that is used for typing
        inputMethodSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imeManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imeManager.showInputMethodPicker();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        boolean enabled = false;
        for (InputMethodInfo info : imm.getEnabledInputMethodList()) {
            if (getPackageName().equals(info.getPackageName())) {
                enabled = true;
                break;
            }
        }
        String current = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        boolean isDefault = current != null && current.startsWith(getPackageName() + "/");

        if (!enabled) {
            statusText.setText(R.string.keyboard_status_enable);
            keyboardSettingsButton.setVisibility(View.VISIBLE);
            inputMethodSettingsButton.setVisibility(View.GONE);
        } else if (!isDefault) {
            statusText.setText(R.string.keyboard_status_default);
            keyboardSettingsButton.setVisibility(View.GONE);
            inputMethodSettingsButton.setVisibility(View.VISIBLE);
        } else {
            statusText.setText(R.string.keyboard_status_done);
            keyboardSettingsButton.setVisibility(View.GONE);
            inputMethodSettingsButton.setVisibility(View.VISIBLE);
        }
    }
}
