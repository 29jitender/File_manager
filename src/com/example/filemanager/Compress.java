package com.example.filemanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compress { 
	  private static final int BUFFER = 2048; 
	 
	  //private String[] _files; 
	      ArrayList<String> _files = new ArrayList<String>();

	  
	  private String _zipFile; 
	 
	  public Compress(ArrayList<String> files, String zipFile) { 
	    _files = files; 
	    _zipFile = zipFile; 
	  } 
	 
	  public Boolean zip() { 
	    try  { 
	      BufferedInputStream origin = null; 
	      FileOutputStream dest = new FileOutputStream(_zipFile); 
	 
	      ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest)); 
	 
	      byte data[] = new byte[BUFFER]; 
	 
	      for(int i=0; i < _files.size(); i++) { 
		    	File fileOrDirectory = new File(_files.get(i));
			    if (fileOrDirectory.isDirectory()){
			    	
			        for (File child : fileOrDirectory.listFiles()){
			 	        FileInputStream fi = new FileInputStream(child.getPath());
				        origin = new BufferedInputStream(fi, BUFFER); 
				        ZipEntry entry = new ZipEntry(fileOrDirectory.getName()+"/"+child.getPath().substring(child.getPath().lastIndexOf("/") + 1)); 
				        out.putNextEntry(entry); 
				        int count; 
				        while ((count = origin.read(data, 0, BUFFER)) != -1) { 
				          out.write(data, 0, count); 
				        } 
				        origin.close();

			        }

			    }
			    
			    else{
 	        FileInputStream fi = new FileInputStream(_files.get(i)); 
	        origin = new BufferedInputStream(fi, BUFFER); 
	        ZipEntry entry = new ZipEntry(_files.get(i).substring(_files.get(i).lastIndexOf("/") + 1)); 
	        out.putNextEntry(entry); 
	        int count; 
	        while ((count = origin.read(data, 0, BUFFER)) != -1) { 
	          out.write(data, 0, count); 
	        } 
	        origin.close(); 
	      } }
	 
	      out.close(); 
	    } catch(Exception e) { 
	      e.printStackTrace(); 
	    }
		return true; 
	 
	  } 
	 
	} 