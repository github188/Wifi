package com.jason.android.wificenter;

import java.util.List;

import com.jason.android.wificenter.adapter.WifiConnectionAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.GroupCipher;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiConfiguration.PairwiseCipher;
import android.net.wifi.WifiConfiguration.Protocol;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;

public class MainActivity extends ActionBarActivity {
	
	ExpandableListView explv_wificonnection;
	
	WifiManager mWifiManager;
	
	WifiConnectionAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 取得WifiManager对象  
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> result = mWifiManager.getConfiguredNetworks();
		
		explv_wificonnection = (ExpandableListView) this.findViewById(R.id.explv_wificonnection);
		explv_wificonnection.setOnGroupCollapseListener(new OnGroupCollapseListener(){

			@Override
			public void onGroupCollapse(int groupPosition) {
				// TODO Auto-generated method stub
				explv_wificonnection.expandGroup(groupPosition);
			}
			
		});
		adapter = new WifiConnectionAdapter(this);
		adapter.addList(result);
		explv_wificonnection.setAdapter(adapter);
		explv_wificonnection.expandGroup(0);
		explv_wificonnection.expandGroup(1);
		
		explv_wificonnection.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition,
					long id) {
				// TODO Auto-generated method stub
				if(groupPosition == 1){
					StringBuilder sb = new StringBuilder();
					WifiConfiguration item = (WifiConfiguration) adapter.getChild(groupPosition, childPosition);
					sb.append("BSSID = "+item.BSSID);
					sb.append("\nnetworkId = "+item.networkId);
					sb.append("\npreSharedKey = "+item.preSharedKey);
					sb.append("\npriority = "+item.priority);
					sb.append("\nSSID = "+item.SSID);
					sb.append("\nstatus = "+WifiConfiguration.Status.strings[item.status]);
					sb.append("\nwepKey = "+item.wepKeys[item.wepTxKeyIndex]);
					sb.append("\nhiddenSSID = "+item.hiddenSSID);
					sb.append('\n');
					sb.append(" KeyMgmt:");
			        for (int k = 0; k < item.allowedKeyManagement.size(); k++) {
			            if (item.allowedKeyManagement.get(k)) {
			                sb.append(" ");
			                if (k < KeyMgmt.strings.length) {
			                    sb.append(KeyMgmt.strings[k]);
			                } else {
			                    sb.append("??");
			                }
			            }
			        }
			        sb.append('\n');
			        sb.append(" Protocols:");
			        for (int p = 0; p < item.allowedProtocols.size(); p++) {
			            if (item.allowedProtocols.get(p)) {
			                sb.append(" ");
			                if (p < Protocol.strings.length) {
			                    sb.append(Protocol.strings[p]);
			                } else {
			                    sb.append("??");
			                }
			            }
			        }
			        sb.append('\n');
			        sb.append(" AuthAlgorithms:");
			        for (int a = 0; a < item.allowedAuthAlgorithms.size(); a++) {
			            if (item.allowedAuthAlgorithms.get(a)) {
			                sb.append(" ");
			                if (a < AuthAlgorithm.strings.length) {
			                    sb.append(AuthAlgorithm.strings[a]);
			                } else {
			                    sb.append("??");
			                }
			            }
			        }
			        sb.append('\n');
			        sb.append(" PairwiseCiphers:");
			        for (int pc = 0; pc < item.allowedPairwiseCiphers.size(); pc++) {
			            if (item.allowedPairwiseCiphers.get(pc)) {
			                sb.append(" ");
			                if (pc < PairwiseCipher.strings.length) {
			                    sb.append(PairwiseCipher.strings[pc]);
			                } else {
			                    sb.append("??");
			                }
			            }
			        }
			        sb.append('\n');
			        sb.append(" GroupCiphers:");
			        for (int gc = 0; gc < item.allowedGroupCiphers.size(); gc++) {
			            if (item.allowedGroupCiphers.get(gc)) {
			                sb.append(" ");
			                if (gc < GroupCipher.strings.length) {
			                    sb.append(GroupCipher.strings[gc]);
			                } else {
			                    sb.append("??");
			                }
			            }
			        }
					//sb.append("hiddenSSID = "+allowedAuthAlgorithms);
					//sb.append("hiddenSSID = "+item.hiddenSSID);
					new AlertDialog.Builder(MainActivity.this).setMessage(sb.toString()).create().show();
				}
				return false;
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
