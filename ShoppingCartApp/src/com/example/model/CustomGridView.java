package com.example.model;

import java.util.ArrayList;
import java.util.List;

import com.example.shoppingcartapp.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomGridView extends BaseAdapter{

	ArrayList<Items> myObjects;
	ViewHolder holder;
	Context context;
	public CustomGridView(Context context,ArrayList<Items> objects){
		myObjects=objects;
		this.context=context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*// TODO Auto-generated method stub
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
        	convertView = mInflater.inflate(R.layout.grid_view, null);
    		holder = new ViewHolder();    	
    		holder.imageView=(ImageView)convertView.findViewById(R.id.imageId);   
    		
    		convertView.setTag(holder);
        } 
        else{
        	holder = (ViewHolder) convertView.getTag();
        }	
        Items items=myObjects.get(position);       
        holder.imageView.setImageBitmap(items.getImage());
        return convertView;*/
		/*ImageView imageView;
        int imageID = 0;
 
        // Want the width/height of the items
        // to be 120dp
        int wPixel = dpToPx(120);
        int hPixel = dpToPx(120);
 
        // Move cursor to current position
        mCursor.moveToPosition(position);
        // Get the current value for the requested column
        imageID = mCursor.getInt(columnIndex);*/
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            // If convertView is null then inflate the appropriate layout file
        	convertView = mInflater.inflate(R.layout.grid_view, null);
        }
        else {
 
        }
 
        /*ImageView imageView = (ImageView) convertView.findViewById(R.id.imageId);
 
        // Set height and width constraints for the image view
        imageView.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
 
        // Set the content of the image based on the provided URI
        imageView.setImageBitmap(myObjects.get(position).getImage());
        // Image should be cropped towards the center
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
 
        // Set Padding for images
        imageView.setPadding(8, 8, 8, 8);
 
        // Crop the image to fit within its padding
        imageView.setCropToPadding(true);*/
 
        
        EditText imageView = (EditText) convertView.findViewById(R.id.imageId);
        imageView.setText("Arindam - "+position);
        return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myObjects.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	

}
