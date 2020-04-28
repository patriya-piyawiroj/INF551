package com.example.inf551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SearchView;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void onClick(View view) throws Exception {
        new JsonReader().test();
        // Do something in response to button click
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setIconified(false);
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioCat);
        int checked = rg.getCheckedRadioButtonId();
        String check = String.valueOf(checked);
        String search = searchView.getQuery().toString();
        Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
        intent.putExtra("search", search);
        intent.putExtra("checked", check);
        startActivity(intent);
    }
}
