package com.example.util;

import java.util.HashMap;
import java.util.Map;

public class ServiceURL {
	
	private String baseUrl="http://169.144.60.217:8085/ShoppingCart/shoppingCart/";
	
	Map<String, Object> urlMap=new HashMap<>();
	public ServiceURL(){
		urlMap.put("LOGIN", "Image/validateUser");
		urlMap.put("GETCATEGORY", "Image/getCategoryList");
		urlMap.put("GETIMAGEBYID", "Image/getImageById");
		urlMap.put("GETPRODUCTLISTBYCATEGORY", "Image/getProductIdListByCategory");
		urlMap.put("GETIMAGEIDLISTBYPRODUCT", "Image/getImageIdListByProduct");
		urlMap.put("GETPRODUCTDTLSBYID", "Image/getProductDtls");
		urlMap.put("GETPRODUCTLISTBYNAME", "Image/getProductIdListByName");
	}
	
	public String getUrl(String ServiceName){
		return baseUrl+(String)urlMap.get(ServiceName);
	}

}
