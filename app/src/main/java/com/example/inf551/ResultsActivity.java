package com.example.inf551;

import com.example.inf551.R;
import android.content.Context;
import android.content.Intent;
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
    String[] country_hdrs = {"Population", "CountryCode", "ID", "Name","District"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        search = intent.getStringExtra("search");
        data = new ArrayList<DataSnapshot>();

        table = (TableLayout) findViewById(R.id.table);
        getAll();
    }

    public void getAll() {
        Query query;
//        query = dbRef.orderByChild("description").
//                    equalTo(itemText.getText().toString());

        query = dbRef.orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot = dataSnapshot.child("city");
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot snapshot = (DataSnapshot) iterator.next();
                    data.add(snapshot);
                    Log.d(TAG, snapshot.toString());
                }
                populateTable();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void populateTable() {

        table.removeAllViews();

        for (int i = 0; i < country_hdrs.length; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            TextView text = new TextView(this);
            text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            text.setGravity(Gravity.CENTER);
            text.setTextSize(18);
            text.setPadding(5, 5, 5, 5);

            // Set key here
            String key = country_hdrs[i];
            text.setText(key);
            row.addView(text);

            // Set value here
            String value = "";//data.get(i).child(key).getValue().toString();
            text.setText(value);
            row.addView(text);

            table.addView(row);
        }
    }
}
