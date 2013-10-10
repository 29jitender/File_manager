 
package com.example.filemanager;      

 import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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

     public FileAdapter(Activity a, int textViewResourceId, ArrayList<FileObject> entries) {
        super(a, textViewResourceId, entries);
		mSelectedItemsIds = new SparseBooleanArray();

        this.entries = entries;
        this.activity = a;
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

         final FileObject file = entries.get(position);
         	Resources res = v.getResources();
			 
            ((ImageView)v.findViewById(R.id.icon)).setImageDrawable(getImageForExtension(file.getFile(), res));

        ((TextView)v.findViewById(R.id.fileName)).setText(file.getName());

        ((TextView)v.findViewById(R.id.fileSize)).setText(file.getSize()); //getting size

        ((TextView)v.findViewById(R.id.fileModified)).setText(file.getModified()); // last modified
  

		 

        
        
         return v; 
    }

     public void sort()
    {
        Collections.sort(entries);
    }
     
     
     public Drawable getImageForExtension(File file, Resources res) {
 		Drawable d = null;
 		String extension = "";
 		
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
 				if (extension.equals(".mp3") || extension.equals(".amr")) {
 					d = res.getDrawable(R.drawable.music);
 				} else if (extension.equals(".mp4") || extension.equals(".avi")
 						|| extension.equals(".mpg")) {
 					d = res.getDrawable(R.drawable.video);
 				} else if (extension.equals(".pdf")) {
 					d = res.getDrawable(R.drawable.pdf);
 				} else if (extension.equals(".jpg")
 						|| extension.equals(".jpeg")) {
 					d = res.getDrawable(R.drawable.image);
 				} else if (extension.equals(".xml")) {
 					d = res.getDrawable(R.drawable.xml);
 				} else if (extension.equals(".apk")) {
 					d = res.getDrawable(R.drawable.android);
 				} else {
 					d = res.getDrawable(R.drawable.none);
 				}
 			} else {
 				d = res.getDrawable(R.drawable.none);
 			}

 		}

 		return d;
 	}
}