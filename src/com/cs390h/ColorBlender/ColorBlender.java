package com.cs390h.ColorBlender;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created by matthewmcguire on 10/11/14.
 */
public class ColorBlender extends Activity {

    /* Request code for communicating with ColorPicker via Intents */
    private static final int PICK_COLOR_REQ_CODE = 1;

    /* Enable view background to be changed in onActivityResult */
    View view;

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
        Intent i = new Intent("com.testingtechs.GET_COLOR");
        startActivityForResult(i, PICK_COLOR_REQ_CODE);

        this.view = view;

        /* Disable button so chosen color cannot be overwritten */
        view.setEnabled(false);
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        if ((requestCode == PICK_COLOR_REQ_CODE) && (resultCode == RESULT_OK)) {
            int[] rgbVals = data.getExtras().getIntArray("RGB");
            this.view.setBackgroundColor(Color.rgb(rgbVals[0], rgbVals[1],
                    rgbVals[2]));
        } else {
            Log.e("onActivityResult --> ", "Error recieving RGB vals from CP");
        }
    }
}
