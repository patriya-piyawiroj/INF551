package com.example.inf551;

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

public class JsonReader {
    List result = new ArrayList<>();;
    Map <String, ArrayList<Integer>> nameMap = new HashMap<String, ArrayList<Integer>>(); // Maps name to possible ids
    Map <Integer, String> idMap = new HashMap<Integer, String>();  // Maps id to name
    ArrayList<Node> links = new ArrayList<Node>(); // Shows links between ids

    public boolean initialised = false;

    private class Node {
        public Integer parent;
        public Integer child;

        public Node(Integer parent, Integer child) {
            this.parent = parent;
            this.child = child;
        }

        public void setChild(Integer child) {
            this.child = child;
        }

        @NonNull
        @Override
        public String toString() {
            return String.format("[%s, %s]",parent,child);
        }
    }


    public void printMap() {
        Iterator<Map.Entry<String, ArrayList<Integer>>> iterator = nameMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<Integer>> entry = iterator.next();
            String format = String.format("[%s, %s]", entry.getKey(), entry.getValue().toString());
            System.out.println(format);
        }
    }

    public ArrayList<String> search(String term) {
        if (initialised) {
            return getTerms(term);
        } else {
            return null;
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public ArrayList<String> getTerms(String searchTerm) {
        ArrayList<String> strings = new ArrayList<String>();
        ArrayList<Integer> list = nameMap.get(searchTerm);
        for (Integer word : list) {
          strings.add(trace(word));
        }
        return strings;
    }

    public String translate(ArrayList<Integer> idArray) {
        String location = "";
        for (Integer id : idArray) {
            if (id == null) { continue; }
            location += idMap.get(id);
            location += " ";
            // TODO: remove space
        }
        return location;
    }

    public String trace(Integer id) {
        ArrayList<Integer> list = traceParent(id);
        list.add(id);
        list.addAll(traceChild(id));
        return translate(list);
    }

    public ArrayList<Integer> traceParent(Integer id) {
        Integer parent = links.get(id).parent;
        ArrayList<Integer> parentList = new ArrayList<Integer>();
        parentList.add(parent);
        while (parent != null) {
            parent = links.get(parent).parent;
            parentList.add(0, parent);
        }
        return parentList;
    }

    public ArrayList<Integer> traceChild(Integer id) {
        Integer child = links.get(id).child;
        ArrayList<Integer> childList = new ArrayList<Integer>();
        childList.add(child);
        while (child != null) {
            child = links.get(child).child;
            childList.add(child);
        }
        return childList;
    }

    public  void test() throws Exception {
        new RetrieveFeedTask().execute("");

//        JSONObject json = readJsonFromUrl("https://graph.facebook.com/19292868552");
//        System.out.println(json.toString());
//        System.out.println(json.get("id"));

//        String json = readUrl("https://www.javascriptkit.com/"
//                + "dhtmltutors/javascriptkit.json");
//
//        Gson gson = new Gson();
//        Page page = gson.fromJson(json, Page.class);
//
//        System.out.println(page.title);
//        for (Item item : page.items)
//            System.out.println("    " + item.title);
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
                    String key = json.get(" Name").toString().substring(1);
                    key = key.substring(1, key.length()-1).toLowerCase();
                    String value = json.get(" ID").toString();
                    value = value.substring(1, value.length()-1);
                    int val = Integer.parseInt(value);

                    Integer previous = null;
                    int position = 0;
                    for (String word : key.split("\\W")){
                        if (word.isEmpty()){ continue; }

                        // Populate name map
                        idMap.put(id, word);

                        // Populate id map
                        ArrayList<Integer> ids = (nameMap.containsKey(word)) ? nameMap.get(word) : new ArrayList<Integer>();
                        ids.add(id);
                        nameMap.put(word, ids);

                        // Populate links
                        links.add(new Node(previous, null));
                        if (position > 0) {
                            links.get(id - 1).setChild(id);
                        }
                        previous = id;
                        id++;
                        position++;
                    }
                }
//                JSONObject json = new JSONObject("{\"balance\": 1000.21, \"num\":100, \"is_vip\":true, \"name\":\"foo\"}");
//                printMap();
//                ArrayList<Integer> test = new ArrayList<Integer>();
//                test.add(null);
//                test.add(2590);
//                test.add(1456);
//                test.add(3304);
//                test.add(1561);
//                System.out.println(translate(test));
//
//                // los; [179, 823, 857, 881, 3203, 3281, 3407, 4514, 4845, 5062]]
//                System.out.println("Links:");
//                System.out.println(links);
//
//                System.out.println(traceChild(179));
//                System.out.println(traceParent(179));
//                System.out.println(trace(179));
//
//                // 1097, 2411, 3685, 5061]
//                System.out.println(getTerms("los"));
//                System.out.println(array.length());
//                System.out.println(map.size());
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
            initialised = true;
        }
    }
}