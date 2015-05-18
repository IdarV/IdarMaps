package com.example.IdarMaps;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import com.google.android.gms.maps.model.LatLng;
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
public class WebkameraPullParser extends AsyncTask<URL, Void, ArrayList<Webcamera>> {
    private static final String ns = null;
    private MyActivity myActivity;

    public WebkameraPullParser(MyActivity myActivity){
        this.myActivity = myActivity;
    }

    public ArrayList<Webcamera> parse(InputStream in) throws IOException, XmlPullParserException {
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

    public ArrayList<Webcamera> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<Webcamera> results = new ArrayList<Webcamera>();
        Webcamera webcamera = null;
        parser.require(XmlPullParser.START_TAG, ns, "webkameraer");
        long start = System.currentTimeMillis();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            if (name != null) {
                if (name.equalsIgnoreCase("webkamera")) {
                    if (webcamera != null) {
                        webcamera.setLatLng();
                        results.add(webcamera);
                    }
                    webcamera = new Webcamera();
                }
                if(webcamera != null) {
                    if (name.equalsIgnoreCase("url")) {
                        parser.require(XmlPullParser.START_TAG, ns, "url");
                        String url = readText(parser);
                        webcamera.setUrl(url);
                        parser.require(XmlPullParser.END_TAG, ns, "url");
                    } else if (name.equalsIgnoreCase("stedsnavn")) {
                        parser.require(XmlPullParser.START_TAG, ns, "stedsnavn");
                        String stedsnavn = readText(parser);
                        webcamera.setStedsnavn(stedsnavn);
                    } else if (name.equalsIgnoreCase("veg")) {
                        parser.require(XmlPullParser.START_TAG, ns, "veg");
                        String veg = readText(parser);
                        webcamera.setVeg(veg);
                    } else if (name.equalsIgnoreCase("landsdel")) {
                        parser.require(XmlPullParser.START_TAG, ns, "landsdel");
                        String landsdel = readText(parser);
                        webcamera.setLandsdel(landsdel);
                    } else if (name.equalsIgnoreCase("lengdegrad")) {
                        parser.require(XmlPullParser.START_TAG, ns, "lengdegrad");
                        String lengdegrad = readText(parser);
                        webcamera.setLengdegrad(lengdegrad);
                    } else if (name.equalsIgnoreCase("breddegrad")) {
                        parser.require(XmlPullParser.START_TAG, ns, "breddegrad");
                        String breddegrad = readText(parser);
                        webcamera.setBreddegrad(breddegrad);
                    } else if(name.equalsIgnoreCase("info")) {
                        parser.require(XmlPullParser.START_TAG, ns, "info");
                        String info = "" + readText(parser);
                        webcamera.setInfo(info);
                    }
                }

            }

        }
        Log.wtf("Parser", "Time to read all data: " + ((System.currentTimeMillis() - start)) + " ms.");
        return results;
    }

    @Override
    protected ArrayList<Webcamera> doInBackground(URL... params) {
        ArrayList<Webcamera> l = new ArrayList<Webcamera>();
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

    protected void onPostExecute(ArrayList<Webcamera> webcameras) {
        myActivity.webcamerasMyActivity.clear();
        myActivity.webcamerasMyActivity.addAll(webcameras);
        myActivity.setCoords();
    }
}
