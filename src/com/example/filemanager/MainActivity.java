package com.example.filemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	 private List<String> items = null;
	    
	    @Override
	    public void onCreate(Bundle icicle) {
	        super.onCreate(icicle);
	        setContentView(R.layout.activity_main);
	        getFiles(new File("/").listFiles());
	    }
	    @Override
	    protected void onListItemClick(ListView l, View v, int position, long id){
	        int selectedRow = (int)id;
	        if(selectedRow == 0){
	            getFiles(new File("/").listFiles());
	        }else{
	            File file = new File(items.get(selectedRow));
	            if(file.isDirectory()){
	                getFiles(file.listFiles());
	            }else{
	            	 Intent intent = new Intent();
 	                 intent.setAction(android.content.Intent.ACTION_VIEW);

  	                 MimeTypeMap mime = MimeTypeMap.getSingleton();
	                 String ext=file.getName().substring(file.getName().lastIndexOf(".")+1);
	                 String type = mime.getMimeTypeFromExtension(ext);
 
	                 intent.setDataAndType(Uri.fromFile(file),type);
	                 startActivity(intent);
	            }
	        }
	    }
	    private void getFiles(File[] files){
	        items = new ArrayList<String>();
	        items.add("root");
	        for(File file : files){
	            items.add(file.getPath());
	        }
	        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,R.layout.list_row, items);
	        setListAdapter(fileList);
	    }

}
