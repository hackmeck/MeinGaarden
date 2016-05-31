package com.example.meingaarden;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
//import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.example.meingaarden.common.Constants;
import com.example.meingaarden.common.HotSpot;
import com.example.meingaarden.common.ImageLoader;
import com.example.meingaarden.common.Utils;
import com.example.meingaarden.provider.HotSpots;
import com.example.meingaarden.provider.TouristAttractions;
import com.example.meingaarden.service.UtilityService;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.meingaarden.provider.HotSpots.HOTSPOTS;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link MeinGaardenAppWidgetConfigureActivity SecurityAppWidgetConfigureActivity}
 */
public class MeinGaardenAppWidget extends AppWidgetProvider {

    private AppWidgetTarget appWidgetTarget; // neu Tutorial 1
    private LatLng mLatestLocation;
    final LatLng curLatLng;

    public MeinGaardenAppWidget() {
        curLatLng = null;
    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = MeinGaardenAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.meingaarden_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MeinGaarden.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.meingaarden_app_widget);
            //views.setOnClickPendingIntent(R.id.play_button, pendingIntent); // auskommentiert, da xml-part deaktiviert
            views.setOnClickPendingIntent(R.id.icon, pendingIntent);

            //To update a label - von flo für tutorial
            // von flo für tutorial
            DateFormat df = new SimpleDateFormat("hh:mm:ss");
            // views.setTextViewText(R.id.widget_label, df.format(new Date())); // auskommentiert, da xml-part deaktiviert

            // Neu Tutorial 1
            appWidgetTarget = new AppWidgetTarget(context, views, R.id.icon, appWidgetIds);

            String cityId = "Kiel";
            //String closestCity = HotSpots.getClosestCity(curLatLng);
            //NEU
            // von Flo um HotSpot in Widget anzuzeigen
            List<HotSpot> hotspots = HOTSPOTS.get(cityId);
            //List<HotSpot> hotspots = HOTSPOTS.get(closestCity);
            // The first (closest) tourist attraction
            HotSpot hotspot = hotspots.get(0);

            // Neu Tutorial 1
            Glide
                    .with(context.getApplicationContext()) // safer
                    .load(hotspot.url)
                    .asBitmap()
                    .into(appWidgetTarget);

            // Limit attractions to send
            int count = hotspots.size() > Constants.MAX_ATTRACTIONS ?
                    Constants.MAX_ATTRACTIONS : hotspots.size();
            // Pull down the tourist attraction images from the network and store
            HashMap<String, Bitmap> bitmaps = new HashMap<>();

            //Uri image_url = hotspot.imageUrl;

            // flo neu test
            views.setTextViewText(R.id.text1, hotspot.name);
            views.setTextViewText(R.id.text2, hotspot.description);
            //views.setImageViewResource(R.id.icon, R.drawable.empty_photo);
            //views.setImageViewUri(R.id.icon, Uri.parse(hotspot.url));
            //views.setImageViewBitmap(R.id.icon, hotspot.image);

            // Loader image - will be shown before loading image
            int loader = R.drawable.loader;

            // Imageview to show
           //ImageView image = (ImageView) findViewById(R.id.icon);

            // Image url
            String image_url = hotspot.url;

            // ImageLoader class instance
            ImageLoader imgLoader = new ImageLoader(context);

            // whenever you want to load an image from url
            // call DisplayImage function
            // url - image url to load
            // loader - loader image, will be displayed before getting image
            // image - ImageView
            //imgLoader.DisplayImage(image_url, loader, RemoteViews);

            //Picasso.with(context).load(image_url).into(views, R.id.icon, appWidgetIds);



            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }




    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            MeinGaardenAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

