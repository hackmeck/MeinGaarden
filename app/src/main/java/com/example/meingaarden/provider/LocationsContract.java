/*
 * Copyright 2013 The Android Open Source Project
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

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Field and table name constants for
 * {@link LocationsProvider}.
 */
public class LocationsContract {
    private LocationsContract() {
    }

    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "com.example.meingaarden1";

    /**
     * Base URI. (content://com.example.meingaarden)
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path component for "ortseintrag"-type resources..
     */
    private static final String PATH_ORTSEINTRAEGE = "ortseintraege";

    /**
     * Columns supported by "ortseintraege" records.
     */
    public static class Ortseintrag implements BaseColumns {
        /**
         * MIME type for lists of ortseintraege.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.meingaarden.ortseintraege";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.meingaarden.ortseintrag";

        /**
         * Fully qualified URI for "ortseintrag" resources.
         * wird z.B. in der Datei SyncAdapter.java Zeile 229 verwendet
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ORTSEINTRAEGE).build();

        /**
         * Table name where records are stored for "ortseintrag" resources.
         */
        public static final String TABLE_NAME = "ortseintrag";
        /**
         * Atom ID. (Note: Not to be confused with the database primary key, which is _ID.
         */
        public static final String COLUMN_NAME_ORTSEINTRAG_ID = "ortseintrag_id";
        /**
         * Article title
         */
        public static final String COLUMN_NAME_NAME = "name";
        /**
         * Article hyperlink. Corresponds to the rel="alternate" link in the
         * Atom spec.
         */
        public static final String COLUMN_NAME_LINK = "link";
        /**
         * Date article was published.
         */
        public static final String COLUMN_NAME_PUBLISHED = "published";
        /**
         * (Short-)Description of the Location.
         */
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        /**
         * LongDescription of the Location.
         */
        public static final String COLUMN_NAME_LONGDESCRIPTION = "long_description";
        /**
         * image (Url) of the Location.
         */
        public static final String COLUMN_NAME_IMAGEURL = "image_url";
        /**
         * secondary image (Url) of the Location.
         */
        public static final String COLUMN_NAME_SECONDARYIMAGEURL = "secondary_image_url";
        /**
         * LatLng of the Location.
         */
        public static final String COLUMN_NAME_LOCATION = "location";
        /**
         * city of the Location.
         */
        public static final String COLUMN_NAME_CITY = "city";
    }
}