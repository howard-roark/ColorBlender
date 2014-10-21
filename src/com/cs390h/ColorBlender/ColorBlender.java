package com.cs390h.ColorBlender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
        SeekBar seekBar = (SeekBar) findViewById(R.id.blendBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setMax(100);
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

            //Set the blended view bg color same as color 1, seek bar will be 0
            findViewById(R.id.blendView).setBackgroundColor
                    (Color.rgb(rgbVals[0], rgbVals[1], rgbVals[2]));
        } else if (view.getId() == R.id.addColorTwo) {
            bgView = findViewById(R.id.colorViewTwo);
        } else {
            Log.e("setBgColor", "Could not set background view properly");
        }
        bgView.setBackgroundColor(Color.rgb(rgbVals[0], rgbVals[1],
                rgbVals[2]));
    }

    /**
     * Overriden method from SeekBar.OnSeekBarChangeListener interface.
     * Not Used
     *
     * @param seekBar
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    /**
     * Overriden method from SeekBar.OnSeekBarChangeListener interface.
     * Not Used
     *
     * @param seekBar
     */
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
        blendColors(progress);
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
        int blendedColorId = ((colorOneId * progress) +
                        (colorTwoId * (100 - progress)));
        if (progress == 0) {
            blendedView.setBackgroundColor(colorOneId);
        } else if (progress == 100) {
            blendedView.setBackgroundColor(colorTwoId);
        } else {
            blendedView.setBackgroundColor(blendedColorId);
        }
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

        hexView.setText("Hexadecimal Value: " + Integer.toHexString(colorId));
        rgbView.setText("RGB Value: " + colorId);
    }
}