package com.example.kasia.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {
    //public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.battery:
            {
                Intent intent_battery = new Intent(this, Battery.class);
                startActivity(intent_battery);
                //break;
            }
            return true;

            case R.id.connect:
            {
                Intent intent_connect = new Intent(this, Connect.class);
                startActivity(intent_connect);
                //return true;
            }
            return true;
            case R.id.current:
            {
                Intent intent_current = new Intent(this, Current.class);
                startActivity(intent_current);
                //return true;
            }
            return true;
            case R.id.speed:
            {
                Intent intent_speed = new Intent(this, Speed.class);
                startActivity(intent_speed);
                //return true;
            }
            return true;
            case R.id.temperature:
            {
                Intent intent_temperature = new Intent(this, Temperature.class);
                startActivity(intent_temperature);
                //return true;
            }
            return true;
            case R.id.map:
            {
                //Intent intent_battery = new Intent(this, Battery.class);
                //startActivity(intent_battery);
                return true;
            }
            //return true;
            case R.id.lights:
            {
                Intent intent_lights = new Intent(this, Lights.class);
                startActivity(intent_lights);
                //return true;
            }
            return true;
            case R.id.settings:
            {
                Intent intent_settings = new Intent(this, Settings.class);
                startActivity(intent_settings);
                //return true;
            }
            return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }



}
