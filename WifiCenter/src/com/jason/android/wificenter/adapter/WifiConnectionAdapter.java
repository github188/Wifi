package com.jason.android.wificenter.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.jason.android.wificenter.R;
import com.jason.android.wificenter.WifiCenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class WifiConnectionAdapter extends BaseExpandableListAdapter {
	
	Context mContext;
	
	WifiManager mWifiManager;
	
	ArrayList<WifiConfiguration> connList = new ArrayList<WifiConfiguration>();
	
	Comparator<WifiConfiguration> cmp = new Comparator<WifiConfiguration>(){

		@Override
		public int compare(WifiConfiguration lhs, WifiConfiguration rhs) {
			// TODO Auto-generated method stub
			if(lhs.status < rhs.status){
				return -1;
			}else if(lhs.status > rhs.status){
				return 1;
			}
			return 0;
		}
		
	};
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {  
		  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            // TODO Auto-generated method stub  
            if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) { 
            	notifyDataSetChanged();
            	
            	int status = WifiCenter.isWifiContected(mContext);
            	if (status == WifiCenter.WIFI_CONNECTED || status == WifiCenter.WIFI_DISCONNECTING){
                	mContext.unregisterReceiver(mBroadcastReceiver);
                }
            	
            }  
        }  
    }; 
	
	public WifiConnectionAdapter(Context context){
		this.mContext = context;
		
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}
	
	public void addList(List<WifiConfiguration> source){
		connList.addAll(source);
		
		Collections.sort(connList, cmp);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		switch(groupPosition){
		case 0:
			return 1;
		case 1:
			return connList.size();
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		switch(groupPosition){
		case 0:
			return "开关";
		case 1:
			return "可用WLAN列表";
		}
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		if(groupPosition == 0){
			return WifiCenter.isWifiContected(mContext);
		}else if(groupPosition == 1){
			return connList.get(childPosition);
		}
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView itemView;
		if(convertView == null){
			itemView = new TextView(mContext);
			itemView.setTextColor(Color.WHITE);
			itemView.setBackgroundColor(Color.BLACK);
		}else{
			itemView = (TextView) convertView;
		}
		
		itemView.setText((CharSequence) this.getGroup(groupPosition));
		
		return itemView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		View itemView = null;
		if(groupPosition == 0){
			if(convertView == null || !((String)convertView.getTag()).equals("OPEN")){
				itemView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.item_wifi_open, null);
				Switch swh_wifi_open = (Switch) itemView.findViewById(R.id.swh_wifi_open);
				swh_wifi_open.setOnCheckedChangeListener(new OnCheckedChangeListener(){

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if((WifiCenter.isWifiContected(mContext) == WifiCenter.WIFI_CONNECTED) != isChecked){
							mContext.registerReceiver(mBroadcastReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
							mWifiManager.setWifiEnabled(isChecked);
							//notifyDataSetChanged();
						}
					}
					
				});
				itemView.setTag("OPEN");
			}else{
				itemView = convertView;
			}
			
			Switch swh_wifi_open = (Switch) itemView.findViewById(R.id.swh_wifi_open);
			int status = (Integer)getChild(groupPosition, childPosition);
			switch(status){
			case WifiCenter.WIFI_CONNECTED:
				swh_wifi_open.setEnabled(true);
				swh_wifi_open.setChecked(true);
				break;
			case WifiCenter.WIFI_CONNECTING:
				swh_wifi_open.setEnabled(false);
				swh_wifi_open.setChecked(false);
				break;
			case WifiCenter.WIFI_DISCONNECTING:
				swh_wifi_open.setEnabled(false);
				swh_wifi_open.setEnabled(true);
				break;
			case WifiCenter.WIFI_CONNECT_FAILED:
				swh_wifi_open.setEnabled(true);
				swh_wifi_open.setChecked(false);
				break;
			}

		}else{
			if(convertView == null || !((String)convertView.getTag()).equals("ITEM")){
				itemView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.item_wifi_config, null);
				itemView.setTag("ITEM");
			}else{
				itemView = convertView;
			}
			
			WifiConfiguration item = (WifiConfiguration) this.getChild(groupPosition, childPosition);
			TextView tv_wifi_name = (TextView) itemView.findViewById(R.id.tv_wifi_name);
			tv_wifi_name.setText(item.SSID);
			TextView tv_wifi_status = (TextView) itemView.findViewById(R.id.tv_wifi_status);
			tv_wifi_status.setText(WifiConfiguration.Status.strings[item.status]);
		}
		
		return itemView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
