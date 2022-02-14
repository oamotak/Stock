package dao;

//PARTSテーブル(部品情報)のDAO 修正済み 以下目次

/*
・全レコードを取得

・NAME(部品名)で検索 ※「search」を含むもの

・ID(部品ID)で検索し、STOCK(在庫数)を表示

・伝票情報を反映し、STOCK(在庫数)を更新

・SPEC_ID(仕様ID)とCOLOR(色)で検索し、※ProductsDAOから取得
	製品に必要なパーツと在庫を表示 
	
・レコードの追加

*/


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Parts;
import model.Voucher;

public class PartsDAO {
	
	//接続情報
	private final String JDBC_URL="jdbc:h2:tcp://localhost/~/stock";
	private final String DB_USER="sa";
	private final String DB_PASS="";
	
	//全レコードを取得
	public List<Parts> getParts(){
		
		List<Parts>partsList=new ArrayList<>();
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM PARTS ";
			PreparedStatement pStmt=conn.prepareStatement(sql);

			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id=rs.getString("ID");
				String spec_id = rs.getString("SPEC_ID");
				String name=rs.getString("NAME");
				String material = rs.getString("MATERIAL");
				String shape = rs.getString("SHAPE");
				String color = rs.getString("COLOR");
				String note = rs.getString("NOTE");
				int stock=rs.getInt("STOCK");
				Date last_update=rs.getDate("LAST_UPDATE");
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				Parts parts=new Parts(id,spec_id,name,material,
						shape,color,note,stock,last_update);
				//リストに格納
				partsList.add(parts);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return partsList;
	}
	
	//NAME(部品名)で検索 ※「search」を含むもの
	public List<Parts> getParts(String search){
		List<Parts>partsList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			
			String sql="SELECT * FROM PARTS WHERE NAME LIKE ? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, search);
			
			//OR ID=? OR STOCK=?
			//pStmt.setString(2, "%"+search+"%");
			//pStmt.setString(3, "%"+search+"%");
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id=rs.getString("ID");
				String spec_id = rs.getString("SPEC_ID");
				String name=rs.getString("NAME");
				String material = rs.getString("MATERIAL");
				String shape = rs.getString("SHAPE");
				String color = rs.getString("COLOR");
				String note = rs.getString("NOTE");
				int stock=rs.getInt("STOCK");
				Date last_update=rs.getDate("LAST_UPDATE");
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				Parts parts=new Parts(id,spec_id,name,material,
						shape,color,note,stock,last_update);
				
				//リストに格納
				partsList.add(parts);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return partsList;
	}
	
	//ID(部品ID)で検索し、STOCK(在庫数)を表示
	public int getPartsStock(String id){
		
		int stock = 0;
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT QUANTITY FROM PARTS WHERE ID=? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, id);
			
