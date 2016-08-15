package com.sdt.vpnproject.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.net.VpnService;

import com.sdt.vpnproject.net.Packet;
import com.sdt.vpnproject.utils.InitConfigureUtils;
import com.sdt.vpnproject.utils.LRUCache;
import com.sdt.vpnproject.utils.LogUtils;

public class UDPSendRunnable extends BaseRunnable{
	private static String TAG="UDPSendOutRunnable";
	private VpnService service;
	private Selector selector;
	private static int TIMEOUT=3000;
	private static int MAX_SIZE=50;
	public LRUCache<String, DatagramChannel> channelCache = new LRUCache<String, DatagramChannel>(MAX_SIZE, 
			new LRUCache.CleanUpCallBack<String, DatagramChannel>() {

				@Override
				public void clean(Entry<String, DatagramChannel> entry) {
					// TODO Auto-generated method stub
					if(entry!=null) InitConfigureUtils.closeResources(entry.getValue());
				}
		
	});
	public UDPSendRunnable(VpnService vpnservice,Selector selector,ConcurrentLinkedQueue<Packet> devToNet) {
		super(devToNet,null);
		// TODO Auto-generated constructor stub
		this.service = vpnservice;
		this.selector = selector;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!Thread.interrupted())
        {
			ConcurrentLinkedQueue<Packet> queue =getSendQueue();
			if(queue.isEmpty()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			Packet packet =queue.poll();
			if(packet !=null) sendUdp(packet);
        }
	}
	private void sendUdp (Packet packet) {
		 
		 try {
			 InetAddress host = InetAddress.getByAddress(packet.gethosts());
			 String ipaddress = host.getHostAddress()+":" +VPN_UDP_PORT;
			 LogUtils.i(TAG, " to host "+host.getHostAddress() +" inputlen="+packet.getData().length);
			 DatagramChannel datagramChannel =channelCache.get(ipaddress);
			 try {
				 if(datagramChannel ==null) {
					 datagramChannel = DatagramChannel.open();
				     datagramChannel.configureBlocking(false);
				     datagramChannel.socket().setSoTimeout(TIMEOUT);
				     datagramChannel.connect(new InetSocketAddress(host, VPN_UDP_PORT));
				     selector.wakeup();
			         datagramChannel.register(selector, SelectionKey.OP_READ,packet.getByteBuffer());
				     service.protect(datagramChannel.socket());
				     channelCache.put(ipaddress, datagramChannel);
				 }
		         ByteBuffer sendToNetwork =packet.getByteBuffer();
		         if(sendToNetwork!=null) {
		        	 InitConfigureUtils.printBytes(packet.getData());
		        	 sendToNetwork.flip();
		        	 //while(sendToNetwork.hasRemaining()) {
		        	 datagramChannel.write(ByteBuffer.wrap(packet.getData()));
		        		// LogUtils.i("Send....");
		        	// }
		        	 LogUtils.i("Send OK");
		         }
			 }catch(IOException e) {
				 LogUtils.e("ERROR Send UDP MSG IP:" +ipaddress);
				 InitConfigureUtils.closeResources(datagramChannel);
				 channelCache.remove(ipaddress);
			 }
			 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			 LogUtils.e("UnknowHost");
		}
	}

}
