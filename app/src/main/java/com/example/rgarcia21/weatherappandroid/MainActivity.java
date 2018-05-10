package com.example.rgarcia21.weatherappandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
    }
    //Store values outside method for future use, only requires 1 fetch
    String radarCity;
    String radarState;
    String inputCity;
    String inputState;
    String inputFeel;
    String inputWind;
    String inputRain;
    String titleList;
    String tempCollector;
    String iconCollector;
    String dataType;
    String state;
    String inputCond;
    String inputTempF;
    String inputBigicon;
    int weatherNumber;

    private class GetWeatherInBackground extends AsyncTask<String, Void, Conditions>
    {
        @Override
        protected Conditions doInBackground(String... locations)
        {
            Weather b = new Weather(locations[0]);
            Conditions c = b.getCond();

            return c;
        }

        @Override
        protected void onPostExecute(Conditions a)
        {
            //Gather required information for tabs so we don't refetch when clicking through data
            radarCity = dataType;
            radarState = state;
            inputCity = a.city;
            inputState = a.inputState;
            inputFeel = a.feelF;
            inputWind = a.wind;
            inputRain = a.rain;
            inputCond = a.condition;
            inputTempF = a.tempF;
            inputBigicon = a.icon;
            titleList = a.title;
            tempCollector = a.fct;
            iconCollector = a.icon2;

            //Update Screen data on first fetch
            String weatherDecimal = a.tempF;
            weatherNumber = (int) Double.parseDouble(weatherDecimal);
            TextView temp = (TextView) findViewById(R.id.weatherNumber);
            temp.setText(weatherNumber + "°");

            TextView city = (TextView) findViewById(R.id.weatherCity);
            city.setText(inputCity + ", " + inputState);

            TextView cond = (TextView) findViewById(R.id.weatherCondition);
            cond.setText(a.condition);

            TextView feels = (TextView) findViewById(R.id.weatherFeel);
            feels.setText("| Feels like " + inputFeel + "° (F)");

            String PACKAGE_NAME = getApplicationContext().getPackageName(); //Used for all dynamic icons
            ImageView weatherIcon = (ImageView) findViewById(R.id.weatherImage);
            int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+ a.icon , null, null);
            weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId));
        }
    }


    public void getWeather(View v) {
        EditText location = (EditText) findViewById(R.id.searchBar);
        dataType = location.getText().toString();

        try {
            Integer.parseInt(dataType);
        } catch (NumberFormatException e) {
            String d = dataType;
            dataType = d.substring(0, d.indexOf(','));
            dataType = dataType.replaceAll(" ", "_");

            state = d.substring(d.indexOf(','), d.length());
            state = state.replaceAll(" ", "");
        }
        new GetWeatherInBackground().execute(dataType, state);
    }

    public void convertActionC(View convertC) {
        TextView temp = (TextView) findViewById(R.id.weatherNumber);
        int convertEquation = (weatherNumber - 32) * 5/9;
        temp.setText(convertEquation + "°");

        TextView feels = (TextView) findViewById(R.id.weatherFeel);
        int convertFeel = (Integer.parseInt(inputFeel) - 32) * 5/9;
        feels.setText("| Feels like " + convertFeel + "° (C)");
    }

    public void convertActionF(View convertF) {
        TextView temp = (TextView) findViewById(R.id.weatherNumber);
        temp.setText(weatherNumber + "°");

        TextView feels = (TextView) findViewById(R.id.weatherFeel);
        feels.setText("| Feels like " + inputFeel + "° (F)");
    }

    public void openHome(View v) {
        //call home
        String weatherDecimal = inputTempF;
        weatherNumber = (int) Double.parseDouble(weatherDecimal);
        TextView temp = (TextView) findViewById(R.id.weatherNumber);
        temp.setText(weatherNumber + "°");

        TextView city = (TextView) findViewById(R.id.weatherCity);
        city.setText(inputCity + ", " + inputState);

        TextView cond = (TextView) findViewById(R.id.weatherCondition);
        cond.setText(inputCond);

        TextView feels = (TextView) findViewById(R.id.weatherFeel);
        feels.setText("| Feels like " + inputFeel + "° (F)");

        String PACKAGE_NAME = getApplicationContext().getPackageName(); //Used for all dynamic icons
        ImageView weatherIcon = (ImageView) findViewById(R.id.weatherImage);
        int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+ inputBigicon , null, null);
        weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId));

        setContentView(R.layout.activity_main);
    }

    public void openRadar(View v) {
        //call radar window (CRASHES)
        WebView radar = (WebView) findViewById(R.id.radarimg);
        radar.getSettings().getJavaScriptEnabled();
        radar.setWebViewClient(new WebViewClient());
        radar.loadUrl("http://api.wunderground.com/api/"+ BuildConfig.ApiKey +"/animatedradar/animatedsatellite/q/"+ radarState + "/" + radarCity + ".gif?num=6&delay=50&interval=30");

        setContentView(R.layout.radar_activity);
    }

    public void openForecast(View v) {
        //call fct window

        TextView city = (TextView) findViewById(R.id.weatherCity);
        city.setText(inputCity + ", " + inputState);

        TextView wind = (TextView) findViewById(R.id.weatherWind);
        wind.setText(inputWind + "mph");

        TextView rain = (TextView) findViewById(R.id.weatherRain);
        rain.setText(inputRain + "%");

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
        TextView title4 = (TextView) findViewById(R.id.forecastTitle3);
        title4.setText(titleArrayList[6]);
        TextView title5 = (TextView) findViewById(R.id.forecastTitle4);
        title5.setText(titleArrayList[8]);

        String [] output3 = tempCollector.split("( )");
        TextView temp1 = (TextView) findViewById(R.id.forecastTemp);
        temp1.setText(output3[0] + "°");
        TextView temp2 = (TextView) findViewById(R.id.forecastTemp1);
        temp2.setText(output3[1] + "°");
        TextView temp3 = (TextView) findViewById(R.id.forecastTemp2);
        temp3.setText(output3[2] + "°");
        TextView temp4 = (TextView) findViewById(R.id.forecastTemp3);
        temp4.setText(output3[3] + "°");
        TextView temp5 = (TextView) findViewById(R.id.forecastTemp4);
        temp5.setText(output3[4] + "°");

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

        ImageView forecastImg3 = (ImageView) findViewById(R.id.forecastIcon3);
        int imgId5 = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+ inputAsArray2[3] , null, null);
        forecastImg3.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId5));

        ImageView forecastImg4 = (ImageView) findViewById(R.id.forecastIcon4);
        int imgId6 = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+ inputAsArray2[4] , null, null);
        forecastImg4.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId6));

        setContentView(R.layout.forecast_activity);
    }
}
//API KEY: 1655f919bbcd29ed (for when I need to check something), remove on completion