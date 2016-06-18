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

package com.example.meingaarden.ui;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.meingaarden.R;
import com.example.meingaarden.common.Constants;
import com.example.meingaarden.common.HotSpot;
import com.example.meingaarden.common.Utils;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;

import static com.example.meingaarden.provider.HotSpots.HOTSPOTS;

/**
 * The tourist attraction detail fragment which contains the details of a
 * a single attraction (contained inside
 * {@link DetailActivity}).
 */
public class DetailHotSpotFragment extends Fragment {

    private static final String EXTRA_HOTSPOT = "hotspot";
    private HotSpot mHotspot;

    public static DetailHotSpotFragment createInstance(String hotspotName) {
        DetailHotSpotFragment detailFragment = new DetailHotSpotFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_HOTSPOT, hotspotName);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    public DetailHotSpotFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        String hotspotName = getArguments().getString(EXTRA_HOTSPOT);
        mHotspot = findHotspot(hotspotName);

        if (mHotspot == null) {
            getActivity().finish();
            return null;
        }

        TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        TextView descTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        TextView distanceTextView = (TextView) view.findViewById(R.id.distanceTextView);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        FloatingActionButton mapFab = (FloatingActionButton) view.findViewById(R.id.mapFab);

        LatLng location = Utils.getLocation(getActivity());
        String distance = Utils.formatDistanceBetween(location, mHotspot.location);
        if (TextUtils.isEmpty(distance)) {
            distanceTextView.setVisibility(View.GONE);
        }

        nameTextView.setText(hotspotName);
        distanceTextView.setText(distance);
        descTextView.setText(mHotspot.longDescription);

        int imageSize = getResources().getDimensionPixelSize(R.dimen.image_size)
                * Constants.IMAGE_ANIM_MULTIPLIER;
        Glide.with(getActivity())
                .load(mHotspot.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.color.lighter_gray)
                .override(imageSize, imageSize)
                .into(imageView);

        mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constants.MAPS_INTENT_URI +
                        //Uri.encode(mAttraction.name + ", " + mAttraction.city))); // alte Methode, fragt Name+Stadt ab, führt bei "unbekannten" Namen zu fehlerhafter Suche
                        mHotspot.location.latitude+ ", " +mHotspot.location.longitude)); // neue Methode, fragt nach Lat+Lng, zeigt immer einen Standort an
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Some small additions to handle "up" navigation correctly
                Intent upIntent = NavUtils.getParentActivityIntent(getActivity());
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // Check if up activity needs to be created (usually when
                // detail screen is opened from a notification or from the
                // Wearable app
                if (NavUtils.shouldUpRecreateTask(getActivity(), upIntent)
                        || getActivity().isTaskRoot()) {

                    // Synthesize parent stack
                    TaskStackBuilder.create(getActivity())
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // On Lollipop+ we finish so to run the nice animation
                    getActivity().finishAfterTransition();
                    return true;
                }

                // Otherwise let the system handle navigating "up"
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Really hacky loop for finding attraction in our static content provider.
     * Obviously would not be used in a production app.
     */
    private HotSpot findHotspot(String hotspotName) {
        for (Map.Entry<String, List<HotSpot>> hotspotsList : HOTSPOTS.entrySet()) {
            List<HotSpot> hotspots = hotspotsList.getValue();
            for (HotSpot hotspot : hotspots) {
                if (hotspotName.equals(hotspot.name)) {
                    return hotspot;
                }
            }
        }
        return null;
    }
}
