package com.example.meingaarden.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.meingaarden.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GalleryAdapter mAdapter;
    RecyclerView mRecyclerView;

    ArrayList<ImageModel> data = new ArrayList<>();

    public static String IMGS[] = {
            "http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Vinetaplatz.png",
            "http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Sommerbad_Katzheide.png",
            "http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Werftpark.png",
            "http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Am_Germaniahafen.png",
            "http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Schwimmhalle_Gaarden.png",
            "http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Krupp_Siedlung.png",
            "http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Iltisbunker.png",
            "http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Apotheke_Gaarden.png",
            "http://flo.visionenundideen.de/AndroidTest/pictures/600x450/DJH_Kiel.png",
            "http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Froebelschule.png",
            "http://flo.visionenundideen.de/AndroidTest/pictures/600x450/BioGaarden.png",
            "http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Hinterhof_Gaarden.png",
            "http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Eis_Eis_lecker.png",
            "http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Juedische_Gemeinde.png"

    };

    public static String NAMES[] = {
            "Vinetaplatz",
            "Sommerbad Katzheide",
            "Werftpark",
            "Am Germaniahafen",
            "Schwimmhalle Gaarden",
            "Krupp Siedlung",
            "Iltisbunker",
            "Apotheke Gaarden",
            "DJH Kiel",
            "Froebelschule",
            "BioGaarden",
            "Hinterhof Gaarden",
            "Eis, Eis lecker",
            "Juedische Gemeinde"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_main);

        for (int i = 0; i < IMGS.length; i++) {

            ImageModel imageModel = new ImageModel();
            // alt
            // imageModel.setName("Image " + i);
            // neu 1
            // imageModel.setName("Image " + (i+1));
            // neu 2
            imageModel.setName(NAMES[i]);
            imageModel.setUrl(IMGS[i]);
            data.add(imageModel);

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // neu von flo für zurück-button

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);


        mAdapter = new GalleryAdapter(MainActivity.this, data);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putParcelableArrayListExtra("data", data);
                        intent.putExtra("pos", position);
                        startActivity(intent);

                    }
                }));

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
               // showDebugDialog(R.string.action_settings,
               //         R.string.action_settings_dialog);
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

}
