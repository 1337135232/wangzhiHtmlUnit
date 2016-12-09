package com.study.httpclient;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

public class HttpClient_1 {
	
	public static void main(String[] args) {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		CloseableHttpClient client2 = HttpClients.createDefault();
	}
}
