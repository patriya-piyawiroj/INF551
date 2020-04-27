package com.example.inf551;

import com.example.inf551.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;


public class ResultsActivity extends AppCompatActivity {

    private static final String TAG = ResultsActivity.class.getSimpleName();
    private Context context;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference();
    TableLayout table;
    String search = ""; // From search view
    ArrayList<DataSnapshot> data ;
    String[] country_hdrs = {"Country", "District", "ID", "Name","Population"};
    ProgressDialog progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        search = intent.getStringExtra("search");
        data = new ArrayList<DataSnapshot>();

        table = (TableLayout) findViewById(R.id.table);
        table.setStretchAllColumns(true);
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


    public void getAll()  {
        Query query;
//        query = dbRef.orderByChild("description").
//                    equalTo(itemText.getText().toString());
        search = search.substring(0,1).toUpperCase() + search.substring(1).toLowerCase();
        String cityname = " '" + search + "'";
        query = dbRef.child("city").orderByChild(" Name").equalTo(cityname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                data.add(iterator.next());
//                dataSnapshot = dataSnapshot.child("city");
//                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
//                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
//                while (iterator.hasNext()) {
//                    DataSnapshot snapshot = (DataSnapshot) iterator.next();
//                    data.add(snapshot);
                    Log.d(TAG, dataSnapshot.toString());
//                }
                populateTable();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void populateTable() {

        table.removeAllViews();
        progressBar.hide();

        Iterable<DataSnapshot> snapshotIterator = data.get(0).getChildren();
        Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

        for (int i = 0; i < country_hdrs.length; i++) {
            DataSnapshot snapshot = (DataSnapshot) iterator.next();

            final TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            final TextView text1 = new TextView(this);
            text1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            text1.setGravity(Gravity.CENTER);
            text1.setTextSize(18);
            text1.setPadding(5, 5, 5, 5);

            // Set key here
            String key = country_hdrs[i];
            text1.setText(key);
            row.addView(text1);

            final TextView text2 = new TextView(this);
            text2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            text2.setGravity(Gravity.CENTER);
            text2.setTextSize(18);
            text2.setPadding(5, 5, 5, 5);

            // Set value here
            Log.d(TAG, snapshot.toString());
            String value = snapshot.getValue().toString();
//            String value = valueSnapshot.exists() ? valueSnapshot.getValue().toString() : "";//.toString();
            text2.setText(value);
            row.addView(text2);

            table.addView(row);
        }


    }


}
