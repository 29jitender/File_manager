 
package com.qaffeinate.cask;      

 import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 

public class FileAdapter
        extends ArrayAdapter<FileObject>
{
    private ArrayList<FileObject> entries;   
    private Activity activity;             
	private SparseBooleanArray mSelectedItemsIds;
	private ArrayList<FileObject> arraylist;

     public FileAdapter(Activity a, int textViewResourceId, ArrayList<FileObject> entries) {
        super(a, textViewResourceId, entries);
		mSelectedItemsIds = new SparseBooleanArray();

        this.entries = entries;
        this.activity = a;
        this.arraylist = new ArrayList<FileObject>();
        this.arraylist.addAll(entries);
    }

     public void toggleSelection(int position)
     {
         selectView(position, !mSelectedItemsIds.get(position));
     }

     public void removeSelection() {
         mSelectedItemsIds = new SparseBooleanArray();
         notifyDataSetChanged();
     }

     public void selectView(int position, boolean value)
     {
         if(value)
             mSelectedItemsIds.put(position, value);
          else
             mSelectedItemsIds.delete(position);
         
         notifyDataSetChanged();
     }
     
     public int getSelectedCount() {
         return mSelectedItemsIds.size();// mSelectedCount;
     }
     
     public SparseBooleanArray getSelectedIds() {
     	return mSelectedItemsIds;
     }
     
     
    @Override
    public View getView(int position, View recycleView, ViewGroup parent) {
        View v = recycleView;

       
        if (v == null)
        {
            LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_row, null);
        }
    	v.setBackgroundColor(mSelectedItemsIds.get(position)? 0x9934B5E4: Color.TRANSPARENT);   //changing colr of selected     	

         final FileObject file = entries.get(position);
         	Resources res = v.getResources();
			 
            ((ImageView)v.findViewById(R.id.icon)).setImageDrawable(getImageForExtension(file.getFile(), res,v));

        ((TextView)v.findViewById(R.id.fileName)).setText(file.getName());

        ((TextView)v.findViewById(R.id.fileSize)).setText(file.getSize()); //getting size

        ((TextView)v.findViewById(R.id.fileModified)).setText(file.getModified()); // last modified
  

		 

        
        
         return v; 
    }

     public void sort()
    {
        Collections.sort(entries);
    }
     
     
     public Drawable getImageForExtension(File file, Resources res,View v) {
 		Drawable d = null;
 		String extension = "";
			String filePath = file.getPath();

 		if (file.isDirectory()) {
 			d = res.getDrawable(R.drawable.folder);
 		} else {
 			String filename = file.getName();
 			int dotPos = filename.lastIndexOf(".");
 			if( dotPos != -1 )
 				extension = filename.substring(dotPos);
 			else
 				extension = "";

 			if (!extension.equals("")) {
 				if (extension.equals(".mp3") //music
 						|| extension.equals(".amr")
 						|| extension.equals(".wma")
 						|| extension.equals(".m4a")
 						|| extension.equals(".m4p")) {					
 					d = res.getDrawable(R.drawable.music);
 					
 				} else if (extension.equals(".mp4") //video
 						|| extension.equals(".avi")
 						|| extension.equals(".3gp")
 						|| extension.equals(".wmv")
 						|| extension.equals(".mpg")) {
 					d = res.getDrawable(R.drawable.video);
 				} else if (extension.equals(".pdf")) {//pdf
 					d = res.getDrawable(R.drawable.pdf);
 				}else if (extension.equals(".doc")//doc
 						|| extension.equals(".docx")
 						|| extension.equals(".rtf")) {
 					d = res.getDrawable(R.drawable.word);
 				} else if (extension.equals(".xls")
 						|| extension.equals(".xlsx")) {//excel
 					d = res.getDrawable(R.drawable.excel);
 				} 
 				else if (extension.equals(".ppt")|| extension.equals(".pptx")) {//ppt
 					d = res.getDrawable(R.drawable.powerpoint);
 				} 
 				else if (extension.equals(".txt")) {//text
 					d = res.getDrawable(R.drawable.notepad);
 				} 
 				else if (extension.equals(".zip")
 						|| extension.equals(".rar")
 						|| extension.equals(".gzip")
 						|| extension.equals(".7z")

 						) {//archive                               
 					d = res.getDrawable(R.drawable.zip);
 				} 
 				
 				else if (extension.equals(".jpg")
 						|| extension.equals(".jpeg")
 						|| extension.equals(".png")
 						|| extension.equals(".gif")
 						|| extension.equals(".tif")
 						|| extension.equals(".tiff")) {
 					 	
 					d = res.getDrawable(R.drawable.image);
 				} else if (extension.equals(".xml")) {
 					d = res.getDrawable(R.drawable.xml);
 				} else if (extension.equals(".apk")) {
 					
  			        PackageInfo packageInfo = getContext().getPackageManager().getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
 			        if(packageInfo != null) {
 			            ApplicationInfo appInfo = packageInfo.applicationInfo;
 			            if (Build.VERSION.SDK_INT >= 8) {
 			                appInfo.sourceDir = filePath;
 			                appInfo.publicSourceDir = filePath;
 			            }
 			            Drawable icon = appInfo.loadIcon(getContext().getPackageManager());
 			          		Bitmap bmpIcon = ((BitmapDrawable) icon).getBitmap();
 			          		d =new BitmapDrawable(bmpIcon); 
 			        }
			 			        else{
			 					d = res.getDrawable(R.drawable.ic_launcher);
			 			        }
 				} else {
 					d = res.getDrawable(R.drawable.none);  //other
 				}
 			} else {
 				d = res.getDrawable(R.drawable.none);  //asdasdasdasd
 			}

 		}

 		return d;
 	}
     
 	 // Filter Class
     public void filter(String charText) {
         charText = charText.toLowerCase(Locale.getDefault());
          entries.clear();
         if (charText.length() == 0) {
        	 entries.addAll(arraylist);
         } else {
             for (FileObject wp : arraylist) {
                 if (wp.getName().toLowerCase(Locale.getDefault())
                         .contains(charText)) {
                	 entries.add(wp);
                 } 
                 else if (wp.getpath().toLowerCase(Locale.getDefault())
                         .contains(charText)) {
                	 entries.add(wp);
                 }
                 
             }
         }
         notifyDataSetChanged();
     }
}