package com.example.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ImageDbHelper extends SQLiteOpenHelper {

    /**
     * DB info for db name, table name, columns, and version
     */
    final static String DB_NAME = "ImageListDB";
    final static int DB_VERSION = 3;
    final static String TABLE_NAME = "ImageList";
    final static String COL_ID = "_id";
    final static String COL_DATE = "Date";
    final static String COL_COPYRIGHT = "Copyright";
    final static String COL_DESCRIPTION = "Description";

    /**
     * ImageDBHelper instance used for Singleton pattern,
     * so multiple classes can access the same db helper
     */
    private static ImageDbHelper instance;

    /**
     * Constructor to create a new dbhelper with the context required
     */
    private ImageDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Checks if an instance of DBhelper was already made,
     * before making a new one or returning the old one
     */
    public static synchronized ImageDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ImageDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Create the table
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( "
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DATE + " TEXT, "
                + COL_COPYRIGHT + " TEXT, "
                + COL_DESCRIPTION + " TEXT);");
    }

    /**
     * Drop and recreate the table after upgrading
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Print cursor for debugging purposes
     */
    public void printCursor(Cursor c) {
        Integer version = c.getExtras().getInt("version");
        Log.d("Cursor", "Version: " + version);

        Integer numColumns = c.getColumnCount();
        Log.d("Cursor", "Number of columns: " + numColumns);

        Log.d("Cursor", "Column names: ");
        String[] columnNames = c.getColumnNames();
        for (String columnName : columnNames) {
            Log.d("Cursor", columnName);
        }

        Integer numResults = c.getCount();
        Log.d("Cursor", "Number of results: " + numResults);

        int idColumnIndex = c.getColumnIndex(COL_ID);
        int dateColumnIndex = c.getColumnIndex(COL_DATE);
        int copyrightColumnIndex = c.getColumnIndex(COL_COPYRIGHT);
        int descriptionColumnIndex = c.getColumnIndex(COL_DESCRIPTION);
        Log.d("Cursor", "Cursor info: ");

        while (c.moveToNext()) {
            String id = c.getString(idColumnIndex);
            String dateString = c.getString(dateColumnIndex);
            String copyrightString = c.getString(copyrightColumnIndex);
            String descriptionString = c.getString(descriptionColumnIndex);

            Log.d("Cursor", id + ", " + dateString + ", " + copyrightString + ", " + descriptionString);
        }
    }

    /**
     * Query a random date from the database for the random images
     */
    public String getRandomDate() {
        /**
         * Get the database and query
         */
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT DISTINCT " + COL_DATE + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        /**
         * Initialize the random date
         */
        String randomDate = null;

        /**
         * Move the cursor to a random position to set the date
         */
        if (cursor != null && cursor.moveToFirst()) {
            int dateColumnIndex = cursor.getColumnIndex(COL_DATE);
            int randomIndex = (int) (Math.random() * cursor.getCount());
            cursor.moveToPosition(randomIndex);
            randomDate = cursor.getString(dateColumnIndex);
            cursor.close();
        }

        db.close();
        return randomDate;
    }
}
