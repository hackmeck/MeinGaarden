package com.example.meingaarden;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.example.meingaarden.common.HotSpot;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import static com.example.meingaarden.provider.HotSpots.HOTSPOTS;

/**
 * Implementation of App Widget functionality.
 */
public class MeinGaardenAppWidget extends AppWidgetProvider {

    private AppWidgetTarget appWidgetTarget; // com.bumptech.glide.request.target.AppWidgetWidget
    private LatLng mLatestLocation; // neu
    final LatLng curLatLng; // neu

    public MeinGaardenAppWidget() { // neu
        curLatLng = null;// neu
    } // neu

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            // Create an Intent to launch MeinGaarden
            Intent intent = new Intent(context, MeinGaarden.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            // Get the layout for the App Widget and attach an on-klick listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views.setOnClickPendingIntent(R.id.icon, pendingIntent);
            // Für Glide
            appWidgetTarget = new AppWidgetTarget(context, views, R.id.icon, appWidgetIds);
            // statisches Hilfskonstrukt
            String cityId = "Kiel";
            List<HotSpot> hotspots = HOTSPOTS.get(cityId);
            // The first (closest) HotSpot
            HotSpot hotspot = hotspots.get(0);

            views.setTextViewText(R.id.text1, hotspot.name);
            views.setTextViewText(R.id.text2, hotspot.description);

            // Bild laden
            Glide
                    .with(context.getApplicationContext()) //safer
                    .load(hotspot.url)
                    .asBitmap()
                    .into(appWidgetTarget);

            //updateAppWidget(context, appWidgetManager, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, views);
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