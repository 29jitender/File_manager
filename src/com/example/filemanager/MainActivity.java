package com.example.filemanager;      

 
import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity  
        extends ListActivity     
        implements AdapterView.OnItemClickListener
 {
    ListView lv;         
    String dirPath;      
    File[] fileList; //array of items
    
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);           
         setContentView(R.layout.activity_main);


        lv = getListView();     
        dirPath = "/";     //initial path
        try {                   
            dirPath = getIntent().getExtras().getString("path");//get the path from intent
        } catch(NullPointerException e) {}

         FileAdapter A = null;
         
         
		try { //this try catch block is to check the exception if we dont have root permission
			A = new FileAdapter(this, 1, new ArrayList<FileObject>());
			   fileList = (new File(dirPath)).listFiles();
			 for (int i = 0; i < fileList.length; i++) //itrating over the length of file list
			    A.add(new FileObject(fileList[i])); //adding item to fileobjec
			 A.sort();
		} catch (Exception e) {
			 new AlertDialog.Builder(this)//opening a dialog box wiht msg
             .setTitle("Need root permission to open this")
             .setNeutralButton("OK", new DialogInterface.OnClickListener(){
                 public void onClick(DialogInterface dialog, int button){
                     onBackPressed();// on press ok come back to home

                 	}
             })
             .show();			e.printStackTrace();
		}

         lv.setAdapter(A);
         lv.setOnItemClickListener(this);

         setTitle(dirPath);//setting title 
    }


     @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
         String path = dirPath + "/" + ((TextView)view.findViewById(R.id.fileName)).getText().toString();
 
            File file1 = new File(path);

         if(file1.isDirectory()) //if its a directry
        {
            
            Intent next = new Intent(MainActivity.this, MainActivity.class);
            
            next.putExtra("path", path);//putting path
            startActivity(next); //starting it again with new path and show that again 
             overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);// random stuff for transition :P
        }
         else
        { // otherwise open the file using defult 
            Intent intent = new Intent();
            //using a builtin Intent.

             intent.setAction(android.content.Intent.ACTION_VIEW);

             File file = new File(path); // defining file
            MimeTypeMap mime = MimeTypeMap.getSingleton();// this is used to open files
            String ext=file.getName().substring(file.getName().lastIndexOf(".")+1); //getting the extension after .
            String type = mime.getMimeTypeFromExtension(ext);  //

             intent.setDataAndType(Uri.fromFile(file),type);//when we have mime will put this in intent to open the file
            startActivity(intent);
        }
    }

     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event)
     {
         if (keyCode == KeyEvent.KEYCODE_BACK)
         {
             finish();    
             if(!dirPath.equals("/"))  //if its home / then no animation
                 overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
         }
         return super.onKeyDown(keyCode, event);
     }
 
 
}
