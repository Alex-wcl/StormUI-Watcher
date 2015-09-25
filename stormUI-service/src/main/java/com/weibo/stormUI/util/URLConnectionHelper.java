package com.weibo.stormUI.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionHelper {
	// 根据URL请求数据， 并返回result
			public static String URLConnection(String urlString) throws IOException {
				URL url = new URL(urlString);
				// 打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream。
				URLConnection uc = url.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
				StringBuffer json = new StringBuffer();
				String data = null;
				while ((data = in.readLine()) != null) {
					json.append(data);
				}
				in.close();
				data = json.toString();
				return data;
			}
}
