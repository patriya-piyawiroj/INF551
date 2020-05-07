package com.example.inf551;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class CountryJsonReader {
    ArrayList<String> countries = new ArrayList<String>();

    public boolean initialised = false;

    Context context;
    public CountryJsonReader(Context context) {
        this.context = context;
        new CountryJsonReader.RetrieveFeedTask().execute("");
    }

    public ArrayList<String> getCountries() {
        return countries;
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
            String url = "https://inf551-49f71.firebaseio.com/country.json";
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

                    String country = json.get(" Name").toString();
                    country = country.substring(2, country.length()-1).toLowerCase();
                    countries.add(country);
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
            String toWrite = SerializeObject.objectToString(countries);
            if (toWrite != null && !toWrite.equalsIgnoreCase("")) {
                SerializeObject.WriteSettings(context, toWrite, "countryList.dat");
            } else {
                SerializeObject.WriteSettings(context, "", "countryList.dat");
            }
        }
    }
}
