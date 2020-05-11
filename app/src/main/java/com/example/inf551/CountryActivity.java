package com.example.inf551;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class CountryActivity extends AppCompatActivity {

    public class CityOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(CountryActivity.this, CityActivity.class);
            intent.putExtra("city", (String) v.getTag());
            intent.putExtra("code", countryCode.substring(1, countryCode.length()-1));

            startActivity(intent);
        }
    }

    public static final String TAG = CountryActivity.class.getSimpleName();

    String year = "";
    String search = "Thailand";
    String countryCode = "";
    String countryName = "";
    String generosity = "";
    String happiness = "";
    boolean searchByCode = false;
    ArrayList<String> cities = new ArrayList<String>();
    ArrayList<String> languages = new ArrayList<String>();

    ArrayList<DataSnapshot> data ;


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference();

    ProgressDialog progressBar;
    TableLayout table;
    TableLayout table2;
    TextView textview;
    TextView languageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        Intent intent = getIntent();
        String searchIntent = intent.getStringExtra("search");
        search = (searchIntent.isEmpty()) ? search : searchIntent;
        searchByCode = intent.getBooleanExtra("searchByCode", false);
        countryCode = intent.getStringExtra("countryCode");
        year = intent.getStringExtra("year");
        if (year==null){
            year = "2019";
        }
        data = new ArrayList<DataSnapshot>();

        table = (TableLayout) findViewById(R.id.countryTable);
        table2 = (TableLayout) findViewById(R.id.cityTable);
        table.setStretchAllColumns(true);
        textview = (TextView) findViewById(R.id.searchText);
        textview.setVisibility(View.INVISIBLE);
        languageText = (TextView) findViewById(R.id.languagesText);
        languageText.setVisibility(View.INVISIBLE);

        progressBar = new ProgressDialog(this);
