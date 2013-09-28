 
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
        if(!isDir) //if not directy
            size = f.length();
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
        return humanReadableByteCount(size, false);
    }

    

    public String getModified()
    {
         SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yy H:mm:ss");
        return sdf.format(modified);
    }

    
    @Override
    public int compareTo(FileObject other)
    {
        if(this.isDir!=other.isDir)
            return this.isDir?-1:1;
        else
            return name.compareTo(other.name);
    }

    
    public static String humanReadableByteCount(long bytes, boolean si) { // getting size of file in redable format
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}
