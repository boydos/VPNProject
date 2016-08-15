package com.sdt.vpnproject;

import java.nio.channels.Selector;

import com.sdt.vpnproject.service.SVPNService;
import com.sdt.vpnproject.utils.IConstants;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements IConstants{
	boolean vpnrunning=false;
    private Selector selector;
	Button btnStart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnStart = (Button)findViewById(R.id.start);
		btnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!vpnrunning){
					startVPN();
				} else {
					stopVPN();
				}
			}
		});
	}
	
	private void startVPN() {
		Intent vpnIntent = VpnService.prepare(this);
		if(vpnIntent!=null) {
			startActivityForResult(vpnIntent, REQUEST_VPN_CODE);
			setVPNRunning(true);
		} else
			onActivityResult(REQUEST_VPN_CODE, RESULT_OK, null);
	}
	
	private void stopVPN() {
		stopService(new Intent(this,SVPNService.class));
		setVPNRunning(false);
	}
		
	@Override
	protected void onActivityResult(int code, int result, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(code, result, data);
		
		if(code ==REQUEST_VPN_CODE&& result==RESULT_OK) {
			startService(new Intent(this, SVPNService.class));
		}
	}
	private void setVPNRunning(boolean running) {
		vpnrunning =running;
		btnStart.setText(running?R.string.stop:R.string.start);
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setVPNRunning(vpnrunning);
	}
	
	
}