			//OR ID=? OR STOCK=?
			//pStmt.setString(2, "%"+search+"%");
			//pStmt.setString(3, "%"+search+"%");
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				stock=rs.getInt("STOCK");
			}
			
			System.out.println(stock);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println(stock);
		return stock;
	}
	
	//伝票情報を反映し、STOCK(在庫数)を更新
	public void setUpdates(List<Voucher>voucherList,Date now){
				
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
					
			//伝票の部品IDで検索し、
			//現在のSTOCK(在庫数)ーISSUE(出庫数)＋RECIEPT(入庫数)
			//LAST_UPDATE(更新日)をnowに更新
			for(Voucher voucher:voucherList) {

				String sql="UPDATE PARTS SET STOCK = STOCK - ? + ?,LAST_UPDATE=? WHERE ID = ? ";
				PreparedStatement pStmt=conn.prepareStatement(sql);
				pStmt.setInt(1,voucher.getIssue());
				pStmt.setInt(2,voucher.getReceipt());
				pStmt.setDate(3, now);
				pStmt.setString(4,voucher.getParts_id());
				
				pStmt.executeUpdate();
				System.out.println("now"+now);
			}
			
			//PARRTSテーブルの全てのLAST_UPDATE(更新日)を更新
			String sql2="UPDATE PARTS SET LAST_UPDATE=?";
			PreparedStatement pStmt=conn.prepareStatement(sql2);
			pStmt.setDate(1, now);pStmt.executeUpdate();

		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	//SPEC_ID(仕様ID)とCOLOR(色)で検索し、※ProductsDAOから取得
	//製品に必要なパーツの在庫を表示
	public Parts getParts(String spec_id,String color){
		
		Parts parts = null;
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM PARTS WHERE SPEC_ID = ? AND COLOR = ?";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, spec_id);
			pStmt.setString(2, color);
			
			ResultSet rs=pStmt.executeQuery();
			
			if(rs.next()) {
				String id=rs.getString("ID");
				String name=rs.getString("NAME");
				String material = rs.getString("MATERIAL");
				String shape = rs.getString("SHAPE");
				String note = rs.getString("NOTE");
				int stock=rs.getInt("STOCK");
				Date last_update=rs.getDate("LAST_UPDATE");
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				parts=new Parts(id,spec_id,name,material,
						shape,color,note,stock,last_update);
				
				
			}	
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return parts;
	}
	
	//IDで検索
	public Parts getParts_inf(String id){
		
		Parts parts = null;
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT* FROM PARTS WHERE ID=? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, id);
			
			ResultSet rs=pStmt.executeQuery();
			
			if(rs.next()) {
				String spec_id = rs.getString("SPEC_ID");
				String name=rs.getString("NAME");
				String material = rs.getString("MATERIAL");
				String shape = rs.getString("SHAPE");
				String color = rs.getString("COLOR");
				String note = rs.getString("NOTE");
				int stock=rs.getInt("STOCK");
				Date last_update=rs.getDate("LAST_UPDATE");
				
				parts =new Parts(id,spec_id,name,material,
						shape,color,note,stock,last_update);
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return parts;
	}
	
	//レコードの追加
	public boolean create(Parts parts) {
		//データベース接続
		try(Connection conn = DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
		//INSERT文の準備
		String sql = "INSERT INTO PARTS "
				+ "(ID,NAME,SPEC_NUM,MATERIAL,COLOR,SHAPE,NOTE,STOCK,LAST_UPDATE)"
				+ "VALUES(?,?,?,?,?,?,?,?,?)";
		PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, parts.getId());
			pStmt.setString(2, parts.getName());
			pStmt.setString(3, parts.getSpec_id());
			pStmt.setString(4, parts.getMaterial());
			pStmt.setString(5, parts.getColor());
			pStmt.setString(6, parts.getShape());
			pStmt.setString(7, parts.getNote());
			pStmt.setInt(8, parts.getStock());
			pStmt.setDate(9, parts.getLast_Update());
		
		int result = pStmt.executeUpdate();
		
		if(result != 1) {
			return false;
		}
	}catch (SQLException e) {
		e.printStackTrace();
		return false;
	}
	return false;
}	
	public String setParts(Parts parts){
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="INSERT INTO PARTS (ID,SPEC_ID,NAME,MATERIAL,SHAPE,COLOR,NOTE,STOCK,LAST_UPDATED) VALUES(?,?,?,?,?,?,?,?,?)";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, parts.getId());
			pStmt.setString(2, parts.getSpec_id());
			pStmt.setString(3, parts.getName());
			pStmt.setString(4, parts.getMaterial());
			pStmt.setString(5, parts.getShape());
			pStmt.setString(6, parts.getColor());
			pStmt.setString(7, parts.getNote());
			pStmt.setInt(8, parts.getStock());
			pStmt.setDate(9, parts.getLast_Update());
			pStmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}return "登録完了しました{ID:"+parts.getId()+"SPEC_ID"+parts.getSpec_id()+"NAME"+parts.getName();
	}
		
	


}






/*
package dao;

//PARTSテーブル(部品情報)のDAO 修正済み 以下目次

/*
・全レコードを取得

・NAME(部品名)で検索 ※「search」を含むもの

・ID(部品ID)で検索し、STOCK(在庫数)を表示

・伝票情報を反映し、STOCK(在庫数)を更新

・SPEC_ID(仕様ID)とCOLOR(色)で検索し、※ProductsDAOから取得
	製品に必要なパーツと在庫を表示 
	
・レコードの追加

*/

