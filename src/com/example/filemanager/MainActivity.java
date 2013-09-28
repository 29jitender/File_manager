package com.example.filemanager;      

 
import java.io.File;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
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
        setTheme(android.R.style.Theme_Light);       

        lv = getListView();     
        dirPath = "/";     
        try {                   
            dirPath = getIntent().getExtras().getString("path");//get the path from intent
        } catch(NullPointerException e) {}

         FileAdapter A = new FileAdapter(this, 1, new ArrayList<FileObject>());
           fileList = (new File(dirPath)).listFiles();
         for (int i = 0; i < fileList.length; i++) //itrating over the length of file list
            A.add(new FileObject(fileList[i])); //adding item to fileobjec
         A.sort();

         lv.setAdapter(A);
         lv.setOnItemClickListener(this);

         setTitle(dirPath.substring(dirPath.lastIndexOf("/")));//setting title 
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
 
}
