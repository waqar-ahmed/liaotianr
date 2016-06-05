package com.app.liaotianr.xmpp;


public class ConnectionStatus {
	
	public static final int DISCONNECTED = 1;
    public static final int CONNECTING = 2;
    public static final int CONNECTED = 3;
    public static final int DISCONNECTING = 4;
    public static boolean NETWORK_AVAILABLE = true;
    public static int CURRENT_STATUS = DISCONNECTED;
}