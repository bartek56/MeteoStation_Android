/********************************************************
 *
 *  Aplikacja na system Android 4.4 KitKat oraz starsze
 *  odczytująca dane pogodowe z zaprojektowanej stacji Meteo
 *
 *  Autor: Bartosz Brzozowski
 *
 *
 *
 ***********************************************************/

package com.example.bartek.meteostation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new WebService().execute("http://meteostation.y0.pl/JSON.php");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_records)
        {
            Intent intent = new Intent(this,RecordsActivity.class);
            startActivity(intent); // uruchomienie
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class WebService extends AsyncTask<String, Void, String>
    {

        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

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

                InputStream in = new BufferedInputStream(connection.getInputStream());

                return streamToString(in);

            } catch (Exception e) {
                Log.d(MainActivity.class.getSimpleName(), e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();

            int light;
            int rain;

            ImageView imageView = (ImageView) findViewById(R.id.imageWheather);

            try {
                // reprezentacja obiektu JSON w Javie
                JSONObject json = new JSONObject(s);

                // pobranie pól obiektu JSON i wyświetlenie ich na ekranie
                ((TextView) findViewById(R.id.tTemperatureValueIn)).setText("In: "+json.optString("tempIn")+"\u00b0C, ");
                ((TextView) findViewById(R.id.tTemperatureValueOut)).setText("Out: "+json.optString("tempOut")+"\u00b0C");
                ((TextView) findViewById(R.id.tLastUpdate)).setText("Last Update \n"+json.optString("lastUpdate"));
                ((TextView) findViewById(R.id.tPressureValue)).setText(json.optString("pressure")+"hPa");
                ((TextView) findViewById(R.id.tHumidityValue)).setText(json.optString("humidity")+"%");

                light = Integer.parseInt(json.optString("light"));
                rain = Integer.parseInt(json.optString("rain"));


                if(rain==0)
                {
                    switch(light) {
                        case 1: imageView.setImageResource(R.drawable.night); break;
                        case 2: imageView.setImageResource(R.drawable.cloudy); break;
                        case 3: imageView.setImageResource(R.drawable.sunny); break;
                    }

                }
                else if(rain==1)
                {
                    switch(light) {
                        case 1: imageView.setImageResource(R.drawable.rain_night); break;
                        case 2: imageView.setImageResource(R.drawable.rain_cloudy); break;
                        case 3: imageView.setImageResource(R.drawable.rain_sunny); break;
                    }
                }
            } catch (Exception e) {
                Log.d(MainActivity.class.getSimpleName(), e.toString());
            }
        }

        // konwersja z InputStream do String
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
