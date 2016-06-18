/*
 * Copyright 2015 Google Inc. All rights reserved.
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

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.meingaarden.AppCompatPreferenceActivity;
import com.example.meingaarden.MeinGaarden;
import com.example.meingaarden.SettingsActivity;
import com.example.meingaarden.common.Attraction;
import com.example.meingaarden.common.HotSpot;
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
public class TouristAttractions extends PreferenceActivity {

    public static final String CITY_SYDNEY = "Sydney";
    public static final String CITY_AHRENSBURG = "Ahrensburg";  // von flo
    public static final String CITY_KIEL = "Kiel";  // von flo
    public static final String CITY_KIEL_GAARDEN = "Kiel-Gaarden";  // von flo
    public static final String CITY_LABOE = "LABOE";  // von flo
    public static final String CITY_LÜTJENBURG = "Lütjenburg";  // von flo
    public static final String CITY_WELLSEE = "Wellsee";  // von flo

    public static final String TEST_CITY = CITY_SYDNEY;

    private static final float TRIGGER_RADIUS = 5000; // 2KM
    public static final String TEST = "pref_key_trigger_radius";
    private static final float TRIGGER_RADIUS3 = Long.getLong(TEST, 2000);
    private static final Long TRIGGER_RADIUS2 = Long.getLong("pref_key_trigger_radius");
    //Long test = Long.valueOf("pref_key_gps_duration");
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
     protected Long test = Long.valueOf(sharedPref.getString("pref_key_trigger_radius", "2000"));
    //private static final Long TRIGGER_RADIUS2 = test;
    private float TRIGGER_RADIUS4 = test;


    //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MeinGaarden.this);


    private static final int TRIGGER_TRANSITION = Geofence.GEOFENCE_TRANSITION_ENTER |
            Geofence.GEOFENCE_TRANSITION_EXIT;
    private static final long EXPIRATION_DURATION = Geofence.NEVER_EXPIRE;

    public static final Map<String, LatLng> CITY_LOCATIONS = new HashMap<String, LatLng>() {{
        put(CITY_SYDNEY, new LatLng(-33.873651, 151.2068896));
        //put(CITY_AHRENSBURG, new LatLng(53.67357, 10.2358)); // Rathaus
        put(CITY_KIEL, new LatLng(54.32262, 10.14784)); // Rathaus
        //put(CITY_KIEL_GAARDEN, new LatLng(54.31146231, 10.14662517)); // Vinetaplatz
        //put(CITY_LABOE, new LatLng(54.41185, 10.23121)); // Marine-Ehrenmal
        //put(CITY_LÜTJENBURG, new LatLng(54.29567, 10.5901)); // Bismarckturm
        //put(CITY_WELLSEE, new LatLng(54.2843, 10.15473)); // EMC
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

                put(CITY_AHRENSBURG, new ArrayList<Attraction>() {{
                    add(new Attraction(
                            "Sydney Opera House",
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae bibendum justo, vitae cursus velit. Suspendisse potenti.",
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed vitae bibendum justo, vitae cursus velit. Suspendisse potenti. Suspendisse scelerisque risus justo, non tincidunt nibh blandit et. Vivamus elit lacus, luctus nec erat in, pharetra semper turpis. Quisque viverra nulla ligula, non pulvinar ante dictum sit amet. Vestibulum aliquet tortor mauris, vel suscipit nisl malesuada eget. Aliquam maximus dictum euismod. Maecenas leo quam, volutpat id diam eget, placerat fringilla ipsum. Nam pretium vehicula augue quis euismod.\n\nNam sed blandit magna. Vestibulum a fermentum arcu. Vestibulum et ligula at nisi luctus facilisis. Proin fermentum enim a nibh commodo finibus. Suspendisse justo elit, vulputate ut ipsum at, pellentesque auctor massa. Praesent vestibulum erat interdum imperdiet dapibus. In hac habitasse platea dictumst. Proin varius orci vitae tempor vulputate.\n\nEtiam sed mollis orci. Integer et ex sed tortor scelerisque blandit semper id libero. Nulla facilisi. Pellentesque tempor magna eget massa ultrices, et efficitur lectus finibus.",
                            Uri.parse("https://lh5.googleusercontent.com/-7fb5ybQhUbo/VGLWjIL4RmI/AAAAAAAAACM/2jLe_msj_tk/w600-no/IMG_0049.JPG"),
                            Uri.parse("https://lh3.googleusercontent.com/-EFEw6s7mT6I/VGLkCH4Xt4I/AAAAAAAAADY/ZlznhaQvb8E/w600-no/DSC_2775.JPG"),
                            new LatLng(-33.858667, 151.214028),
                            CITY_AHRENSBURG
                    ));
                }});

                put(CITY_KIEL, new ArrayList<Attraction>() {{

                    add(new Attraction(
                            "Vinetaplatz",
                            "Das Zentrum Gaardens. Beliebter Treffpunkt aller Kulturen.",
                            "Das Zentrum Gaardens. Beliebter Treffpunkt aller Kulturen.\n\nMittwochs und Samstags findet hier der Wochenmarkt statt.\n\n.....",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Vinetaplatz.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Vinetaplatz.png"),
                            new LatLng(54.31136, 10.14663),
                            CITY_KIEL
                    ));

                    add(new Attraction(
                            "BioGaarden",
                            "Erster Bioladen Gaardens mit gemütlicher Einkaufs- und Kaffee-Atmosphäre.",
                            "Kaiserstraße 56, 24143 Kiel.\n\nErster Bioladen Gaardens mit gemütlicher Einkaufs- und Kaffee-Atmosphäre.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/BioGaarden.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/BioGaarden.png"),
                            new LatLng(54.31097, 10.14784),
                            CITY_KIEL
                    ));

                    add(new Attraction(
                            "Gaardener Apotheke",
                            "Das Gebäude der Gaardener Apotheke entstand 1907.",
                            "Karlstal 33, 24143 Kiel.\n\nDas Gebäude der Gaardener Apotheke entstand 1907.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Apotheke_Gaarden.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Apotheke_Gaarden.png"),
                            new LatLng(54.3105298, 10.1457164),
                            CITY_KIEL
                    ));

                    add(new Attraction(
                            "Sommerbad Katzheide",
                            "Im Sommer ein Muss.",
                            "Im Sommer ein Muss. Im Jahre 1960 erbaut, erfuhr es 1993/94 seine letzte Grundsanierung.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Sommerbad_Katzheide.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Sommerbad_Katzheide.png"),
                            new LatLng(54.30966, 10.15629),
                            CITY_KIEL
                    ));

                    add(new Attraction(
                            "Volkspark Gaarden",
                            "Der Volkspark wird von Gaardenern eigentlich Werftpark genannt.",
                            "Der Volkspark wurde 1899 als Park für Bedienstete der kaiserlichen Werft eingeweiht.\n\n" +
                                    "1923-25 wurde der Park nach Plänen des Städtebauers Willy Hahn (1887-1930) und des Gartenarchitekten Leberecht Migge (1881-1935) zum Volkspark für Sport und Spiel umgebaut\n\n" +
                                    "1999 fand zum 100-jährigem Bestehen die letzte große Umgestaltung statt.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Werftpark.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Werftpark.png"),
                            new LatLng(54.31441, 10.15701),
                            CITY_KIEL
                    ));

                    add(new Attraction(
                            "Germaniahafen",
                            "1998 fertiggestelltes Hafenbecken für Gastsegler und Traditionsschiffe.",
                            "1998 fertiggestelltes Hafenbecken für Gastsegler und Traditionsschiffe.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Am_Germaniahafen.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Am_Germaniahafen.png"),
                            new LatLng(54.31489, 10.13659),
                            CITY_KIEL
                    ));

                    add(new Attraction(
                            "Jugendherberge",
                            "Das Zentrum Gaardens. Beliebter Treffpunkt aller Kulturen.",
                            "Das Zentrum Gaardens. Beliebter Treffpunkt aller Kulturen. Sed vitae bibendum justo, vitae cursus velit. Suspendisse potenti. Suspendisse scelerisque risus justo, non tincidunt nibh blandit et. Vivamus elit lacus, luctus nec erat in, pharetra semper turpis. Quisque viverra nulla ligula, non pulvinar ante dictum sit amet. Vestibulum aliquet tortor mauris, vel suscipit nisl malesuada eget. Aliquam maximus dictum euismod. Maecenas leo quam, volutpat id diam eget, placerat fringilla ipsum. Nam pretium vehicula augue quis euismod.\n\nNam sed blandit magna. Vestibulum a fermentum arcu. Vestibulum et ligula at nisi luctus facilisis. Proin fermentum enim a nibh commodo finibus. Suspendisse justo elit, vulputate ut ipsum at, pellentesque auctor massa. Praesent vestibulum erat interdum imperdiet dapibus. In hac habitasse platea dictumst. Proin varius orci vitae tempor vulputate.\n\nEtiam sed mollis orci. Integer et ex sed tortor scelerisque blandit semper id libero. Nulla facilisi. Pellentesque tempor magna eget massa ultrices, et efficitur lectus finibus.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/DJH_Kiel.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/DJH_Kiel.png"),
                            new LatLng(54.31383, 10.14248),
                            CITY_KIEL
                    ));

                    add(new Attraction(
                            "Schwimmhalle Gaarden",
                            "Badespass in der Johannesstrasse 8.",
                            "Badespass in der Johannesstrasse 8.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Schwimmhalle_Gaarden.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Schwimmhalle_Gaarden.png"),
                            new LatLng(54.31308, 10.14289),
                            CITY_KIEL
                    ));

                    add(new Attraction(
                            "Krupp-Siedlung",
                            "1901 entstanden erste Werkswohnungen der Krupp-Germania Werft.",
                            "1901 entstanden erste Werkswohnungen der Krupp-Germania Werft. Es entstand eine gute Anbindung der Arbeiter an die Werft und eine hohe Wohnqualität durch Toiletten innerhalb der Wohnungen, die den damaligen Standard übertrafen.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Krupp_Siedlung.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Krupp_Siedlung.png"),
                            new LatLng(54.30614, 10.14796),
                            CITY_KIEL
                    ));

                    add(new Attraction(
                            "Iltisbunker",
                            "Neuerdings wieder Veranstaltungsort wartet dieser Bunker mit einem beeindruckendem Gemälde auf.",
                            "Neuerdings wieder Veranstaltungsort wartet dieser Bunker mit einem beeindruckendem Gemälde auf.\n\n",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Iltisbunker.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Iltisbunker.png"),
                            new LatLng(54.30699, 10.14604),
                            CITY_KIEL
                    ));
                    /**
                    add(new Attraction(
                            "Subrosa",
                            "Das Zentrum Gaardens. Beliebter Treffpunkt aller Kulturen.",
                            "Das Zentrum Gaardens. Beliebter Treffpunkt aller Kulturen. Sed vitae bibendum justo, vitae cursus velit. Suspendisse potenti. Suspendisse scelerisque risus justo, non tincidunt nibh blandit et. Vivamus elit lacus, luctus nec erat in, pharetra semper turpis. Quisque viverra nulla ligula, non pulvinar ante dictum sit amet. Vestibulum aliquet tortor mauris, vel suscipit nisl malesuada eget. Aliquam maximus dictum euismod. Maecenas leo quam, volutpat id diam eget, placerat fringilla ipsum. Nam pretium vehicula augue quis euismod.\n\nNam sed blandit magna. Vestibulum a fermentum arcu. Vestibulum et ligula at nisi luctus facilisis. Proin fermentum enim a nibh commodo finibus. Suspendisse justo elit, vulputate ut ipsum at, pellentesque auctor massa. Praesent vestibulum erat interdum imperdiet dapibus. In hac habitasse platea dictumst. Proin varius orci vitae tempor vulputate.\n\nEtiam sed mollis orci. Integer et ex sed tortor scelerisque blandit semper id libero. Nulla facilisi. Pellentesque tempor magna eget massa ultrices, et efficitur lectus finibus.",
                            Uri.parse("https://lh5.googleusercontent.com/-7fb5ybQhUbo/VGLWjIL4RmI/AAAAAAAAACM/2jLe_msj_tk/w600-no/IMG_0049.JPG"),
                            Uri.parse("https://lh3.googleusercontent.com/-EFEw6s7mT6I/VGLkCH4Xt4I/AAAAAAAAADY/ZlznhaQvb8E/w600-no/DSC_2775.JPG"),
                            new LatLng(54.31458, 10.14903),
                            CITY_KIEL
                    ));
                    */
                    add(new Attraction(
                            "Gemeinschaftsschule Am Brook",
                            "Fröbelschule - Erbaut 1910 vom Städtischen Hochbauamt.",
                            "Fröbelschule - Erbaut 1910 vom Städtischen Hochbauamt. Durch Bomben 1944 zerstört. Wiedererbaut von Architekt BDA Ernst Prinz AD 1951.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Froebelschule.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Froebelschule.png"),
                            new LatLng(54.30573, 10.14507),
                            CITY_KIEL
                    ));
                    /**
                    add(new Attraction(
                            "Räucherei",
                            "Das Zentrum Gaardens. Beliebter Treffpunkt aller Kulturen.",
                            "Das Zentrum Gaardens. Beliebter Treffpunkt aller Kulturen.\n\nNam sed blandit magna. Vestibulum a fermentum arcu. Vestibulum et ligula at nisi luctus facilisis. Proin fermentum enim a nibh commodo finibus. Suspendisse justo elit, vulputate ut ipsum at, pellentesque auctor massa. Praesent vestibulum erat interdum imperdiet dapibus. In hac habitasse platea dictumst. Proin varius orci vitae tempor vulputate.\n\nEtiam sed mollis orci. Integer et ex sed tortor scelerisque blandit semper id libero. Nulla facilisi. Pellentesque tempor magna eget massa ultrices, et efficitur lectus finibus.",
                            Uri.parse("https://lh5.googleusercontent.com/-7fb5ybQhUbo/VGLWjIL4RmI/AAAAAAAAACM/2jLe_msj_tk/w600-no/IMG_0049.JPG"),
                            Uri.parse("https://lh3.googleusercontent.com/-EFEw6s7mT6I/VGLkCH4Xt4I/AAAAAAAAADY/ZlznhaQvb8E/w600-no/DSC_2775.JPG"),
                            new LatLng(54.30776, 10.14514),
                            CITY_KIEL
                    ));
                    */
                    add(new Attraction(
                            "Medusa",
                            "Im Medusa finden regelmäßig private und öffentliche Veranstaltungen statt.",
                            "Im Medusa finden regelmäßig private und öffentliche Veranstaltungen statt.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Medusahof.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Medusahof.png"),
                            new LatLng(54.31036, 10.14816),
                            CITY_KIEL
                    ));

                    add(new Attraction(
                            "Mevlana Moschee (Starpalast)",
                            "Der ehemalige Starpalast ist nun die Mevlana Moschee",
                            "Das Zentrum Gaardens. Beliebter Treffpunkt aller Kulturen. Sed vitae bibendum justo, vitae cursus velit. Suspendisse potenti. Suspendisse scelerisque risus justo, non tincidunt nibh blandit et. Vivamus elit lacus, luctus nec erat in, pharetra semper turpis. Quisque viverra nulla ligula, non pulvinar ante dictum sit amet. Vestibulum aliquet tortor mauris, vel suscipit nisl malesuada eget. Aliquam maximus dictum euismod. Maecenas leo quam, volutpat id diam eget, placerat fringilla ipsum. Nam pretium vehicula augue quis euismod.\n\nNam sed blandit magna. Vestibulum a fermentum arcu. Vestibulum et ligula at nisi luctus facilisis. Proin fermentum enim a nibh commodo finibus. Suspendisse justo elit, vulputate ut ipsum at, pellentesque auctor massa. Praesent vestibulum erat interdum imperdiet dapibus. In hac habitasse platea dictumst. Proin varius orci vitae tempor vulputate.\n\nEtiam sed mollis orci. Integer et ex sed tortor scelerisque blandit semper id libero. Nulla facilisi. Pellentesque tempor magna eget massa ultrices, et efficitur lectus finibus.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Mevlana_Moschee.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Mevlana_Moschee.png"),
                            new LatLng(54.30995, 10.14668),
                            CITY_KIEL
                    ));

                    add(new Attraction(
                            "Jüdische Gemeinde",
                            "Jüdische Gemeinde Kiel und Region",
                            "Das Zentrum Gaardens. Beliebter Treffpunkt aller Kulturen. Sed vitae bibendum justo, vitae cursus velit. Suspendisse potenti. Suspendisse scelerisque risus justo, non tincidunt nibh blandit et. Vivamus elit lacus, luctus nec erat in, pharetra semper turpis. Quisque viverra nulla ligula, non pulvinar ante dictum sit amet. Vestibulum aliquet tortor mauris, vel suscipit nisl malesuada eget. Aliquam maximus dictum euismod. Maecenas leo quam, volutpat id diam eget, placerat fringilla ipsum. Nam pretium vehicula augue quis euismod.\n\nNam sed blandit magna. Vestibulum a fermentum arcu. Vestibulum et ligula at nisi luctus facilisis. Proin fermentum enim a nibh commodo finibus. Suspendisse justo elit, vulputate ut ipsum at, pellentesque auctor massa. Praesent vestibulum erat interdum imperdiet dapibus. In hac habitasse platea dictumst. Proin varius orci vitae tempor vulputate.\n\nEtiam sed mollis orci. Integer et ex sed tortor scelerisque blandit semper id libero. Nulla facilisi. Pellentesque tempor magna eget massa ultrices, et efficitur lectus finibus.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Juedische_Gemeinde.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Juedische_Gemeinde.png"),
                            new LatLng(54.31243, 10.14899),
                            CITY_KIEL
                    ));

                    add(new Attraction(
                            "Villa Karlstal",
                            "Das Zentrum Gaardens. Beliebter Treffpunkt aller Kulturen.",
                            "Das Zentrum Gaardens. Beliebter Treffpunkt aller Kulturen. Sed vitae bibendum justo, vitae cursus velit. Suspendisse potenti. Suspendisse scelerisque risus justo, non tincidunt nibh blandit et. Vivamus elit lacus, luctus nec erat in, pharetra semper turpis. Quisque viverra nulla ligula, non pulvinar ante dictum sit amet. Vestibulum aliquet tortor mauris, vel suscipit nisl malesuada eget. Aliquam maximus dictum euismod. Maecenas leo quam, volutpat id diam eget, placerat fringilla ipsum. Nam pretium vehicula augue quis euismod.\n\nNam sed blandit magna. Vestibulum a fermentum arcu. Vestibulum et ligula at nisi luctus facilisis. Proin fermentum enim a nibh commodo finibus. Suspendisse justo elit, vulputate ut ipsum at, pellentesque auctor massa. Praesent vestibulum erat interdum imperdiet dapibus. In hac habitasse platea dictumst. Proin varius orci vitae tempor vulputate.\n\nEtiam sed mollis orci. Integer et ex sed tortor scelerisque blandit semper id libero. Nulla facilisi. Pellentesque tempor magna eget massa ultrices, et efficitur lectus finibus.",
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Villa_Karlstal.png"),
                            Uri.parse("http://flo.visionenundideen.de/AndroidTest/pictures/600x450/Villa_Karlstal.png"),
                            new LatLng(54.31029, 10.14528),
                            CITY_KIEL
                    ));



                }});

                /** vorerst auskommentiert, um Absturz zu vermeiden ...
                put(CITY_WELLSEE, new ArrayList<Attraction>() {{
                    add(new Attraction(
                            "EMC",
                            "Arbeiten in angespanntem Betriebsklima.",
                            "Sie wollten schon immer in angespannter Atmosphäre Arbeiten?\n Mangelnde Qualität ist für Sie selbstverständlich?\n Ihnen mangelt es an Kompetenz und Führungsqualität?\n\n Dann freuen wir uns auf Ihre Bewerbungsunterlagen.",
                            Uri.parse("https://lh5.googleusercontent.com/-7fb5ybQhUbo/VGLWjIL4RmI/AAAAAAAAACM/2jLe_msj_tk/w600-no/IMG_0049.JPG"),
                            Uri.parse("https://lh3.googleusercontent.com/-EFEw6s7mT6I/VGLkCH4Xt4I/AAAAAAAAADY/ZlznhaQvb8E/w600-no/DSC_2775.JPG"),
                            new LatLng(54.2843, 10.15473),
                            CITY_WELLSEE
                    ));
                }});
                 */

    }};



    /**
     * Creates a list of geofences based on the city locations
     */
    public static List<Geofence> getGeofenceList() {

        List<Geofence> geofenceList = new ArrayList<Geofence>();

        for (String city : CITY_LOCATIONS.keySet()) {
            LatLng cityLatLng = CITY_LOCATIONS.get(city);
            geofenceList.add(new Geofence.Builder()
                    .setCircularRegion(cityLatLng.latitude, cityLatLng.longitude, TRIGGER_RADIUS)
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
