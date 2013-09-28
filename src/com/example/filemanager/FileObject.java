 
package com.example.filemanager;      

 import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

 
public class FileObject     

        implements Comparable<FileObject> 
{
    boolean isDir;
    String path, name ;
    long size;
    Date modified;

     public FileObject(File f)
    {
         isDir = f.isDirectory();
        path = f.getPath();
        name = f.getName();
        if(!isDir) //if not dir
            size = f.length();//get the size of file
        modified = new Date(f.lastModified());

          
    }

     public String getName()
    {
        return name;
    }

    public String getSize()
    {
        if(isDir)        
            return "";
        return "have to do this";
    }

    

    public String getModified()
    {
         SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yy H:mm:ss");
        return sdf.format(modified);
    }

    
    @Override
    public int compareTo(FileObject other) // if its not a dir put all dir on the top else all files in sorted order
    {
        if(this.isDir!=other.isDir)
            return this.isDir?-1:1;
        else
            return name.compareTo(other.name);
    }

   

}
