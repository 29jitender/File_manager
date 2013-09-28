 
package com.example.filemanager;      

 import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        ((TextView)v.findViewById(R.id.fileName)).setText(file.getName());
        ((TextView)v.findViewById(R.id.fileSize)).setText(file.getSize()); //getting size
        ((TextView)v.findViewById(R.id.fileModified)).setText(file.getModified()); // last modified
         return v; 
    }

     public void sort()
    {
        Collections.sort(entries);
    }
}