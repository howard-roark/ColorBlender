package com.cs390h.ColorBlender;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

/**
 * Created by matthewmcguire on 10/11/14.
 */
public class ColorBlender extends Activity {

    /* Request code for communicating with ColorPicker via Intents */
    private static final int PICK_COLOR_REN_CODE = 1;

    /**
     * Overriden onCreate to start app.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    /**
     * Handle when button one is clicked, handled in main.xml with onClick.
     * Intent will launch the color picker and disable the button from
     * further use.
     *
     * @param view
     */
    public void addColorButtonClicked(View view) {
        /* Create Intent to open ColorPicker app */
        Intent i = new Intent("android.intent.action.MAIN");
        i.setComponent(ComponentName.unflattenFromString
                ("com.testingtechs.ColorPicker"));
        i.addCategory("android.intent.category.LAUNCHER");
        startActivityForResult(i, PICK_COLOR_REN_CODE);

        //TODO send color to the appropriate views background via setColor()

        view.setEnabled(false);
    }

    private void setColor(View view, Color color) {

    }
}
