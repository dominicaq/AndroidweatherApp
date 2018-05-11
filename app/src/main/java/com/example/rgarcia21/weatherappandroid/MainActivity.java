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
import android.widget.Toast;

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
    String dataType;
    String state;

    String inputCity;
    String inputState;
    String inputFeel;
    String inputWind;
    String inputRain;
    String titleList;
    String tempCollector;
    String iconCollector;
    String inputCond;
    String inputTempF;
    String inputBigicon;

    private class GetWeatherInBackground extends AsyncTask<String, Void, Conditions>
    {
        @Override
        protected Conditions doInBackground(String... locations)
        {
            Weather b = new Weather(locations[0],locations[1]); //State, City
            Conditions c = b.getCond();

            return c;
        }

        @Override
        protected void onPostExecute(Conditions a)
        {
            //Gather required information for tabs so we don't refetch when clicking through them
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
            Button setData = (Button) findViewById(R.id.homebt);
            setData.performClick();
        }
    }

    public void getWeather() {
        EditText location = (EditText) findViewById(R.id.searchBar);
        dataType = location.getText().toString();

        //Prevents splitting of a ZIP
        try {
            Integer.parseInt(dataType);
        } catch (NumberFormatException e) {
            String d = dataType;
            dataType = d.substring(0, d.indexOf(','));
            dataType = dataType.replaceAll("\\\\s+", "_");

            state = d.substring(d.indexOf(','), d.length());
            state = state.replaceAll("\\\\s+", "");
        }
        new GetWeatherInBackground().execute(dataType, state);
    }

    public void getWeatherButton(View v){
        EditText location = (EditText) findViewById(R.id.searchBar);
        if (location.getText() == null){
            Toast toast2 = Toast.makeText(getApplicationContext(), "Please enter Input", Toast.LENGTH_SHORT);
            toast2.show();
        } else{
            try {
                getWeather();
            }
            catch (ErrorCatch e)
            {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT);
                toast1.show();
            }
        }
    }

    public void convertActionC(View v) {
        Button cButton = (Button) findViewById(R.id.cBut);
        cButton.setTextColor(Color.GRAY);

        Button fButton = (Button) findViewById(R.id.fBut);
        fButton.setTextColor(Color.WHITE);

        String weatherDecimal = inputTempF;
        int weatherNumber = (int) Double.parseDouble(weatherDecimal);
        TextView temp = (TextView) findViewById(R.id.weatherNumber);
        weatherNumber = (weatherNumber - 32) * 5/9;
        temp.setText(weatherNumber + "°");

        TextView feels = (TextView) findViewById(R.id.weatherFeel);
        int convertFeel = (int) Double.parseDouble(inputFeel);
        convertFeel = (convertFeel - 32) * 5/9;
        feels.setText("Feels like " + convertFeel + "° (C)");
    }

    public void convertActionF(View v) {
        Button cButton = (Button) findViewById(R.id.cBut);
        cButton.setTextColor(Color.WHITE);

        Button fButton = (Button) findViewById(R.id.fBut);
        fButton.setTextColor(Color.GRAY);

        String weatherDecimal = inputTempF;
        int weatherNumber = (int) Double.parseDouble(weatherDecimal);
        TextView temp = (TextView) findViewById(R.id.weatherNumber);
        temp.setText(weatherNumber + "°");

        TextView feels = (TextView) findViewById(R.id.weatherFeel);
        feels.setText("Feels like " + inputFeel + "° (F)");
    }

    public void openHome(View v) {
        //call home
        setContentView(R.layout.activity_main);

        String weatherDecimal = inputTempF;
        int weatherNumber = (int) Double.parseDouble(weatherDecimal);
        TextView temp = (TextView) findViewById(R.id.weatherNumber);
        temp.setText(weatherNumber + "°");

        TextView city = (TextView) findViewById(R.id.weatherCity);
        city.setText(inputCity + ", " + inputState);

        TextView cond = (TextView) findViewById(R.id.weatherCondition);
        cond.setText(inputCond + " |");

        TextView feels = (TextView) findViewById(R.id.weatherFeel);
        feels.setText("| Feels like " + inputFeel + "° (F)");

        String PACKAGE_NAME = getApplicationContext().getPackageName(); //Used for all dynamic icons
        ImageView weatherIcon = (ImageView) findViewById(R.id.weatherImage);
        int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+ inputBigicon , null, null);
        weatherIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId));
    }

    public void openRadar(View v) {
        //call radar window
        setContentView(R.layout.radar_activity);

        TextView city = (TextView) findViewById(R.id.weatherCity);
        city.setText(inputCity + ", " + inputState);

        WebView radar = (WebView) findViewById(R.id.radarimg);
        radar.getSettings().getJavaScriptEnabled();
        radar.setWebViewClient(new WebViewClient());
        radar.loadUrl("http://api.wunderground.com/api/"+ BuildConfig.ApiKey +"/animatedradar/animatedsatellite/q/"+ inputState + "/" + inputCity + ".gif?num=6&delay=50&interval=30");
    }

    public void openForecast(View v) {
        //call fct window
        setContentView(R.layout.forecast_activity);

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
    }
}
//API KEY: 1655f919bbcd29ed (for when I need to check something), remove on completion