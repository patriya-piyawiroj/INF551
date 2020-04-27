package com.example.inf551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View view) {
        // Do something in response to button click
        SearchView searchView = findViewById(R.id.searchView);
        String search = searchView.getQuery().toString();
        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
        intent.putExtra("search", search);
        startActivity(intent);
    }
}
