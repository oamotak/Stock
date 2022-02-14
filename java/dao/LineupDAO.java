package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Lineup;

public class LineupDAO {
	//接続情報
		private final String JDBC_URL="jdbc:h2:tcp://localhost/~/stock";
		private final String DB_USER="sa";
		private final String DB_PASS="";
	//全レコードの取得
		public List<Lineup> getLineup(){
			List<Lineup> lineupList = new ArrayList<>();
				
			try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
				String sql="SELECT * FROM LINEUP";
				PreparedStatement pStmt=conn.prepareStatement(sql);
					
				ResultSet rs=pStmt.executeQuery();
					
				while(rs.next()) {
					String id = rs.getString("ID");
					String name = rs.getString("NAME");
					String color1 = rs.getString("COLOR1");	
					String color2 = rs.getString("COLOR2");
					String color3 = rs.getString("COLOR3");
					String color4 = rs.getString("COLOR4");
					String color5 = rs.getString("COLOR5");
					
					//Lineupインスタンスに格納
					Lineup lineup = new Lineup(id,name,color1,color2,color3,color4,color5);
						
					//Listに追加
					lineupList.add(lineup);
				}
			}catch(SQLException e) {
					e.printStackTrace();
					return null;
		}
			return lineupList;
		}
		public Lineup search(String name) {
			Lineup lineup = null;
			
			try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
				String sql="SELECT * FROM LINEUP WHERE NAME=?";
				PreparedStatement pStmt=conn.prepareStatement(sql);
				pStmt.setString(1,name);
					
				ResultSet rs=pStmt.executeQuery();
					
				if(rs.next()) {
					String id = rs.getString("ID");
					String color1 = rs.getString("COLOR1");	
					String color2 = rs.getString("COLOR2");
					String color3 = rs.getString("COLOR3");
					String color4 = rs.getString("COLOR4");
					String color5 = rs.getString("COLOR5");
					
					//Lineupインスタンスに格納
					 lineup = new Lineup(id,name,color1,color2,color3,color4,color5);
						
				}
			}catch(SQLException e) {
					e.printStackTrace();
					return null;
		}return lineup;
		}
}
