package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

public class Search extends ToolbarFunctionality {

    /**
     * Textview to display the selected date
     */
    private TextView dateDisplay;

    /**
     * Image info for the currently searched image
     */
    private int year;
    private int month;
    private int day;
    private String copyright;
    private String description;
    private JSONObject imageInfo;
    private TextView url;
    private String hdurlLink;

    /**
     * Formats and updates the date text for the selected date
     */
    protected void updateDate() {
        String formattedMonth = String.format(Locale.US, "%02d", month);
        String formattedDay = String.format(Locale.US, "%02d", day);
        dateDisplay.setText(getResources().getString(R.string.date) + ": " + year + "-" + formattedMonth + "-" + formattedDay);
    }


    /**
     * Loads the activity and button functionality
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupToolbar();

        /**
         * Elements needed to modify
         */
        url = (TextView)findViewById(R.id.urlDisplay);
        TextView hdurl = (TextView)findViewById(R.id.hdurlDisplay);
        dateDisplay = (TextView)findViewById(R.id.dateDisplay);
        Button btnChangeDate = (Button)findViewById(R.id.btnChangeDate);
        Button btnLoad = (Button)findViewById(R.id.load);
        Button btnFavorite = (Button)findViewById(R.id.favorite);
        ImageView nasaImageView = (ImageView)findViewById(R.id.nasaImg);

        /**
         * Updates the selected day to the current day
         */
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        updateDate();

        /**
         * Sends the user to the browser for the HD image when clicked
         */
        hdurl.setOnClickListener((click) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(hdurlLink));
            startActivity(intent);
        });

        /**
         * Whenever a user selects a date, it updates the date text to display it
         */
        DatePickerDialog.OnDateSetListener dateSetListener = (view, newYear, newMonth, newDay) -> {
            year = newYear;
            month = newMonth + 1;
            day = newDay;
            updateDate();
        };

        /**
         * Opens a date picker dialog to select a date
         */
        btnChangeDate.setOnClickListener((click) -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, dateSetListener, year, month - 1, day);
            datePickerDialog.show();
        });

        /**
         * Calls the api to get the image
         */
        btnLoad.setOnClickListener((click) -> {
            String formattedMonth = String.format(Locale.US, "%02d", month);
            String formattedDay = String.format(Locale.US, "%02d", day);
            String date = year + "-" + formattedMonth + "-" + formattedDay;

            NasaImage req = new NasaImage(this, nasaImageView, date, this);
            req.execute();
        });

        /**
         * Adds the image and its information to the db for your favorites list
         */
        btnFavorite.setOnClickListener((click) -> {
            if (imageInfo != null) {
                String formattedMonth = String.format(Locale.US, "%02d", month);
                String formattedDay = String.format(Locale.US, "%02d", day);
                String date = year + "-" + formattedMonth + "-" + formattedDay;

                String copyright;
                String description;

                try {
                    if (imageInfo.has("copyright")) {
                        copyright = imageInfo.getString("copyright");
                    } else {
                        copyright = "No copyright";
                    }
                    description = imageInfo.getString("explanation");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                ImageDbHelper dbHelper = ImageDbHelper.getInstance(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues newItemRow = new ContentValues();
                newItemRow.put(dbHelper.COL_DATE, date);
                newItemRow.put(dbHelper.COL_COPYRIGHT, copyright);
                newItemRow.put(dbHelper.COL_DESCRIPTION, description);
                long newId = db.insert(dbHelper.TABLE_NAME, null, newItemRow);
            }
        });
    }

    /**
     * Updates the image info sent by the NasaImage class
     */
    public void setImageInfo(JSONObject imageInfo) {
        this.imageInfo = imageInfo;

        try{
            url.setText(imageInfo.getString("url"));
            hdurlLink = imageInfo.getString("hdurl");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}