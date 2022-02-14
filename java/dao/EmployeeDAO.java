package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Employee;



public class EmployeeDAO {
	private final String JDBC_URL="jdbc:h2:tcp://localhost/~/stockManegement";
	private final String DB_USER="sa";
	private final String DB_PASS="";
	public Employee getEmployee(String i,String p){
		Employee emp=null;
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM EMPLOYEE WHERE ID=? AND PASSWORD=? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			
			pStmt.setString(1, i);
			pStmt.setString(2, p);

			
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String name=rs.getString("NAME");
				String id=rs.getString("ID");
				String pass=rs.getString("PASSWORD");
				//String type=rs.getString("TYPE");
				emp=new Employee(id,pass,name);
				
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return emp;
	}
	
	public String setEmployee(String name,String id,String pass) {
		String msg="";
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM EMPLOYEE WHERE ID=?";
			String sql2="INSERT INTO EMPLOYEE VALUES(?, ?, ?);";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, id);
			ResultSet rs=pStmt.executeQuery();
			if(name.length()==0 || id.length()==0 || pass.length()==0) {throw new SQLException("すべての項目に入力してください");}
			if(rs.next()) {
				msg="ID"+ "がかぶってます、登録できません";
			}else {
				PreparedStatement pStmt2=conn.prepareStatement(sql2);
				pStmt2.setString(1, id);
				pStmt2.setString(2, name);
				pStmt2.setString(3, pass);
				pStmt2.executeUpdate();
				msg=id+name+"社員登録完了しました";
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
		return msg;
	}
}



