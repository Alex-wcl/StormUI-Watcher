package com.weibo.stormUI.servlet;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.DocumentException;

import com.weibo.stormUI.model.User;
import com.weibo.stormUI.util.XmlHelper;


/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userName=request.getParameter("username");
		String password=request.getParameter("password");
		if(userName != null && password != null){
			userName=new String(userName.getBytes("ISO-8859-1"),"GB2312");
			password=new String(password.getBytes("ISO-8859-1"),"GB2312");
		}else{
			request.getRequestDispatcher("/error.html").forward(request, response); 
			return ;
		}
		boolean isValidate = false;
		try {
			isValidate = userValidate(Thread.currentThread().getContextClassLoader().getResource("users.xml"),userName,password);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		if(isValidate){
			//通过验证
			session.setAttribute("username", userName);
			session.setAttribute("password", password);
			request.getRequestDispatcher("/variables.jsp").forward(request, response);
		}else{
			request.getRequestDispatcher("/error.html").forward(request, response); 
			//未通过验证
		}
	}
	
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)throws IOException,ServletException{
	}
	private boolean userValidate(URL userXmlURL,String userName,String password) throws DocumentException{
		if(userName != null && password != null){
			List<User> users = XmlHelper.getUsers(userXmlURL);
			int userSize = users.size();
			for(int i = 0;i < userSize;i++){
				if(userName.equals(users.get(i).getName()) && password.equals(users.get(i).getPassword())){
					return true;
				}
			}
		}
		return false;
	}
}
