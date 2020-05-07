package com.example.inf551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private AutoCompleteTextView searchView;
    ArrayList<String> cities;
    ArrayList<String> countries;
    CityJsonReader cityReader;
    CountryJsonReader countryReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityReader = new CityJsonReader();
        countryReader = new CountryJsonReader();

        searchView = (AutoCompleteTextView) findViewById(R.id.searchView);

        cities = cityReader.getCities();
        countries = countryReader.getCountries();

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, cities);
        searchView.setAdapter(cityAdapter);//setting the adapter data into the AutoCompleteTextView
        searchView.setThreshold(3);
    }

    public void radioChanged(View view) {
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioCat);
        int checked = rg.getCheckedRadioButtonId();
        if (checked == R.id.radio1) {
            ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_1, cities);
            searchView.setAdapter(cityAdapter);//setting the adapter data into the AutoCompleteTextView
            searchView.setThreshold(3);
        } else if (checked == R.id.radio2){
            ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_1, countries);
            searchView.setAdapter(countryAdapter);//setting the adapter data into the AutoCompleteTextView
            searchView.setThreshold(3);
        }
    }

    public void onClick(View view) throws Exception {
        String search = searchView.getText().toString();

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioCat);
        int checked = rg.getCheckedRadioButtonId();


        if (checked == R.id.radio1) {
            Intent intent = new Intent(MainActivity.this, CityActivity.class);
            intent.putExtra("search", search);

            startActivity(intent);
        } else if (checked == R.id.radio2){
            Intent intent = new Intent(MainActivity.this, CountryActivity.class);
            intent.putExtra("searchByCode", false);
            intent.putExtra("search", search);
            startActivity(intent);
        }
    }

}
