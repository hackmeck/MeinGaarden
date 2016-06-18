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

package com.example.meingaarden.common;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * A simple shared hotspot class to easily pass data around.
 */
public class HotSpot {
    public String name;
    public String description;
    public String longDescription;
    public String url; // Anpassung für Tutorial androidhive.info/2012/07/android-loading-image-from-url-http
    public Uri imageUrl;
    public Uri secondaryImageUrl;
    public LatLng location;
    public String city;

    public Bitmap image;
    public Bitmap secondaryImage;
    public String distance;

    public HotSpot() {}
    /** alt - funktioniert!!
    public HotSpot(String name, String description, String longDescription, Uri imageUrl,
                   Uri secondaryImageUrl, LatLng location, String city) {
        this.name = name;
        this.description = description;
        this.longDescription = longDescription;
        this.imageUrl = imageUrl;
        this.secondaryImageUrl = secondaryImageUrl;
        this.location = location;
        this.city = city;
    }
     */
    // Neuer Versuch für Tutorial androidhive.info/2012/07/android-loading-image-from-url-http
    public HotSpot(String name, String description, String longDescription, Uri imageUrl, String url,
                   Uri secondaryImageUrl, LatLng location, String city) {
        this.name = name;
        this.description = description;
        this.longDescription = longDescription;
        this.imageUrl = imageUrl;
        this.url = url;
        this.secondaryImageUrl = secondaryImageUrl;
        this.location = location;
        this.city = city;
    }
}