/*
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Parts;
import model.Voucher;

public class PartsDAO {
	
	//接続情報
	private final String JDBC_URL="jdbc:h2:tcp://localhost/~/stock";
	private final String DB_USER="sa";
	private final String DB_PASS="";
	
	//全レコードを取得
	public List<Parts> getParts(){
		
		List<Parts>partsList=new ArrayList<>();
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM PARTS ";
			PreparedStatement pStmt=conn.prepareStatement(sql);

			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id=rs.getString("ID");
				String spec_id = rs.getString("SPEC_ID");
				String name=rs.getString("NAME");
				String material = rs.getString("MATERIAL");
				String shape = rs.getString("SHAPE");
				String color = rs.getString("COLOR");
				String note = rs.getString("NOTE");
				int stock=rs.getInt("STOCK");
				Date last_update=rs.getDate("LAST_UPDATE");
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				Parts parts=new Parts(id,spec_id,name,material,
						shape,color,note,stock,last_update);
				//リストに格納
				partsList.add(parts);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return partsList;
	}
	
	//NAME(部品名)で検索 ※「search」を含むもの
	public List<Parts> getParts(String search){
		List<Parts>partsList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			
			String sql="SELECT * FROM PARTS WHERE NAME LIKE ? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, search);
			
			//OR ID=? OR STOCK=?
			//pStmt.setString(2, "%"+search+"%");
			//pStmt.setString(3, "%"+search+"%");
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id=rs.getString("ID");
				String spec_id = rs.getString("SPEC_ID");
				String name=rs.getString("NAME");
				String material = rs.getString("MATERIAL");
				String shape = rs.getString("SHAPE");
				String color = rs.getString("COLOR");
				String note = rs.getString("NOTE");
				int stock=rs.getInt("STOCK");
				Date last_update=rs.getDate("LAST_UPDATE");
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				Parts parts=new Parts(id,spec_id,name,material,
						shape,color,note,stock,last_update);
				
				//リストに格納
				partsList.add(parts);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return partsList;
	}
	
	//ID(部品ID)で検索し、STOCK(在庫数)を表示
	public int getPartsStock(String id){
		
		int stock = 0;
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT QUANTITY FROM PARTS WHERE ID=? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, id);
			
			//OR ID=? OR STOCK=?
			//pStmt.setString(2, "%"+search+"%");
			//pStmt.setString(3, "%"+search+"%");
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				stock=rs.getInt("STOCK");
			}
			
			System.out.println(stock);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println(stock);
		return stock;
	}
	
	//伝票情報を反映し、STOCK(在庫数)を更新
	public void setUpdates(List<Voucher>voucherList,Date now){
				
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
					
			//伝票の部品IDで検索し、
			//現在のSTOCK(在庫数)ーISSUE(出庫数)＋RECIEPT(入庫数)
			//LAST_UPDATE(更新日)をnowに更新
			for(Voucher voucher:voucherList) {

				String sql="UPDATE PARTS SET STOCK = STOCK - ? + ?,LAST_UPDATE=? WHERE ID = ? ";
				PreparedStatement pStmt=conn.prepareStatement(sql);
				pStmt.setInt(1,voucher.getIssue());
				pStmt.setInt(2,voucher.getReceipt());
				pStmt.setDate(3, now);
				pStmt.setString(4,voucher.getParts_id());
				
				pStmt.executeUpdate();
				System.out.println("now"+now);
			}
			
			//PARRTSテーブルの全てのLAST_UPDATE(更新日)を更新
			String sql2="UPDATE PARTS SET LAST_UPDATE=?";
			PreparedStatement pStmt=conn.prepareStatement(sql2);
			pStmt.setDate(1, now);pStmt.executeUpdate();

		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	//SPEC_ID(仕様ID)とCOLOR(色)で検索し、※ProductsDAOから取得
	//製品に必要なパーツの在庫を表示
	public List<Parts> getParts(String spec_id,String color){
		
		List<Parts>partsList=new ArrayList<>();
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM PARTS WHERE SPEC_ID = ? AND COLOR = ?";
			PreparedStatement pStmt=conn.prepareStatement(sql);

			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id=rs.getString("ID");
				String name=rs.getString("NAME");
				String material = rs.getString("MATERIAL");
				String shape = rs.getString("SHAPE");
				String note = rs.getString("NOTE");
				int stock=rs.getInt("STOCK");
				Date last_update=rs.getDate("LAST_UPDATE");
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				Parts parts=new Parts(id,spec_id,name,material,
						shape,color,note,stock,last_update);
				//リストに格納
				partsList.add(parts);
			}	
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return partsList;
	}
	
	//IDで検索
	public Parts getParts_inf(String id){
		
		Parts parts = null;
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT* FROM PARTS WHERE ID=? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, id);
			
			ResultSet rs=pStmt.executeQuery();
			
			if(rs.next()) {
				String spec_id = rs.getString("SPEC_ID");
				String name=rs.getString("NAME");
				String material = rs.getString("MATERIAL");
				String shape = rs.getString("SHAPE");
				String color = rs.getString("COLOR");
				String note = rs.getString("NOTE");
				int stock=rs.getInt("STOCK");
				Date last_update=rs.getDate("LAST_UPDATE");
				
				parts =new Parts(id,spec_id,name,material,
						shape,color,note,stock,last_update);
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return parts;
	}
	
	//レコードの追加
	public boolean create(Parts parts) {
		//データベース接続
		try(Connection conn = DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
		//INSERT文の準備
		String sql = "INSERT INTO PARTS "
				+ "(ID,NAME,SPEC_NUM,MATERIAL,COLOR,SHAPE,NOTE,STOCK,LAST_UPDATE)"
				+ "VALUES(?,?,?,?,?,?,?,?,?)";
		PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, parts.getId());
			pStmt.setString(2, parts.getName());
			pStmt.setString(3, parts.getSpec_id());
			pStmt.setString(4, parts.getMaterial());
			pStmt.setString(5, parts.getColor());
			pStmt.setString(6, parts.getShape());
			pStmt.setString(7, parts.getNote());
			pStmt.setInt(8, parts.getStock());
			pStmt.setDate(9, parts.getLast_Update());
		
		int result = pStmt.executeUpdate();
		
		if(result != 1) {
			return false;
		}
	}catch (SQLException e) {
		e.printStackTrace();
		return false;
	}
	return false;
}	
public String setParts(Parts parts){
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="INSERT INTO PARTS (ID,SPEC_ID,NAME,MATERIAL,SHAPE,COLOR,NOTE,STOCK,LAST_UPDATED) VALUES(?,?,?,?,?,?,?,?,?)";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, parts.getId());
			pStmt.setString(2, parts.getSpec_id());
			pStmt.setString(3, parts.getName());
			pStmt.setString(4, parts.getMaterial());
			pStmt.setString(5, parts.getShape());
			pStmt.setString(6, parts.getColor());
			pStmt.setString(7, parts.getNote());
			pStmt.setInt(8, parts.getStock());
			pStmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}return "登録完了しました{ID:"+parts.getId()+"SPEC_ID"+parts.getSpec_id()+"NAME"+parts.getName();
	}
		
	
}







/*

package dao;

//PARTSテーブル(部品情報)のDAO 修正済み 以下目次


・全レコードを取得

・NAME(部品名)で検索 ※「search」を含むもの

・ID(部品ID)で検索し、STOCK(在庫数)を表示

・伝票情報を反映し、STOCK(在庫数)を更新

・SPEC_ID(仕様ID)とCOLOR(色)で検索し、※ProductsDAOから取得
	製品に必要なパーツと在庫を表示 
	
・レコードの追加




import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Parts;
import model.Voucher;

public class PartsDAO {
	
	//接続情報
	private final String JDBC_URL="jdbc:h2:tcp://localhost/~/stock";
	private final String DB_USER="sa";
	private final String DB_PASS="";
	
	//全レコードを取得
	public List<Parts> getParts(){
		
		List<Parts>partsList=new ArrayList<>();
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM PARTS ";
			PreparedStatement pStmt=conn.prepareStatement(sql);

			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id=rs.getString("ID");
				String spec_id = rs.getString("SPEC_ID");
				String name=rs.getString("NAME");
				String material = rs.getString("MATERIAL");
				String shape = rs.getString("SHAPE");
				String color = rs.getString("COLOR");
				String note = rs.getString("NOTE");
				int stock=rs.getInt("STOCK");
				Date last_update=rs.getDate("LAST_UPDATE");
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				Parts parts=new Parts(id,spec_id,name,material,
						shape,color,note,stock,last_update);
				//リストに格納
				partsList.add(parts);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return partsList;
	}
	
	//NAME(部品名)で検索 ※「search」を含むもの
	public List<Parts> getParts(String search){
		List<Parts>partsList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			
			String sql="SELECT * FROM PARTS WHERE NAME LIKE ? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, search);
			
			//OR ID=? OR STOCK=?
			//pStmt.setString(2, "%"+search+"%");
			//pStmt.setString(3, "%"+search+"%");
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id=rs.getString("ID");
				String spec_id = rs.getString("SPEC_ID");
				String name=rs.getString("NAME");
				String material = rs.getString("MATERIAL");
				String shape = rs.getString("SHAPE");
				String color = rs.getString("COLOR");
				String note = rs.getString("NOTE");
				int stock=rs.getInt("STOCK");
				Date last_update=rs.getDate("LAST_UPDATE");
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				Parts parts=new Parts(id,spec_id,name,material,
						shape,color,note,stock,last_update);
				
				//リストに格納
				partsList.add(parts);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return partsList;
	}
	
	//ID(部品ID)で検索し、STOCK(在庫数)を表示
	public int getPartsStock(String id){
		
		int stock = 0;
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT QUANTITY FROM PARTS WHERE ID=? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, id);
			
			//OR ID=? OR STOCK=?
			//pStmt.setString(2, "%"+search+"%");
			//pStmt.setString(3, "%"+search+"%");
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				stock=rs.getInt("STOCK");
			}
			
			System.out.println(stock);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println(stock);
		return stock;
	}
	
	//伝票情報を反映し、STOCK(在庫数)を更新
	public void setUpdates(List<Voucher>voucherList,Date now){
				
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
					
			//伝票の部品IDで検索し、
			//現在のSTOCK(在庫数)ーISSUE(出庫数)＋RECIEPT(入庫数)
			//LAST_UPDATE(更新日)をnowに更新
			for(Voucher voucher:voucherList) {

				String sql="UPDATE PARTS SET STOCK = STOCK - ? + ?,LAST_UPDATE=? WHERE ID = ? ";
				PreparedStatement pStmt=conn.prepareStatement(sql);
				pStmt.setInt(1,voucher.getIssue());
				pStmt.setInt(2,voucher.getReceipt());
				pStmt.setDate(3, now);
				pStmt.setString(4,voucher.getParts_id());
				
				pStmt.executeUpdate();
			}
			/*
			//PARRTSテーブルの全てのLAST_UPDATE(更新日)を更新
			String sql2="UPDATE PARTS SET LAST_UPDATE=?";
			PreparedStatement pStmt=conn.prepareStatement(sql2);
			pStmt.setDate(1, now);pStmt.executeUpdate();
			

		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public void setUpdates(Date now){
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
					
			//在庫の最終更新日変更
			String sql="UPDATE PARTS SET LAST_UPDATE=?";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setDate(1, now);pStmt.executeUpdate();

		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	

	//SPEC_ID(仕様ID)とCOLOR(色)で検索し、※ProductsDAOから取得
	//製品に必要なパーツの在庫を表示
	public List<Parts> getParts(String spec_id,String color){
		
		List<Parts>partsList=new ArrayList<>();
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM PARTS WHERE SPEC_ID = ? AND COLOR = ?";
			PreparedStatement pStmt=conn.prepareStatement(sql);

			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id=rs.getString("ID");
				String name=rs.getString("NAME");
				String material = rs.getString("MATERIAL");
				String shape = rs.getString("SHAPE");
				String note = rs.getString("NOTE");
				int stock=rs.getInt("STOCK");
				Date last_update=rs.getDate("LAST_UPDATE");
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				Parts parts=new Parts(id,spec_id,name,material,
						shape,color,note,stock,last_update);
				//リストに格納
				partsList.add(parts);
			}	
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return partsList;
	}
	
	//レコードの追加
	public boolean create(Parts parts) {
		//データベース接続
		try(Connection conn = DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
		//INSERT文の準備
		String sql = "INSERT INTO PARTS "
				+ "(ID,NAME,SPEC_NUM,MATERIAL,COLOR,SHAPE,NOTE,STOCK,LAST_UPDATE)"
				+ "VALUES(?,?,?,?,?,?,?,?,?)";
		PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, parts.getId());
			pStmt.setString(2, parts.getName());
			pStmt.setString(3, parts.getSpec_id());
			pStmt.setString(4, parts.getMaterial());
			pStmt.setString(5, parts.getColor());
			pStmt.setString(6, parts.getShape());
			pStmt.setString(7, parts.getNote());
			pStmt.setInt(8, parts.getStock());
			pStmt.setDate(9, parts.getLast_Update());
		
		int result = pStmt.executeUpdate();
		
		if(result != 1) {
			return false;
		}
	}catch (SQLException e) {
		e.printStackTrace();
		return false;
	}
	return false;
}	
	 

}

*/


