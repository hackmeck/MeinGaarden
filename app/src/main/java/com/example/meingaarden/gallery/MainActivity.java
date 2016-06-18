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

            //"https://images.unsplash.com/photo-1444090542259-0af8fa96557e?q=80&fm=jpg&w=1080&fit=max&s=4b703b77b42e067f949d14581f35019b",
            //"https://images.unsplash.com/photo-1439546743462-802cabef8e97?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            //"https://images.unsplash.com/photo-1441155472722-d17942a2b76a?q=80&fm=jpg&w=1080&fit=max&s=80cb5dbcf01265bb81c5e8380e4f5cc1",
            //"https://images.unsplash.com/photo-1437651025703-2858c944e3eb?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            //"https://images.unsplash.com/photo-1431538510849-b719825bf08b?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            //"https://images.unsplash.com/photo-1434873740857-1bc5653afda8?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            //"https://images.unsplash.com/photo-1439396087961-98bc12c21176?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            //"https://images.unsplash.com/photo-1433616174899-f847df236857?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            //"https://images.unsplash.com/photo-1438480478735-3234e63615bb?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
            //"https://images.unsplash.com/photo-1438027316524-6078d503224b?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_main);

        for (int i = 0; i < IMGS.length; i++) {

            ImageModel imageModel = new ImageModel();
            imageModel.setName("Image " + i);
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
