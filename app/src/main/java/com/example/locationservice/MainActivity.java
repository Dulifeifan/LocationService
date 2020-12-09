package com.example.locationservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    Connect connect;
    EditText username, password;
    TextView register;
    Button login;
    String result="0";

    public class Connect extends AsyncTask<String,Void,String> {

        @Override
        protected  String doInBackground(String... params){
            try{
                result="0";
                String method = (String)params[0];
                String username = (String)params[1];
                String password = (String)params[2];
                String data= URLEncoder.encode("method","UTF-8")+"="+URLEncoder.encode(method,"UTF-8");
                data+="&"+URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                data+="&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
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
        setContentView(R.layout.log_in);

        connect=new Connect();
        login =(Button) findViewById(R.id.button_log_in);
        username= (EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText2);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_username=username.getText().toString();
                String login_password=password.getText().toString();
                connect.execute("login", login_username, login_password);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(result.equals("0"))
                {
                    System.out.println("44444444");
                }
                String[] temp;
                String delimeter = ";";
                temp = result.split(delimeter);
                if(temp[0].equals("Succeed")){
                    Toast.makeText(MainActivity.this,"Login Success!", Toast.LENGTH_SHORT).show();
                    Intent intent_proff =new Intent(MainActivity.this, Profile.class);
                    intent_proff.putExtra("username",temp[1]);
                    intent_proff.putExtra("fullname",temp[2]);
                    intent_proff.putExtra("interest",temp[3]);
                    intent_proff.putExtra("userfrequency",temp[4]);
                    startActivity(intent_proff);
                }
                else {
                    Toast.makeText(MainActivity.this,"Login Failed.", Toast.LENGTH_SHORT).show();
                }
                connect=new Connect();
            }
        });

        register=(TextView) findViewById(R.id.textView3);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_reg =new Intent(MainActivity.this, Register.class);
                startActivity(intent_reg);
            }
        });
    }
}
