package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.FileInputStream;
import java.io.IOException;

public class DetailsFragment extends Fragment {
    /**
     * The context and activity being affected.
     */
    Context ctx;
    Favorites activity;

    /**
     * Initializes the details fragment.
     *
     * @param ctx The context it's called from.
     * @param activity the activity it's called from.
     */
    public DetailsFragment(Context ctx, Favorites activity) {
        this.ctx = ctx;
        this.activity = activity;
    }

    /**
     * Loads an item from the favorites list to the fragment to display its
     * date, copyright, description, image, and add functionality to delete it.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.image_fragment, container, false);

        /**
         * Elements of the layout required to modify
         */
        TextView dateText = (TextView)view.findViewById(R.id.date);
        TextView copyrightText = (TextView)view.findViewById(R.id.copyright);
        TextView descriptionText = (TextView)view.findViewById(R.id.description);
        Button deleteBtn = (Button)view.findViewById(R.id.delete);
        ImageView img = (ImageView)view.findViewById(R.id.nasaImg);

        /**
         * Gets the image data
         */
        Bundle imageInfo = getArguments();
        String imageDate = imageInfo.getString("date");
        String imageCopyright = imageInfo.getString("copyright");
        String imageDescription = imageInfo.getString("description");

        Bitmap pic;
        try {
            FileInputStream fileInputStream = ctx.openFileInput(imageDate);
            pic = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /**
         * Sets the image data
         */
        dateText.setText(imageDate);
        copyrightText.setText(String.valueOf(imageCopyright));
        descriptionText.setText(String.valueOf(imageDescription));
        img.setImageBitmap(pic);

        /**
         * Delete the fragment, then tell the activity to remove it from the list and db
         */
        deleteBtn.setOnClickListener((click) -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();
            activity.deleteImage();
        });

        return view;
    }
}