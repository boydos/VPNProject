package com.sdt.vpnproject.service;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sdt.vpnproject.net.Packet;
import com.sdt.vpnproject.utils.ByteBufferPool;
import com.sdt.vpnproject.utils.InitConfigureUtils;
import com.sdt.vpnproject.utils.LogUtils;


public class VPNRunnable extends BaseRunnable {
	private FileDescriptor vpnFileDescriptor;
	public VPNRunnable(FileDescriptor vpnfd,ConcurrentLinkedQueue<Packet> deviceToNetwork,ConcurrentLinkedQueue<Packet> networkTodevice) {
		super(deviceToNetwork, networkTodevice);
		this.vpnFileDescriptor =vpnfd;
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		 FileChannel vpnInput = new FileInputStream(vpnFileDescriptor).getChannel();
		 FileChannel vpnOutput = new FileOutputStream(vpnFileDescriptor).getChannel();
		try{
			ByteBuffer bufferToNetwork = null;
            boolean dataSent = true;
            boolean dataRec = true;
			while (!Thread.interrupted())
            {
				 if (dataSent)
                     bufferToNetwork = ByteBufferPool.acquire();
				 else
                     bufferToNetwork.clear();
				 int len = vpnInput.read(bufferToNetwork);
				 if(len>0) {
					 Packet packet = new Packet(bufferToNetwork, len);
					 getSendQueue().offer(packet);
					 ByteBufferPool.release(bufferToNetwork);
					 dataSent =true;
				 } else {
					 dataSent=false;
				 }
				 
				 Packet packet = getRecvQueue().poll();
				 if(packet!=null) {
					 dataRec =true;
					 ByteBuffer receiveByteBuffer = packet.getByteBuffer();
					 if(receiveByteBuffer!=null) {
						 receiveByteBuffer.flip();
						 vpnOutput.write(receiveByteBuffer);
						 LogUtils.i("Receive OK");
					 }
				 } else{
					 dataRec =false;
				 }
				 if(!dataSent&&!dataRec) Thread.sleep(100);
            }
		}catch(IOException e) {
			//e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} finally {
			InitConfigureUtils.closeResources(vpnInput,vpnOutput);
		}
	}
}