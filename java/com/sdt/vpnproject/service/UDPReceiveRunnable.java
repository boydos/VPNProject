package com.sdt.vpnproject.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sdt.vpnproject.net.Packet;
import com.sdt.vpnproject.utils.ByteBufferPool;
import com.sdt.vpnproject.utils.InitConfigureUtils;
import com.sdt.vpnproject.utils.LogUtils;

public class UDPReceiveRunnable extends BaseRunnable{
	private Selector selector;
	public UDPReceiveRunnable(Selector selector,ConcurrentLinkedQueue<Packet> netToDev) {
		super(null, netToDev);
		// TODO Auto-generated constructor stub
		this.selector = selector;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!Thread.interrupted()) {
			try {
				int ready = selector.select();
				if (ready == 0) {
	                Thread.sleep(10);
	                continue;
	            }
				
				Iterator<SelectionKey>  keys =selector.selectedKeys().iterator();
				while(keys.hasNext()) {
					SelectionKey key = keys.next();
				    if (key.isValid() && key.isReadable()) {
				    	DatagramChannel channel = (DatagramChannel)key.channel();
				    	ByteBuffer receiveBuffer = ByteBufferPool.acquire();
				    	int readBytes = channel.read(receiveBuffer);
				    	if(readBytes>0) {
					    	LogUtils.i("Receive byte =" +readBytes);
					    	Packet packet = new Packet(receiveBuffer, readBytes);
					    	InitConfigureUtils.printBytes(packet.getData());
					    	getRecvQueue().offer(packet);
				    	}
				    	ByteBufferPool.release(receiveBuffer);
				    }
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
