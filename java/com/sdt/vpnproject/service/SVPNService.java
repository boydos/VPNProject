package com.sdt.vpnproject.service;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;

import com.sdt.vpnproject.R;
import com.sdt.vpnproject.net.Packet;
import com.sdt.vpnproject.utils.IConstants;
import com.sdt.vpnproject.utils.InitConfigureUtils;
import com.sdt.vpnproject.utils.LogUtils;
import com.sdt.vpnproject.utils.ThreadPool;

public class SVPNService extends VpnService implements IConstants {
	private static boolean isRunning =false;
	private static ParcelFileDescriptor vpnfd=null;
	private static Selector selector;
	private static ConcurrentLinkedQueue<Packet> deviceToNetworkQueue =new ConcurrentLinkedQueue<Packet>();
	private static ConcurrentLinkedQueue<Packet> networkTodeviceQueue =new ConcurrentLinkedQueue<Packet>();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		startVPN();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopVPN();
	}
	private void initThread() throws IOException {

		ThreadPool.runThread(new VPNRunnable(vpnfd.getFileDescriptor(),deviceToNetworkQueue,networkTodeviceQueue));
		ThreadPool.runThread(new UDPSendRunnable(this,selector,deviceToNetworkQueue));
		ThreadPool.runThread(new UDPReceiveRunnable(selector, networkTodeviceQueue));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}
	
	private void startVPN() {
		LogUtils.i("START VPN");
		isRunning = true;
		if(vpnfd ==null) {
			Builder builder = new Builder();
			builder.addAddress(VPN_TUN_IP_ADDRESS, 32);
			builder.addRoute(VPN_TUN_ROUTES, 1);
			builder.addRoute(VPN_TUN_ROUTES1, 1);
			vpnfd=builder.setSession(getString(R.string.vpnSession)).setConfigureIntent(null).establish();
		}
		try {
			selector =Selector.open();
			initThread();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cleanVPN();
		}
	}
	
	public  void stopVPN() {
		LogUtils.i("STOP VPN");
		isRunning = false;
		ThreadPool.shutDownNow();
		cleanVPN();
	}
	private  void cleanVPN() {
		InitConfigureUtils.closeResources(selector,vpnfd);
		vpnfd=null;
		selector=null;
	}
	
	
}
