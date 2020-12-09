package com.example.locationservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;

public class Profile extends AppCompatActivity implements Runnable{
    TextView fullname, username, interest, frequency;
    Button getnews,locatefriends,configuration;
    Connect connect;
    String result="0";

    //Threads
    int Current_user_update_frequency=5; //per 5 seconds
    Thread update_thread = new Thread(Profile.this); //Thread
    boolean show_info_flag=true;



    public class Connect extends AsyncTask<String,Void,String> {

        @Override
        protected  String doInBackground(String... params){
            try{
                result="0";
                String method = (String)params[0];
                String username = (String)params[1];
                String mac=(String)params[2];
                String wifi=(String)params[3];
//                String time=(String)params[3];
                String data= URLEncoder.encode("method","UTF-8")+"="+URLEncoder.encode(method,"UTF-8");
                data+="&"+URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                data+="&"+URLEncoder.encode("mac","UTF-8")+"="+URLEncoder.encode(mac,"UTF-8");
                data+="&"+URLEncoder.encode("wifi","UTF-8")+"="+URLEncoder.encode(wifi,"UTF-8");
//                data+="&"+URLEncoder.encode("time","UTF-8")+"="+URLEncoder.encode(time,"UTF-8");
                String link="http://10.0.2.2:8888/demo/index.php";
                URL url=new URL(link);
                URLConnection conn=url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                BufferedReader reader= new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb=new StringBuilder();
                String line=null;
                while ((line=reader.readLine())!=null){
                    sb.append(line);
                }
                Log.d("My Result: ", sb.toString());
                result=sb.toString();
            }
            catch (Exception e){
                result="Exception: " + e.getMessage();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        connect=new Connect();
        fullname = (TextView)findViewById(R.id.textView9);
        username = (TextView)findViewById(R.id.textView11);
        interest = (TextView)findViewById(R.id.textView13);
        frequency = (TextView)findViewById(R.id.textView15);
        configuration =(Button) findViewById(R.id.button4);
        locatefriends=(Button)findViewById(R.id.button2);
        getnews=(Button)findViewById(R.id.button);

        Intent intentMyProf=getIntent();
        fullname.setText(intentMyProf.getStringExtra("fullname"));
        username.setText(intentMyProf.getStringExtra("username"));
        interest.setText(intentMyProf.getStringExtra("interest"));
        frequency.setText(intentMyProf.getStringExtra("userfrequency"));

        Current_user_update_frequency=Integer.valueOf((String)frequency.getText()).intValue();

        connect=new Connect();

        //start thread
        if(this.update_thread.getState()== Thread.State.NEW) {
            this.update_thread.start();
        }

        configuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_config =new Intent(Profile.this, Configuration.class);
                intent_config.putExtra("username",(String)username.getText());
                intent_config.putExtra("fullname",(String)fullname.getText());
                intent_config.putExtra("interest",(String)interest.getText());
                intent_config.putExtra("userfrequency",(String)frequency.getText());
                startActivity(intent_config);
            }
        });
        locatefriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_loc=new Intent(Profile.this,LocateFriends.class);
                startActivity(intent_loc);
            }
        });
        getnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_getnews= new Intent(Profile.this, GetNews.class);
                intent_getnews.putExtra("interest",(String)interest.getText());
                startActivity(intent_getnews);
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        this.show_info_flag = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.show_info_flag = true;
    }

    @Override
    public void run() {
        while(true) {
            try {
                if(show_info_flag){
                    // Get Mac Address of this local device.
                    GetMac get_mac = new GetMac(Profile.this);
                    final String local_mac = get_mac.getMacAddr();//get_mac.get_Local_Mac();

                    String[] temp;
                    String delimeter = ",";
                    temp = local_mac.split(delimeter);
                    final String mac=temp[0];
                    final String wifi=temp[1];
                    // Print information in Logcat.
                    Log.d("xincoder_thread", local_mac);
                    // Toast information
                    runOnUiThread(new Runnable() {
                        public void run() {
//                            Date now = new Date();
//                            long time = now.getTime();
//                            connect.execute("mac", (String) username.getText(), local_mac, String.valueOf(time));
                            connect.execute("mac", (String) username.getText(), mac,wifi);
                            connect=new Connect();
                            Toast.makeText(Profile.this, "Mac Address Updated.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                // Sleep 5 seconds.
                Thread.sleep(this.Current_user_update_frequency * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
