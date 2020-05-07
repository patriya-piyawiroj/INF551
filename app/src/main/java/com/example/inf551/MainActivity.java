package com.example.inf551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.AutoCompleteTextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private AutoCompleteTextView searchView;
    ArrayList<String> cities;
    ArrayList<String> countries;
    CityJsonReader cityReader;
    CountryJsonReader countryReader;


    private void createList() {
        String checkCity = SerializeObject.ReadSettings(getApplicationContext(), "cityList.dat");
        String checkCountry = SerializeObject.ReadSettings(getApplicationContext(), "countryList.dat");
        boolean exist = !(checkCity.equals("")&&checkCountry.equals(""));
        Object obj = SerializeObject.stringToObject(checkCity);
        System.out.println(exist+" "+obj);

        if (!exist){
            cityReader = new CityJsonReader(getApplicationContext());
            countryReader = new CountryJsonReader(getApplicationContext());
            cities = cityReader.getCities();
            countries = countryReader.getCountries();
        } else {
            cities = (ArrayList<String>) SerializeObject.stringToObject(checkCity);
            countries = (ArrayList<String>) SerializeObject.stringToObject(checkCountry);
            System.out.println("citylist: " + cities);
            System.out.println("countryList: "+ countries);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createList();
        searchView = (AutoCompleteTextView) findViewById(R.id.searchView);
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
            int idx = search.indexOf("(");
            String city = search.substring(0, idx-1);
            String code = search.substring(idx+1, idx+4);
            intent.putExtra("city", city);
            intent.putExtra("code", code);
            startActivity(intent);
        } else if (checked == R.id.radio2){
            Intent intent = new Intent(MainActivity.this, CountryActivity.class);
            intent.putExtra("searchByCode", false);
            intent.putExtra("search", search);
            startActivity(intent);
        }
    }

}
