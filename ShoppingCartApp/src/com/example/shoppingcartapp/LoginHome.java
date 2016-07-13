package com.example.shoppingcartapp;


import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.model.CategoryListresponseModel;
import com.example.model.LoginResponseModel;
import com.example.util.RequestMethod;
import com.example.util.RestClient;
import com.example.util.ServiceURL;
import com.example.util.ServiceUtil;
import com.google.android.gms.internal.u;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginHome extends ActionBarActivity {

	Context context=this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginhome);

		((Button) findViewById(R.id.loginBtn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				final String userId=((EditText)findViewById(R.id.userId)).getText().toString();
				final String password=((EditText)findViewById(R.id.password)).getText().toString();

				/*ExecutorService exe=Executors.newSingleThreadExecutor();						
				Future<?> f=exe.submit(new Runnable() {
					
					@Override
					public void run() {*/		
				try{
						ServiceUtil util=new ServiceUtil();	
						RestClient client=new RestClient();
						
						client.AddParam("memberId", userId);
						client.AddParam("password", password);
						client.AddHeader("Content-Type", "application/json");
						client.AddHeader("Accept", "application/json");
						
						String response=util.invokeWS("LOGIN",client.getParam(),client.getHeaders(),RequestMethod.POST);
						LoginResponseModel responseModel=(LoginResponseModel)util.getObject(response, LoginResponseModel.class);

						if(Integer.parseInt(responseModel.getErrorCode())<0){
														
							/*AlertDialog.Builder builder=new AlertDialog.Builder(context);
							builder.setMessage(responseModel.getErrorMsg());							
							AlertDialog alertDialog = builder.create();
						    alertDialog.show();*/
						}else{
							if(responseModel.getRole().equalsIgnoreCase("USER")){
								//Toast.makeText(LoginHome.this, "Login Success", Toast.LENGTH_SHORT).show();
								RestClient client2=new RestClient();
								
								String categoryStr=util.invokeWS("GETCATEGORY",client2.getParam(),client2.getHeaders(),RequestMethod.GET);
								/*String categoryStr="";
								try{
									JSONArray jsonArray=new JSONArray(response1);
									for(int count=0;count<jsonArray.length();count++){
										JSONObject jsonObject=(JSONObject)jsonArray.get(count);
										CategoryListresponseModel cateListresponseModel=(CategoryListresponseModel)util.getObject(jsonObject.toString(), CategoryListresponseModel.class);
										categoryStr+=cateListresponseModel.getCategoryId()+"~"+cateListresponseModel.getCategoryName()+"/";										
									}
									categoryStr=categoryStr.substring(0, categoryStr.length()-1);
								}catch(Exception e){
									e.getMessage();
								}*/
								
								
								SharedPreferences.Editor editor=getSharedPreferences("CategoryList", 0).edit();
								editor.putString("value", categoryStr);
								editor.commit();
								
								Intent intent = new Intent(getApplicationContext(),
										UserHomeScreen.class);
								startActivity(intent);
							}
						}	
				}catch(Exception e){
					throw new RuntimeException(e);
				}
				/*	}	*/				
				/*}); */
				
				
			}
		});
	}


		   
}
