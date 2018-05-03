package com.example.rgarcia21.weatherappandroid;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
//
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Weather {
    private String zipcode;
    private JsonElement jse;
    private JsonElement jse1;
    private String city;
    private String state;


    // Constructor

    public Weather(String zip){
        zipcode = zip;
    }
    public Weather(String cit, String sta)
    {
        city = cit;
        state = sta;
    }

    public Conditions getCond()
    {
        if(jse == null){
            fetchData();
        }

        try {
            String latitude = jse.getAsJsonObject().get("current_observation").getAsJsonObject().get("observation_location").getAsJsonObject().get("latitude").getAsString();
            String longitude = jse.getAsJsonObject().get("current_observation").getAsJsonObject().get("observation_location").getAsJsonObject().get("longitude").getAsString();
            String city = jse.getAsJsonObject().get("current_observation").getAsJsonObject().get("display_location").getAsJsonObject().get("city").getAsString();
            String tempF = jse.getAsJsonObject().get("current_observation").getAsJsonObject().get("temp_f").getAsString();
            String tempC = jse.getAsJsonObject().get("current_observation").getAsJsonObject().get("temp_c").getAsString();
            String feelC = jse.getAsJsonObject().get("current_observation").getAsJsonObject().get("feelslike_c").getAsString();
            String feelF = jse.getAsJsonObject().get("current_observation").getAsJsonObject().get("feelslike_f").getAsString();
            String condition = jse.getAsJsonObject().get("current_observation").getAsJsonObject().get("weather").getAsString();
            String wind = jse.getAsJsonObject().get("current_observation").getAsJsonObject().get("wind_mph").getAsString();
            String rain = jse.getAsJsonObject().get("current_observation").getAsJsonObject().get("precip_today_in").getAsString();
            String icon = jse.getAsJsonObject().get("current_observation").getAsJsonObject().get("icon").getAsString();
            String inputState = jse.getAsJsonObject().get("current_observation").getAsJsonObject().get("display_location").getAsJsonObject().get("state").getAsString();
            String title = "title";
            String icon2 = "icon";
            String fct = "high";
            Conditions c = new Conditions(latitude, longitude, city, tempF, tempC, feelC, feelF, condition, wind, rain, icon,
                    getForecastTomorrow(title), getForecastAfter(icon2), getForecastAfter2(fct), inputState);

            return c;
        }

        catch(NullPointerException e)
        {
            throw new ErrorCatch();
        }
    }


    public String getForecastTomorrow(String nameObj) {

        fetchData();
        JsonArray fcArray = jse.getAsJsonObject()
                .get("forecast").getAsJsonObject()
                .get("txt_forecast").getAsJsonObject()
                .get("forecastday").getAsJsonArray();


        String fore = "";


        for (int i = 1; i < fcArray.size() - 8; i += 2) {
            String f = fcArray.get(i).getAsJsonObject().get(nameObj).getAsString();
            fore += f + " ";

            //System.out.println(fore);


        }


        return fore;
    }



    public String getForecastAfter(String nameObj) {

        fetchData();
        JsonArray fcArray = jse.getAsJsonObject()
                .get("forecast").getAsJsonObject()
                .get("txt_forecast").getAsJsonObject()
                .get("forecastday").getAsJsonArray();


        String fore = "";


        for (int i = 2; i < fcArray.size() - 8; i += 2) {
            String f = fcArray.get(i).getAsJsonObject().get(nameObj).getAsString();

            fore += f + " ";


        }


        return fore;
    }
    public String getForecastAfter2(String nameObj) {

        fetchData();
        JsonArray fcArray = jse.getAsJsonObject()
                .get("forecast").getAsJsonObject()
                .get("simpleforecast").getAsJsonObject()
                .get("forecastday").getAsJsonArray();


        String fore = "";


        for (int i = 0; i < fcArray.size(); i += 2) {
            String f = fcArray.get(i).getAsJsonObject().get(nameObj).getAsJsonObject().get("fahrenheit").getAsString();

            fore += f + " ";


        }


        return fore;
    }

    private void fetchData() {

        String wundergroundRequest;
        if(zipcode!= null) {

            wundergroundRequest = "http://api.wunderground.com/api/a571d3aa8d465016/conditions/forecast10day/q/" + zipcode + ".json";
        }

        else{

            wundergroundRequest = "http://api.wunderground.com/api/1655f919bbcd29ed/conditions/forecast10day/q/" + state + "/" + city + ".json";
        }

        try {

            URL wundergroundURL = new URL(wundergroundRequest);

            InputStream is = wundergroundURL.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            jse = new JsonParser().parse(br);
        } catch (java.net.MalformedURLException mue) {
            System.out.println("URL not well formed");
            mue.printStackTrace();
        } catch (java.io.IOException ioe) {
            System.out.println("Got IO Exception");
            ioe.printStackTrace();
        }

    }



    public static void main(String[] args){
        String zip = "91911";
        String cit = "San Diego";
        String sta = "CA";
        Weather w = new Weather(zip);

        //System.out.println(w.getForecastTomorrow("high"));

        Conditions a = w.getCond();

        System.out.println(a.latitude
                + "\n" + a.longitude
                + "\n" + a.city
                + "\n" + a.tempF
                + "\n" + a.tempC
                + "\n" + a.feelC
                + "\n" + a.feelF
                + "\n" + a.condition
                + "\n" + a.wind
                + "\n" + a.rain
                + "\n" + a.icon
                + "\n" + a.title
                + "\n" + a.icon2
                + "\n" + a.fct);
        System.out.println(w.getForecastAfter("icon"));

    }



}