package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.FileInputStream;
import java.io.IOException;

public class RandomImages extends ToolbarFunctionality {
    /**
     * The progressbarasync class to take care of randomly loading images
     */
    ProgressBarAsync progress;


    /**
     * Loads the toolbar
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_images);
        setupToolbar();
    }

    /**
     * Restarts the random image loop when the activity is loaded
     */
    @Override
    protected void onResume() {
        super.onResume();

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        ImageView randomImg = (ImageView)findViewById(R.id.randomImg);
        ImageDbHelper dbHelper = ImageDbHelper.getInstance(this);

        progress = new ProgressBarAsync(this, progressBar, dbHelper, randomImg);
        progress.execute();
    }


    /**
     * Stops the random image loop when the activity is closed
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (progress != null) {
            progress.cancel(true);
        }
    }
}