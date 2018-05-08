package com.example.rgarcia21.weatherappandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText location = (EditText) findViewById(R.id.searchBar);
        location.setText("95648"); //Lincoln, CA
        Button startupData = (Button) findViewById(R.id.searchButton);
        startupData.performClick();
        location.setText("");

        //Transparent code, testing
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //
    }

    //
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    //Store values outside method
    String inputCity;
    String inputState;
    String inputFeel;
    String winds;
    String rains;
    String titleList;
    String tempCollector;
    String iconCollector;
    int inputWeatherF;

    public void getWeather(View v) {
        // Disable threading. We'll fix this later.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Get the text from the input field
        EditText location = (EditText) findViewById(R.id.searchBar);
        String dataType = location.getText().toString();
        String state = "";

        try {
            Integer.parseInt(dataType);
        } catch (NumberFormatException e) {
            String d = dataType;
            dataType = d.substring(0, d.indexOf(','));
            dataType = dataType.replaceAll(" ", "_");

            state = d.substring(d.indexOf(','), d.length());
            state = state.replaceAll(" ", "");
        }

        Weather b = new Weather(dataType, state);
        Conditions a = b.getCond();

        // Set the text of GUI elements
        String weatherDecimal = a.tempF;
        int weatherNumber = (int) Double.parseDouble(weatherDecimal);
        TextView temp = (TextView) findViewById(R.id.weatherNumber);
        temp.setText(weatherNumber + "°");

        TextView city = (TextView) findViewById(R.id.weatherCity);
        city.setText(a.city + ", " + a.inputState);

        TextView cond = (TextView) findViewById(R.id.weatherCondition);
        cond.setText(a.condition);

        TextView feels = (TextView) findViewById(R.id.weatherFeel);
        feels.setText("Feels like " + a.feelF + "° (F)");

        String PACKAGE_NAME = getApplicationContext().getPackageName(); //Used for all dynamic icons
        ImageView weatherIcon = (ImageView) findViewById(R.id.weatherImage);
        int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+ a.icon , null, null);
        weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId));

        //Gather required information for tabs
        inputCity = dataType;
        inputState = state;
        inputWeatherF = weatherNumber;
        inputFeel = a.feelF;
        winds = a.wind;
        rains = a.rain;
        titleList = a.title;
        tempCollector = a.fct;
        iconCollector = a.icon2;
    }

    public void convertActionC(View convertC) {
        TextView temp = (TextView) findViewById(R.id.weatherNumber);
        int convertEquation = (inputWeatherF - 32) * 5/9;
        temp.setText(convertEquation + "°");

        TextView feels = (TextView) findViewById(R.id.weatherFeel);
        int convertFeel = (Integer.parseInt(inputFeel) - 32) * 5/9;
        feels.setText(convertFeel + "° (C)");
    }

    public void convertActionF(View convertF) {
        TextView temp = (TextView) findViewById(R.id.weatherNumber);
        temp.setText(inputWeatherF + "°");

        TextView feels = (TextView) findViewById(R.id.weatherFeel);
        feels.setText(inputFeel + "° (F)");
    }

    public void wundergroundCredit(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.wunderground.com/"));
        startActivity(browserIntent);
    }

    public void radarButton(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://api.wunderground.com/api/"+ BuildConfig.ApiKey +"/animatedradar/animatedsatellite/q/"+ inputState +"/" + inputCity + ".gif?num=6&delay=50&interval=30"));
        startActivity(browserIntent);
    }

    public void openHome(View v) {
        //call home
        setContentView(R.layout.activity_main);
    }

    public void openRadar(View v) {
        //call radar window
        setContentView(R.layout.radar_activity);
    }

    public void openForecast(View v) {
        //call fct window
        setContentView(R.layout.forecast_activity);

        TextView wind = (TextView) findViewById(R.id.weatherWind);
        wind.setText(winds + "mph");

        TextView rain = (TextView) findViewById(R.id.weatherRain);
        rain.setText(rains + "%");

        //Icon code
        String PACKAGE_NAME = getApplicationContext().getPackageName(); //Used for all dynamic icons

        //Titles
        String[] titleArrayList = titleList.split(" ");
        TextView title1 = (TextView) findViewById(R.id.forecastTitle);
        title1.setText(titleArrayList[0]);
        TextView title2 = (TextView) findViewById(R.id.forecastTitle1);
        title2.setText(titleArrayList[2]);
        TextView title3 = (TextView) findViewById(R.id.forecastTitle2);
        title3.setText(titleArrayList[4]);

        String [] output3 = tempCollector.split("( )");
        TextView temp1 = (TextView) findViewById(R.id.forecastTemp);
        temp1.setText(output3[0] + "°");
        TextView temp2 = (TextView) findViewById(R.id.forecastTemp1);
        temp2.setText(output3[1] + "°");
        TextView temp3 = (TextView) findViewById(R.id.forecastTemp2);
        temp3.setText(output3[2] + "°");

        String [] inputAsArray2 = iconCollector.split("( )");

        ImageView forecastImg = (ImageView) findViewById(R.id.forecastIcon);
        int imgId2 = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+ inputAsArray2[0] , null, null);
        forecastImg.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId2));


        ImageView forecastImg1 = (ImageView) findViewById(R.id.forecastIcon1);
        int imgId3 = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+ inputAsArray2[1] , null, null);
        forecastImg1.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId3));

        ImageView forecastImg2 = (ImageView) findViewById(R.id.forecastIcon2);
        int imgId4 = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+ inputAsArray2[2] , null, null);
        forecastImg2.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId4));
    }
}
//API KEY: 1655f919bbcd29ed (for when I need to check something), remove on completion