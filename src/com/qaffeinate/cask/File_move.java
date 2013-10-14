package com.qaffeinate.cask;

import java.util.ArrayList;

public class File_move {
	public static ArrayList<String> path_list;
	public static Boolean move=false;
	
	public ArrayList<String> getpath(){
		return path_list;
	}
	public void setpath(ArrayList<String> path_list){
		this.path_list=path_list;
	}
	public void setmove(Boolean move){
		this.move=move;
	}   
}
