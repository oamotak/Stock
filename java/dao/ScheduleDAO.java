package dao;

//SCHEDULEテーブル(製造予定表)のDAO 新規作成 以下目次
/*
 ・全レコードの取得
 ・日付から作る製品を検索
 ・製品から作る日を検索
 ・登録されている日付の取得(TreeSetで受け取り)
 ・レコードの追加
 ・レコードの削除
 */
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import model.Schedule;

//SQLのDate型の”yyyy-mm-dd”
//valueOfメソッドを使用するとString型からSQLのDate型に変換

public class ScheduleDAO {
	//接続情報
	private final String JDBC_URL="jdbc:h2:tcp://localhost/~/stock";
	private final String DB_USER="sa";
	private final String DB_PASS="";
	
/*
	//日付から製造する製品を検索
	public List<Product> schedule(String today,String fdate){
		List<Product>productList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT NAME,PRICE,STOCK,IMG FROM PRODUCTS WHERE DATE BETWEEN ? AND ? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, today);
			pStmt.setString(2, fdate);
			
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String name=rs.getString("NAME");
				int price=rs.getInt("PRICE");
				int stock=rs.getInt("STOCK");
				String img=rs.getString("IMG");
				

			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return productList;
	}
*/
	//全レコードの取得
	public List<Schedule> scheduleALL(){
		
		List<Schedule> scheduleList = new ArrayList<>();
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM SCHEDULE";
			PreparedStatement pStmt=conn.prepareStatement(sql);
						
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				Date date = rs.getDate("DATE");
				String id = rs.getString("ID");
				String name = rs.getString("NAME");
				String color = rs.getString("COLOR");
				int quantity = rs.getInt("QUANTITY");
				String schedule_id = rs.getString("SCHEDULE_ID");
				
				Schedule schedule = new Schedule(date,id,name,color,quantity,schedule_id);
				scheduleList.add(schedule);
				
			}
		 }catch(SQLException e) {
			e.printStackTrace();
			return null;
		  }
			return scheduleList;
	}
	
	
	//日付から作る製品を検索
	public List<Schedule> scheduleProducts(Date date){
		
		List<Schedule> scheduleList = new ArrayList<>();
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM SCHEDULE WHERE DATE = ?";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			ResultSet rs=pStmt.executeQuery();
			pStmt.setDate (1,date);
			
			while(rs.next()) {
				String id = rs.getString("ID");
				String name = rs.getString("NAME");
				String color = rs.getString("COLOR");
				int quantity = rs.getInt("QUANTITY");
				String schedule_id = rs.getString("SCHEDULE_ID");
				
				Schedule schedule = new Schedule(date,id,name,color,quantity,schedule_id);
				scheduleList.add(schedule);
			}
		 }catch(SQLException e) {
			e.printStackTrace();
			return null;
		  }
			return scheduleList;
	}
	
	//製品から作る日を検索
	public List<Schedule> scheduleDate(String name,String id,String color){
		
		List<Schedule> scheduleList = new ArrayList<>();
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM SCHEDULE WHERE NAME = ? OR ID = ? OR COLOR = ?";
			PreparedStatement pStmt=conn.prepareStatement(sql);
				pStmt.setString(1,name);
				pStmt.setString(2,id);
				pStmt.setString(3,color);
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				Date date = rs.getDate("DATE");
				int quantity = rs.getInt("QUANTITY");
				String schedule_id = rs.getString("SCHEDULE_ID");		
				Schedule schedule = new Schedule(date,id,name,color,quantity,schedule_id);
				scheduleList.add(schedule);
			}
		 }catch(SQLException e) {
			e.printStackTrace();
			return null;
		  }
			return scheduleList;
	}
	
	//登録されている日付の取得(TreeSetで受け取り)
	public Set<Date> getDate(){
		
		Set<Date> dateList = new TreeSet<>();
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT DATE FROM SCHEDULE";
			PreparedStatement pStmt=conn.prepareStatement(sql);
						
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				Date date = rs.getDate("DATE");
				
				dateList.add(date);
				
			}
		 }catch(SQLException e) {
			e.printStackTrace();
			return null;
		  }
			return dateList;
	}
	
	//schedule_idから作る日を検索
		public Schedule Getschedule(String schedule_id){
			
			Schedule schedule = null;
			
			try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
				String sql="SELECT * FROM SCHEDULE WHERE SCHEDULE_ID = ?";
				PreparedStatement pStmt=conn.prepareStatement(sql);
					pStmt.setString(1,schedule_id);
					
				ResultSet rs=pStmt.executeQuery();
				
				while(rs.next()) {
					Date date = rs.getDate("DATE");
					int quantity = rs.getInt("QUANTITY");
					String name = rs.getString("NAME");
					String id = rs.getString("ID");
					String color = rs.getString("COLOR");
					schedule = new Schedule(date,id,name,color,quantity,schedule_id);
					
				}
			 }catch(SQLException e) {
				e.printStackTrace();
				return null;
			  }
				return schedule;
		}
	
	//レコードの追加
	public boolean create(Schedule schedule) {
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="INSERT INTO SCHEDULE (DATE,ID,NAME,COLOR,QUANTITY,SCHEDULE_ID)VALUES(?,?,?,?,?,?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);
					pStmt.setDate(1,schedule.getDate());
					pStmt.setString(2,schedule.getId());
					pStmt.setString(3,schedule.getName());
					pStmt.setString(4,schedule.getColor());
					pStmt.setInt(5,schedule.getQuantity());
					pStmt.setString(6,schedule.getSchedule_id());
			int result = pStmt.executeUpdate();
			
			if(result != 1){
				return false;
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//レコードを削除
	public boolean delete(String schedule_id) {
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="DELETE FROM SCHEDULE WHERE SCHEDULE_ID=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
					pStmt.setString(1,schedule_id);
					
			int result = pStmt.executeUpdate();
			
			if(result != 1){
				return false;
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
