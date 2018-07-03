package com.stalnobcrs.weatherinfo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView weatherDetails;
    public void check(View view){
        Log.i("CityName",cityName.getText().toString());
        DownloadTask task=new DownloadTask();
        String cont="";
        try {
            cont=task.execute("http://api.openweathermap.org/data/2.5/weather?q="+cityName.getText().toString()+"&APPID=0bbb6664a9e971901676727256893746").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        weatherDetails.setText(cont);
    }
    class DownloadTask extends AsyncTask<String,Void,String>{
    protected String doInBackground(String... strings) {
        URL url;
        HttpURLConnection httpURLConnection;
        String result = "";
        try {
            url = new URL(strings[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream in = httpURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
            while (data != -1) {
                char cur = (char) data;
                result += cur;
                data = reader.read();
            }
         } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String infoWeather = "";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String weather = jsonObject.getString("weather");
            JSONArray jsonArray = new JSONArray(weather);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject part = jsonArray.getJSONObject(i);
                String main = part.getString("main");
                String desc = part.getString("description");
                Log.i("main", main);
                Log.i("desc", desc);
                infoWeather += main + ":" + desc;
                if (i + 1 != jsonArray.length()) {
                    infoWeather += " & ";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return infoWeather;
    }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName=(EditText)findViewById(R.id.editText);
        weatherDetails=(TextView)findViewById(R.id.textView);
    }
}
