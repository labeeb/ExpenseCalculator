package com.libu.expensecalulator.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.net.Uri;
import android.os.Environment;

public class Logger {
	private static final boolean CUSTOM_LOG_ONLY = false;
	private static final boolean DEFAULT_LOG_ONLY = true;
	
	private static final String TAG = "Custom Log";
	private static final int LOG_MAX_LENGHT=5000000;//in bytes
	
	public static final String LOG_DIR = Environment.getExternalStorageDirectory() +"/log" ;//"/Android/data/com.zenprise/file/log/";
	public static final String LOG_FILE_NAME = "log";
	public static final String LOG_FILE_EXTENTION = ".html";
	
	private String className;
	 
	public Logger(String className) {
		this.className = className;
	}
	
	public final void e(String message ){
		 e( null, message );
	}
	
	public final void e(String filter,String message ){
		if(filter != null){
			message = filter+":"+message;
		}
		if(DEFAULT_LOG_ONLY){
			android.util.Log.e(className,filter+message);
		}else if(CUSTOM_LOG_ONLY){
			StringBuffer buffer = new StringBuffer(new Date().toLocaleString());
			buffer.append(className).append(":").append(message);
			WriteToFile(LogType.ERROR, filter,message);
		}else{
			android.util.Log.e(className,message);
			StringBuffer buffer = new StringBuffer(new Date().toLocaleString());
			buffer.append(className).append(":").append(message);
			WriteToFile(LogType.ERROR, filter,message);
		}
	}
	
	public void d(String message) {
		d(null, message);
	}
	
	public final void d(String filter,String message ){
		if(filter != null){
			message = filter+":"+message;
		}
		if(DEFAULT_LOG_ONLY){
			android.util.Log.d(className,message);
		}else if(CUSTOM_LOG_ONLY){
			StringBuffer buffer = new StringBuffer(new Date().toLocaleString());
			buffer.append(className).append(":").append(message);
			WriteToFile(LogType.DEBUG,className, message);
		}else{
			android.util.Log.d(className,message);
			StringBuffer buffer = new StringBuffer(new Date().toLocaleString());
			buffer.append(className).append(":").append(message);
			WriteToFile(LogType.DEBUG,className, message);
		}
	}
	
	public void w(String message){
		w(null, message);
	}
	
	public final void w(String filter,String message ){
		if(filter != null){
			message = filter+":"+message;
		}
		if(DEFAULT_LOG_ONLY){
			android.util.Log.w(className,message);
		}else if(CUSTOM_LOG_ONLY){
			StringBuffer buffer = new StringBuffer(new Date().toLocaleString());
			buffer.append(className).append(":").append(message);
			WriteToFile(LogType.WARN,className, message);
		}else{
			android.util.Log.w(className,message);
			StringBuffer buffer = new StringBuffer(new Date().toLocaleString());
			buffer.append(className).append(":").append(message);
			WriteToFile(LogType.WARN,className, message);
		}		
	}
	
	public void i(String message){
		i(null, message);
	}
	
	public final void i(String filter,String message ){
		if(filter != null){
			message = filter+":"+message;
		}
		if(DEFAULT_LOG_ONLY){
			android.util.Log.i(className,message);
		}else if(CUSTOM_LOG_ONLY){
			StringBuffer buffer = new StringBuffer(new Date().toLocaleString());
			buffer.append(className).append(":").append(message);
			WriteToFile(LogType.INFO,className, message);
		}else{
			android.util.Log.i(className,message);
			
			StringBuffer buffer = new StringBuffer(new Date().toLocaleString());
			buffer.append(className).append(":").append(message);
			WriteToFile(LogType.INFO,className, message);
		}		
	}

	/*public void v(String tag2, String string) {
		android.util.Log.v(tag2, string);
	}*/

	public void e(String string, String string2, Exception e) {
		android.util.Log.e(string, string2, e);
		/*if(CUSTOMLOG){
			WriteToFile(LogType.ERROR, string2);
		}else{
			android.util.Log.i(message, tag);
		}*/
		
	}
	
	
	
	//to save in file 
	
	
	private enum LogType{
		ERROR("red"),
		DEBUG("blue"),
		INFO("green"),
		WARN("yellow");		
		
		private final String value;

        private LogType(final String newValue) {
            value = newValue;
        }

        //public String getValue() { return value; }
	};
	 
	
	public static List<String> getFilePath(){
		File file = new File(LOG_DIR);
		  if (file.isDirectory()) {
		    String names[] = file.list();
		    if(names.length > 0){
		    	List<String> fileList = new ArrayList<String>(names.length);
		    	for(String name:names){
		    		fileList.add(LOG_DIR+"/"+name);
		    	}
		    	return fileList;
		    }
		  } 
		  return null;
		/*ArrayList<Uri> uris = new ArrayList<Uri>();
		File file = new File(filePath);
		if (file.exists()) {
			Uri u = Uri.fromFile(file);
			uris.add(u);
		}
		
		return LOG_DIR+LOG_FILE_NAME+LOG_FILE_EXTENTION;*/
	}
	
	public static ArrayList<Uri> getLogFile(){
		ArrayList<Uri> uris = new ArrayList<Uri>();
		File extrnal = new File(LOG_DIR);
		File logFile = new File(extrnal,LOG_FILE_NAME+LOG_FILE_EXTENTION);
		if (logFile.exists()) {
			Uri u = Uri.fromFile(logFile);
			uris.add(u);
		}
		return uris;		
	}
	
	public static final boolean WriteToFile(LogType color,String tag,String message){
		PrintWriter pw = null;
		try {
			File extrnal = new File(LOG_DIR);
			if(extrnal.mkdir()||extrnal.mkdirs()){
				android.util.Log.d(TAG, "dir created ");
			}else{
				android.util.Log.d(TAG, "dir not created");
			}
			File logFile = new File(extrnal,LOG_FILE_NAME+LOG_FILE_EXTENTION);
			if (!logFile.exists()) {
				logFile.createNewFile();				
				android.util.Log.e(TAG, "new file created");
				pw = new PrintWriter(new FileWriter(logFile));
			}else{						
				android.util.Log.e(TAG, "old file with size:"+logFile.length());
				if(logFile.length() > LOG_MAX_LENGHT){
					File newLogFile = new File(extrnal,LOG_FILE_NAME+"_old"+LOG_FILE_EXTENTION);
					if (newLogFile.exists()) {
						newLogFile.delete();				
						android.util.Log.e(TAG, "newLogFile file deleted ");
					}
					android.util.Log.e(TAG, "since old file size is more new file created ");
					//FileUtils.moveFile(logFile, newLogFile);
					pw = new PrintWriter(new FileWriter(logFile, false));
				}else{
					pw = new PrintWriter(new FileWriter(logFile, true));
				}
				
			}
			
			StringBuffer buffer = new StringBuffer("<table border=\"1\" width=\"100%\"><td width=\"20%\">");
			buffer.append(new Date().toLocaleString()).append("</td ><td width=\"20%\">");
			buffer.append("\n<span style=\"color:").append(color.value).append("\">").append(tag).append("</span></td>");
			buffer.append("<td >").append(message).append("</td >");
			buffer.append("</table>");
			//buffer.append("\n<span style=\"color:").append(color.value).append("\">").append(message).append("</span><br>");

			pw.println(buffer.toString());
			android.util.Log.d(TAG, "added to file ");
			pw.flush();
			return true;
			
		} catch (IOException e) {
			android.util.Log.e(TAG, message);
			e.printStackTrace();
		} finally {
			// Close the PrintWriter
			if (pw != null)
				pw.close();

		}
		return false;
	}
	
	
}
