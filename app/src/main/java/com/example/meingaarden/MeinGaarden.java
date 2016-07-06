/*
 * Copyright 2016 Florian Amrhein. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.meingaarden;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.meingaarden.gallery.MainActivity;
import com.example.meingaarden.ui.AppNewsListActivity;
import com.example.meingaarden.ui.AttractionListActivity;
import com.example.meingaarden.ui.EntryListActivity;
import com.example.meingaarden.ui.HotSpotListActivity;
import com.example.meingaarden.ui.HotSpotsListActivity;

public class MeinGaarden extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mein_gaarden);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.fab, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // neu von flo, um Settings-Werte zu initiieren ...
        PreferenceManager.setDefaultValues(this, R.xml.pref_notification, false);

        // neu von flo als settings-test
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MeinGaarden.this);
        Long test = Long.valueOf(sharedPref.getString("pref_key_trigger_radius",
                "2000"));

        if (test == 5000) {
            Toast.makeText(getApplicationContext(), "juchu - 5000",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mein_gaarden, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // XYZTourist Beispiel
        /** alt
        if (id == R.id.action_appnews) {
            Intent intent = new Intent(this, EntryListActivity.class);
            startActivity(intent);
            return true;

         */
        // AppNews
        if (id == R.id.action_appnews) {
            Intent intent = new Intent(this, AppNewsListActivity.class);
            startActivity(intent);
            return true;
        }
        // Gallery
        if (id == R.id.action_gallery) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        // Attractions
        if (id == R.id.action_attractions) {
            Intent intent = new Intent(this, AttractionListActivity.class);
            startActivity(intent);
            return true;
        }
        // XYZTourist Beispiel
        if (id == R.id.action_hotspots) {
            Intent intent = new Intent(this, HotSpotsListActivity.class);
            startActivity(intent);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_attractions) {
            // Handle the camera action
            Intent intent = new Intent(this, AttractionListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_gallery) {
            //confirmFireMissiles();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_hotspots) {
            // Handle the camera action
            Intent intent = new Intent(this, HotSpotsListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_appnews) {
            // Handle the camera action
            Intent intent = new Intent(this, AppNewsListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_share) {
            confirmFireMissiles();
        } else if (id == R.id.nav_send) {
            confirmFireMissiles();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Von Flo für Dialog
    public void confirmFireMissiles() {
        DialogFragment newFragment= new FireMissilesDialogFragment();
        newFragment.show(getSupportFragmentManager(), "missiles");
    }
}