/*　変更前
package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Parts;
import model.Voucher;

public class PartsDAO {
	private final String JDBC_URL="jdbc:h2:tcp://localhost/~/stockManegement";
	private final String DB_USER="sa";
	private final String DB_PASS="";
	public List<Parts> getParts(){
		List<Parts>partsList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM PARTS ";
			PreparedStatement pStmt=conn.prepareStatement(sql);

			
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String name=rs.getString("NAME");
				String id=rs.getString("ID");
				int stock=rs.getInt("QUANTITY");
				Date date=rs.getDate("UPDATE");
				//String img=rs.getString("IMG");
				System.out.println(name);
				Parts parts=new Parts(id,name,stock,date);
				partsList.add(parts);
				
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return partsList;
	}
	public List<Parts> getParts(String search){
		List<Parts>partsList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM PARTS WHERE NAME LIKE ? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, search);
			
			//OR ID=? OR STOCK=?
			//pStmt.setString(2, "%"+search+"%");
			//pStmt.setString(3, "%"+search+"%");
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String name=rs.getString("NAME");
				String id=rs.getString("ID");
				int stock=rs.getInt("QUANTITY");
				Date date=rs.getDate("UPDATE");
				//String img=rs.getString("IMG");
				System.out.println(name);
				Parts parts=new Parts(id,name,stock,date);
				partsList.add(parts);
				
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return partsList;
	}
	
	public int getPartsStock(String id){
		int stock=0;
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT QUANTITY FROM PARTS WHERE ID=? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, id);
			
			//OR ID=? OR STOCK=?
			//pStmt.setString(2, "%"+search+"%");
			//pStmt.setString(3, "%"+search+"%");
			
			ResultSet rs=pStmt.executeQuery();
			while(rs.next()) {
			stock=rs.getInt("QUANTITY");
			}
			System.out.println(stock);

			
		}catch(SQLException e) {
			e.printStackTrace();
			
			
		}
		System.out.println(stock);
		return stock;
		
		
	}
	
	public void setUpdates(List<Voucher>voucherList,Date now){
		
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			
			
			for(Voucher voucher:voucherList) {
			String sql="UPDATE PARTS  SET QUANTITY =SQUANTITY - ? + ?,UPDATE=? WHERE ID = ? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setInt(1,voucher.getIssue());
			pStmt.setInt(2,voucher.getReceipt());
			pStmt.setDate(3, now);
			pStmt.setString(4,voucher.getGoodsId());
			pStmt.executeUpdate();
			System.out.println("now"+now);
			}
			
			String sql2="UPDATE PARTS SET UPDATE=?";
			PreparedStatement pStmt=conn.prepareStatement(sql2);
			pStmt.setDate(1, now);pStmt.executeUpdate();

			
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		
		
	}
	
}
public void setUpdates(List<Voucher>voucherList,Date now){
	
	
	try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
		
		
		for(Voucher voucher:voucherList) {
		String sql="UPDATE PARTS  SET QUANTITY =SQUANTITY - ? + ?,UPDATE=? WHERE ID = ? ";
		PreparedStatement pStmt=conn.prepareStatement(sql);
		pStmt.setInt(1,voucher.getIssue());
		pStmt.setInt(2,voucher.getReceipt());
		pStmt.setDate(3, now);
		pStmt.setString(4,voucher.getGoodsId());
		pStmt.executeUpdate();
		System.out.println("now"+now);
		}
		
		String sql2="UPDATE PARTS SET UPDATE=?";
		PreparedStatement pStmt=conn.prepareStatement(sql2);
		pStmt.setDate(1, now);pStmt.executeUpdate();

		
		
		
	}catch(SQLException e) {
		e.printStackTrace();
		
	}
	
	
}
*/


