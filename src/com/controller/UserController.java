package com.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.Dao.UserDao;
import com.Model.User;


@WebServlet("/")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       private UserDao userdao;
    
    public UserController() {
       this.userdao=new UserDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			this.doGet(request, response);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				String action=request.getServletPath();
				switch(action) {
				case "/new":
					try {
					shownewform(request, response);
					}catch(SQLException e) {
						e.printStackTrace();
					}
					break;
					
				case "/insert":
					try {
						insertUser(request, response);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					break;
					
				case "/delete":
					try {
						deleteUser(request, response);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					break;
					
					
				case "/edit":
					try {
						showeditform(request, response);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					break;
					
					
				case "/update":
					try {
						updateUser(request, response);
					} catch (SQLException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
					
					
				default:
					try {
						listusers(request, response);
					} catch (SQLException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
		}
	}
	public void listusers(HttpServletRequest request,HttpServletResponse response )
			throws SQLException,IOException, ServletException{
		List<User> listuser=userdao.selectallusers();
		request.setAttribute("listuser", listuser);
		RequestDispatcher dispathcher =request.getRequestDispatcher("user-list.jsp");
		dispathcher.forward(request, response);
	}
	private void updateUser(HttpServletRequest request,HttpServletResponse response) 
			throws SQLException,IOException{
		int id=Integer.parseInt(request.getParameter("id"));
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String country=request.getParameter("country");
		
		User user=new User(id,name,email,country);
		userdao.update(user);
		response.sendRedirect("list");
	}
	private void deleteUser(HttpServletRequest request,HttpServletResponse response) 
			throws SQLException,IOException{
			int id=Integer.parseInt(request.getParameter("id"));
			userdao.delete(id);
			response.sendRedirect("list");
	}
	private void showeditform(HttpServletRequest request,HttpServletResponse response) 
			throws SQLException,IOException, ServletException{
			int id=Integer.parseInt(request.getParameter("id"));
			User existinguser=userdao.selectuserbyid(id);
			RequestDispatcher dispatcher=request.getRequestDispatcher("user-form.jsp");
			request.setAttribute("user", existinguser);
			dispatcher.forward(request, response);
	}
	private void shownewform(HttpServletRequest request,HttpServletResponse response) 
			throws ServletException,IOException,SQLException{
		
		RequestDispatcher dispatcher=request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);
	}
		
	private void insertUser(HttpServletRequest request,HttpServletResponse response) 
			throws SQLException,IOException{
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String country=request.getParameter("country");
		User newuser=new User(name,email,country);
		userdao.insert(newuser);
		response.sendRedirect("list");
		
		
	}

}
