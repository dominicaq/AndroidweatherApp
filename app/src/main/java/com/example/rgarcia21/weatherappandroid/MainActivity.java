package com.example.rgarcia21.weatherappandroid;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



public void getWeather(View v)
{
    // Disable threading. We'll fix this later.
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);

    // Get the text from the input field
    EditText location = (EditText) findViewById(R.id.searchBar);
    String dataType = location.getText().toString();
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

        String temF = a.tempF;
        String felF = a.feelF;
        temF.replaceAll("\\.0*$", "");
        felF.replaceAll("\\.0*$", "");

    // Set the text GUI elements
        TextView temp = (TextView) findViewById(R.id.weatherNumber);
        temp.setText(temF + "°");

        TextView city = (TextView) findViewById(R.id.weatherCity);
        city.setText(a.city + ", " + a.inputState);

        TextView feels = (TextView) findViewById(R.id.weatherFeel);
        feels.setText("Feels like: " + felF + "° (F)");

        //Icon code
        int id = getResources().getIdentifier("drawable/" + a.icon, null, null);
        ImageView weatherIcon = (ImageView) findViewById(R.id.weatherImage);
        weatherIcon.setImageResource(id);

        TextView wind = (TextView) findViewById(R.id.weatherWind);
        feels.setText(a.wind + "mph");

        TextView rain = (TextView) findViewById(R.id.weatherRain);
        feels.setText(a.rain + "%");
    }
}
