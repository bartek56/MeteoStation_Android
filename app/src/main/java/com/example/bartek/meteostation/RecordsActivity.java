/********************************************************
 *
 *
 *  Autor: Bartosz Brzozowski
 *
 *
 ***********************************************************/

package com.example.bartek.meteostation;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
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

public class RecordsActivity extends AppCompatActivity {

    private static int day;
    private static int month;
    private static int year;

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void bSelectDate(View view) {
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker1);
        this.day = datePicker.getDayOfMonth();
        this.month = datePicker.getMonth();
        this.year = datePicker.getYear();

        Intent intent = new Intent(this,Records2.class);
        startActivity(intent);
    }



}
