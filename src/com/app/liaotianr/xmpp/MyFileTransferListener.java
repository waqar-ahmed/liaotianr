package com.app.liaotianr.xmpp;

import java.io.File;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.app.liaotianr.utilities.Utility;



public class MyFileTransferListener implements FileTransferListener {

	private static final String TAG = "MyFileTransferListener";
    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";
    
	private String sdCardPath;
	private File folder;
    private static Context mContext;
    private static String mRootFolder;
    private static String mFileFolder;
    private static String mPath;
	File saveTo;
	static boolean isErrorOccured = false;
    

	
    public MyFileTransferListener(Context context, String rootFolder, String fileFolder){
    	mContext = context;
    	mRootFolder = rootFolder;
    	mFileFolder = fileFolder;
		sdCardPath = Environment.getExternalStorageDirectory().toString();
    	mPath = sdCardPath + mRootFolder + mFileFolder;
    }

	private void createIfNotExistReceivedFileFolder() {
	    folder = new File(sdCardPath + mRootFolder);
	    createFolder(folder);
	    folder = new File(sdCardPath + mRootFolder + mFileFolder);
	    createFolder(folder);
	}

	private void createFolder(File file) {
		if(!file.exists() && ! file.isDirectory())
	    {
	    	Log.d("tag","directory created..path is " + file.getAbsolutePath());
	    	file.mkdir();
	    }
	    else
	    {
	    	Log.d("tag","directory exsit .. path is " + file.getAbsolutePath());
	    }
	}

	@Override
	public void fileTransferRequest(final FileTransferRequest request) {
		// TODO Auto-generated method stub
		
		saveTo = new File(mPath + "/" +request.getFileName());
		
//		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            Utility.Log(TAG, "SD CARD not mounted");
//			mRequest.reject();
//            return;
//        }
		
	    if (saveTo.exists()) {
             //Utility.Log(TAG, "File Already exists");
             request.reject();
             
            Intent intent = new Intent();
 	        intent.setAction(XMPPIntentFilter.ACTION_XMPP_FILE_DOWNLOAD_PROGRESS);
 	        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
 	        intent.putExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_PROGRESS, 0.0);
 	        intent.putExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_DOWNLOAD_STATUS, XMPPConstants.FILE_ALREADY_EXIST);
 	        mContext.sendBroadcast(intent);
             
