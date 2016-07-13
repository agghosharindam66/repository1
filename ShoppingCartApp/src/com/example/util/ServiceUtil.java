package com.example.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.NameValuePair;

import com.google.gson.Gson;

public class ServiceUtil {

	public final static Gson obj = new Gson();

	public String invokeWS(final String serviceName,
			final Map<String, Object> params,
			final ArrayList<NameValuePair> headers, final RequestMethod method)
			throws Exception {

		String response;
		ExecutorService exe = Executors.newSingleThreadExecutor();
		Future<String> f = exe.submit(new Callable<String>() {
			@Override
			public String call() {
				String url = new ServiceURL().getUrl(serviceName);
				RestClient client = new RestClient(url);
				client.setParamMap(params);
				client.setHeaderList(headers);

				try {
					client.Execute(method);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return client.getResponse();
			}
		});
		response = f.get();
		exe.shutdown();
		return response;

	}

	public InputStream invokeFileWS(final String serviceName,
			final Map<String, Object> params,
			final ArrayList<NameValuePair> headers, final RequestMethod method)
			throws Exception {

		InputStream response;
		ExecutorService exe = Executors.newSingleThreadExecutor();
		Future<InputStream> f = exe.submit(new Callable<InputStream>() {
			@Override
			public InputStream call() {
				String url = new ServiceURL().getUrl(serviceName);
				RestClient client = new RestClient(url);
				client.setParamMap(params);
				client.setHeaderList(headers);

				try {
					client.Execute(method);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return client.getFileInputStream();
			}
		});
		response = f.get();
		exe.shutdown();
		return response;

	}
	@SuppressWarnings("unchecked")
	public <T> T getObject(String response, Class<T> className) {

		Object responseObj = obj.fromJson(response, className);
		return (T) responseObj;
	}
}
