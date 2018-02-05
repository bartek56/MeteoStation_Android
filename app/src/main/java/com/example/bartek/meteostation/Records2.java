/********************************************************
 *
 *
 *  Autor: Bartosz Brzozowski
 *
 *
 ***********************************************************/
package com.example.bartek.meteostation;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Records2 extends AppCompatActivity {

RecordsActivity recordsActivity=new RecordsActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String html="http://meteostation.y0.pl/JSON.php?year="+recordsActivity.getYear()+"&month="+recordsActivity.getMonth()+"&day="+recordsActivity.getDay();


        new WebService()
                .execute(html);
    }

    private class WebService extends AsyncTask<String, Void, String>
    {

        private ProgressDialog dialog = new ProgressDialog(Records2.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();

                InputStream in = new BufferedInputStream(
                        connection.getInputStream());

                return streamToString(in);


            } catch (Exception e) {
                Log.d(MainActivity.class.getSimpleName(), e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();


            try {
                JSONObject json = new JSONObject(s);

                if(json.optString("minTempIn")=="null")
                {
                    Log.d(MainActivity.class.getSimpleName(), "brak danych");
                    ((TextView) findViewById(R.id.tDate)).setText("No Date\nChoose other date");

                }
                else {
                    Log.d(MainActivity.class.getSimpleName(), "zajebiście");
                    ((TextView) findViewById(R.id.tMinTemperatureInValue)).setText(json.optString("minTempIn") + "°C");
                    ((TextView) findViewById(R.id.tMinTemperatureOutValue)).setText(json.optString("minTempOut") + "°C");
                    ((TextView) findViewById(R.id.tMinPressureValue)).setText(json.optString("minPressure") + "hPa");
                    ((TextView) findViewById(R.id.tMinHumidityValue)).setText(json.optString("minHumidity") + "%");

                    ((TextView) findViewById(R.id.tMaxTemperatureInValue)).setText(json.optString("maxTempIn") + "°C");
                    ((TextView) findViewById(R.id.tMaxTemperatureOutValue)).setText(json.optString("maxTempOut") + "°C");
                    ((TextView) findViewById(R.id.tMaxPressureValue)).setText(json.optString("maxPressure") + "hPa");
                    ((TextView) findViewById(R.id.tMaxHumidityValue)).setText(json.optString("maxHumidity") + "%");


                    ((TextView) findViewById(R.id.tMinTemperatureInTime)).setText(json.optString("minTempInTime"));

                    ((TextView) findViewById(R.id.tMinTemperatureOutTime)).setText(json.optString("minTempOutTime"));

                    ((TextView) findViewById(R.id.tMinPressureTime)).setText(json.optString("minPressureTime"));

                    ((TextView) findViewById(R.id.tMinHumidityTime)).setText(json.optString("minHumidityTime"));

                    ((TextView) findViewById(R.id.tMaxTemperatureInTime)).setText(json.optString("maxTempInTime"));

                    ((TextView) findViewById(R.id.tMaxTemperatureOutTime)).setText(json.optString("maxTempOutTime"));

                    ((TextView) findViewById(R.id.tMaxPressureTime)).setText(json.optString("maxPressureTime"));

                    ((TextView) findViewById(R.id.tMaxHumidityTime)).setText(json.optString("maxHumidityTime"));

                    ((TextView) findViewById(R.id.tDate)).setText(json.optString("date"));
                }

            } catch (Exception e) {
                Log.d(MainActivity.class.getSimpleName(), e.toString());
            }
        }

        public String streamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;

            try {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                reader.close();

            } catch (IOException e) {
                Log.d(MainActivity.class.getSimpleName(), e.toString());
            }
            return stringBuilder.toString();
        }
    }

}
