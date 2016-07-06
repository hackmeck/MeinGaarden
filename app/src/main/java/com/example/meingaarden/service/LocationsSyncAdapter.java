/*
 * Copyright 2013 The Android Open Source Project
 * Copyright 2016 Florian Amrhein
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

package com.example.meingaarden.service;

import android.accounts.Account; // Verwendung: Zeile 141
import android.annotation.TargetApi; // Verwendung: Zeile 119
import android.content.AbstractThreadedSyncAdapter; // Verwendung: Zeile 62 und weitere
import android.content.ContentProviderClient; // Verwendung: Zeile 62, 140, 142
import android.content.ContentProviderOperation; // Zeile 231 ...
import android.content.ContentResolver; // Verwendung: Zeile 89,113,211,229,290,291
import android.content.Context; // Zeile 124 ...
import android.content.OperationApplicationException;
import android.content.SyncResult; // zeile 154 ...
import android.database.Cursor; // zeile 261 ...
import android.net.Uri; // zeil 241 ...
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

// import com.example.meingaarden.net.FeedParser; // komplett ersetzt durch LocationParser
import com.example.meingaarden.net.LocationParser;
// import com.example.meingaarden.provider.FeedContract; // Verwendung nur noch in Zeile 290
import com.example.meingaarden.provider.LocationsContract;
// import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Define a sync adapter for the app.
 *
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 *
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
class LocationsSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "LocationsSyncAdapter";

    /**
     * URL to fetch content from during a sync.
     *
     * <p>This points to the Android Developers Blog. (Side note: We highly recommend reading the
     * Android Developer Blog to stay up to date on the latest Android platform developments!)
     */
    //private static final String FEED_URL = "http://android-developers.blogspot.com/atom.xml";
    //private static final String FEED_URL = "http://www.flo.visionenundideen.de/AndroidTest/testfeed.xml";
    //private static final String FEED_URL = "http://www.flo.visionenundideen.de/hackmeck/index.php/feed/atom/"; // greift auf hauptfeed zu
    private static final String LOCATIONS_URL = "http://www.flo.visionenundideen.de/AndroidTest/locations.xml"; // wird in Zeile 144 benötigt

    /**
     * Network connection timeout, in milliseconds.
     */
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;

    /**
     * Project used when querying content provider. Returns all known fields.
     */
    private static final String[] PROJECTION = new String[] {
            LocationsContract.Ortseintrag._ID,
            LocationsContract.Ortseintrag.COLUMN_NAME_ORTSEINTRAG_ID,
            LocationsContract.Ortseintrag.COLUMN_NAME_NAME,
            LocationsContract.Ortseintrag.COLUMN_NAME_LINK,
            LocationsContract.Ortseintrag.COLUMN_NAME_PUBLISHED,
            LocationsContract.Ortseintrag.COLUMN_NAME_DESCRIPTION,
            LocationsContract.Ortseintrag.COLUMN_NAME_LONGDESCRIPTION,
            LocationsContract.Ortseintrag.COLUMN_NAME_IMAGEURL,
            LocationsContract.Ortseintrag.COLUMN_NAME_SECONDARYIMAGEURL,
            LocationsContract.Ortseintrag.COLUMN_NAME_LOCATION,
            LocationsContract.Ortseintrag.COLUMN_NAME_CITY};

    // Constants representing column positions from PROJECTION.
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_ORTSEINTRAG_ID = 1; // wird in Zeile 241 verwendet
    public static final int COLUMN_NAME = 2; // wird in Zeile 242 verwendet
    public static final int COLUMN_LINK = 3;
    public static final int COLUMN_PUBLISHED = 4;
    public static final int COLUMN_DESCRIPTION = 5;
    public static final int COLUMN_LONGDESCRIPTION = 6;
    public static final int COLUMN_IMAGEURL = 7;
    public static final int COLUMN_SECONDARYIMAGEURL = 8;
    public static final int COLUMN_LOCATION = 9;
    public static final int COLUMN_CITY = 10;

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public LocationsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public LocationsSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     .
     *
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     *
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");
        try {
            final URL location = new URL(LOCATIONS_URL);
            InputStream stream = null;

            try {
                Log.i(TAG, "Streaming data from network: " + location);
                stream = downloadUrl(location); // Die Methode 'downloadUrl(java.net.URL)' wird in Zeile 301 definiert
                updateLocalLocationsData(stream, syncResult); // Die Methode wird in Zeile 207 definiert
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Feed URL is malformed", e);
            syncResult.stats.numParseExceptions++;
            return;
        } catch (IOException e) {
            Log.e(TAG, "Error reading from network: " + e.toString());
            syncResult.stats.numIoExceptions++;
            return;
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (RemoteException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        } catch (OperationApplicationException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        }
        Log.i(TAG, "Network synchronization complete");
    }

    /**
     * Read XML from an input stream, storing it into the content provider.
     *
     * <p>This is where incoming data is persisted, committing the results of a sync. In order to
     * minimize (expensive) disk operations, we compare incoming data with what's already in our
     * database, and compute a merge. Only changes (insert/update/delete) will result in a database
     * write.
     *
     * <p>As an additional optimization, we use a batch operation to perform all database writes at
     * once.
     *
     * <p>Merge strategy:
     * 1. Get cursor to all items in feed<br/>
     * 2. For each item, check if it's in the incoming data.<br/>
     *    a. YES: Remove from "incoming" list. Check if data has mutated, if so, perform
     *            database UPDATE.<br/>
     *    b. NO: Schedule DELETE from database.<br/>
     * (At this point, incoming database only contains missing items.)<br/>
     * 3. For any items remaining in incoming list, ADD to database.
     */
    public void updateLocalLocationsData(final InputStream stream, final SyncResult syncResult)
            throws IOException, XmlPullParserException, RemoteException,
            OperationApplicationException, ParseException {
        final LocationParser locationParser = new LocationParser(); // initiiert die Klasse LocationParser, was vorher noch nicht der Fall war
        final ContentResolver contentResolver = getContext().getContentResolver();

        Log.i(TAG, "Parsing stream as Atom feed");
        final List<LocationParser.Ortseintrag> ortseintraege = locationParser.parse(stream); // LocationParser.Ortseintrag siehe LocationParser.java Zeile 270
        Log.i(TAG, "Parsing complete. Found " + ortseintraege.size() + " ortseintraege");


        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, LocationParser.Ortseintrag> entryMap = new HashMap<String, LocationParser.Ortseintrag>();
        for (LocationParser.Ortseintrag e : ortseintraege) {
            entryMap.put(e.id, e);
        }

        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = LocationsContract.Ortseintrag.CONTENT_URI; // Get all entries - Meint die Klasse LocationContract.Ortseintrag (Datei LocationContract.java Zeile 49) und hier CONTENT_URI Zeile 65 ff
        Cursor c = contentResolver.query(uri, PROJECTION, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

        // Find stale data
        int id;
        String entryId;
        String title;
        String link;
        long published;
        String description; // neu von flo
        String longDescription; // neu von flo
        //Uri imageUrl; // neu von flo - sollte vielleicht lieber String sein ???
        String imageUrl; // neu von flo
        //Uri secondaryImageUrl; // neu von flo - sollte vielleicht lieber String sein ???
        String secondaryImageUrl; // neu von flo
        //LatLng location; // neu von flo - sollte vielleicht lieber String sein ???
        String location; // neu von flo
        String city; // neu von flo
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID);
            entryId = c.getString(COLUMN_ORTSEINTRAG_ID);
            title = c.getString(COLUMN_NAME);
            link = c.getString(COLUMN_LINK);
            published = c.getLong(COLUMN_PUBLISHED);
            description = c.getString(COLUMN_DESCRIPTION); // neu von flo
            longDescription = c.getString(COLUMN_LONGDESCRIPTION); // neu von flo
            //imageUrl = c.getUri(COLUMN_IMAGEURL); // neu von flo
            imageUrl = c.getString(COLUMN_IMAGEURL); // neu von flo, da c.getUri Fehler anzeigt
            //secondaryImageUrl = c.getUri(COLUMN_SECONDARYIMAGEURL); // neu von flo
            secondaryImageUrl = c.getString(COLUMN_SECONDARYIMAGEURL); // neu von flo
            //location = c.getLatLng(COLUMN_LOCATION); // neu von flo
            location = c.getString(COLUMN_LOCATION); // neu von flo
            city = c.getString(COLUMN_CITY); // neu von flo
            LocationParser.Ortseintrag match = entryMap.get(entryId);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(entryId);
                // Check to see if the entry needs to be updated
                Uri existingUri = LocationsContract.Ortseintrag.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                if ((match.name != null && !match.name.equals(title)) ||
                        (match.link != null && !match.link.equals(link)) ||
                        (match.published != published) ||
                        (match.description != null && !match.description.equals(description)) ||
                        (match.longDescription != null && !match.longDescription.equals(longDescription)) ||
                        //(match.imageUrl != null && !match.imageUrl.equals(imageUrl)) ||
                        //(match.secondaryImageUrl != null && !match.secondaryImageUrl.equals(secondaryImageUrl)) ||
                        (match.location != null && !match.location.equals(location)) ||
                        (match.city != null && !match.city.equals(city))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_NAME, match.name) // match = Ortseintrag
                            .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_LINK, match.link)
                            .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_PUBLISHED, match.published)
                            .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_DESCRIPTION, match.description)
                            .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_LONGDESCRIPTION, match.longDescription)
                            .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_IMAGEURL, match.imageUrl)
                            //.withValue(LocationsContract.Ortseintrag.COLUMN_NAME_SECONDARYIMAGEURL, match.secondaryImageUrl)
                            .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_LOCATION, match.location)
                            .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_CITY, match.city)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = LocationsContract.Ortseintrag.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Add new items
        for (LocationParser.Ortseintrag e : entryMap.values()) {
            Log.i(TAG, "Scheduling insert: entry_id=" + e.id);
            batch.add(ContentProviderOperation.newInsert(LocationsContract.Ortseintrag.CONTENT_URI)
                    .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_ORTSEINTRAG_ID, e.id) // e = Ortseintrag
                    .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_NAME, e.name)
                    .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_LINK, e.link)
                    .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_PUBLISHED, e.published)
                    .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_DESCRIPTION, e.description)
                    .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_LONGDESCRIPTION, e.longDescription)
                    .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_IMAGEURL, e.imageUrl)
                    .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_SECONDARYIMAGEURL, e.secondaryImageUrl)
                    .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_LOCATION, e.location)
                    .withValue(LocationsContract.Ortseintrag.COLUMN_NAME_CITY, e.city)
                    .build());
            syncResult.stats.numInserts++;
        }
        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(LocationsContract.CONTENT_AUTHORITY, batch); // lass ich erst einmal unverändert, da CONTENT_AUTHORITY gleich
        mContentResolver.notifyChange(
                LocationsContract.Ortseintrag.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets an input stream.
     */
    private InputStream downloadUrl(final URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(NET_READ_TIMEOUT_MILLIS /* milliseconds */);
        conn.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}
