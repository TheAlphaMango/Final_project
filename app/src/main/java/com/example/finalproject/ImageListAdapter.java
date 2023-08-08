package com.example.finalproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends BaseAdapter {
    /**
     * Keep the list of images, and the inflater
     */
    List<Image> imageList = new ArrayList<>();
    LayoutInflater inflater;

    /**
     * Set the inflater to use later
     */
    public ImageListAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    /**
     * Overriding default methods
     */
    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View old, ViewGroup parent) {
        View newView = old;

        if (newView == null) {
            newView = inflater.inflate(R.layout.list_info, parent, false);
        }

        Image currentImage = (Image) getItem(position);
        TextView item = newView.findViewById(R.id.date);
        item.setText(currentImage.getDate());

        return newView;
    }

    /**
     * Add an image to the list
     */
    public void addImage(Image newImage) {
        imageList.add(newImage);
        notifyDataSetChanged();
    }

    /**
     * Remove an image from the list
     */
    public void removeItem(int position) {
        imageList.remove(position);
        notifyDataSetChanged();
    }
}
