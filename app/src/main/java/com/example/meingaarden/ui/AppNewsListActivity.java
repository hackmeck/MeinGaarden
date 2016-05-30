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

package com.example.meingaarden.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.meingaarden.R;
import com.example.meingaarden.common.Utils;
import com.example.meingaarden.service.UtilityService;

/**
 * The main tourist attraction activity screen which contains a list of
 * attractions sorted by distance.
 */
public class AppNewsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new EntryListFragment()) // geändert von flo vgl. AttractionListActivity
                    .commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // UtilityService.requestLocation(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.appnews, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            //case R.id.test_notification:
                //UtilityService.triggerWearTest(this, false);
                //showDebugDialog(R.string.action_test_notification,
                //        R.string.action_test_notification_dialog);
                //return true;
            case R.id.menu_settings:
                //UtilityService.triggerWearTest(this, true);
                showDebugDialog(R.string.action_settings,
                        R.string.action_settings_dialog);
                return true;
            //case R.id.test_toggle_geofence:
                //boolean geofenceEnabled = Utils.getGeofenceEnabled(this);
                //Utils.storeGeofenceEnabled(this, !geofenceEnabled);
                //Toast.makeText(this, geofenceEnabled ?
                //        "Debug: Geofencing trigger disabled" :
                //        "Debug: Geofencing trigger enabled", Toast.LENGTH_SHORT).show();
                //return true;
        }
        return super.onOptionsItemSelected(item);
    }




    /**
     * Show a basic debug dialog to provide more info on the built-in debug
     * options.
     */
    private void showDebugDialog(int titleResId, int bodyResId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(titleResId)
                .setMessage(bodyResId)
                .setPositiveButton(android.R.string.ok, null);
        builder.create().show();
    }
}
