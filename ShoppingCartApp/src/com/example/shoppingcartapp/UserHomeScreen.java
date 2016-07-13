package com.example.shoppingcartapp;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.model.CategoryListresponseModel;
import com.example.model.CustomListView;
import com.example.model.Items;
import com.example.model.ProductDtlsModel;
import com.example.util.RequestMethod;
import com.example.util.RestClient;
import com.example.util.ServiceUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserHomeScreen extends ActionBarActivity{
	Context context=this;
	ArrayList<CategoryListresponseModel> categoryList=new ArrayList<>();
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userhomescreen);
		SharedPreferences setting=getSharedPreferences("CategoryList", 0);
		Toast.makeText(context, setting.getString("value", "0"), Toast.LENGTH_SHORT).show();		
		String categoryStr=setting.getString("value", "0");
		try{
			JSONArray jsonArray=new JSONArray(categoryStr);
			ServiceUtil util=new ServiceUtil();		
			for(int count=0;count<jsonArray.length();count++){
				JSONObject jsonObject=(JSONObject)jsonArray.get(count);				
				CategoryListresponseModel cateListresponseModel=(CategoryListresponseModel)util.getObject(jsonObject.toString(), CategoryListresponseModel.class);
				categoryList.add(cateListresponseModel);
			}
			((Button)findViewById(R.id.searchBtn)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Toast.makeText(context, ((TextView)findViewById(R.id.search)).getText(), Toast.LENGTH_SHORT).show();
					String productName=((TextView)findViewById(R.id.search)).getText().toString();
					ServiceUtil util=new ServiceUtil();	
					RestClient client=new RestClient();	
					try{						
						client.AddParam("productName", productName);			
						client.AddHeader("Content-Type", "application/json");
						client.AddHeader("Accept", "application/json");			
						String response=util.invokeWS("GETPRODUCTLISTBYNAME",client.getParam(),client.getHeaders(),RequestMethod.POST);
						ArrayList<String> productList=util.getObject(response, ArrayList.class);
						final ListView listView=(ListView)findViewById(R.id.mylist);
						ArrayList<Items> itemList=new ArrayList<>();
						for (String productId:productList) {
							Items items=new Items();
							client.AddParam("productId", String.valueOf(productId));	
							String response1=util.invokeWS("GETIMAGEIDLISTBYPRODUCT",client.getParam(),client.getHeaders(),RequestMethod.POST);
							ArrayList<String> imageIdList=util.getObject(response1, ArrayList.class);
											
							String imageId=imageIdList.get(0);
							RestClient client2=new RestClient();
							client2.AddParam("imageId", String.valueOf(imageId));
							client2.AddParam("productId", String.valueOf(productId));	
							client2.AddHeader("Accept", "images/*");
							client2.AddHeader("Content-Type", "application/json");
							final InputStream response2=util.invokeFileWS("GETIMAGEBYID",client2.getParam(),client2.getHeaders(),RequestMethod.POST);
							final BufferedInputStream bufferedInputStream = new BufferedInputStream(response2);				

							ExecutorService exe = Executors.newSingleThreadExecutor();
							Future<Bitmap> f = exe.submit(new Callable<Bitmap>() {
								@Override
								public Bitmap call() throws Exception {
									Options options = new BitmapFactory.Options();
									options.inPurgeable = true;
									options.outHeight = 50;
									options.outWidth = 50;
									options.inSampleSize = 4;
									Bitmap bmp=BitmapFactory.decodeStream(bufferedInputStream,null,options);
									bufferedInputStream.close();						
									return bmp;							
								}				
							});							    
							items.setImage(f.get());
							exe.shutdown();		
							
							String productDtlsJson=util.invokeWS("GETPRODUCTDTLSBYID",client.getParam(),client.getHeaders(),RequestMethod.POST);
							ProductDtlsModel productDtlsModel=util.getObject(productDtlsJson, ProductDtlsModel.class);
							items.setName(productDtlsModel.getProductName());
							items.setPrice(String.valueOf(productDtlsModel.getPrice()));
							items.setProductId(productId);
							itemList.add(items);
						}		
						CustomListView adapter = new CustomListView(context, android.R.layout.simple_list_item_1, itemList);
						listView.setAdapter(adapter);
					}
					catch(Exception e){
						throw new RuntimeException(e);
					}
					catch(Throwable e){
						throw new RuntimeException(e);
					}
				}
			});
		}
		catch(Exception e){
			e.getMessage();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		for(int count=0;count<categoryList.size();count++){
			CategoryListresponseModel categoryListresponseModel=(CategoryListresponseModel)categoryList.get(count);
			menu.add(0, Integer.parseInt(categoryListresponseModel.getCategoryId()), 0, categoryListresponseModel.getCategoryName());			
		}
		menu.removeItem(R.id.action_settings);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		ServiceUtil util=new ServiceUtil();	
		RestClient client=new RestClient();	
		try{						
			client.AddParam("categoryId", String.valueOf(id));			
			client.AddHeader("Content-Type", "application/json");
			client.AddHeader("Accept", "application/json");			
			String response=util.invokeWS("GETPRODUCTLISTBYCATEGORY",client.getParam(),client.getHeaders(),RequestMethod.POST);
			ArrayList<String> productList=util.getObject(response, ArrayList.class);
			final ListView listView=(ListView)findViewById(R.id.mylist);
			ArrayList<Items> itemList=new ArrayList<>();
			for (String productId:productList) {
				Items items=new Items();
				client.AddParam("productId", String.valueOf(productId));	
				String response1=util.invokeWS("GETIMAGEIDLISTBYPRODUCT",client.getParam(),client.getHeaders(),RequestMethod.POST);
				ArrayList<String> imageIdList=util.getObject(response1, ArrayList.class);
								
				String imageId=imageIdList.get(0);
				RestClient client2=new RestClient();
				client2.AddParam("imageId", String.valueOf(imageId));
				client2.AddParam("productId", String.valueOf(productId));	
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
						options.inSampleSize = 4;
						Bitmap bmp=BitmapFactory.decodeStream(bufferedInputStream,null,options);						
						bufferedInputStream.close();						
						return bmp;							
					}				
				});							    
				items.setImage(f.get());
				exe.shutdown();		
				
				String productDtlsJson=util.invokeWS("GETPRODUCTDTLSBYID",client.getParam(),client.getHeaders(),RequestMethod.POST);
				ProductDtlsModel productDtlsModel=util.getObject(productDtlsJson, ProductDtlsModel.class);
				items.setName(productDtlsModel.getProductName());
				items.setPrice(String.valueOf(productDtlsModel.getPrice()));
				items.setProductId(productId);
				itemList.add(items);
			}		
			CustomListView adapter = new CustomListView(context, android.R.layout.simple_list_item_1, itemList);
			listView.setAdapter(adapter);
			
			((ListView)findViewById(R.id.mylist)).setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Items items=(Items)parent.getAdapter().getItem(position);
					Toast.makeText(context, position+items.getProductId(), Toast.LENGTH_SHORT).show();
					SharedPreferences.Editor editor=getSharedPreferences("ProductDtls", 0).edit();
					editor.putString("productId", items.getProductId());
					editor.commit();
					Intent intent=new Intent(context,DisplayImage.class);
					startActivity(intent);
				}
			});
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		catch(Throwable e){
			throw new RuntimeException(e);
		}
		return super.onOptionsItemSelected(item);
	}
}
