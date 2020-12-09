package com.example.locationservice;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;


public class GetMac {
    Context current_context;
    WifiManager wifiManager;

    String Local_Mac; // mobile device mac address


    public GetMac(Context context){
        this.current_context = context;
        init();
    }

    private void init(){
        wifiManager = (WifiManager) current_context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
        wifiManager.startScan();
    }


    public String get_Local_Mac() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        this.Local_Mac = wifiInfo.getMacAddress();
        return this.Local_Mac;
//        WifiManager wifiManager = (WifiManager)current_context.getSystemService(Context.WIFI_SERVICE);
//        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
//        }
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        int ipAddress = wifiInfo.getIpAddress();
//        String ip=(ipAddress & 0xFF ) + "." +
//                ((ipAddress >> 8 ) & 0xFF) + "." +
//                ((ipAddress >> 16 ) & 0xFF) + "." +
//                ( ipAddress >> 24 & 0xFF) ;
//        return ip;
    }

    public String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0") && !nif.getName().equalsIgnoreCase("eth0"))
                    continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString()+","+get_Local_Mac();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

}
