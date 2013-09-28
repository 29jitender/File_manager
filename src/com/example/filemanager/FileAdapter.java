 
package com.example.filemanager;      

 import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
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

     public FileAdapter(Activity a, int textViewResourceId, ArrayList<FileObject> entries) {
        super(a, textViewResourceId, entries);
        this.entries = entries;
        this.activity = a;
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
			 

        ((TextView)v.findViewById(R.id.fileName)).setText(file.getName());
        ((TextView)v.findViewById(R.id.fileSize)).setText(file.getSize()); //getting size
        ((TextView)v.findViewById(R.id.fileModified)).setText(file.getModified()); // last modified
        ((ImageView)v.findViewById(R.id.icon)).setImageDrawable(getImageForExtension(file.getFile(), res));
  

		 

        
        
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
 					d = res.getDrawable(R.drawable.musique);
 				} else if (extension.equals(".mp4") || extension.equals(".avi")
 						|| extension.equals(".mpg")) {
 					d = res.getDrawable(R.drawable.movie);
 				} else if (extension.equals(".pdf")) {
 					d = res.getDrawable(R.drawable.pdf);
 				} else if (extension.equals(".jpg")
 						|| extension.equals(".jpeg")) {
 					d = res.getDrawable(R.drawable.jpg);
 				} else if (extension.equals(".xml")) {
 					d = res.getDrawable(R.drawable.xml);
 				} else if (extension.equals(".apk")) {
 					d = res.getDrawable(R.drawable.android);
 				} else {
 					d = res.getDrawable(R.drawable.fichier);
 				}
 			} else {
 				d = res.getDrawable(R.drawable.fichier);
 			}

 		}

 		return d;
 	}
}