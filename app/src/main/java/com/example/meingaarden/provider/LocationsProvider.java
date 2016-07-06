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

package com.example.meingaarden.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.common.db.SelectionBuilder;

public class LocationsProvider extends ContentProvider {
    LocationsDatabase mDatabaseHelper; // siehe Zeile 225 ff.

    /**
     * Content authority for this provider.
     */
    private static final String AUTHORITY = LocationsContract.CONTENT_AUTHORITY; // wurde definiert in com.example.meingaarden.provider.FeedContract.java

    // The constants below represent individual URI routes, as IDs. Every URI pattern recognized by
    // this ContentProvider is defined using sUriMatcher.addURI(), and associated with one of these
    // IDs.
    //
    // When a incoming URI is run through sUriMatcher, it will be tested against the defined
    // URI patterns, and the corresponding route ID will be returned.
    /**
     * URI ID for route: /ortseintraege
     */
    public static final int ROUTE_ORTSEINTRAEGE = 1; // 1+2 werden bereits von FeedProvider.java verwendet

    /**
     * URI ID for route: /ortseintraege/{ID}
     */
    public static final int ROUTE_ORTSEINTRAEGE_ID = 2; // 1+2 werden bereits von FeedProvider.java verwendet

    /**
     * UriMatcher, used to decode incoming URIs.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, "ortseintraege", ROUTE_ORTSEINTRAEGE);
        sUriMatcher.addURI(AUTHORITY, "ortseintraege/*", ROUTE_ORTSEINTRAEGE_ID);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new LocationsDatabase(getContext());
        return true;
    }

    /**
     * Determine the mime type for entries returned by a given URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTE_ORTSEINTRAEGE:
                return LocationsContract.Ortseintrag.CONTENT_TYPE;
            case ROUTE_ORTSEINTRAEGE_ID:
                return LocationsContract.Ortseintrag.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /**
     * Perform a database query by URI.
     *
     * <p>Currently supports returning all entries (/entries) and individual entries by ID
     * (/entries/{ID}).
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        SelectionBuilder builder = new SelectionBuilder();
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case ROUTE_ORTSEINTRAEGE_ID:
                // Return a single entry, by ID.
                String id = uri.getLastPathSegment();
                builder.where(LocationsContract.Ortseintrag._ID + "=?", id);
            case ROUTE_ORTSEINTRAEGE:
                // Return all known entries.
                builder.table(LocationsContract.Ortseintrag.TABLE_NAME)
                       .where(selection, selectionArgs);
                Cursor c = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /**
     * Insert a new entry into the database.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        assert db != null;
        final int match = sUriMatcher.match(uri);
        Uri result;
        switch (match) {
            case ROUTE_ORTSEINTRAEGE:
                long id = db.insertOrThrow(LocationsContract.Ortseintrag.TABLE_NAME, null, values);
                result = Uri.parse(LocationsContract.Ortseintrag.CONTENT_URI + "/" + id);
                break;
            case ROUTE_ORTSEINTRAEGE_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    /**
     * Delete an entry by database by URI.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_ORTSEINTRAEGE:
                count = builder.table(LocationsContract.Ortseintrag.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_ORTSEINTRAEGE_ID:
                String id = uri.getLastPathSegment();
                count = builder.table(LocationsContract.Ortseintrag.TABLE_NAME)
                        .where(LocationsContract.Ortseintrag._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    /**
     * Update an etry in the database by URI.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_ORTSEINTRAEGE:
                count = builder.table(LocationsContract.Ortseintrag.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_ORTSEINTRAEGE_ID:
                String id = uri.getLastPathSegment();
                count = builder.table(LocationsContract.Ortseintrag.TABLE_NAME)
                        .where(LocationsContract.Ortseintrag._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    /**
     * SQLite backend for @{link FeedProvider}.
     *
     * Provides access to an disk-backed, SQLite datastore which is utilized by FeedProvider. This
     * database should never be accessed by other parts of the application directly.
     */
    static class LocationsDatabase extends SQLiteOpenHelper {
        /** Schema version. */
        public static final int DATABASE_VERSION = 2;
        /** Filename for SQLite file. */
        public static final String DATABASE_NAME = "locations.db";

        private static final String TYPE_TEXT = " TEXT";
        private static final String TYPE_INTEGER = " INTEGER";
        private static final String COMMA_SEP = ",";
        /** SQL statement to create "entry" table. */
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + LocationsContract.Ortseintrag.TABLE_NAME + " (" +
                        LocationsContract.Ortseintrag._ID + " INTEGER PRIMARY KEY," +
                        LocationsContract.Ortseintrag.COLUMN_NAME_ORTSEINTRAG_ID + TYPE_TEXT + COMMA_SEP + // Zeile 75 LocationsContract
                        LocationsContract.Ortseintrag.COLUMN_NAME_NAME    + TYPE_TEXT + COMMA_SEP + // Zeile 79 LocationsContract
                        LocationsContract.Ortseintrag.COLUMN_NAME_LINK + TYPE_TEXT + COMMA_SEP +
                        LocationsContract.Ortseintrag.COLUMN_NAME_PUBLISHED + TYPE_INTEGER + COMMA_SEP +
                        LocationsContract.Ortseintrag.COLUMN_NAME_DESCRIPTION + TYPE_TEXT + COMMA_SEP +
                        LocationsContract.Ortseintrag.COLUMN_NAME_LONGDESCRIPTION + TYPE_TEXT + COMMA_SEP +
                        LocationsContract.Ortseintrag.COLUMN_NAME_IMAGEURL + TYPE_TEXT + COMMA_SEP +
                        LocationsContract.Ortseintrag.COLUMN_NAME_SECONDARYIMAGEURL + TYPE_TEXT + COMMA_SEP +
                        LocationsContract.Ortseintrag.COLUMN_NAME_LOCATION + TYPE_TEXT + COMMA_SEP +
                        LocationsContract.Ortseintrag.COLUMN_NAME_CITY + TYPE_TEXT + ")";

        /** SQL statement to drop "entry" table. */
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + LocationsContract.Ortseintrag.TABLE_NAME;

        public LocationsDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }
}
