package com.jason.android.wificenter;

import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;

public class WifiCenter {
      
    public static final int WIFI_CONNECTED = 0x01;  
    public static final int WIFI_CONNECT_FAILED = 0x02;  
    public static final int WIFI_CONNECTING = 0x03; 
    public static final int WIFI_DISCONNECTING = 0x04; 
    /** 
     * 判断wifi是否连接成功,不是network 
     *  
     * @param context 
     * @return 
     */  
    public static int isWifiContected(Context context) {  
        ConnectivityManager connectivityManager = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo wifiNetworkInfo = connectivityManager  
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
          
        if (wifiNetworkInfo.getDetailedState() == DetailedState.OBTAINING_IPADDR  
                || wifiNetworkInfo.getDetailedState() == DetailedState.CONNECTING) {  
            return WIFI_CONNECTING;  
        } else if (wifiNetworkInfo.getDetailedState() == DetailedState.CONNECTED) {  
            return WIFI_CONNECTED;  
        } else if(wifiNetworkInfo.getDetailedState() == DetailedState.DISCONNECTING) {
        	return WIFI_DISCONNECTING;
    	}else{  
            return WIFI_CONNECT_FAILED;  
        }  
    }  
      
    
}
