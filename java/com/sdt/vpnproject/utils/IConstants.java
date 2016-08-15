package com.sdt.vpnproject.utils;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.sdt.vpnproject.net.Packet;

public interface IConstants {
	public static final int REQUEST_VPN_CODE=0xDD;
	
	public static final int VPN_UDP_PORT=4500;
	
	public static final String VPN_TUN_IP_ADDRESS="1.202.156.188";
	public static final String VPN_TUN_ROUTES="0.0.0.0";
	public static final String VPN_TUN_ROUTES1="128.0.0.0";
	
	
	public static final int IP_HEADER_LEN=20;
	
	public static ConcurrentLinkedQueue<Packet> deviceToNetworkQueue =new ConcurrentLinkedQueue<Packet>();
	public static ConcurrentLinkedQueue<Packet> networkTodeviceQueue =new ConcurrentLinkedQueue<Packet>();

}
