package com.letansang.sangweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView textViewName,textViewWeather,textViewTemp,textViewDate,textViewHumidity,textViewPressure;
    EditText editText;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa();
        GetCurrentWeatherData("Hanoi");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editText.getText().toString();
                GetCurrentWeatherData(city);
            }
        });
    }

    private void anhxa() {
        textViewName = findViewById(R.id.textViewName);
        textViewTemp = findViewById(R.id.textViewTemp);
        textViewWeather = findViewById(R.id.textViewWeather);
        textViewDate = findViewById(R.id.textViewDate);
        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewPressure = findViewById(R.id.textViewPressure);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
    }

    public void GetCurrentWeatherData (String city){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=230f27fdd0cba880fc050c33623cf16b&units=metric";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("test",response);
                    // get Name
                    JSONObject jsonObject = new JSONObject(response);
                    String day = jsonObject.getString("dt");
                    String name = jsonObject.getString("name");
                    textViewName.setText("City: "+name);
                    // get date
                    long l = Long.valueOf(day);
                    Date date = new Date(l*1000L);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy \nHH:mm");
                    String Date = simpleDateFormat.format(date);
                    textViewDate.setText(Date);
                    // get status
                    JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                    String status = jsonObjectWeather.getString("main");
                    textViewWeather.setText(status);
                    //get temp, humidity, pressure
                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    String temp = jsonObjectMain.getString("temp");
                    double tempD = Double.valueOf(temp);
                    String TEMP = String.valueOf(tempD);
                    textViewTemp.setText(TEMP+ "°C");
                    String humidity = jsonObjectMain.getString("humidity");
                    textViewHumidity.setText("Humidity: "+ humidity+"%");
                    String pressure = jsonObjectMain.getString("pressure");
                    textViewPressure.setText("Pressure: "+ pressure+"hPa");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

}