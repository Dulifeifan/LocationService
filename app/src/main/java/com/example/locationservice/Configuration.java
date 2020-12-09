package com.example.locationservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Configuration extends AppCompatActivity {

    EditText username, fullname;
    CheckBox Science, Business, Computers, Education;
    Button submit;
    String result="0";
    Connect connect;
    String finalUpdate_interests;
    String finalUpdate_freq="10";

    public class Connect extends AsyncTask<String,Void,String> {

        @Override
        protected  String doInBackground(String... params){
            try{
                result="0";
                String method = (String)params[0];
                String username = (String)params[1];
                String fullname = (String)params[2];
                String interest=(String)params[3];
                String freq = (String)params[4];
                String ori_username=(String)params[5];
                String data= URLEncoder.encode("method","UTF-8")+"="+URLEncoder.encode(method,"UTF-8");
                data+="&"+URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                data+="&"+URLEncoder.encode("fullname","UTF-8")+"="+URLEncoder.encode(fullname,"UTF-8");
                data+="&"+URLEncoder.encode("interest","UTF-8")+"="+URLEncoder.encode(interest,"UTF-8");
                data+="&"+URLEncoder.encode("freq","UTF-8")+"="+URLEncoder.encode(freq,"UTF-8");
                data+="&"+URLEncoder.encode("ori_username","UTF-8")+"="+URLEncoder.encode(ori_username,"UTF-8");
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
        setContentView(R.layout.activity_configeration);

        connect=new Connect();
        username= (EditText)findViewById(R.id.editText6);
        fullname= (EditText)findViewById(R.id.editText7);
        Science=(CheckBox)findViewById(R.id.checkBox2);
        Business=(CheckBox)findViewById(R.id.checkBox3);
        Computers=(CheckBox)findViewById(R.id.checkBox4);
        Education=(CheckBox)findViewById(R.id.checkBox5);
        submit= (Button)findViewById(R.id.button5);

        Intent intentMyConfig=getIntent();
        final String text_fullname=intentMyConfig.getStringExtra("fullname");
        final String text_name=intentMyConfig.getStringExtra("username");
        final String text_interest=intentMyConfig.getStringExtra("interest");
        final String text_freq=intentMyConfig.getStringExtra("userfrequency");
        username.setText(text_name);
        fullname.setText(text_fullname);

        connect=new Connect();

        String[] temp;
        String delimeter = "_";
        temp = text_interest.split(delimeter);
        for(int i=0; i<temp.length; i++)
        {
            if(temp[i].charAt(0)=='S')
            {
                Science.setChecked(true);
            }
            else if(temp[i].charAt(0)=='B')
            {
                Business.setChecked(true);
            }
            else if(temp[i].charAt(0)=='C')
            {
                Computers.setChecked(true);
            }
            else if(temp[i].charAt(0)=='E')
            {
                Education.setChecked(true);
            }
        }



//        final String update_loginname=username.getText().toString();
//        final String update_fullname=fullname.getText().toString();






        final String[] selected_freq = {""};
        String[] frequencychoise = new String[]{"10 Seconds", "1 Minute", "15 Minutes", "30 Minutes", "60 Minutes"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, frequencychoise);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = super.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_freq[0] =adapter.getItem(i);
                String update_freq="10";
                if(selected_freq[0].equals("10 Seconds"))
                {
                    update_freq="10";
                }
                else if(selected_freq[0].equals("1 Minute"))
                {
                    update_freq="60";
                }
                else if(selected_freq[0].equals("15 Minutes"))
                {
                    update_freq="900";
                }
                else if(selected_freq[0].equals("30 Minutes"))
                {
                    update_freq="1800";
                }
                else if(selected_freq[0].equals("60 Minutes"))
                {
                    update_freq="3600";
                }

                finalUpdate_freq = update_freq;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String update_loginname=username.getText().toString();
                final String update_fullname=fullname.getText().toString();
                if(update_loginname.isEmpty()||update_fullname.isEmpty())
                {
                    Toast.makeText(Configuration.this,"Update Failed.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String update_interests="";
                    if(Science.isChecked())
                    {
                        update_interests+="Sciences(Default)";
                    }
                    if(Business.isChecked())
                    {
                        if(!update_interests.isEmpty())
                        {
                            update_interests+="_";
                        }
                        update_interests+="Business";
                    }
                    if(Computers.isChecked())
                    {
                        if(!update_interests.isEmpty())
                        {
                            update_interests+="_";
                        }
                        update_interests+="Computers";
                    }
                    if(Education.isChecked())
                    {
                        if(!update_interests.isEmpty())
                        {
                            update_interests+="_";
                        }
                        update_interests+="Education";
                    }
                    if(update_interests.isEmpty())
                    {
                        update_interests="Sciences(Default)";
                    }
                    finalUpdate_interests = update_interests;

                    connect.execute("update", update_loginname,update_fullname, finalUpdate_interests, finalUpdate_freq,text_name);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while(result.equals("0"))
                    {
                        System.out.println("44444444");
                    }
                    Toast.makeText(Configuration.this,"Update Success!", Toast.LENGTH_SHORT).show();
                    String[] temp;
                    String delimeter = ";";
                    temp = result.split(delimeter);
                    Intent intent_proff =new Intent(Configuration.this, Profile.class);
                    intent_proff.putExtra("username",temp[1]);
                    intent_proff.putExtra("fullname",temp[2]);
                    intent_proff.putExtra("interest",temp[3]);
                    intent_proff.putExtra("userfrequency",temp[4]);
                    startActivity(intent_proff);
                    connect=new Connect();
                }

            }
        });

    }
}
