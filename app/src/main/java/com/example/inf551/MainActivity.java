package com.example.inf551;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AutoCompleteTextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private AutoCompleteTextView searchView;
    AutoCompleteTextView actv;
    ArrayList<String> autocomplete;
    JsonReader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reader = new JsonReader();

        searchView = findViewById(R.id.searchView);
        actv = (AutoCompleteTextView) findViewById(R.id.searchView);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (autocomplete == null || autocomplete.size() == 1) {
                    setAutofill(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (actv.getAdapter() != null) {
                    if (actv.getAdapter().getCount() == 0) {
                        autocomplete = null;
                    }
                }
            }
        });

//        //Creating the instance of ArrayAdapter containing list of fruit names
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                (this, android.R.layout.select_dialog_item, autocomplete);
//        //Getting the instance of AutoCompleteTextView
//        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.searchView);
//        actv.setThreshold(3);//will start working from first character
//        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        //#C0C0C0
    }

    public void setAutofill(String term) {
        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayList<String> results = reader.search(term);
        if (results == null || results.size() == 0) {return;};
        autocomplete = results;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, autocomplete);
        //Getting the instance of AutoCompleteTextView
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setThreshold(1);
    }

    public void onClick(View view) throws Exception {
       // new JsonReader().test();
        // Do something in response to button click
//        searchView.setIconified(false);
      //  String search = searchView.getQuery().toString();

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioCat);
        int checked = rg.getCheckedRadioButtonId();


        if (checked == R.id.radio1) {
            Intent intent = new Intent(MainActivity.this, CityActivity.class);
      //      intent.putExtra("search", search);
            startActivity(intent);
        } else if (checked == R.id.radio2){
            Intent intent = new Intent(MainActivity.this, CountryActivity.class);
            intent.putExtra("searchByCode", false);
      //      intent.putExtra("search", search);
            startActivity(intent);
        }
    }

}
