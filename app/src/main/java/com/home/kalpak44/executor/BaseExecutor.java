package com.home.kalpak44.executor;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.home.kalpak44.utilities.Settings;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by kalpak44 on 23.05.16.
 */
public class BaseExecutor extends AsyncTask<String, Void, String[]> {
    private JSch jsch = null;
    private Session session = null;
    private ChannelExec channel = null;
    private JSchException jSchException = null;
    private IOException ioException = null;
    private NullPointerException nullPointerException = null;
    private Settings config;
    private AsyncResponse delegate = null;


    public BaseExecutor(Settings config){
        super();
        this.config = config;
    }
    public BaseExecutor setAsyncResponse(AsyncResponse delegate) {
        this.delegate = delegate;
        return this;
    }

    @Override
    protected String[] doInBackground(String... commands) {
        int count = commands.length;
        String[] responses = new String[count];
        for (int l = 0;l < count;l++){
            Log.i("soft", "exec: "+commands[l]);

            jsch = new JSch();
            try {
                if(session == null){
                    session = jsch.getSession(config.getUsername(), config.getHost(), config.getPort());
                    session.setPassword(config.getPassword());
                    Properties cf = new Properties();
                    cf.put("StrictHostKeyChecking", "no");
                    session.setConfig(cf);
                }
                if(!session.isConnected()){
                    session.connect();
                }
                channel = (ChannelExec) session.openChannel("exec");

                channel.setCommand(commands[l] + System.lineSeparator());
                channel.setPty(true);

                BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
                OutputStream out = channel.getOutputStream();
                channel.setErrStream(System.err);
                channel.connect();

                if(commands[l].length()>4 && commands[l].substring(0,4).equals("sudo")){
                    out.write((config.getPassword()+System.lineSeparator()).getBytes());
                }
                out.flush();

                String line,result = null;
                while((line = in.readLine())!=null){
                    if(!line.isEmpty()){
                        result = line;
                    }
                }
                responses[l] = result;

            } catch (JSchException e) {
                this.jSchException = e;
                responses = null;
            } catch (IOException e) {
                this.ioException = e;
                responses = null;
            } catch (NullPointerException e){
                this.nullPointerException = e;
                responses = null;
            }
        }
        if(channel != null){
            channel.disconnect();
        }
        if(session != null){
            session.disconnect();
        }
        return responses;
    }
    protected void onPostExecute(String[] responses){
        if(responses != null){
            for (int i=0;i<responses.length;i++){
                Log.i("soft", "response["+i+"]: " + responses[i]);
            }
        }
        delegate.rcvResponse(responses);
    }

}
