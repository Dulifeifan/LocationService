package com.example.locationservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class Register extends AppCompatActivity {

    Connect connect;
    EditText username, password, confirmpassword;
    Button register;
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
        setContentView(R.layout.activity_register);

        register =(Button) findViewById(R.id.button3);
        username= (EditText)findViewById(R.id.editText3);
        password=(EditText)findViewById(R.id.editText4);
        confirmpassword=(EditText)findViewById(R.id.editText5);
        connect=new Connect();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reg_username=username.getText().toString();
                String reg_password=password.getText().toString();
                String reg_confpassword=confirmpassword.getText().toString();
                if(!reg_password.equals(reg_confpassword)){
                    Toast.makeText(Register.this, "Failed: Password doesn't match.",
                                    Toast.LENGTH_SHORT).show();
                }
                else {
                    connect.execute("register", reg_username,reg_password);
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
                        Toast.makeText(Register.this,"Register Success!", Toast.LENGTH_SHORT).show();
                        Intent intent_prof =new Intent(Register.this, Profile.class);
                        intent_prof.putExtra("username",temp[1]);
                        intent_prof.putExtra("fullname",temp[2]);
                        intent_prof.putExtra("interest",temp[3]);
                        intent_prof.putExtra("userfrequency",temp[4]);
                        startActivity(intent_prof);
                    }
                    else {
                        Toast.makeText(Register.this,"Register Failed.", Toast.LENGTH_SHORT).show();
                    }
                }
                connect=new Connect();
            }
        });
    }
}
