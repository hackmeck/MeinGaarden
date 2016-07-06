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

package com.example.meingaarden.provider;

import android.net.Uri;

import com.example.meingaarden.common.Attraction;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Static data content provider.
 */
public class FreifunkHotSpots {

    public static final String CITY_SYDNEY = "Sydney";
    public static final String CITY_KIEL = "Kiel";  // von flo

    public static final String TEST_CITY = CITY_SYDNEY;

    private static final float TRIGGER_RADIUS = 2000; // 2KM

    private static final Long TRIGGER_RADIUS2 = Long.getLong("pref_key_trigger_radius");

    private static final int TRIGGER_TRANSITION = Geofence.GEOFENCE_TRANSITION_ENTER |
            Geofence.GEOFENCE_TRANSITION_EXIT;
    private static final long EXPIRATION_DURATION = Geofence.NEVER_EXPIRE;

    public static final Map<String, LatLng> CITY_LOCATIONS = new HashMap<String, LatLng>() {{
        put(CITY_SYDNEY, new LatLng(-33.873651, 151.2068896));
        put(CITY_KIEL, new LatLng(54.32262, 10.13217)); // Rathaus Kiel
    }};

    /**
     * All photos used with permission under the Creative Commons Attribution-ShareAlike License.
     */
    public static final HashMap<String, List<Attraction>> ATTRACTIONS =
            new HashMap<String, List<Attraction>>() {{

                put(CITY_SYDNEY, new ArrayList<Attraction>() {{
                        add(new Attraction(
                                "Sydney Opera House",
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae bibendum justo, vitae cursus velit. Suspendisse potenti.",
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae bibendum justo, vitae cursus velit. Suspendisse potenti. Suspendisse scelerisque risus justo, non tincidunt nibh blandit et. Vivamus elit lacus, luctus nec erat in, pharetra semper turpis. Quisque viverra nulla ligula, non pulvinar ante dictum sit amet. Vestibulum aliquet tortor mauris, vel suscipit nisl malesuada eget. Aliquam maximus dictum euismod. Maecenas leo quam, volutpat id diam eget, placerat fringilla ipsum. Nam pretium vehicula augue quis euismod.\n\nNam sed blandit magna. Vestibulum a fermentum arcu. Vestibulum et ligula at nisi luctus facilisis. Proin fermentum enim a nibh commodo finibus. Suspendisse justo elit, vulputate ut ipsum at, pellentesque auctor massa. Praesent vestibulum erat interdum imperdiet dapibus. In hac habitasse platea dictumst. Proin varius orci vitae tempor vulputate.\n\nEtiam sed mollis orci. Integer et ex sed tortor scelerisque blandit semper id libero. Nulla facilisi. Pellentesque tempor magna eget massa ultrices, et efficitur lectus finibus.",
                                Uri.parse("https://lh5.googleusercontent.com/-7fb5ybQhUbo/VGLWjIL4RmI/AAAAAAAAACM/2jLe_msj_tk/w600-no/IMG_0049.JPG"),
                                Uri.parse("https://lh3.googleusercontent.com/-EFEw6s7mT6I/VGLkCH4Xt4I/AAAAAAAAADY/ZlznhaQvb8E/w600-no/DSC_2775.JPG"),
                                new LatLng(-33.858667, 151.214028),
                                CITY_SYDNEY
                        ));
                }});

                put(CITY_KIEL, new ArrayList<Attraction>() {{

                    add(new Attraction(
                            "HotSpot Vinetaplatz",
                            "Freifunk HotSpot direkt im Herzen Gaardens. Gute Verweilmöglichkeiten in direkter Umgebung.",
                            "Freifunk HotSpot direkt im Herzen Gaardens. Gute Verweilmöglichkeiten in direkter Umgebung.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Vinetaplatz.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Vinetaplatz.png"),
                            new LatLng(54.31137, 10.14664),
                            CITY_KIEL
                    ));

                    add(new Attraction(
                            "HotSpot BioGaarden",
                            "Freifunk bei gemütlicher Einkaufs- und Kaffee-Atmosphäre.",
                            "Freifunk bei gemütlicher Einkaufs- und Kaffee-Atmosphäre.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Vinetaplatz.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Vinetaplatz.png"),
                            new LatLng(54.31097, 10.14784),
                            CITY_KIEL
                    ));

                }});

    }};

    /**
     * Creates a list of geofences based on the city locations
     */
    public static List<Geofence> getGeofenceList() {
        List<Geofence> geofenceList = new ArrayList<Geofence>();
        for (String city : CITY_LOCATIONS.keySet()) {
            LatLng cityLatLng = CITY_LOCATIONS.get(city);
            geofenceList.add(new Geofence.Builder()
                    .setCircularRegion(cityLatLng.latitude, cityLatLng.longitude, TRIGGER_RADIUS2)
                    .setRequestId(city)
                    .setTransitionTypes(TRIGGER_TRANSITION)
                    .setExpirationDuration(EXPIRATION_DURATION)
                    .build());
        }
        return geofenceList;
    }

    public static String getClosestCity(LatLng curLatLng) {
        if (curLatLng == null) {
            // If location is unknown return test city so some data is shown
            return TEST_CITY;
        }

        double minDistance = 0;
        String closestCity = null;
        for (Map.Entry<String, LatLng> entry: CITY_LOCATIONS.entrySet()) {
            double distance = SphericalUtil.computeDistanceBetween(curLatLng, entry.getValue());
            if (minDistance == 0 || distance < minDistance) {
                minDistance = distance;
                closestCity = entry.getKey();
            }
        }
        return closestCity;
    }
}
