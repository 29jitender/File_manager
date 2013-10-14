package com.example.filemanager;      

 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


public class MainActivity  
        extends SherlockListActivity     
  {	     public static ProgressDialog dialog ;//dialog

	  RelativeLayout paste_layout=null;
	  TextView copyormove;
	  private ActionMode mActionMode;
     ListView listview;         
    String dir_Path;      
    File[] file_list; //array of items
    FileAdapter Adapter = null;
    Boolean check_selected=false;
    //navigation 
    ListView list;
	NavListAdapter adapter;
	 
	ArrayList<String> title;
    
    ///
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);           
         ////////////////navigation
        dir_Path = Environment.getExternalStorageDirectory().toString();     //initial path
        try {                   
            dir_Path = getIntent().getExtras().getString("path");//get the path from intent
        } catch(NullPointerException e) {}

          title = new ArrayList<String>();

        
        	String temp_path=dir_Path;//removing sd card path  .replace(Environment.getExternalStorageDirectory().toString(), "")
        	
        	while(temp_path.contains("/")){
        		int lastslashPosition = temp_path.lastIndexOf('/');        		
        		title.add(temp_path.subSequence(lastslashPosition+1, temp_path.length()).toString());         		
        		temp_path =temp_path.subSequence(0, lastslashPosition).toString();        		

        		
        	}
        
     	// Generate title
 		
 		 
 		 	 

  		adapter = new NavListAdapter(this, title); 		
 		// Hide the ActionBar Title
 		//getSupportActionBar().setDisplayShowTitleEnabled(false); 		
 		// Create the Navigation List in your ActionBar
 		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
 		if(!dir_Path.equals(Environment.getExternalStorageDirectory().toString())){
 			getSupportActionBar().setDisplayHomeAsUpEnabled(true);//not for home
 		}     //initial path
 
 	 		

 		// Listen to navigation list clicks
 		ActionBar.OnNavigationListener navlistener = new OnNavigationListener() {

 			@Override
 			public boolean onNavigationItemSelected(int position, long itemId) {
 				 StringBuffer sb = new StringBuffer("");
 				 if(position!=0){
   				for(int i=(title.size()-1);i>=position;i--){
  					sb.append("/"+title.get(i));
  					
  					
  				} 
    				Intent next = new Intent(MainActivity.this, MainActivity.class);
   				next.putExtra("path", sb.toString());//putting path
   		        next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 

   				finish();
   				startActivity(next); //starting it again with new path and show that again
   			    overridePendingTransition(0,0);
 				 }
  				return true;
 			}

 		};
 		// Set the NavListAdapter into the ActionBar Navigation
 		getSupportActionBar().setListNavigationCallbacks(adapter, navlistener);
         
         
         ////////////
         
        setContentView(R.layout.activity_main);

         
         
	      paste_layout =(RelativeLayout)findViewById(R.id.paste_layout);
	      
	      copyormove =(TextView)findViewById(R.id.copyormove);

        listview = getListView();     
       
         
         
         
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
       //  setTitle(dir_Path);//setting title 
    }


     @Override
	protected void onResume() {
	      dialog=new ProgressDialog(MainActivity.this);	
	  	Button paste =(Button)findViewById(R.id.paste);
    	Button cancel =(Button)findViewById(R.id.cancel);
    	
    	
    	
    	
 		    final ArrayList<String> path_recived  ;
		    if(File_move.path_list!=null){
 		    	path_recived=File_move.path_list;
		    	
		    	paste_layout.setVisibility(View.VISIBLE);
		    	if(File_move.move){
	    				copyormove.setText("Move here");

		    	}
		    	else{
	    				copyormove.setText("Copy here");
		    		
		    	}
		    
		    	
		    	paste.setOnClickListener(new View.OnClickListener(){
		    		public void onClick(View View3) {
		    			
		    			if(File_move.move){
		    				 class file_move_async extends AsyncTask<Void, Void, Integer> {
		    			    	 
		    			         protected Integer doInBackground(Void... params) {
		    			        	 
		    			        		for(int i=0;i<path_recived.size();i++){
		    			    				String sourcePath=path_recived.get(i);
		    			    				File sourc_file = new File(sourcePath);
		    			    				String filename = null;
		    			    				int lastslashPosition = sourcePath.lastIndexOf('/');
		    			    				if( lastslashPosition > 0 ) {
		    			    					filename = sourcePath.substring(lastslashPosition + 1);
		    			    				}
		    			    				File dest_file=new File(dir_Path+"/"+filename);

  		    			    				String destcheck=dir_Path+"/"+filename;
 
		    			    				if(destcheck.equals(sourcePath)){}
		    			    				else{
		    			    				if(sourc_file.isDirectory()) //if its a directry
		    			    			        {
 
		    			    					  try {
		    										copy_fileordir(sourc_file, dest_file);
		    	 								} catch (IOException e) {
		    										// TODO Auto-generated catch block
		    										e.printStackTrace();
		    									}
		    			    					  
		    			    			         }
		    			    				  else{
		    			    				
		    			    				 
		    			    				
		    			    				
		    			    				try {
		    			    					
		    			    					copy_fileordir(sourc_file, dest_file) ; 
		    	 							} catch (IOException e) {
		    									// TODO Auto-generated catch block
		    									e.printStackTrace();
		    								}
		    			    				  }
		    	 					    	
		    			    			}}
		    			             return null;
		    			              
		    			             
		    			         }

		    			         
		    			@Override
		    			         protected void onPreExecute() {
		    					dialog.setMessage("Moving please wait");
		    					dialog.show();
		    			             super.onPreExecute();
		    			         }


		    				protected void onPostExecute(Integer result) {
		    					
		    					for(int i=0;i<path_recived.size();i++){
		    	    				
		    	    				String path=path_recived.get(i);
		    	    				File file = new File(path);
		    	    				DeleteRecursive(file);
		    	    				
		    	    				
		    	    			}
		    	    			
		    	    			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
		    	    					Uri.parse("file://" + Environment.getExternalStorageDirectory())));//refreshing
		    	    			
 		       				 	 
		    					
		    					
		    					File_move.path_list = null; //removing list
			    				File_move.move=false;
						    	paste_layout.setVisibility(View.GONE);
						    	dialog.dismiss();

						    	refresh_activity();
		    			             super.onPostExecute(result);
		    			         }


							private void DeleteRecursive(File fileOrDirectory) {
								if (fileOrDirectory.isDirectory())
							        for (File child : fileOrDirectory.listFiles())
							            DeleteRecursive(child);

							    fileOrDirectory.delete();								
							}
		    			     }
		    				
		    				
		    				
		    		            new file_move_async().execute();//checking api after the values are updated in conf_api

		    				 
		    				 
		    				 
		    				 
		    				 
		    				 
		    				 
		    			}	else{	    	
 		    					class Copy_file_async extends AsyncTask<Void, Void, Integer> {
		    			    	 
		    			         protected Integer doInBackground(Void... params) {

		    			        		for(int i=0;i<path_recived.size();i++){
		    			    				String sourcePath=path_recived.get(i);
		    			    				File sourc_file = new File(sourcePath);
		    			    				String filename = null;
		    			    				int lastslashPosition = sourcePath.lastIndexOf('/');
		    			    				if( lastslashPosition > 0 ) {
		    			    					filename = sourcePath.substring(lastslashPosition + 1);
		    			    				}
		    			    				
		    			    				File dest_file=new File(dir_Path+"/"+filename);
 		    			    				String destcheck=dir_Path+"/"+filename;
 
		    			    				if(destcheck.equals(sourcePath)){}
		    			    				else{
		    			    				  if(sourc_file.isDirectory()) //if its a directry
		    			    			        {
		    			    					  
		    	 
		    			    					  try {
		    										copy_fileordir(sourc_file, dest_file);
		    	 								} catch (IOException e) {
		    										// TODO Auto-generated catch block
		    										e.printStackTrace();
		    									}
		    			    					  
		    			    			         }
		    			    				  else{
		    			    				
		    			    				
		    			    				
		    			    				try {
		    			    					
		    			    					copy_fileordir(sourc_file, dest_file) ; 
		    	 							} catch (IOException e) {
		    									// TODO Auto-generated catch block
		    									e.printStackTrace();
		    								}
		    			    				
		    			    				
		    			    				  }
		    			    					
		    			    			}
		    			    				 
		    			        		}
		    			             return null;
		    			              
		    			             
		    			         }

		    			         
		    			@Override
		    			         protected void onPreExecute() {
		    					dialog.setMessage("Copying please wait");
		    					dialog.show();
		    			             super.onPreExecute();
		    			         }


		    				protected void onPostExecute(Integer result) {
		    					
		    					File_move.path_list = null; //removing list
			    				File_move.move=false;
						     	paste_layout.setVisibility(View.GONE);
 						    	dialog.dismiss();

		    	    			refresh_activity();
	    			             super.onPostExecute(result);

		    			         }


						 
		    			     }
		    				
		    				
		    				
		    		            new Copy_file_async().execute();//checking api after the values are updated in conf_api

		    				
		    				
		    		
					    	
		    			}
		    		
 		    		} });

 		    }
		    else{
		    	cancel.setOnClickListener(new View.OnClickListener(){
		    		public void onClick(View View3) {
		    			File_move.path_list = null; //removing list
				    	paste_layout.setVisibility(View.GONE);

			    		} });
		    	paste.setOnClickListener(new View.OnClickListener(){
		    		public void onClick(View View3) {
		 	    		Toast.makeText(MainActivity.this, "Select other Directory", Toast.LENGTH_SHORT).show();


			    		} });
		    	
		    }
		super.onResume();
	}
  
     
     
    
 
     
     

     public static void copy_fileordir(File sourceLocation, File targetLocation)
    	        throws IOException {

    	    if (sourceLocation.isDirectory()) {
    	    	
    	        if (!targetLocation.exists()) {
    	            targetLocation.mkdir();
    	        }
    	         
    	        
    	        String[] children = sourceLocation.list();
    	        for (int i = 0; i < sourceLocation.listFiles().length; i++) {

    	            copy_fileordir(new File(sourceLocation, children[i]),
    	                    new File(targetLocation, children[i]));
    	        }
    	    } else {

    	        InputStream in = new FileInputStream(sourceLocation);

    	        OutputStream out = new FileOutputStream(targetLocation);

     	        byte[] buf = new byte[1024];
    	        int len;
    	        while ((len = in.read(buf)) > 0) {
    	            out.write(buf, 0, len);
    	        }
    	        in.close();
    	        out.close();
    	    }

    	}
 
	 
   
 	@Override
 	protected void onListItemClick(ListView l, View v, int position, long id) {		
 		if(mActionMode == null) {//normal list click

 			onsingleclick(v);
 		
 		
 		
 		} else
 			// add or remove selection for current list item
 			onListItemCheck(position);		
 	}
 	private void onListItemCheck(int position) {
 		
 		Adapter.toggleSelection(position);
        boolean hasCheckedItems = Adapter.getSelectedCount() > 0;        

        if (hasCheckedItems && mActionMode == null)
        	// there are some selected items, start the actionMode
        {  mActionMode = startActionMode(new ActionModeCallback());
        openOptionsMenu(); 
        }
        else if (!hasCheckedItems && mActionMode != null)
        	// there no selected items, finish the actionMode
            mActionMode.finish();
        

        if(mActionMode != null)
        	mActionMode.setTitle(String.valueOf(Adapter.getSelectedCount()) + " selected");
        
     }
 	
 	 public void onsingleclick(View view)
     {
 		 String path = dir_Path + "/" + ((TextView)view.findViewById(R.id.fileName)).getText().toString();
 		 
          File file1 = new File(path);
 	    	paste_layout.setVisibility(View.GONE);

       if(file1.isDirectory()) //if its a directry
      {
          
          Intent next = new Intent(MainActivity.this, MainActivity.class);
          next.putExtra("path", path);//putting path
           finish();
          next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 

          startActivity(next); //starting it again with new path and show that again 
 		    overridePendingTransition(0,0);

       }
       else
      { // otherwise open the file using defult 
          Intent intent = new Intent();
          //using a builtin Intent.

           try {
			intent.setAction(android.content.Intent.ACTION_VIEW);

			   File file = new File(path); // defining file
			  MimeTypeMap mime = MimeTypeMap.getSingleton();// this is used to open files
			  String ext=file.getName().substring(file.getName().lastIndexOf(".")+1); //getting the extension after .
			  String type = mime.getMimeTypeFromExtension(ext);  //
			 
			   intent.setDataAndType(Uri.fromFile(file),type);//when we have mime will put this in intent to open the file
			  
			   startActivity(intent);
		} catch (Exception e) {
			new AlertDialog.Builder(this)//opening a dialog box wiht msg
            .setTitle("No Appication can perform this action")
            .setNeutralButton("OK", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int button){
                   // onBackPressed();// on press ok come back to home

                	}
            })
            .show();	
			e.printStackTrace();
		}
          
      }
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
 		    final ArrayList<String> paths = new ArrayList<String>();

 			for (int i = 0; i < selected.size(); i++){				
			    if (selected.valueAt(i)) {
			    	paths.add(Adapter.getItem(selected.keyAt(i)).getFile().getAbsolutePath());
  			    }
 			}
			
			switch(item.getItemId()){
	    	case R.id.copy:
	    		File_move send_path = new File_move();
	    		send_path.setpath(paths);
	    		send_path.setmove(false);
 	    		//Toast.makeText(MainActivity.this, paths+"", Toast.LENGTH_LONG).show();
    			//refresh_activity();
		    	paste_layout.setVisibility(View.VISIBLE);
		    	copyormove.setText("Copy here");
	    		break;
	    	case R.id.delet:
	    		
	    		
	    		new AlertDialog.Builder(MainActivity.this)
	            .setIcon(android.R.drawable.ic_dialog_alert)
	            .setTitle("Delete")
	            .setMessage("You sure you want to delete ?")
	            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

	                @Override
	                public void onClick(DialogInterface dialog11, int which) {

	                	
	                	dialog.setMessage("Deleting please wait");
    					dialog.show();
    					
    					Thread thread = new Thread()
    					{
    					    @Override
    					    public void run() {
    					    	for(int i=0;i<paths.size();i++){
    			    				
    			    				String path=paths.get(i);
    			    				File file = new File(path);
    			    				DeleteRecursive(file);
    			    				
    			    				
    			    			}
    			    			
    			    			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
    			    					Uri.parse("file://" + Environment.getExternalStorageDirectory())));//refreshing
		    					dialog.dismiss();

     			    			refresh_activity();    
    					        }
    					    
    					};

    					thread.start();
    					               	
	                	
	                	
	                }

	            })
	            .setNegativeButton("Cancel", null)
	            .show();
	    		
	    			
	    		
	    		break;
	    	case R.id.move:
	    		File_move send_path_move = new File_move();
	    		send_path_move.setpath(paths);
	    		send_path_move.setmove(true);
		    	paste_layout.setVisibility(View.VISIBLE);
		    	copyormove.setText("Move here");
    			//refresh_activity();

 	    		
	    		break;	        		
	    	case R.id.rename:
	    		 
	    		for(int i=(paths.size()-1);i>=0;i--){
    				
    				String path=paths.get(i);
    				File file = new File(path);
    				if(i==(paths.size()-1)){
    					renamefile(file,true);	
    				}
    				else{
    					renamefile(file,false);
    				}
    				
   	    		}

	    		break;	
		    	case R.id.compress:
		    		compress_function(paths);
	    		break;
	    		
		    	case R.id.share:
		    		share_file(paths);
 	    		break;
		    	case R.id.newfolder:
		    			
		    			create_folder(paths.get(0));

		    	break;
	    		
	    	case R.id.selectall:
	    		
	    		if(check_selected){
	    			
	    			mActionMode.finish();//removing selected
	    			check_selected=false;
	    			}
	    			else{
			            mActionMode.finish();//removing selected
			    		for(int x=0;x< file_list.length;x++){//adding again
			    			onListItemCheck(x);	    		}
			    		check_selected=true;
	    			}
	    		break;

	    	}
			
						
			
			// close action mode
			mode.finish();
			return false;
		}
		
		
		public void create_folder(String path){			
			final File current_dir =new File(path);
			
			LayoutInflater li = LayoutInflater.from(MainActivity.this);
			View promptsView = li.inflate(R.layout.prompts, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					MainActivity.this);
 			alertDialogBuilder.setView(promptsView);

			final EditText userInput = (EditText) promptsView
					.findViewById(R.id.editTextDialogUserInput);
			final TextView msg = (TextView) promptsView
					.findViewById(R.id.msg_text);
			msg.setText("Please enter folder name: ");

			// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog11,int id) {
				     
				    	 File dir = new File(current_dir.getParent()+"/"+userInput.getText().toString());
							
				    	 	try{
							  if(dir.mkdir()) {
								  refresh_activity();
								  
							  } else {
					 	    		Toast.makeText(MainActivity.this, "Oops!! Something went wrong", Toast.LENGTH_SHORT).show();
							  }
							}catch(Exception e){
							  e.printStackTrace();
							}
				    	
				    }
				  })
				.setNegativeButton("Cancel",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
				    	
				    	 	  
 			              
					dialog.cancel();
				    }
				  });
			
 						AlertDialog alertDialog = alertDialogBuilder.create();

 						alertDialog.show();
			
			 
 			
		}
		
		
		
		
		public void share_file(ArrayList<String> paths){
			ArrayList<Uri> imageUris = new ArrayList<Uri>();
    		for(int i=0;i<paths.size();i++){
    			
    			imageUris.add(Uri.fromFile(new File(paths.get(i)))); 
    		}
    			File file =new File(paths.get(0));
    		  MimeTypeMap mime = MimeTypeMap.getSingleton();// this is used to open files
              String ext=file.getName().substring(file.getName().lastIndexOf(".")+1); //getting the extension after .
              String type = mime.getMimeTypeFromExtension(ext);  //
    		//paths
    		Intent shareIntent = new Intent();
    		shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
    		shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
    		shareIntent.setType(type);
    		startActivity(Intent.createChooser(shareIntent, "Share"));
			
		}
		
		public void compress_function(final ArrayList<String> paths){
			LayoutInflater li = LayoutInflater.from(MainActivity.this);
			View promptsView = li.inflate(R.layout.prompts, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					MainActivity.this);

			// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);

			final EditText userInput = (EditText) promptsView
					.findViewById(R.id.editTextDialogUserInput);
			final TextView msg = (TextView) promptsView
					.findViewById(R.id.msg_text);
			msg.setText("Give a name to zip ");

			// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog11,int id) {
				    	dialog.setMessage("Compressing please wait.");
    					dialog.show();
    					
    					Thread thread = new Thread()
    					{
    					    @Override
    					    public void run() {
    					    	File first = new File(paths.get(0));
    					    	Boolean check=false;
    					    	
    					    	for(int i=0;i<file_list.length;i++){
    					    		int lastslashPosition = file_list[i].getPath().lastIndexOf('/');        		
    				        		String name =file_list[i].getPath().subSequence(lastslashPosition+1, file_list[i].getPath().length()).toString();
     				        		if(name.equals(userInput.getText().toString()+".zip")){
      				        			check=true;
     					    		}
    					    		
    					    	}
    					    	Log.i("matched",check+"");
    					    	if(!userInput.getText().toString().equals("")){// only do if user has enterd something
    					    		if(check){
 
    			    					dialog.dismiss();

    					    		}
    					    		else{
    					    		Compress obj =new Compress(paths, first.getParent()+"/"+userInput.getText()+".zip");
    					    		//obj.zip(); 
    					    		if(obj.zip()){
    					    			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
    	    			    					Uri.parse("file://" + Environment.getExternalStorageDirectory())));//refreshing
    			    					dialog.dismiss();
    					    			refresh_activity();
    					    		}}
    					    		
    					    	}
    					    	else{
 			    					dialog.dismiss();

    					    	}
    					        }
    					    
    					};

    					thread.start();
    					
				    	
			    		
 			            	  
 			              	 
				    	
				    }
				  })
				.setNegativeButton("Cancel",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
				    	
				    	 	  
 			              
					dialog.cancel();
				    }
				  });
			
			// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();
			
		}
		
		public void renamefile(final File from,final Boolean last_file){
			LayoutInflater li = LayoutInflater.from(MainActivity.this);
			View promptsView = li.inflate(R.layout.prompts, null);
			final String parentname=from.getParent();
			final String file_name=from.getName();
			String filePath = from.getPath();

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					MainActivity.this);

			// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);

			final EditText userInput = (EditText) promptsView
					.findViewById(R.id.editTextDialogUserInput);
			final TextView msg = (TextView) promptsView
					.findViewById(R.id.msg_text);
			msg.setText("Rename: "+file_name);

			// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {

				    	  String extension = "";//gettin extension
				    	  int dotPos = file_name.lastIndexOf(".");
				    	  if( dotPos != -1 )
			 				extension = file_name.substring(dotPos);
				    	  else
			 				extension = "";

			 			 
				    	File from = new File(parentname,file_name);
				    	if(!userInput.getText().toString().equals("")){// only do if user has enterd something
 
				    	File to = new File(parentname,userInput.getText()+""+extension);
 				    	from.renameTo(to);
 				    	sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
 		    					Uri.parse("file://" + Environment.getExternalStorageDirectory())));//refreshing
 				    	if(last_file){
 			            	refresh_activity();
			 	    		//Toast.makeText(MainActivity.this, from.getName(), Toast.LENGTH_SHORT).show();

 			            }  
				    	}
				    	
 			            	  
 			              	 
				    	
				    }
				  })
				.setNegativeButton("Cancel",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
				    	
				    	if(last_file){
 			            	//refresh_activity();
			 	    		//Toast.makeText(MainActivity.this, from.getName(), Toast.LENGTH_SHORT).show();

 			            }  		  
 			              
					dialog.cancel();
				    }
				  });

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

		}
		
		
		public void DeleteRecursive(File fileOrDirectory) {
		    if (fileOrDirectory.isDirectory())
		        for (File child : fileOrDirectory.listFiles())
		            DeleteRecursive(child);

		    fileOrDirectory.delete();
		}
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// remove selection 
			Adapter.removeSelection();
			mActionMode = null;
		}
		
	}
  
 	public  void refresh_activity(){
		Intent intent = getIntent();
		    finish();
		    startActivity(intent);
		    overridePendingTransition(0,0);

	}

	private void updateStorageLabel() {
		long total, aval;
		int kb = 1024;
		
		StatFs fs = new StatFs(Environment.
								getExternalStorageDirectory().getPath());
		
		total = fs.getBlockCount() * (fs.getBlockSize() / kb);
		aval = fs.getAvailableBlocks() * (fs.getBlockSize() / kb);
		
//		storagetext.setText(String.format("sdcard: Total %.2f GB " +
//							  "\t\tAvailable %.2f GB", 
//							  (double)total / (kb * kb), (double)aval / (kb * kb)));
	}
	@Override
	public void onBackPressed() {
		
		
		
		String sdcard = Environment.getExternalStorageDirectory().toString();     //initial path
		if(dir_Path.equals(sdcard)){
			File_move.path_list = null; //removing list

				finish();
		}else{
		int lastslashPosition = dir_Path.lastIndexOf('/');

		Intent next = new Intent(MainActivity.this, MainActivity.class);
		
        next.putExtra("path", dir_Path.subSequence(0, lastslashPosition));//putting path
        next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 

        startActivity(next); //starting it again with new path and show that again
		    overridePendingTransition(0,0);

		}
 		super.onBackPressed();
	}
 
	//defult menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	     int itemId = item.getItemId();
	     switch (itemId)
	     {
	         
	         case android.R.id.home:
	             Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
	             mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	             startActivity(mainIntent);
	         default:
	             break;
	     }
	 
	     return true;
	 }
	

 } 
 