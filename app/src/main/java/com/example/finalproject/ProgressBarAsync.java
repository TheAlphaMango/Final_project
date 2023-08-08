package com.example.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.FileInputStream;
import java.io.IOException;

public class ProgressBarAsync extends AsyncTask<String, Integer, Void> {

    /**
     * The context to use, the progress bar and imageview to update,
     * and the dbhelper to query the data
     */
    Context ctx;
    ProgressBar progressBar;
    ImageDbHelper dbHelper;
    ImageView randomImage;

    /**
     * The picture to load during progress updates
     */
    Bitmap pic;

    /**
     * Initialize the variables
     */
    public ProgressBarAsync(Context ctx, ProgressBar progressBar, ImageDbHelper dbHelper, ImageView randomImage) {
        this.ctx = ctx;
        this.progressBar = progressBar;
        this.dbHelper = dbHelper;
        this.randomImage = randomImage;
    }

    /**
     * Gets a random date from the db, then loads the image from that file,
     * then progresses the bar
     */
    @Override
    protected Void doInBackground(String... strings) {
        while (true) {

            /**
             * Get random date and load the picture
             */
            String date = dbHelper.getRandomDate();

            try {
                FileInputStream fileInputStream = ctx.openFileInput(date);
                pic = BitmapFactory.decodeStream(fileInputStream);
                fileInputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            /**
             * Update the progress bar
             */
            for (int i = 0; i < 100; i++) {
                if (isCancelled()) {
                    break;
                }

                try {
                    publishProgress(i);
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (isCancelled()) {
                break;
            }
        }

        return null;
    }

    /**
     * Updates the image and progress bar
     */
    protected void onProgressUpdate(Integer... values) {
        randomImage.setImageBitmap(pic);
        progressBar.setProgress(values[0]);
        super.onProgressUpdate(values);
    }
}
