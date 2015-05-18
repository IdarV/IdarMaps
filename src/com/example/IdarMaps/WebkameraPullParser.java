package com.example.IdarMaps;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cyzla on 18.05.2015.
 * http://developer.android.com/training/basics/network-ops/xml.html
 */
public class WebkameraPullParser extends AsyncTask<URL, Void, List>{
    private static final String ns = null;


    public List parse(InputStream in) throws IOException, XmlPullParserException {
        try{
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        }finally {
            in.close();
        }
    }

    public List readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        List results = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "webkameraer");
        int i = 0;
        while(parser.next() != XmlPullParser.END_DOCUMENT && i < 100){
          /*  if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }*/
            String name = parser.getName();
            if(name != null) {
                i++;
                if (name.equalsIgnoreCase("url")) {
                    parser.require(XmlPullParser.START_TAG, ns, "url");
                    String title = readText(parser);
                    Log.wtf("Parserino", "URL : " + title);
                    parser.require(XmlPullParser.END_TAG, ns, "url");
                }
                else if (name.equalsIgnoreCase("stedsnavn")) {
                    parser.require(XmlPullParser.START_TAG, ns, "stedsnavn");
                    String stedsnavn = readText(parser);
                    Log.wtf("Parserino", "Stedsnavn: " + stedsnavn);
                }
                else if (name.equalsIgnoreCase("veg")) {
                    parser.require(XmlPullParser.START_TAG, ns, "veg");
                    String stedsnavn = readText(parser);
                    Log.wtf("Parserino", "veg: " + stedsnavn);
                }
                else if (name.equalsIgnoreCase("landsdel")) {
                    parser.require(XmlPullParser.START_TAG, ns, "landsdel");
                    String stedsnavn = readText(parser);
                    Log.wtf("Parserino", "landsdel: " + stedsnavn);
                }
                else if (name.equalsIgnoreCase("lengdegrad")) {
                    parser.require(XmlPullParser.START_TAG, ns, "lengdegrad");
                    String stedsnavn = readText(parser);
                    Log.wtf("Parserino", "lengdegrad: " + stedsnavn);
                }
                else if (name.equalsIgnoreCase("breddegrad")) {
                    parser.require(XmlPullParser.START_TAG, ns, "breddegrad");
                    String stedsnavn = readText(parser);
                    Log.wtf("Parserino", "breddegrad: " + stedsnavn);
                }

            }

        }

        return results;
    }

    @Override
    protected List doInBackground(URL... params) {
        List l = new ArrayList<>();
        try {
           l = parse(params[0].openStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return l;

    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
