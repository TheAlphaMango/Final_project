package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;

import androidx.fragment.app.FragmentManager;

public class Favorites extends ToolbarFunctionality {
    /**
     * The list adapter to modify
     */
    ImageListAdapter favoritesListAdapter;

    /**
     * The position in the list the user clicked on
     */
    int position;

    /**
     * Creates the list adapter,
     * adds an event to listen for clicks to load the fragment,
     * and loads all the items from the DB
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        setupToolbar();

        /**
         * Creates the adapter
         */
        ListView favoritesListLayout = (ListView)findViewById(R.id.favoritesList);
        LayoutInflater inflater = getLayoutInflater();
        favoritesListAdapter = new ImageListAdapter(inflater);
        favoritesListLayout.setAdapter(favoritesListAdapter);

        /**
         * Loads the fragment for the item clicked
         */
        favoritesListLayout.setOnItemClickListener((parent, view, position, id) -> {
            this.position = position;
            Image selectedChar = (Image)favoritesListAdapter.getItem(position);
            Bundle imageInfo = new Bundle();
            imageInfo.putString("date", selectedChar.getDate());
            imageInfo.putString("copyright", selectedChar.getCopyright());
            imageInfo.putString("description", selectedChar.getDescription());

            FragmentManager fm = getSupportFragmentManager();
            DetailsFragment df = new DetailsFragment(this, this);

            df.setArguments(imageInfo);
            fm.beginTransaction().replace(R.id.framelayout, df).commit();
        });

        /**
         * Creates a dbhelper, and gets all the data from it
         */

        ImageDbHelper dbHelper = ImageDbHelper.getInstance(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String [] columns = {dbHelper.COL_ID, dbHelper.COL_DATE, dbHelper.COL_COPYRIGHT, dbHelper.COL_DESCRIPTION};
        Cursor results = db.query(false, dbHelper.TABLE_NAME, columns,
                null, null, null, null, null, null);
        dbHelper.printCursor(results);

        results.moveToFirst();
        int idColumnIndex = results.getColumnIndex(dbHelper.COL_ID);
        int dateColumnIndex = results.getColumnIndex(dbHelper.COL_DATE);
        int copyrightColumnIndex = results.getColumnIndex(dbHelper.COL_COPYRIGHT);
        int descriptionColumnIndex = results.getColumnIndex(dbHelper.COL_DESCRIPTION);

        /**
         * Creates a new image with the image data, and loads it into the list adapter
         */
        do {
            String date = results.getString(dateColumnIndex);
            String copyright = results.getString(copyrightColumnIndex);
            String description = results.getString(descriptionColumnIndex);

            Image newImage = new Image(date, copyright, description);
            favoritesListAdapter.addImage(newImage);
        } while (results.moveToNext());

        results.close();
    }

    /**
     * Deletes an image from the list of favorites and the db
     */
    public void deleteImage() {
        /**
         * Get the image and associated date for the db query
         */
        Image image = (Image)favoritesListAdapter.getItem(position);
        String date = image.getDate();

        /**
         * Deletes the image from the list using the position
         */
        favoritesListAdapter.removeItem(position);

        /**
         * Deletes the image from the db
         */
        ImageDbHelper dbHelper = ImageDbHelper.getInstance(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = dbHelper.COL_DATE + " = ?";
        String[] whereArgs = {date};

        int rowsDeleted = db.delete(ImageDbHelper.TABLE_NAME, whereClause, whereArgs);
    }
}