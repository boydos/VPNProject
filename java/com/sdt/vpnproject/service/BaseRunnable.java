package com.sdt.vpnproject.service;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.sdt.vpnproject.net.Packet;
import com.sdt.vpnproject.utils.IConstants;

public abstract class BaseRunnable implements Runnable,IConstants{
	private ConcurrentLinkedQueue<Packet> deviceToNetwork =new ConcurrentLinkedQueue<Packet>();
	private ConcurrentLinkedQueue<Packet> networkToDevice =new ConcurrentLinkedQueue<Packet>();
	public BaseRunnable(ConcurrentLinkedQueue<Packet> devToNet,ConcurrentLinkedQueue<Packet> netToDev) {
		this.deviceToNetwork = devToNet;
		this.networkToDevice = netToDev;
	}
	public ConcurrentLinkedQueue<Packet> getSendQueue() {
		return this.deviceToNetwork;
	}
	public ConcurrentLinkedQueue<Packet> getRecvQueue() {
		return this.networkToDevice;
	}
	
	public boolean isSendQueueEmpty() {
		return isQueueEmpty(this.deviceToNetwork);
	}
	public boolean isRecvQueueEmpty() {
		return isQueueEmpty(this.networkToDevice);
	}
	private boolean isQueueEmpty(ConcurrentLinkedQueue<Packet> queue) {
		return queue==null|| queue.isEmpty();
	}
}
