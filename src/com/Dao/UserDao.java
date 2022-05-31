package com.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.Model.User;

public class UserDao {

	private String jdbc_URL="jdbc:mysql://localhost:3306/demo?useSSL=false";
	private String jdbc_username="root";
	private String jdbc_password="root";
	
	private static final String INSERT_USER_SQL="INSERT INTO User"+"(name,email,country)VALUES"+
	"(?,?,?);";
	
	
	private static final String SELECT_USER_BY_ID =	"select id,name,email,country from user where id=?";
	private static final String SELECT_ALL_USERS  =	"select * from user";
	private static final String DELETE_USER_SQL	  =	"delete from user where id=?";
	private static final String UPDATE_USER_SQL	  =	"update user set name=?	,email=?,country=? where id=?";
	
	
	
	protected Connection getconnection() {
		Connection connection=null;
		try {
			Class.forName("com.jdbc.mysql.cj.Driver");
			connection=DriverManager.getConnection(jdbc_URL,jdbc_username,jdbc_password);
			System.out.println("connection success");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
		
	}
	
	
	//method to insert data into DB
	public void insert(User user) throws SQLException {
		try( Connection connection=getconnection();
			PreparedStatement ps=connection.prepareStatement(INSERT_USER_SQL)){
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getCountry());
			ps.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	} 
	
	//update method 
	
	public boolean update(User user) throws SQLException{
		boolean rowupdated;
		try (
			Connection connection=getconnection();
			PreparedStatement stmt=connection.prepareStatement(UPDATE_USER_SQL)){
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getCountry());
			rowupdated=stmt.executeUpdate()>0;
		}
		return rowupdated;
	}
		
		//select user by id
		
		public User selectuserbyid(int id) {
			User user=null;
			try {Connection connection=getconnection();
			PreparedStatement ps=connection.prepareStatement(SELECT_USER_BY_ID);
			ps.setInt(1, id);
			System.out.println(ps);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				String name =rs.getString("name");
				String email =rs.getString("email");
				String country =rs.getString("country");
				user =new User(id,name,email,country);
			}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return user;
			
		}
		
		
		//select all users
		public List<User> selectallusers() {
			List<User>users=new ArrayList<>();
			try (Connection connection=getconnection();
			PreparedStatement ps=connection.prepareStatement(SELECT_ALL_USERS);){	
			System.out.println(ps);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				int id=rs.getInt("id");
				String name =rs.getString("name");
				String email =rs.getString("email");
				String country =rs.getString("country");
				users.add(new User(id,name,email,country));
			}	
		}
			 catch (SQLException e) {
				e.printStackTrace();
			}		
			return users;
		}
	
		
		//delete 
		public boolean delete(int id) throws SQLException{
			boolean rowdeleted;
			try (
				Connection connection=getconnection();
				PreparedStatement stmt=connection.prepareStatement(DELETE_USER_SQL);){
				stmt.setInt(1, id);
				rowdeleted=stmt.executeUpdate()>0;			
			}
			return rowdeleted;
		}
		
		@SuppressWarnings("unused")
		private void printSQLException(SQLException ex) {
			for (Throwable e : ex) {
				if (e instanceof SQLException) {
					e.printStackTrace(System.err);
					System.err.println("SQLState: " + ((SQLException) e).getSQLState());
					System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
					System.err.println("Message: " + e.getMessage());
					Throwable t = ex.getCause();
					while (t != null) {
						System.out.println("Cause: " + t);
						t = t.getCause();
					}
				}
			}
		}
	}

