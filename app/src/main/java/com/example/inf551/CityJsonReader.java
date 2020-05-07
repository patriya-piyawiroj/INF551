package com.example.inf551;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CityJsonReader {
    ArrayList<String> cities = new ArrayList<String>();

    public boolean initialised = false;
    Context context;

    public CityJsonReader(Context context) {
        this.context = context;
        new RetrieveFeedTask().execute("");
    }

    public ArrayList<String> getCities() {
        return cities;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


    class RetrieveFeedTask extends AsyncTask<String, String, JSONObject> {
        private Exception exception;

        protected JSONObject doInBackground(String... urls) {
            String url = "https://inf551-49f71.firebaseio.com/city.json";
            InputStream is = null;
            try {
                is = new URL(url).openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                Log.d("", "JSON" + jsonText);
                JSONArray array = new JSONArray(jsonText);

                int id = 0;
                for (int i = 0 ; i < array.length(); i++ ) {
                    JSONObject json = (JSONObject) array.get(i);
                    System.out.println(json);

                    String city = json.get(" Name").toString();
                    city = city.substring(1, city.length()-1).toLowerCase();
                    String country = json.get(" CountryCode").toString().substring(1);
                    country = country.substring(1, country.length()-1).toLowerCase();
                    city += " (" + country + ") ";
                    city = city.substring(1, city.length()-1).toLowerCase();
                    cities.add(city);
                }
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(JSONObject result) {
            String toWrite = SerializeObject.objectToString(cities);
            if (toWrite != null && !toWrite.equalsIgnoreCase("")) {
                SerializeObject.WriteSettings(context, toWrite, "cityList.dat");
            } else {
                SerializeObject.WriteSettings(context, "", "cityList.dat");
            }
        }
    }
}