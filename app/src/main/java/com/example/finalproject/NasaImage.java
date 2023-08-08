package com.example.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

class NasaImage extends AsyncTask<String, Integer, Void> {

    /**
     * The URL to request the image from
     */
    private String NASA_URL = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=";

    /**
     * The imageview, context, and activity to use,
     * and the date to query the URL
     */
    private ImageView nasaImg;
    private String date;
    private Context ctx;
    private Search activity;

    /**
     * The JSON info associated with the date
     */
    private JSONObject NasaInfo;

    /**
     * The picture found in the JSON info
     */
    private Bitmap pic;

    /**
     * Initializes the variables
     */
    NasaImage(Context ctx, ImageView nasaImg, String date, Search activity) {
        this.ctx = ctx;
        this.nasaImg = nasaImg;
        this.date = date;
        this.activity = activity;
    }

    /**
     * Query the API in its own thread to get the image info and load it
     */
    @Override
    protected Void doInBackground(String... strings) {
        /**
         * Initialize variables for url connection
         */
        URL url;
        HttpURLConnection urlConnection = null;
        InputStream response = null;
        try {

            /**
             * Check if the file for that date exists, and get the JSON object with that date
             */
            File file = new File(ctx.getFilesDir(), date);

            url = new URL(NASA_URL + date);
            urlConnection = (HttpURLConnection) url.openConnection();
            response = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            String result = sb.toString();

            NasaInfo = new JSONObject(result);

            /**
             * If the file doesn't exist, save the image from the URL to a file
             * so it can load it afterwards
             */
            if (!file.exists()) {
                String imgurl = NasaInfo.getString("url");
                URL img_url = new URL(imgurl);
                InputStream imgInputStream = img_url.openStream();
                FileOutputStream imgOutputStream = ctx.openFileOutput(date, Context.MODE_PRIVATE);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = imgInputStream.read(buffer)) != -1) {
                    imgOutputStream.write(buffer, 0, bytesRead);
                }

                imgOutputStream.close();
                imgInputStream.close();
            }

            /**
             * Load the picture from the file
             */
            FileInputStream fileInputStream = ctx.openFileInput(date);
            pic = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * Update the picture when it's done loading, and send back the associated image info
     */
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        nasaImg.setImageBitmap(pic);

        activity.setImageInfo(NasaInfo);
    }
}