package com.example.locationservice;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.YuvImage;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class LocateFriends extends AppCompatActivity implements
        SearchView.OnQueryTextListener {

    Connect connectsearch, connectlocation;
    String result;
    private SearchView searchView;
    private ListView listView;

    private List<String> username_list=new ArrayList<>();
    private List<String> mac_list=new ArrayList<>();
    private List<String> localation_list=new ArrayList<>();
    private List<String> time_list=new ArrayList<>();
    private List<String> final_list=new ArrayList<>();

    class Connect extends AsyncTask<String,Void,String> {
        @Override
        protected  String doInBackground(String... params){
            try{
                result="0";
                String method = (String)params[0];
                String mac = (String)params[1];
                String data= URLEncoder.encode("method","UTF-8")+"="+URLEncoder.encode(method,"UTF-8");
                data+="&"+URLEncoder.encode("mac_location","UTF-8")+"="+URLEncoder.encode(mac,"UTF-8");
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
        setContentView(R.layout.activity_locate_friends);

        connectsearch=new Connect();
        connectlocation=new Connect();

        connectsearch.execute("search","1");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(result.equals("0"))
        {
            System.out.println("44444444");
        }
        String[] temp;
        String delimeter = "<br>";
        temp = result.split(delimeter);
        for (int i = 0; i < temp.length; i++) {
            String[] word;
            String d2 = ";";
            word = temp[i].split(d2);
            username_list.add(word[0]);
            if(word[1].isEmpty())
            {
                mac_list.add("No Location Info");
            }
            else {
                mac_list.add(word[1]);
            }
            String time = word[2];
            Date date = TimeCalculate.re_time(time);
            time_list.add(TimeCalculate.natureTime(date));
        }
        connectsearch=new Connect();

        for(int i=0;i<mac_list.size();i++)
        {
            connectlocation.execute("location", mac_list.get(i));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(result.equals("0"))
            {
                System.out.println("44444444");
            }
            localation_list.add(result);
            connectlocation=new Connect();
        }
        for(int j=0;j<username_list.size();j++)
        {
            final_list.add(username_list.get(j)+"       "+localation_list.get(j)+"    "+time_list.get(j));
        }
        connectlocation=new Connect();
        listView=(ListView)findViewById(R.id.lv);
        listView.setAdapter(new ArrayAdapter<String>(LocateFriends.this,R.layout.support_simple_spinner_dropdown_item,final_list));
        listView.setTextFilterEnabled(true);
        searchView=(SearchView)findViewById(R.id.sv);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(LocateFriends.this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Friend's Name: ");
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (TextUtils.isEmpty(s)) {
            listView.clearTextFilter();
        } else {
            listView.setFilterText(s);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    static class TimeCalculate {

//        private final static long YEAR = 1000 * 60 * 60 * 24 * 365L;
//        private final static long MONTH = 1000 * 60 * 60 * 24 * 30L;
//        private final static long DAY = 1000 * 60 * 60 * 24L;
//        private final static long HOUR = 1000 * 60 * 60L;
//        private final static long MINUTE = 1000 * 60L;
//        public String time_re(Date date) {
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String time_normal = format.format(date);
//            return time_normal;
//        }
        public static Date re_time(String time) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = format.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }
        public static String natureTime(Date date) {
            Date now = new Date();
            long sub = now.getTime() - date.getTime();
            if (sub > 1000 * 60 * 60 * 24 * 365L) {
                return ((sub - (1000 * 60 * 60 * 24 * 365L)) / (1000 * 60 * 60 * 24 * 365L) + 1) + " year(s) ago";
            }
            else if (sub > 1000 * 60 * 60 * 24 * 30L) {
                return ((sub - (1000 * 60 * 60 * 24 * 30L)) / (1000 * 60 * 60 * 24 * 30L) + 1) + " month(s) ago";
            }
            else if (sub > 1000 * 60 * 60 * 24L) {
                return ((sub -(1000 * 60 * 60 * 24L)) / (1000 * 60 * 60 * 24L) + 1) + " day(s) ago";
            }
            else if (sub > 1000 * 60 * 60L) {
                return ((sub - (1000 * 60 * 60L)) / (1000 * 60 * 60L) + 1) + " hour(s) ago";
            }
            else if (sub > 1000 * 60L) {
                return ((sub - (1000 * 60L)) / (1000 * 60L) + 1) + " minute(s) ago";
            }
            else
            {
                return (sub/1000)+1+" second(s) ago";
            }
            //return "Just now";
        }
    }
}