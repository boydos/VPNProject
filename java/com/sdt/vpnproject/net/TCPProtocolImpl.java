package com.sdt.vpnproject.net;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;


public class TCPProtocolImpl implements TCPProtocol{
	
	private int bufferSize;
	
	public TCPProtocolImpl( int bufferSize) {
		this.bufferSize = bufferSize;
	}

	@Override
	public void handleAccept(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		SocketChannel client =((ServerSocketChannel) key.channel()).accept();
		client.configureBlocking(false);
		client.register(key.selector(), SelectionKey.OP_READ,ByteBuffer.allocate(bufferSize));
	}

	@Override
	public void handleRead(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		SocketChannel client =(SocketChannel) key.channel();
		ByteBuffer buffer =(ByteBuffer) key.attachment();
		buffer.clear();
		
		long byteRead = client.read(buffer);
		if(byteRead>0) {
			buffer.flip();
			
			
			
			client.write(buffer);
			key.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
		}
	}

	@Override
	public void handleWrite(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
