 
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
    Date last_modified;
      File rec;

     public FileObject(File recived_file)
    {
         isDir = recived_file.isDirectory();
        path = recived_file.getPath();
        name = recived_file.getName();
        if(!isDir) //if not directy
            size = recived_file.length();
        last_modified = new Date(recived_file.lastModified());
        rec=recived_file;
          
    }

     public String getName()
    {
        return name;
    }
     public File getFile(){
    	 
		return rec;
    	 
     }
    public String getSize()
    {
        if(isDir)        
            return "";//folder this is ;)
        return bytecount_format(size, false);
    }

    

    public String getModified()
    {
         SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yy H:mm:ss");
        return sdf.format(last_modified);
    }

    
    @Override
    public int compareTo(FileObject other)//compering dir and files if dir it should come on top otherwish sort files 
    {
        if(this.isDir!=other.isDir)
            return this.isDir?-1:1;
        else
            return name.compareTo(other.name);
    }

    
    public static String bytecount_format(long bytes, boolean si) { // getting size of file in redable format
        int unit = si ? 1000 : 1024;// 1024 bit to byte or for
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + ("");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}
