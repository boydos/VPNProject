package com.sdt.vpnproject.net;

import java.nio.ByteBuffer;

public class Packet {
	private static int IP_HEADER_LEN =20;
	private ByteBuffer bufData;
	private byte[] data;
	private int dataLen=0;
	
	public Packet(ByteBuffer buffer,int readLen) {
		this.bufData = buffer;
		this.dataLen =readLen;
		mkIPData();
	}
	public byte [] getData() {
		return data;
	}
	public ByteBuffer getByteBuffer() {
		return bufData;
	}
	private void mkIPData() {
		if(bufData ==null) {
			data =null;
			return;
		}
		if(dataLen <=0) return;
		byte [] temp = bufData.array();
		int start=0;
		for(byte b : temp) {
			if((b&0xff)==0x45) {
				break;
			}
			start++;
		}
		data = getbytes(bufData.array(),start, dataLen);
	}
	public byte [] gethosts() {
		 if(data==null || data.length<IP_HEADER_LEN) return null;
		 byte[] addressBytes = new byte[4];
		 int index=IP_HEADER_LEN-4;
		 for(int i=index;i<4+index;i++) {
			 addressBytes[i-index]=data[i];
		 }
		 return addressBytes;
	}
	
	public byte[] getbytes(byte[] src,int start,int len) {
		if(src == null) return null;
		if(start>=src.length) return null;
		byte[] result = new byte[len];
		for(int i=start;i<start+len&&i<src.length;i++) {
			result[i-start] =src[i];
		}
		return result;
	}
}
