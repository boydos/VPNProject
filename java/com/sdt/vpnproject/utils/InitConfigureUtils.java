package com.sdt.vpnproject.utils;

import java.io.Closeable;
import java.io.IOException;

import android.util.Log;

public class InitConfigureUtils {
	private static String TAG="InitConfigureUtils tds";
	public static void printBytes(byte[] inputs) {
		 String i="0x";
        for(byte b:inputs) {
        	i+=Integer.toHexString(b&0xff) +" ";
        }
        Log.i(TAG, "inputbuffer:"+i);
	}
	
	public static void closeResources(Closeable...closeables) {
		for(Closeable resource :closeables) {
			if(resource!=null) {
				try {
					resource.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
   }
}
