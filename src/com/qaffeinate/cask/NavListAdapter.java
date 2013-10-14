package com.qaffeinate.cask;

import java.util.ArrayList;

import com.example.filemanager.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NavListAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	 ArrayList<String> mTitle;
	//String[] mSubTitle;
	//int[] mIcon;
	LayoutInflater inflater;

	public NavListAdapter(Context context,  ArrayList<String> title
			 ) {
		this.context = context;
		this.mTitle = title;
		//this.mSubTitle = subtitle;
		//this.mIcon = icon;
	}

	@Override
	public int getCount() {
		return mTitle.size();
	}

	@Override
	public Object getItem(int position) {
		return mTitle.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// Declare Variables
		TextView txtTitle;
		//TextView txtSubTitle;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.nav_list_item, parent, false);
		
		// Locate the TextViews in nav_list_item.xml
		txtTitle = (TextView) itemView.findViewById(R.id.title);
	//	txtSubTitle = (TextView) itemView.findViewById(R.id.subtitle);

		// Set the results into TextViews
		txtTitle.setText(mTitle.get(position));
		//txtSubTitle.setText(mSubTitle[position]);

		return itemView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// Declare Variables
		TextView txtTitle;
	//	TextView txtSubTitle;
		//ImageView imgIcon;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dropdownView = inflater.inflate(R.layout.nav_dropdown_item, parent,
				false);
		
		// Locate the TextViews in nav_dropdown_item.xml
		txtTitle = (TextView) dropdownView.findViewById(R.id.title);
		//txtSubTitle = (TextView) dropdownView.findViewById(R.id.subtitle);
		
		// Locate the ImageView in nav_dropdown_item.xml
		//imgIcon = (ImageView) dropdownView.findViewById(R.id.icon);

		// Set the results into TextViews
		txtTitle.setText(mTitle.get(position));
		//txtSubTitle.setText(mSubTitle[position]);
		
		// Set the results into ImageView
		//imgIcon.setImageResource(mIcon[position]);

		return dropdownView;
	}

}
