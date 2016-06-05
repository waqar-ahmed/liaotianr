package com.app.liaotianr.xmpp;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import com.app.liaotianr.utilities.Utility;

public class MyPacketListener implements PacketListener{

	String TAG = "MyPacketListener";
	
	public MyPacketListener(){
	}
	
	@Override
	public void processPacket(Packet packet) {
		// TODO Auto-generated method stub
		Utility.Log(TAG, "Packet received from : "+packet.getFrom()+" --> "+ packet.getError());
		
	}

}
