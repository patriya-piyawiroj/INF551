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

    public static final String TAG = CountryActivity.class.getSimpleName();

    String search = "Thailand";
    String countryCode = "";
    String countryName = "";
    boolean searchByCode = false;

    ArrayList<DataSnapshot> data ;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference();

    ProgressDialog progressBar;
    TableLayout table;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        Intent intent = getIntent();
        String searchIntent = intent.getStringExtra("search");
        search = (searchIntent.isEmpty()) ? search : searchIntent;
        searchByCode = intent.getBooleanExtra("searchByCode", false);
        countryCode = intent.getStringExtra("countryCode");

        data = new ArrayList<DataSnapshot>();

        table = (TableLayout) findViewById(R.id.countryTable);
        table.setStretchAllColumns(true);
        textview = (TextView) findViewById(R.id.searchText);
        progressBar = new ProgressDialog(this);
        startLoadData();
    }

    public void startLoadData() {
        progressBar.setCancelable(false);
        progressBar.setMessage("Fetching Invoices..");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        getAll();
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
                populateTable();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populateTable() {
        table.removeAllViews();
        progressBar.hide();

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
                    textview.setText(String.format("Search results for : %s (COUNTRY)", countryName));
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
    }
}
