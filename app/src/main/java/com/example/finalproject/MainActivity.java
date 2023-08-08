package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ToolbarFunctionality {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();

        /**
         * Elements needed to modify
         */
        TextView welcomeMsg = (TextView)findViewById(R.id.welcome);
        Button btn = (Button)findViewById(R.id.save);
        EditText nameTxt = (EditText)findViewById(R.id.username);

        /**
         * Load the user's name preference
         */
        SharedPreferences preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String name = preferences.getString("name", "");

        /**
         * Give them a customized welcome message
         */
        nameTxt.setText(name);
        welcomeMsg.setText(getResources().getString(R.string.welcome) + " " + name + "!");

        /**
         * Update the message when their name is updated
         */
        btn.setOnClickListener((click) -> {
            String newName = String.valueOf(nameTxt.getText());
            editor.putString("name", newName);
            editor.commit();
            welcomeMsg.setText(getResources().getString(R.string.welcome) + " " + newName + "!");
        });
    }

}