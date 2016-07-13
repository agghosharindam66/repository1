package com.example.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;

public class RestClient {

	private Map<String, Object> params;
	private ArrayList<NameValuePair> headers;

	private String url;

	private int responseCode;
	private String message;

	private String response;
	private InputStream fileInputStream;

	public String getResponse() {
		return response;
	}

	public String getErrorMessage() {
		return message;
	}

	public int getResponseCode() {
		return responseCode;
	}
	
	

	/**
	 * @return the fileInputStream
	 */
	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	/**
	 * @param fileInputStream the fileInputStream to set
	 */
	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public RestClient(String url) {
		this.url = url;
		params = new HashMap<>();
		headers = new ArrayList<NameValuePair>();
	}

	public RestClient() {
		params = new HashMap<>();
		headers = new ArrayList<NameValuePair>();
	}

	public void AddParam(String name, String value) {
		params.put(name, value);
	}

	public void AddHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}

	public Map<String, Object> getParam() {
		return params;
	}

	public ArrayList<NameValuePair> getHeaders() {
		return headers;
	}

	public void setHeaderList(ArrayList<NameValuePair> headerList) {
		headers = headerList;
	}

	public void setParamMap(Map<String, Object> paramMap) {
		params = paramMap;
	}

	public void Execute(RequestMethod method) throws Exception {
		switch (method) {
		case GET: {
			// add parameters
			/*
			 * String combinedParams = ""; if(!params.isEmpty()){ combinedParams
			 * += "?"; for(NameValuePair p : params) { String paramString =
			 * p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
			 * if(combinedParams.length() > 1) { combinedParams += "&" +
			 * paramString; } else { combinedParams += paramString; } } }
			 * 
			 * HttpGet request = new HttpGet(url + combinedParams);
			 */			
			 
			HttpGet request = new HttpGet(url);
			for(NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue()); 
			}
			executeRequest(request, url);
			break;
		}
		case POST: {
			HttpPost request = new HttpPost(url);

			// add headers
			for (NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}

			// if(!params.isEmpty()){
			// request.setEntity(new StringEntity("{\"memberId\":\"" + "1" +
			// "\",\"password\":\"" + "1234" + "\"}",
			// "application/json;charset=UTF-8"));
			Gson gson = new Gson();
			String obj = gson.toJson(params);

			StringEntity se = new StringEntity(obj);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			request.setEntity(se);
			// }

			executeRequest(request, url);
			break;
		}
		}
	}

	private void executeRequest(HttpUriRequest request, String url) {
		HttpClient client = new DefaultHttpClient();

		HttpResponse httpResponse;

		try {
			httpResponse = client.execute(request);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			message = httpResponse.getStatusLine().getReasonPhrase();

			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {
				
				InputStream instream = entity.getContent();
				Header header[]=((Header[])request.getHeaders("Accept"));
				if(header.length>0 && header[0].getValue().equalsIgnoreCase("images/*")){
					response="OK";
					fileInputStream=instream;
				}
				else{
					response = convertStreamToString(instream);
					instream.close();
				}				
				// Closing the input stream will trigger connection release
				
			}

		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			e.getMessage();
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		}
		catch (Throwable e) {
			System.out.println(e.getMessage());
		}
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	
}