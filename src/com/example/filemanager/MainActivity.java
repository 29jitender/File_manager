package com.example.filemanager;      

 
import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


public class MainActivity  
        extends SherlockListActivity     
        implements AdapterView.OnItemClickListener
 {	private ActionMode mActionMode;

    ListView listview;         
    String dir_Path;      
    File[] file_list; //array of items
    FileAdapter Adapter = null;
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);           
         setContentView(R.layout.activity_main);


        listview = getListView();     
        dir_Path = Environment.getExternalStorageDirectory().toString();     //initial path
        try {                   
            dir_Path = getIntent().getExtras().getString("path");//get the path from intent
        } catch(NullPointerException e) {}

         
         
         
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
         
         
         listview.setOnItemLongClickListener(new OnItemLongClickListener() {
 			@Override
 			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
 				onListItemCheck(position);
 				//listview.setEnabled(false);

 				return true;
 			}
 		});
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
 	protected void onListItemClick(ListView l, View v, int position, long id) {		
 		if(mActionMode == null) {
 	         listview.setOnItemClickListener(this);//opening folder if it is single
 		} else
 			// add or remove selection for current list item
 			onListItemCheck(position);		
 	}
 	private void onListItemCheck(int position) {
 		Adapter.toggleSelection(position);
        boolean hasCheckedItems = Adapter.getSelectedCount() > 0;        

        if (hasCheckedItems && mActionMode == null)
        	// there are some selected items, start the actionMode
            mActionMode = startActionMode(new ActionModeCallback());
        else if (!hasCheckedItems && mActionMode != null)
        	// there no selected items, finish the actionMode
            mActionMode.finish();
        

        if(mActionMode != null)
        	mActionMode.setTitle(String.valueOf(Adapter.getSelectedCount()) + " selected");
    }
 	
 	private class ActionModeCallback implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// inflate contextual menu	
			mode.getMenuInflater().inflate(R.menu.contextual, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {			
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			// retrieve selected items 
 			SparseBooleanArray selected = Adapter.getSelectedIds();
 		    ArrayList<String> paths = new ArrayList<String>();

 			for (int i = 0; i < selected.size(); i++){				
			    if (selected.valueAt(i)) {
			    	paths.add(Adapter.getItem(selected.keyAt(i)).getFile().getAbsolutePath());
  			    }
 			}
			
			switch(item.getItemId()){
	    	case R.id.copy:
				Toast.makeText(MainActivity.this, paths+"", Toast.LENGTH_LONG).show();

	    		break;
	    	case R.id.delet:
	    			for(int i=0;i<paths.size();i++){
	    				
	    				String path=paths.get(i);
	    				File file = new File(path);
	    				boolean deleted = file.delete();
	    				
	    				Toast.makeText(MainActivity.this, "delet go gaya", Toast.LENGTH_LONG).show();
	    				 Intent intent = getIntent();
	    				    finish();
	    				    startActivity(intent);
	    				
	    			}
	    		
	    		
	    		break;
	    	case R.id.move:
				
				Toast.makeText(MainActivity.this, paths+"", Toast.LENGTH_LONG).show();

	    		
	    		break;
	     
	   		
	   		

	    	}
			
						
			
			// close action mode
			mode.finish();
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// remove selection 
			Adapter.removeSelection();
			mActionMode = null;
		}
		
	}
 } 
 