package com.example.IdarMaps;

import android.os.AsyncTask;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Cyzla on 18.05.2015.
 * http://developer.android.com/training/basics/network-ops/xml.html
 */
public class WebkameraPullParser extends AsyncTask<URL, Void, ArrayList<Webcamera>> {
    private static final String ns = null;
    private MyActivity myActivity;

    public WebkameraPullParser(MyActivity myActivity) {
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
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            if (name != null) {
                webcamera = saveWebcameraAndCreateNewIfNecessary(webcamera, name, results);
                readAndHandleNextXmlTag(webcamera, parser, name);
            }

        }
        return results;
    }

    @Override
    protected ArrayList<Webcamera> doInBackground(URL... params) {
        ArrayList<Webcamera> l = new ArrayList<Webcamera>();
        try {
            l = parse(params[0].openStream());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return l;

    }

    private Webcamera saveWebcameraAndCreateNewIfNecessary(Webcamera webcamera, String name, ArrayList<Webcamera> results) {
        if (name.equalsIgnoreCase("webkamera")) {
            if (webcamera != null) {
                webcamera.setLatLng();
                results.add(webcamera);
            }
            webcamera = new Webcamera();
        }
        return webcamera;

    }

    private void readAndHandleNextXmlTag(Webcamera webcamera, XmlPullParser parser, String name) throws IOException, XmlPullParserException {
        if (webcamera != null) {
            switch (name) {
                case ("url"):
                    parser.require(XmlPullParser.START_TAG, ns, "url");
                    webcamera.setUrl(readText(parser));
                    break;
                case ("stedsnavn"):
                    parser.require(XmlPullParser.START_TAG, ns, "stedsnavn");
                    webcamera.setStedsnavn(readText(parser));
                    break;
                case ("veg"):
                    parser.require(XmlPullParser.START_TAG, ns, "veg");
                    webcamera.setVeg(readText(parser));
                    break;
                case ("landsdel"):
                    parser.require(XmlPullParser.START_TAG, ns, "landsdel");
                    webcamera.setLandsdel(readText(parser));
                    break;
                case ("lengdegrad"):
                    parser.require(XmlPullParser.START_TAG, ns, "lengdegrad");
                    webcamera.setLengdegrad(readText(parser));
                    break;
                case ("breddegrad"):
                    parser.require(XmlPullParser.START_TAG, ns, "breddegrad");
                    webcamera.setBreddegrad(readText(parser));
                    break;
                case ("info"):
                    parser.require(XmlPullParser.START_TAG, ns, "info");
                    webcamera.setInfo(readText(parser));
                    break;
                default:
                    break;
            }
        }
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
