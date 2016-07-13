package com.example.shoppingcartapp;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.example.model.Items;
import com.example.util.RequestMethod;
import com.example.util.RestClient;
import com.example.util.ServiceUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DisplayImage extends ActionBarActivity{
	ArrayList<String> imageIdList;
	final ArrayList<Bitmap> bitmapList=new ArrayList<>();;
	Context context=this;
	int index=1;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_displayproduct);
		SharedPreferences setting=getSharedPreferences("ProductDtls", 0);
		String productId=setting.getString("productId", "");
		ServiceUtil util=new ServiceUtil();	
		RestClient client=new RestClient();	
		client.AddParam("productId", String.valueOf(productId));	
		client.AddHeader("Content-Type", "application/json");
		client.AddHeader("Accept", "application/json");		
		RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.relativeLayout);
		LinearLayout ll = new LinearLayout(this);
	    ll.setOrientation(LinearLayout.HORIZONTAL);
	    ll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	    RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	    ll.setGravity(Gravity.CENTER);
		try{
			String response1=util.invokeWS("GETIMAGEIDLISTBYPRODUCT",client.getParam(),client.getHeaders(),RequestMethod.POST);
			imageIdList=util.getObject(response1, ArrayList.class);
			Toast.makeText(context, imageIdList.toString(), Toast.LENGTH_LONG).show();
			Items items =new Items();
			ArrayList<Items> itemList=new ArrayList<>();

			for (int count = 0; count < imageIdList.size(); count++) {
				String imageId=imageIdList.get(count).toString();
				RestClient client2=new RestClient();
				client2.AddParam("imageId", imageId);
				client2.AddParam("productId", productId);	
				client2.AddHeader("Accept", "images/*");
				client2.AddHeader("Content-Type", "application/json");
				final InputStream response2=util.invokeFileWS("GETIMAGEBYID",client2.getParam(),client2.getHeaders(),RequestMethod.POST);
				final BufferedInputStream bufferedInputStream = new BufferedInputStream(response2);				

				ExecutorService exe = Executors.newSingleThreadExecutor();
				Future<Bitmap> f = exe.submit(new Callable<Bitmap>() {
					@Override
					public Bitmap call() throws Exception {
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inPurgeable = true;
						options.outHeight = 50;
						options.outWidth = 50;
						//options.inSampleSize = 64;
						Bitmap bmp=BitmapFactory.decodeStream(bufferedInputStream,null,options);						
						bufferedInputStream.close();						
						return bmp;							
					}				
				});							    
				items.setImage(f.get());
				itemList.add(items);
				((ImageView)findViewById(R.id.imageFrame)).setImageBitmap(f.get());
				ImageView iv=new ImageView(context);
				iv.setImageBitmap(f.get());
				iv.setLayoutParams(new LayoutParams(50,50));
				iv.setPadding(0, 0, 10, 0);
				iv.setId(count);
				ll.addView(iv);
				bitmapList.add(f.get());
				exe.shutdown();
				iv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v){
						((ImageView)findViewById(R.id.imageFrame)).setImageBitmap(bitmapList.get(v.getId()));						
					}
				});
			}
			params1.addRule(RelativeLayout.BELOW, R.id.imageFrame);
			relativeLayout.addView(ll, params1);
			setContentView(relativeLayout);
			
		}
		catch(Exception e){
			
		}
	}

}
