package com.cs390h.ColorBlender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by matthewmcguire on 10/11/14.
 */
public class ColorBlender extends Activity
        implements SeekBar.OnSeekBarChangeListener {

    /* Request code for communicating with ColorPicker via Intents */
    private static final int PICK_COLOR_REQ_CODE = 1;

    /* Enable view background to be changed in onActivityResult */
    private View view;

    /* Bundle to save state of app */
    public final Bundle saveState = new Bundle();

    private int[] COLOR_ONE_BG = new int[3];
    private int[] COLOR_TWO_BG = new int[3];

    /* Tags for saving and retrieving state of app */
    private final String COLOR_ONE = "Color 1 BG";
    private final String COLOR_TWO = "Color 2 BG";
    private final String SAVE_STATE = "saveState";

    /**
     * Overriden onCreate to start app.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SeekBar seekBar = (SeekBar) findViewById(R.id.blendBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setMax(100);

        if (savedInstanceState != null) {
            Bundle bun = savedInstanceState.getBundle(SAVE_STATE);
            Log.d("BUN", " " + bun.containsKey(COLOR_ONE));
            try {
                setBgColor(findViewById(R.id.addColorOne),
                        bun.getIntArray(COLOR_ONE));
            } catch (NullPointerException npe) {
                Log.d("Null BackGround Colors",
                        "NPE when restoring background One");
            }

            try {
                setBgColor(findViewById(R.id.addColorTwo),
                        bun.getIntArray(COLOR_TWO));
            } catch (NullPointerException npe) {
                Log.d("Null BackGround Colors",
                        "NPE when restoring background Two");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //Save bg color to bundle
        saveState.putIntArray(COLOR_ONE, COLOR_ONE_BG);
        saveState.putIntArray(COLOR_TWO, COLOR_TWO_BG);

        savedInstanceState.putBundle(SAVE_STATE, saveState);
    }

    /**
     * Add reset button to the menu bar.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * When the reset button from the menu bar is selected reset the app.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.reset:
                COLOR_ONE_BG = null;
                COLOR_TWO_BG = null;
                onCreate(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        if ((requestCode == PICK_COLOR_REQ_CODE) && (resultCode == RESULT_OK)) {
            int[] rgbVals = data.getExtras().getIntArray("RGB");
            setBgColor(this.view, rgbVals);
        } else {
            Log.e("onActivityResult --> ", "Error receiving RGB vals from CP");
        }
    }

    /**
     * Will set the background color of the view holding the button that was
     * used to grab a new color from the ColorPicker app.
     *
     * @param view
     * @param rgbVals
     */
    private void setBgColor(View view, int[] rgbVals) {
        View bgView = findViewById(R.id.blankView);//Empty view for default
        if (view.getId() == R.id.addColorOne) {
            bgView = findViewById(R.id.colorOneView);
            COLOR_ONE_BG = rgbVals;
        } else if (view.getId() == R.id.addColorTwo) {
            bgView = findViewById(R.id.colorViewTwo);
            COLOR_TWO_BG = rgbVals;
        } else {
            Log.e("setBgColor", "Could not set background view properly");
        }
        bgView.setBackgroundColor(Color.rgb(rgbVals[0], rgbVals[1],
                rgbVals[2]));

        /* Disable button so chosen color cannot be overwritten */
        view.setEnabled(false);
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    /**
     * Listen for changes to the SeekBar and blend the two colors accordingly.
     *
     * @param seekBar
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        boolean buttonOneEnabled = findViewById(R.id.addColorOne).isEnabled();
        boolean buttonTwoEnabled = findViewById(R.id.addColorTwo).isEnabled();
        saveState.putInt("Bar Progress", progress);

        if (!buttonOneEnabled && !buttonTwoEnabled) {
            blendColors(progress);
        }

        Log.d("SEEK BAR -->", "Progress: " + progress +
                ". From User: " + fromUser);
    }

    /**
     * Return the color for the background of which ever color view is passed in
     * to be used in savedState Bundle and blending colors.
     *
     * @param view
     * @return
     */
    private int getColorId(View view) {
        ColorDrawable cd = (ColorDrawable) view.getBackground();
        return cd.getColor();
    }

    /**
     * Blend the colors based on the movement of the seekbar.
     *
     * @param progress
     */
    private void blendColors(int progress) {
        View blendedView = findViewById(R.id.blendView);
        View colorOne = findViewById(R.id.colorOneView);
        View colorTwo = findViewById(R.id.colorViewTwo);
        int colorOneId = getColorId(colorOne);
        int colorTwoId = getColorId(colorTwo);

//        //RGB for color one
//        int redOne = (colorOneId >> 16) & 0xFF;
//        int greenOne = (colorOneId >> 8) & 0xFF;
//        int blueOne = (colorOneId >> 0) & 0xFF;
//
//        //RGB for color two
//        int redTwo = (colorTwoId >> 16) & 0xFF;
//        int greenTwo = (colorTwoId >> 8) & 0xFF;
//        int blueTwo = (colorTwoId >> 0) & 0xFF;
//
//        //Blended RGB of two colors
//        int blendedRed = (redOne * progress + redTwo * (100 - progress)) /
//                (Math.max((redOne + redTwo), 1));
//        int blendedGreen = (greenOne * progress + greenTwo * (100 - progress)) /
//                (Math.max((greenOne + greenTwo), 1));
//        int blendedBlue = (blueOne * progress + blueTwo * (100 - progress)) /
//                (Math.max((blueOne + blueTwo), 1));
//
//        int blendedColorId = ((blendedRed << 16) & 0xFFFFFF) +
//                ((blendedGreen << 8) & 0xFFFFFF) +
//                ((blendedBlue << 0) & 0xFFFFFF);

        int blendedColorId = ((colorOneId * (100 - progress)) +
                (colorTwoId * progress));
        Log.d("Blended Color Id -->", "" + blendedColorId);
        blendedView.setBackgroundColor(blendedColorId);

        updateTextValues(blendedColorId);
        Log.d("Color One ID -->", "" + colorOneId +
                "Color Two Id -->: " + colorTwoId);
    }

    /**
     * Print the value of the new color in hex and rgb once the user stops
     * blending the colors.
     *
     * @param colorId
     */
    private void updateTextValues(int colorId) {
        TextView hexView = (TextView) findViewById(R.id.hexView);
        TextView rgbView = (TextView) findViewById(R.id.rgbView);

        hexView.setText("Hexadecimal Value: " + String.format("#%06X",
                (0xFFFFFF & colorId)));
        rgbView.setText("Red: " + ((colorId >> 16) & 0xFF) +
                " Green: " + ((colorId >> 8) & 0xFF) +
                " Blue: " + ((colorId >> 0) & 0xFF));
    }
}