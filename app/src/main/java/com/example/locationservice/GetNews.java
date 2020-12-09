package com.example.locationservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GetNews extends AppCompatActivity {

    Connect connect;
    TextView news;
    String result="0";
    List<String> news_list=new ArrayList<>();

    public class Connect extends AsyncTask<String,Void,String> {

        @Override
        protected  String doInBackground(String... params){
            try{
                result="0";
                String method = (String)params[0];
                String interest = (String)params[1];
                String data= URLEncoder.encode("method","UTF-8")+"="+URLEncoder.encode(method,"UTF-8");
                data+="&"+URLEncoder.encode("interest","UTF-8")+"="+URLEncoder.encode(interest,"UTF-8");
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
                System.out.println(result);
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
        setContentView(R.layout.activity_get_news);

        news=(TextView)findViewById(R.id.textView5);

        Intent intentMyGet=getIntent();
        String interest= intentMyGet.getStringExtra("interest");
        List<String> interest_list=new ArrayList<>();
        connect=new Connect();

        String[] temp;
        String delimeter = "_";
        temp = interest.split(delimeter);
        for(int i=0; i<temp.length; i++)
        {
            if(temp[i].charAt(0)=='S')
            {
                interest_list.add("Sciences");
            }
            else if(temp[i].charAt(0)=='B')
            {
                interest_list.add("Business");
            }
            else if(temp[i].charAt(0)=='C')
            {
                interest_list.add("Computers");
            }
            else if(temp[i].charAt(0)=='E')
            {
                interest_list.add("Education");
            }
        }
        for(int i=0;i<interest_list.size();i++)
        {
            System.out.println(interest_list.get(i));
            connect.execute("getnews", interest_list.get(i));
            news_list.add("\n-------"+interest_list.get(i)+"-------\n\n");
            try {
                System.out.println("111");
                Thread.sleep(15);
                System.out.println("222");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("333");
            System.out.println(result);
            while(result.equals("0"))
            {
                System.out.println("44444444");
            }

            System.out.println(result);
            System.out.println("444");
            String[] temp_news;
            String d2 = "<br>";
            temp_news = result.split(d2);
            for(int j=0;j<temp_news.length;j++)
            {
                news_list.add(temp_news[j]+"\n\n");
            }
            connect=new Connect();
        }
        System.out.println("555");

        String text="";
        for(int i=0;i<news_list.size();i++)
        {
            System.out.println("666");
            text=text+news_list.get(i);
        }
        System.out.println("777");
        news.setText(text);
    }
}
