package com.example.inf551;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    TableLayout table;
    String search = ""; // From search view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        search = intent.getStringExtra("search");

        table = (TableLayout) findViewById(R.id.table);
        populateTable();
    }

    private void populateTable() {
        int rows = 5;
        int cols = 5;
        String[][] results = {{"1", "2", "3", "4", "5"},
                {"1", "2", "3", "4", "5"},
                {"1", "2", "3", "4", "5"},
                {"1", "2", "3", "4", "5"},
                {"1", "2", "3", "4", "5"}};

        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


            // inner for loop
            for (int j = 0; j < cols; j++) {

                TextView text = new TextView(this);
                text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                text.setGravity(Gravity.CENTER);
                text.setTextSize(18);
                text.setPadding(5, 5, 5, 5);

                // Set string here
                text.setText(results[i][j]);
                row.addView(text);

            }
            table.addView(row);
        }
    }
}
