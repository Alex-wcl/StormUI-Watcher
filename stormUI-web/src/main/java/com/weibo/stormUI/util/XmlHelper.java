package com.weibo.stormUI.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.weibo.stormUI.model.User;

public class XmlHelper {
	public static List<User> getUsers(URL userXmlURL) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(userXmlURL);
		Element root = document.getRootElement();
		List<Element> childElements = root.elements();
		List<User> users = new ArrayList<User>();
		for (Element child : childElements) {
			User user = new User();
			user.setName(child.elementText("username"));
			user.setPassword(child.elementText("password"));
			users.add(user);
		}
		return users;
	}
}
