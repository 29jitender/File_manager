package com.example.filemanager;      

 
import java.io.File;
import java.util.ArrayList;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;

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
    ListView listview;         
    String dir_Path;      
    File[] file_list; //array of items
     public int getPreviousPageImageResource() { return R.drawable.left_arrow; }
    public int getNextPageImageResource() { return R.drawable.right_arrow; }
    public int getZoomInImageResource() { return R.drawable.zoom_in; }
    public int getZoomOutImageResource() { return R.drawable.zoom_out; }
    public int getPdfPasswordLayoutResource() { return R.layout.pdf_file_password; }
    public int getPdfPageNumberResource() { return R.layout.dialog_pagenumber; }
    public int getPdfPasswordEditField() { return R.id.etPassword; }
    public int getPdfPasswordOkButton() { return R.id.btOK; }
    public int getPdfPasswordExitButton() { return R.id.btExit; }
    public int getPdfPageNumberEditField() { return R.id.pagenum_edit; }
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);           
         setContentView(R.layout.activity_main);


        listview = getListView();     
        dir_Path = "/storage";     //initial path
        try {                   
            dir_Path = getIntent().getExtras().getString("path");//get the path from intent
        } catch(NullPointerException e) {}

         FileAdapter Adapter = null;
         
         
		try { //this try catch block is to check the exception if we dont have root permission
			Adapter = new FileAdapter(this, 1, new ArrayList<FileObject>());
			   file_list = (new File(dir_Path)).listFiles();
			 for (int i = 0; i < file_list.length; i++) //itrating over the length of file list
			    Adapter.add(new FileObject(file_list[i])); //adding item to fileobjec
			 Adapter.sort();
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

         listview.setAdapter(Adapter);
         listview.setOnItemClickListener(this);

         setTitle(dir_Path);//setting title 
    }


     @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
         String path = dir_Path + "/" + ((TextView)view.findViewById(R.id.fileName)).getText().toString();
 
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
           if(ext.equals("pdf")){//for pdf
		    	 
       		  
                  Intent intent212 = new Intent(this, View_pdf.class);//using Android-Pdf-Viewer-Library
      	        intent212.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, file1.getAbsolutePath());//sending path 
      	        
      	        startActivity(intent212);
 
           }
           else{// for rest
             intent.setDataAndType(Uri.fromFile(file),type);//when we have mime will put this in intent to open the file
            startActivity(intent);
           }
        }
    }

     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event)
     {
         if (keyCode == KeyEvent.KEYCODE_BACK)
         {
             finish();    
             if(!dir_Path.equals("/"))  //if its home / then no animation
                 overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
         }
         return super.onKeyDown(keyCode, event);
     }
 
 
}
