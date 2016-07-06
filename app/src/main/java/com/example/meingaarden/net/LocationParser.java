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

package com.example.meingaarden.net;

import android.net.Uri;
import android.text.format.Time;
import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parses location-xml-files.
 *
 * <p>Given an InputStream representation of a feed, it returns a List of entries,
 * where each list element represents a single entry (post) in the XML feed.
 *
 * <p>An example of an Atom feed can be found at:
 * http://en.wikipedia.org/w/index.php?title=Atom_(standard)&oldid=560239173#Example_of_an_Atom_1.0_feed
 */
public class LocationParser {

    // Constants indicting XML element names that we're interested in
    private static final int TAG_ID = 1;
    private static final int TAG_NAME = 2;
    private static final int TAG_PUBLISHED = 3;
    private static final int TAG_LINK = 4;
    private static final int TAG_DESCRIPTION = 5; // wird in Zeile 165 verwendet
    private static final int TAG_LONGDESCRIPTION = 6;
    private static final int TAG_IMAGEURL = 7;
    private static final int TAG_SECONDARYIMAGEURL = 8;
    private static final int TAG_LOCATION = 9;
    private static final int TAG_CITY = 10;

    // We don't use XML namespaces
    private static final String ns = null;

    /** Parse an Atom feed, returning a collection of Entry objects.
     *
     * @param in Atom feed, as a stream.
     * @return List of {@link LocationParser.Ortseintrag} objects.
     * @throws XmlPullParserException on error parsing feed.
     * @throws IOException on I/O error.
     */
    public List<Ortseintrag> parse(InputStream in)
            throws XmlPullParserException, IOException, ParseException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    /**
     * Decode a feed attached to an XmlPullParser.
     *
     * @param parser Incoming XMl
     * @return List of {@link LocationParser.Ortseintrag} objects.
     * @throws XmlPullParserException on error parsing feed.
     * @throws IOException on I/O error.
     */
    private List<Ortseintrag> readFeed(XmlPullParser parser)
            throws XmlPullParserException, IOException, ParseException {
        List<Ortseintrag> ortseintraege = new ArrayList<Ortseintrag>();

        // Search for <locations> tags. These wrap the beginning/end of an Atom document.
        //
        // Example:
        // <?xml version="1.0" encoding="utf-8"?>
        // <locations xmlns="http://localhost/2016">
        // ...
        // </locations>
        parser.require(XmlPullParser.START_TAG, ns, "locations");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the <entry> tag. This tag repeates inside of <feed> for each
            // article in the feed.
            //
            // Example:
            // <entry>
            //   <title>Article title</title>
            //   <link rel="alternate" type="text/html" href="http://example.com/article/1234"/>
            //   <link rel="edit" href="http://example.com/admin/article/1234"/>
            //   <id>urn:uuid:218AC159-7F68-4CC6-873F-22AE6017390D</id>
            //   <published>2003-06-27T12:00:00Z</published>
            //   <updated>2003-06-28T12:00:00Z</updated>
            //   <summary>Article summary goes here.</summary>
            //   <author>
            //     <name>Rick Deckard</name>
            //     <email>deckard@example.com</email>
            //   </author>
            // </entry>
            if (name.equals("ortseintrag")) {
                ortseintraege.add(readOrtseintrag(parser));
            } else {
                skip(parser);
            }
        }
        return ortseintraege;
    }

    /**
     * Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
     * off to their respective "read" methods for processing. Otherwise, skips the tag.
     */
    private Ortseintrag readOrtseintrag(XmlPullParser parser)
            throws XmlPullParserException, IOException, ParseException {
        parser.require(XmlPullParser.START_TAG, ns, "ortseintrag");
        String id = null;
        String name = null;
        String link = null;
        long publishedOn = 0;
        String description = null;
        String longDescription = null;
        String imageUrl = null;
        Uri secondaryImageUrl = null;
        //LatLng location = null; // alt: hat weiter unten Probleme gemacht, da kein String
        String location = null;
        String city = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagname = parser.getName();
            if (tagname.equals("id")){
                // Example: <id>urn:uuid:218AC159-7F68-4CC6-873F-22AE6017390D</id>
                id = readTag(parser, TAG_ID);
            } else if (tagname.equals("name")) {
                // Example: <name>Article title</name>
                name = readTag(parser, TAG_NAME);
            } else if (tagname.equals("link")) {
                // Example: <link rel="alternate" type="text/html" href="http://example.com/article/1234"/>
                //
                // Multiple link types can be included. readAlternateLink() will only return
                // non-null when reading an "alternate"-type link. Ignore other responses.
                String tempLink = readTag(parser, TAG_LINK);
                if (tempLink != null) {
                    link = tempLink;
                }
            } else if (tagname.equals("published")) {
                // Example: <published>2003-06-27T12:00:00Z</published>
                Time t = new Time();
                t.parse3339(readTag(parser, TAG_PUBLISHED));
                publishedOn = t.toMillis(false);
            } else if (tagname.equals("description")) {
                // Example: <description>Article description</description>
                description = readTag(parser, TAG_DESCRIPTION);
            } else if (tagname.equals("longdescription")) {
                // Example: <description>Article description</description>
                longDescription = readTag(parser, TAG_LONGDESCRIPTION);
            } else if (tagname.equals("imageurl")) {
                // Example: <description>Article description</description>
                imageUrl = readTag(parser, TAG_IMAGEURL);
            } else if (tagname.equals("secondaryimageurl")) {
                // Example: <description>Article description</description>
                secondaryImageUrl = Uri.parse(readTag(parser, TAG_SECONDARYIMAGEURL));
            } else if (tagname.equals("location")) {
                // Example: <description>Article description</description>
                location = readTag(parser, TAG_LOCATION);
            } else if (tagname.equals("city")) {
                // Example: <description>Article description</description>
                city = readTag(parser, TAG_CITY);
            } else {
                skip(parser);
            }
        }
        return new Ortseintrag(id, name, link, publishedOn, description, longDescription, imageUrl, secondaryImageUrl, location, city);
    }

    /**
     * Process an incoming tag and read the selected value from it.
     */
    private String readTag(XmlPullParser parser, int tagType)
            throws IOException, XmlPullParserException {
        String tag = null;
        String endTag = null;

        switch (tagType) {
            case TAG_ID:
                return readBasicTag(parser, "id");
            case TAG_NAME:
                return readBasicTag(parser, "name");
            case TAG_PUBLISHED:
                return readBasicTag(parser, "published");
            case TAG_DESCRIPTION:
                return readBasicTag(parser, "description");
            case TAG_LONGDESCRIPTION:
                return readBasicTag(parser, "longdescription");
            case TAG_IMAGEURL:
                return readBasicTag(parser, "imageurl");
            case TAG_SECONDARYIMAGEURL:
                return readBasicTag(parser, "secondaryimageurl");
            case TAG_LOCATION:
                return readBasicTag(parser, "location");
            case TAG_CITY:
                return readBasicTag(parser, "city");
            case TAG_LINK:
                return readAlternateLink(parser);
            default:
                throw new IllegalArgumentException("Unknown tag type: " + tagType);
        }
    }

    /**
     * Reads the body of a basic XML tag, which is guaranteed not to contain any nested elements.
     *
     * <p>You probably want to call readTag().
     *
     * @param parser Current parser object
     * @param tag XML element tag name to parse
     * @return Body of the specified tag
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readBasicTag(XmlPullParser parser, String tag)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return result;
    }

    /**
     * Processes link tags in the feed.
     */
    private String readAlternateLink(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String link = null;
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (relType.equals("alternate")) {
            link = parser.getAttributeValue(null, "href");
        }
        while (true) {
            if (parser.nextTag() == XmlPullParser.END_TAG) break;
            // Intentionally break; consumes any remaining sub-tags.
        }
        return link;
    }

    /**
     * For the tags title and summary, extracts their text values.
     */
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    /**
     * Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
     * if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
     * finds the matching END_TAG (as indicated by the value of "depth" being 0).
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    /**
     * This class represents a single entry (post) in the XML feed.
     *
     * It includes the data members "name", "link", and "summary."
     */
    public static class Ortseintrag {
        public final String id;
        public final String name;
        public final String link;
        public final long published;
        public final String description;
        public final String longDescription;
        // public final Uri imageUrl; // macht leider bisher Probleme ... deshalb neu als String definiert
        public final String imageUrl;
        public final Uri secondaryImageUrl;
        public final String location;
        public final String city;

        Ortseintrag(String id, String name, String link, long published, String description, String longDescription, String imageUrl, Uri secondaryImageUrl, String location, String city) {
            this.id = id;
            this.name = name;
            this.link = link;
            this.published = published;
            this.description = description;
            this.longDescription = longDescription;
            this.imageUrl = imageUrl;
            this.secondaryImageUrl = secondaryImageUrl;
            this.location = location;
            this.city = city;
        }
    }
}
