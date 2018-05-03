package com.example.rgarcia21.weatherappandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

//Portion of GUI code that was used for search bar
/*
        String dataType = ZIPField.getText();
        String state = "";

        try {
            Integer.parseInt(dataType);
        }
        catch (NumberFormatException e) {
                String d = dataType;
                dataType = d.substring(0,d.indexOf(','));
                dataType = dataType.replaceAll(" ", "_");

                state = d.substring(d.indexOf(','), d.length());
                state = state.replaceAll(" ", "");
        }

        Weather b = new Weather(dataType, state);
        Conditions a = b.getCond();
 */