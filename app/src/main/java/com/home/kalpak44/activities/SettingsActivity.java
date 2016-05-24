package com.home.kalpak44.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.home.kalpak44.executor.AsyncResponse;
import com.home.kalpak44.executor.Executor;
import com.home.kalpak44.utilities.MainContext;
import com.home.kalpak44.utilities.Settings;

/**
 * Created by kalpak44 on 23.05.16.
 */
public class SettingsActivity extends Activity {
    private EditText portEditText,hostEditText,usernameEditText,passwordEditText;
    private Settings config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        portEditText = (EditText) findViewById(R.id.editTextPort);
        hostEditText = (EditText) findViewById(R.id.editTextHost);
        usernameEditText = (EditText) findViewById(R.id.editTextUsername);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        config = ((MainContext)getApplicationContext()).getConfig();
        portEditText.setText(config.getPort()+"");
        hostEditText.setText(config.getHost());
        usernameEditText.setText(config.getUsername());
        passwordEditText.setText(config.getPassword());


    }

    public void onConnectBtnClick(View view){
        config.setPort(Integer.parseInt(portEditText.getText().toString()));
        config.setHost(hostEditText.getText().toString());
        config.setUsername(usernameEditText.getText().toString());
        config.setPassword(passwordEditText.getText().toString());

        //execute echo hello
        final ProgressDialog dialog = ProgressDialog.show(SettingsActivity.this, "",
                "Loading. Please wait...", true);
        dialog.show();
        new Executor(getApplicationContext()).getBaseExecutor().setAsyncResponse(new AsyncResponse() {
            @Override
            public void rcvResponse(String[] responses) {
                dialog.hide();
                if(responses != null){
                    if(responses[0].equals("hello")){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }else{
                    Toast toast = Toast.makeText(SettingsActivity.this, "No connection", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 350);
                    toast.show();
                }
            }

        }).execute("echo 'hello'");

    }




    @Override
    public void onBackPressed() {
        //nothing
    }

}
