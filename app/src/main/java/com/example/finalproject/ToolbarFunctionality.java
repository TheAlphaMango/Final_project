package com.example.finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class ToolbarFunctionality extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(Color.WHITE);
        DrawerLayout drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.my_navigation);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.homebtn) {
            if (!(this instanceof MainActivity)) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        } else if (item.getItemId() == R.id.searchbtn) {
            if (!(this instanceof Search)) {
                startActivity(new Intent(this, Search.class));
                finish();
            }
        } else if (item.getItemId() == R.id.favoritesbtn) {
            if (!(this instanceof Favorites)) {
                startActivity(new Intent(this, Favorites.class));
                finish();
            }
        } else if (item.getItemId() == R.id.randomimgbtn) {
            if (!(this instanceof RandomImages)) {
                startActivity(new Intent(this, RandomImages.class));
                finish();
            }
        } else if (item.getItemId() == R.id.exitbtn) {
            finishAffinity();
        }
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String msg = null;
        if (this instanceof MainActivity) {
            msg = getResources().getString(R.string.homehint);
        } else if (this instanceof Search) {
            msg = getResources().getString(R.string.searchhint);
        } else if (this instanceof Favorites) {
            msg = getResources().getString(R.string.favoritehint);
        } else if (this instanceof RandomImages) {
            msg = getResources().getString(R.string.randomhint);
        }

        if (msg != null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getResources().getString(R.string.hint));
            alert.setMessage(msg);
            alert.setNeutralButton("Ok", (click, arg) -> { });
            alert.create().show();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbarmenu, menu);
        return true;
    }


}