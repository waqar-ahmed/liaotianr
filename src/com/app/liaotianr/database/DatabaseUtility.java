package com.app.liaotianr.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;

public class DatabaseUtility {

	 //Copy the database from assets 
    public static void copyDataBase(Context context, String databaseName, String path) throws IOException 
    { 
        InputStream mInput = context.getAssets().open("databases/" + databaseName); 
        String outFileName = path + databaseName; 
        OutputStream mOutput = new FileOutputStream(outFileName); 
        byte[] mBuffer = new byte[1024]; 
        int mLength; 
        while ((mLength = mInput.read(mBuffer))>0) 
        { 
            mOutput.write(mBuffer, 0, mLength); 
        } 
        mOutput.flush(); 
        mOutput.close(); 
        mInput.close(); 
    } 
    
    //Check that the database exists here: /data/data/your package/databases/Da Name 
    public static boolean checkDataBase(String databaseName, String path) 
    { 
        File dbFile = new File(path + databaseName); 
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists()); 
        return dbFile.exists(); 
    } 
	
}
