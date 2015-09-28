package com.weibo.stormUI.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionHelper {
	// 根据URL请求数据， 并返回result
			public static String URLConnection(String urlString) {
				URL url = null;
				try {
					url = new URL(urlString);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream。
				URLConnection uc = null;
				try {
					uc = url.openConnection();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BufferedReader in = null;
				try {
					in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				StringBuffer json = new StringBuffer();
				String data = null;
				try {
					while ((data = in.readLine()) != null) {
						json.append(data);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				data = json.toString();
				return data;
			}
}
