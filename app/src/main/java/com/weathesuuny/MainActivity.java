package com.weathesuuny;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.weathesuuny.WeatherClass.WeatherClass;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    String url;
    String city;
    String weatherType;
    String weatherDescription;
    String weatherIcon;
    Integer minTemp;
    Integer maxTemp;
    Double mainTemp;
    Double windSpeed;
    String iconUrl;

    TextView txtCity;
    TextView txtWeatherType;
    TextView txtWeatherDescription;
    ImageView imgWeatherIcon;
    TextView txtMinTemp;
    TextView txtMaxTemp;
    TextView txtMainTemp;
    TextView txtWindSpeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtCity = findViewById(R.id.txtcity);
        txtWeatherType = findViewById(R.id.txtweather);
        txtWeatherDescription = findViewById(R.id.txtDescription);
        txtMinTemp = findViewById(R.id.txtminTemp);
        txtMaxTemp = findViewById(R.id.txtmaxTemp);
        txtMainTemp = findViewById(R.id.txtMainTemp);
        txtWindSpeed = findViewById(R.id.txtWindSpeed);

        url = "https://api.openweathermap.org/data/2.5/weather?q=Tehran&units=metric&APPID=140f88d03d5cbfb6d018df31638ed465";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Gson gson = new Gson();

                WeatherClass WeatherClass = gson.fromJson(response.toString(), WeatherClass.class);

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String weatherData = jsonObject.getString("weather");
                    JSONArray jsonArray = new JSONArray(weatherData);

                    weatherType = "";
                    weatherDescription = "";
                    weatherIcon = null;
                    iconUrl = null;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject weatherPart = jsonArray.getJSONObject(i);
                        weatherType = weatherPart.getString("main");
                        weatherDescription = weatherPart.getString("description");
                        weatherIcon = weatherPart.getString("icon");
                        String wIcon = weatherIcon;
                    }

                    txtWeatherType.setText(weatherType);
                    txtWeatherDescription.setText(weatherDescription);
                    iconUrl = "http://openweathermap.org/img/wn/" + weatherIcon + ".png";
                    Picasso.get().load(Uri.parse(iconUrl)).into(imgWeatherIcon);
//                    Glide.with(MainActivity.this).load("url").into(imgWeatherIcon);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                city = WeatherClass.getName();
                txtCity.setText(city);

                //minTemp = WeatherClass.getMain().getTempMin();
               // txtMinTemp.setText(minTemp.toString() + "°C");

               // maxTemp = WeatherClass.getMain().getTempMax();
               // txtMaxTemp.setText(maxTemp.toString() + "°C");

                mainTemp = WeatherClass.getMain().getTemp();
                txtMainTemp.setText(mainTemp.toString() + "°C");

                windSpeed = WeatherClass.getWind().getSpeed();
                txtWindSpeed.setText(windSpeed.toString() + "m/s");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
        TextView txtsearch=findViewById(R.id.txtsearch);
        txtsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SearchPage.class);
                startActivity(intent);
            }
        });

        Button btnForcase =findViewById(R.id.btnForcase);
        btnForcase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this,ForcastWeather.class);
                startActivity(intent);

            }
        });
    }
}