//        table2 = (TableLayout) findViewById(R.id.cityTable);
        startLoadData();
    }

    public void startLoadData() {
        progressBar.setCancelable(false);
        progressBar.setMessage("Fetching Invoices..");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        getAll();
    }

    private void queryHappiess(){
        Query query;
        String happyq = countryName.trim();
        System.out.println("Name: " + happyq);
        query = dbRef.child(year).orderByChild("Country").equalTo(happyq);
        query.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot snapshot = iterator.next();
                    //    data.add(snapshot);
                  //  cities.add(snapshot.child(" Name").getValue().toString().substring(1));
                    generosity = snapshot.child("Generosity").getValue().toString();
                    happiness = snapshot.child("Happiness Score").getValue().toString();
                }
                newRow();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void newRow(){
        int padding = 5;
        String[] rank_names = {"Generosity", "Happiness Score"};
        String[] rank_values = {generosity, happiness};
//        table.removeAllViews();

        for (int i=0; i<2; i++) {

            final TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setWeightSum(2);

            final TextView text1 = new TextView(this);
            text1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
            text1.setGravity(Gravity.CENTER);
            text1.setTextSize(18);
            text1.setPadding(padding, padding, padding, padding);
            text1.setText(rank_names[i]);
            text1.setBackgroundColor(Color.parseColor("#f8f8f8"));

            final TextView text2 = new TextView(this);
            text2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
            text2.setGravity(Gravity.CENTER);
            text2.setTextSize(18);
            text2.setPadding(padding, padding, padding, padding);
            text2.setBackgroundColor(Color.parseColor("#ffffff"));
            text2.setText(rank_values[i]);

            row.addView(text1);
            row.addView(text2);
            table.addView(row);
        }
    }

    private void queryCity(){
        Query query;
        System.out.println(countryCode);
        query = dbRef.child("city").orderByChild(" CountryCode").equalTo(" "+countryCode);
        query.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot snapshot = (DataSnapshot) iterator.next();
                    //    data.add(snapshot);
                    cities.add(snapshot.child(" Name").getValue().toString().substring(1));
                    //    Log.d(TAG, snapshot.child(" Name").getValue().toString());
                }
                setCities();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void queryLanguage(){
        Query query;
        System.out.println("In language: " + countryCode);
        query = dbRef.child("countrylanguage").orderByChild("CountryCode").equalTo(countryCode);
        query.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot snapshot = (DataSnapshot) iterator.next();
                    //    data.add(snapshot);
                    //  cities.add(snapshot.child(" Name").getValue().toString().substring(1));
//                    Log.d(TAG, snapshot.child(" Language").getValue().toString());
                    String language = (String) snapshot.child(" Language").getValue();
                    languages.add(language);
                }
                setLanguage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setLanguage() {
        if (data.isEmpty()) {
            return;
        }
        languageText.setVisibility(View.VISIBLE);
        if (languages.isEmpty()) {
            languageText.setText("No languages found");
            return;
        }
        String text = "Languages: ";
        Iterator<String> it = languages.iterator();
        String language = it.next();
        text += language;
        while (it.hasNext()) {
            text += ", ";
            text += it.next();
        }
        languageText.setText(text);
    }

    private void setCities(){
        table2.removeAllViews();
        if (data.isEmpty()) {
            return;
        }

        int padding = 5;

        final TableRow header = new TableRow(this);
        header.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        final TextView headerText = new TextView(this);
        headerText.setText("Cities in " + countryName);
        headerText.setGravity(Gravity.CENTER);
        headerText.setBackgroundColor(Color.parseColor("#f0f0f0"));
        TableRow.LayoutParams layout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layout.span = 4;
        headerText.setLayoutParams(layout);
        header.addView(headerText);
        table2.addView(header);

        for (int i = 0; i < cities.size();){
            final TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            for (int j=0; j<2; j++) {
                if (i>=cities.size()) {continue;}
                final TextView text1 = new TextView(this);
                text1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                text1.setGravity(Gravity.CENTER);
                text1.setTextSize(18);
                text1.setPadding(padding, padding, padding, padding);
                text1.setBackgroundColor(Color.parseColor("#ffffff"));

                String city = cities.get(i);
                city = city.substring(1, city.length()-1);
                text1.setText(city);
                text1.setTag(city);
                text1.setOnClickListener(new CityOnClickListener());
                row.addView(text1);
                i++;
            }
            table2.addView(row);
        }

    }


    public void getAll() {
        Query query;

        if (searchByCode) {
            String name = countryCode.substring(1);
            query = dbRef.child("country").orderByChild(" Code").equalTo(name);

        } else {
            search = Utility.capitalizeString(search.toLowerCase());
            String name = " '" + search + "'";
            query = dbRef.child("country").orderByChild(" Name").equalTo(name);
        }
        query.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot snapshot = (DataSnapshot) iterator.next();
                    data.add(snapshot);
                    Log.d(TAG, dataSnapshot.toString());
                }
                setCountry();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setCountry() {
        table.removeAllViews();
        textview.setVisibility(View.VISIBLE);
        progressBar.hide();

        if (data.isEmpty()) {
            textview.setText("No results found");
            return;
        }

        for (int n = -1; n < data.size(); n++) {
            Iterable<DataSnapshot> snapshotIterator = (n==-1) ? data.get(n+1).getChildren() : data.get(n).getChildren() ;
            Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
            boolean isNotHeader = true;
            while (iterator.hasNext() && isNotHeader) {
                if (n == -1) {isNotHeader = false;};
                DataSnapshot snapshot = (DataSnapshot) iterator.next();
                int padding = 8;

                final TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                row.setWeightSum(2);

                final TextView text1 = new TextView(this);
                text1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
                text1.setGravity(Gravity.CENTER);
                text1.setTextSize(18);
                text1.setPadding(padding, padding, padding, padding);

                // Set key here
                String key = (String) snapshot.getKey();

                if (key.contains("Name")){
                    countryName = snapshot.getValue().toString().replace("'","");
                    textview.setText(String.format("Search results for: %s (COUNTRY)", countryName));
                }

                if (key.equals(" Code")){
                    countryCode = snapshot.getValue().toString();
                }

                if (n == -1) {
                    text1.setText(" ");
                    text1.setBackgroundColor(Color.parseColor("#f0f0f0"));
                } else {
                    text1.setText(key);
                    text1.setBackgroundColor(Color.parseColor("#f8f8f8"));
                }
                row.addView(text1);

                final TextView text2 = new TextView(this);
                text2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
                text2.setGravity(Gravity.CENTER);
                text2.setTextSize(18);
                text2.setPadding(padding, padding, padding, padding);

                // Set value here
                if (n == -1) {
                    text2.setText("Countries");
                    text2.setBackgroundColor(Color.parseColor("#f0f0f0"));
                } else {
                    String value = (String) snapshot.getValue();
                    StringBuilder formatter = new StringBuilder(value);
                    formatter.delete(0, 1);
                    formatter.deleteCharAt(formatter.length() - 1);
                    value = formatter.toString().replace("'", "");
                    text2.setBackgroundColor(Color.parseColor("#ffffff"));
                    text2.setText(value);

                }
                row.addView(text2);

                // Add onclick listener to go to next page
                if (n > -1) {
                    row.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
//                            Intent intent = new Intent(CityActivity.this, CountryActivity.class);
//                            intent.putExtra("search", countryCode);
//                            startActivity(intent);
                        }
                    });
                }

                table.addView(row);
            }
            // add separator row
            final TableRow trSep = new TableRow(this);
            TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            trParamsSep.setMargins(5, 5, 5, 5);

            trSep.setLayoutParams(trParamsSep);
            TextView tvSep = new TextView(this);
            TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            tvSepLay.span = 4;
            tvSep.setLayoutParams(tvSepLay);
            tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
            tvSep.setHeight(1);

            trSep.addView(tvSep);
            table.addView(trSep, trParamsSep);
        }
        queryLanguage();
        queryCity();
        queryHappiess();
    }
}