             return ;
        }
		
		new AsynFileDownload(request).execute();
		
	}

	private static String guessMimeType(File f) {       
        String filename = f.getName();
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) return DEFAULT_MIME_TYPE;
        
        String extension = filename.substring(lastDot);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (mimeType == null) {
            return DEFAULT_MIME_TYPE;
        } else {
            return mimeType;
        }
    }
	
	public class AsynFileDownload extends AsyncTask<Void, Double, String>{

		FileTransferRequest mRequest;
		IncomingFileTransfer mTransfer;
		String mReceviedFrom ;
		double percent;
		String currentStatus = "";
		Intent intent;
		
		public AsynFileDownload(FileTransferRequest request){
			
			mRequest = request;
	    	createIfNotExistReceivedFileFolder();
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			mReceviedFrom = mRequest.getRequestor();
			
			mTransfer = mRequest.accept();

			//Utility.Log(TAG, "File size : "+ mRequest.getFileSize());
			
            //Utility.Log(TAG, "File transfer ready ... : " + saveTo.getName()+ " --> "+ mRequest.getFileSize() / 1024 + " KiB");
            try {
				mTransfer.recieveFile(saveTo);
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            //Utility.Log(TAG, "File transfer status : "+ mTransfer.getStatus());
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try {
				while(!mTransfer.isDone()) {
		              try{
		                 Thread.sleep(1000L);
		              }catch (Exception e) {
		                 Log.e("", e.getMessage());
		              }
		              //Utility.Log(TAG, "Trasnfer status : "+mTransfer.getStatus().toString());
		              currentStatus = mTransfer.getStatus().toString();
		              
		              if (mTransfer.getStatus() == org.jivesoftware.smackx.filetransfer.FileTransfer.Status.in_progress) {
		                percent = ((int) (mTransfer.getProgress() * 10000)) / 100.0;
		                publishProgress(percent);
		        	}
		              
		              if(mTransfer.getStatus().equals(org.jivesoftware.smackx.filetransfer.FileTransfer.Status.error)) {
		                 //Utility.Log("ERROR!!! ", mTransfer.getError() + "");
		            	  isErrorOccured = true;
		            	  return null;
		              }
		              
		              if(mTransfer.getStatus().equals(org.jivesoftware.smackx.filetransfer.FileTransfer.Status.cancelled)) {
			                 //Utility.Log(TAG, "user cancelled...");
			                 currentStatus = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.cancelled.toString();
			                 isErrorOccured = true;
			                 return null;
			              }
			              
		              
		              if(mTransfer.getException() != null) {
		                 mTransfer.getException().printStackTrace();
		              }
		           }
				
		            //Utility.Log(TAG, "Trasnfer status : "+mTransfer.getStatus().toString());
		            
		            if (mTransfer.getStatus().equals(org.jivesoftware.smackx.filetransfer.FileTransfer.Status.complete)) {
			            	//Utility.Log(TAG, "File transfer complete "+saveTo.getAbsolutePath());
			               // downloadManager only works from API 12 or higher
			               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			                   DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
			                   dm.addCompletedDownload(saveTo.getName(), "Received by " + "LIAOTIANR" + " from " + mReceviedFrom, false, guessMimeType(saveTo), mPath, saveTo.length(), true);
			               }
		            } 
		            else {
		            	Utility.Log(TAG, "some thing eroor..");
		            	currentStatus = XMPPConstants.FILE_DOWNLOAD_ERROR;
		            	isErrorOccured = true;
		            }
			}
			catch(Exception ex){
				ex.printStackTrace();
				isErrorOccured = true;
				return null;
			}
			
			//Utility.Log(TAG, "download file size is "+ saveTo.length());
			
			return saveTo.getAbsolutePath();
		}
		
		@Override
		protected void onProgressUpdate(Double... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			//Utility.Log(TAG, "Percent : "+percent +"%");
			
			intent = new Intent();
	        intent.setAction(XMPPIntentFilter.ACTION_XMPP_FILE_DOWNLOAD_PROGRESS);
	        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
	        intent.putExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_PROGRESS, percent);
	        intent.putExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_DOWNLOAD_STATUS, currentStatus);
	        mContext.sendBroadcast(intent);
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			intent = new Intent();
	        intent.setAction(XMPPIntentFilter.ACTION_XMPP_FILE_DOWNLOAD_PROGRESS);
	        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
	        intent.putExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_DOWNLOAD_STATUS, currentStatus);
	        
			if(currentStatus == org.jivesoftware.smackx.filetransfer.FileTransfer.Status.complete.toString()){
		        intent.putExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_PROGRESS, 100.0);
		        isErrorOccured = false;
		        
		        if(mRequest.getFileSize() != saveTo.length()){
		        	intent.putExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_PROGRESS, 00.0);
			        intent.putExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_DOWNLOAD_STATUS, XMPPConstants.FILE_DOWNLOAD_ERROR);
			        saveTo.delete();
			        isErrorOccured = true;
				}
			}
			else{
				intent.putExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_PROGRESS, 00.0);
				isErrorOccured = true;
			}

	        mContext.sendBroadcast(intent);
	        
	        //if not any error occured during downloading file, then broadcast file download completion broadcast
	        if(!isErrorOccured){
	        	intent = new Intent();
		        intent.setAction(XMPPIntentFilter.ACTION_XMPP_FILE_DOWNLOAD_COMPLETE);
		        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		        intent.putExtra(XMPPConstants.IntentKeys.XmppFileTransferKey.XMPP_FILE_DOWNLOAD_COMPLETE, result);
		        mContext.sendBroadcast(intent);
	        }
		}
	}
}
