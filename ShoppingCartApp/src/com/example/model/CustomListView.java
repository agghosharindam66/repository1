package com.example.model;

import java.util.List;

import com.example.shoppingcartapp.R;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListView extends ArrayAdapter<Items>{

	List<Items> mObjects;
	Context context;
	ViewHolder holder;
	public CustomListView(Context context, int resource, List<Items> objects) {
		super(context, resource, objects);
		this.context = context;
		mObjects=objects;	
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
        	convertView = mInflater.inflate(R.layout.item_list, null);
    		holder = new ViewHolder();    	
    		holder.imageView=(ImageView)convertView.findViewById(R.id.imageView);   
    		holder.name=(TextView)convertView.findViewById(R.id.name);
    		holder.price=(TextView)convertView.findViewById(R.id.price);
    		convertView.setTag(holder);
        } 
        else{
        	holder = (ViewHolder) convertView.getTag();
        }	
        Items items=mObjects.get(position);       
        holder.imageView.setImageBitmap(items.getImage());
        holder.name.setText(items.getName());
        holder.price.setText(items.getPrice());
        return convertView;
	}

}
