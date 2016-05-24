package com.home.kalpak44.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.PopupMenu;


import com.home.kalpak44.executor.AsyncResponse;
import com.home.kalpak44.executor.Executor;
import com.home.kalpak44.utilities.Settings;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TimePicker tpHourMin;
    private FloatingActionButton fab;
    private SeekBar s1,s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Executor(getApplicationContext()).getBaseExecutor().setAsyncResponse(new AsyncResponse() {
            @Override
            public void rcvResponse(String[] responses) {
                if(responses != null){
                    initComponents();
                }else{
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                }

            }
        }).execute("echo 'test connection'");

    }
    private void initVolume(){
        String command  = "amixer get Master,0 | egrep -o \"[0-9]+%\" | tr -d '%'";
        new Executor(getApplicationContext()).getBaseExecutor().setAsyncResponse(new AsyncResponse() {
            @Override
            public void rcvResponse(String[] responses) {
                try {
                    int volume = Integer.parseInt(responses[0]);
                    s1.setProgress(volume);
                }catch (NumberFormatException e){
                    Toast.makeText(MainActivity.this, "wtf?", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute(command);

    }
    private void setVolume(int volume){
        String command = "amixer sset Master "+volume+"%";
        new Executor(getApplicationContext()).getBaseExecutor().setAsyncResponse(new AsyncResponse() {
            @Override
            public void rcvResponse(String[] responses) {
                //nothing
            }
        }).execute(command);
    }
    private void setBrightness(int b){
        double a = round(b/11.1+0.9,2);
        String command = "export DISPLAY=:0.0 && xgamma -gamma "+a;
        new Executor(getApplicationContext()).getBaseExecutor().setAsyncResponse(new AsyncResponse() {
            @Override
            public void rcvResponse(String[] responses) {
                //nothing
            }
        }).execute(command);

    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.UP);
        return bd.doubleValue();
    }

    private void shutdownNow(){
        String command = "sudo shutdown -P now";
        new Executor(getApplicationContext()).getBaseExecutor().setAsyncResponse(new AsyncResponse() {
            @Override
            public void rcvResponse(String[] responses) {
                //nothing
            }
        }).execute(command);
    }
    private void rebootNow(){
        String command = "sudo reboot";
        new Executor(getApplicationContext()).getBaseExecutor().setAsyncResponse(new AsyncResponse() {
            @Override
            public void rcvResponse(String[] responses) {
                //nothing
            }
        }).execute(command);
    }
    private void shutdownOn(int h,int min){
        String command = "sudo shutdown -P "+h+":"+min;
        new Executor(getApplicationContext()).getBaseExecutor().setAsyncResponse(new AsyncResponse() {
            @Override
            public void rcvResponse(String[] responses) {
                //nothing
            }
        }).execute(command);
    }
    private void shutdownChancel(){
        String command = "shutdown -c";
        new Executor(getApplicationContext()).getBaseExecutor().setAsyncResponse(new AsyncResponse() {
            @Override
            public void rcvResponse(String[] responses) {
                //nothing
            }
        }).execute(command);
    }


    private void initComponents(){
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        s1 = (SeekBar)findViewById(R.id.seekBar);
        s2 = (SeekBar) findViewById(R.id.seekBar2);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });



        s1.setOnSeekBarChangeListener(

            new SeekBar.OnSeekBarChangeListener() {
                int progress = 0;
                @Override
                public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                    progress = progresValue;
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    //nothing
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Toast.makeText(MainActivity.this, "s : " + progress, Toast.LENGTH_SHORT).show();
                    setVolume(progress);
                }
            });
        s2.setOnSeekBarChangeListener(

                new SeekBar.OnSeekBarChangeListener() {
                    int progress = 0;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                        progress = progresValue;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        //nothing
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Toast.makeText(MainActivity.this, "b : " + progress, Toast.LENGTH_SHORT).show();
                        setBrightness(progress);
                    }
                });
        s2.setProgress(0);
        initTimePicker();
        initVolume();
    }


    private void initTimePicker() {
        tpHourMin = (TimePicker) findViewById(R.id.timePicker);
        tpHourMin.setIs24HourView(true);

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        tpHourMin.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
        tpHourMin.setCurrentMinute(c.get(Calendar.MINUTE) + 5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_about) {
            Toast.makeText(MainActivity.this, "Created by Ptaha", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showMenu(View v) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(MainActivity.this, fab);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.shutdown_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.shutdown_now:
                        shutdownNow();
                        return true;
                    case R.id.restart_now:
                        rebootNow();
                        return true;
                    case R.id.shutdown_on_time:
                        shutdownOn(tpHourMin.getCurrentHour(),tpHourMin.getCurrentMinute());
                        Toast.makeText(MainActivity.this, "Shutdown -P "+tpHourMin.getCurrentHour()+":"
                                +tpHourMin.getCurrentMinute(), Toast.LENGTH_SHORT).show();
                        tpHourMin.setEnabled(false);
                        return true;
                    case R.id.shutdown_chancel:
                        shutdownChancel();
                        Toast.makeText(MainActivity.this, "Shutdown -C", Toast.LENGTH_SHORT).show();
                        tpHourMin.setEnabled(true);
                        return true;
                    default:
                        return true;
                }

            }
        });

        popup.show();//showing popup menu
    }


}
