package com.app.liaotianr.xmpp;

import java.io.File;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;


public class XMPPFileManager {
	
	static XMPPFileManager mFileManager ;
	static MyFileTransferListener listener ;
	static XMPPConnection mConnection;
	static FileTransferManager mTransferManager;
	static Context mContext ;
	
	private XMPPFileManager(XMPPConnection connection, Context context, String rootFolder, String fileFolder){
		mConnection = connection;
		mContext = context;
		listener = new MyFileTransferListener(context, rootFolder, fileFolder);
		mTransferManager = new FileTransferManager(mConnection);
		mTransferManager.addFileTransferListener(listener);
	}
	
	public static XMPPFileManager getInstance(XMPPConnection connection, Context context, String rootFolder, String fileFolder){
		if(mFileManager == null){
			mFileManager = new XMPPFileManager(connection, context, rootFolder, fileFolder);
		}
		return mFileManager;
	}
	
	public static void sendFile(String path, String to){
		path = Environment.getExternalStorageDirectory().toString()+"/XinXin/Received Files/the web application hackers handbook -finding and exploiting security flaws (2011) -mantesh.pdf";
		mFileManager.new AsynSendFile().execute(path, to);
	}
	
	public void dispose(){
		
		if(mConnection !=null){
			mTransferManager.removeFileTransferListener(listener);
		}
		
		mConnection = null;
		listener = null;
		mTransferManager = null;
		mFileManager = null;
	}
	
	public class AsynSendFile extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			Intent intent;
			boolean isErrorOccured = false;
			//"adnan@sol/Spark 2.6.3"
			OutgoingFileTransfer transfer = mTransferManager.createOutgoingFileTransfer(arg0[1]);
			File file = new File(arg0[0]);
			if(!file.exists()){
				// show error
				intent = new Intent();
		        intent.setAction(XMPPIntentFilter.ACTION_XMPP_FILE_SEND_REQUEST);
		        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		        intent.putExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_SEND, XMPPConstants.FILE_NOT_EXIST);
		        mContext.sendBroadcast(intent);
			}
			try {
			   transfer.sendFile(file, "test_file");
			} catch (XMPPException e) {
			   e.printStackTrace();
			}
			while(!transfer.isDone()) {
			   if(transfer.getStatus().equals(org.jivesoftware.smackx.filetransfer.FileTransfer.Status.error)) {
			      System.out.println("ERROR!!! " + transfer.getError());
			      isErrorOccured = true;
			   } 
			   else if (transfer.getStatus().equals(org.jivesoftware.smackx.filetransfer.FileTransfer.Status.cancelled)
			                    || transfer.getStatus().equals(org.jivesoftware.smackx.filetransfer.FileTransfer.Status.refused)) {
			      System.out.println("Cancelled!!! " + transfer.getError());
			      isErrorOccured = true;
			   }
			   try {
			      Thread.sleep(1000L);
			   } catch (InterruptedException e) {
			      e.printStackTrace();
			      isErrorOccured = true;
			   }
			}
			if(transfer.getStatus().equals(org.jivesoftware.smackx.filetransfer.FileTransfer.Status.refused) || transfer.getStatus().equals(org.jivesoftware.smackx.filetransfer.FileTransfer.Status.error)
			 || transfer.getStatus().equals(org.jivesoftware.smackx.filetransfer.FileTransfer.Status.cancelled)){
			   System.out.println("refused cancelled error " + transfer.getError());
			   isErrorOccured = true;
			} else {
			   System.out.println("Success");
			   isErrorOccured = false;
			}
			
			intent = new Intent();
	        intent.setAction(XMPPIntentFilter.ACTION_XMPP_FILE_SEND_REQUEST);
	        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
			
	        if(isErrorOccured){
		        intent.putExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_SEND, XMPPConstants.FILE_SENDING_ERROR);
			}
	        else{
	        	intent.putExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_SEND, XMPPConstants.FILE_SUCCESSFULLY_SENT);
	        }
	        mContext.sendBroadcast(intent);
			
			return null;
		}
	}
	
}


