package com.example.rgarcia21.weatherappandroid;

public class Conditions {

    String longitude;
    String latitude;
    String title;
    String city;
    String tempF;
    String tempC;
    String feelC;
    String feelF;
    String condition;
    String wind;
    String rain;
    String icon;
    String icon2;
    String fct;
    String inputState;






    public Conditions(String latitude, String longitude, String city, String tempF, String tempC, String feelC, String feelF,
                      String condition, String wind, String rain, String icon, String title, String icon2, String fct, String inputState)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.tempF = tempF;
        this.tempC = tempC;
        this.feelC = feelC;
        this.feelF = feelF;
        this.condition = condition;
        this.rain = rain;
        this.wind = wind;
        this.icon = icon;
        this.title  = title;
        this.icon2 = icon2;
        this.fct = fct;
        this.inputState = inputState;



    }

}



