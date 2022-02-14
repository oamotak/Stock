package dao;

//VOUCHERテーブル(伝票情報)のDAO 修正済み 以下目次

/*
・全レコードの取得

・PARTS_ID(部品ID)で検索 （完全一致）

・DATE(日付(在庫が動く日))で検索
	指定した日付までのものでかつ、在庫「未反映」の伝票を表示し、
	在庫「未反映」を「反映済」に変更する

・PARTS_ID(部品ID)、かつ在庫「未反映」で検索

・DATE(日付(在庫が動く日))で検索
	now～futureの期間で検索

・DATE(日付(在庫が動く日))と部品IDで検索
	now～futureの期間のｓ(部品ID)の伝票
	 
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

import model.Voucher;

public class VoucherDAO {
	//接続情報
	private final String JDBC_URL="jdbc:h2:tcp://localhost/~/stock";
	private final String DB_USER="sa";
	private final String DB_PASS="";
	
	//全レコードの取得
	public List<Voucher> getVoucher(){
		List<Voucher>voucherList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM VOUCHER ORDER BY DATE ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id = rs.getString("ID");
				Date date = rs.getDate("DATE");
				String parts_id = rs.getString("PARTS_ID");
				String parts_name = rs.getString("PARTS_NAME");
				int issue = rs.getInt("ISSUE");
				int receipt = rs.getInt("RECEIPT");;
				String type = rs.getString("TYPE");
				String note = rs.getString("NOTE");
				String done = rs.getString("DONE");
				int unit_price = rs.getInt("UNIT_PRICE");
				int price = rs.getInt("PRICE");
				double defect_rate = rs.getDouble("DEFECT_RATE");
				Date last_update=rs.getDate("LAST_UPDATE");
				
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				//Voucherインスタンスに格納
				Voucher voucher=new Voucher(id,date,parts_id,parts_name,
						issue,receipt,type,note,done,unit_price,price,
						defect_rate,last_update);
				
				//Listに追加
				voucherList.add(voucher);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return voucherList;
	}
	
	//PARTS_ID(部品ID)で検索 （完全一致）
	public List<Voucher> getVoucher(String parts_id){
		List<Voucher>voucherList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM VOUCHER WHERE PARTS_ID=? ORDER BY DATE";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, parts_id);
			
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id = rs.getString("ID");
				Date date = rs.getDate("DATE");
				String parts_name = rs.getString("PARTS_NAME");
				int issue = rs.getInt("ISSUE");
				int receipt = rs.getInt("RECEIPT");
				String type = rs.getString("TYPE");
				String note = rs.getString("NOTE");
				String done = rs.getString("DONE");
				int unit_price = rs.getInt("UNIT_PRICE");
				int price = rs.getInt("PRICE");
				double defect_rate = rs.getDouble("DEFECT_RATE");
				Date last_update=rs.getDate("LAST_UPDATE");
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				//Voucherインスタンスに格納
				Voucher voucher=new Voucher(id,date,parts_id,parts_name,
						issue,receipt,type,note,done,unit_price,price,
						defect_rate,last_update);
				//Listに追加
				voucherList.add(voucher);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return voucherList;
	}
	
	//DATE(日付(在庫が動く日))で検索
	//指定した日付までのものでかつ、在庫「未反映」の伝票を表示し、
	//在庫「未反映」を「反映済」に変更するメソッド
	public List<Voucher> getVoucher(Date now){
		List<Voucher>voucherList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
						
			String sql="SELECT * FROM VOUCHER WHERE DATE<=? AND DONE='未反映' ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setDate(1, now);
			
			//検索にヒットしたレコードの
			//在庫「未反映」を「反映済」に変更するSQL文
			String sqlDone="UPDATE VOUCHER SET DONE='反映済' WHERE ID= ? ";
			PreparedStatement pStmtDone=conn.prepareStatement(sqlDone);
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id = rs.getString("ID");
				Date date = rs.getDate("DATE");
				String parts_id = rs.getString("PARTS_ID");
				String parts_name = rs.getString("PARTS_NAME");
				int issue = rs.getInt("ISSUE");
				int receipt = rs.getInt("RECEIPT");
				String type = rs.getString("TYPE");
				String note = rs.getString("NOTE");
				String done = rs.getString("DONE");
				int unit_price = rs.getInt("UNIT_PRICE");
				int price = rs.getInt("PRICE");
				double defect_rate = rs.getDouble("DEFECT_RATE");
				Date last_update=rs.getDate("LAST_UPDATE");
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				//Voucherインスタンスに格納
				Voucher voucher=new Voucher(id,date,parts_id,parts_name,
						issue,receipt,type,note,done,unit_price,price,
						defect_rate,last_update);
				//Listに追加
				voucherList.add(voucher);
				
				//伝票IDで検索し、
				//「未反映」を「反映済」に変更するSQL文を実行
				pStmtDone.setString(1,id);
				pStmtDone.executeUpdate();
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}	
		return voucherList;
	}
	
	//PARTS_ID(部品ID)、かつ在庫「未反映」で検索
	public List<Voucher> getUndoneVoucher(String parts_id){
		List<Voucher>voucherList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			
			String sql="SELECT * FROM VOUCHER WHERE PARTS_ID=? AND DONE='未反映' ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1,parts_id );
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id = rs.getString("ID");
				Date date = rs.getDate("DATE");
				String parts_name = rs.getString("PARTS_NAME");
				int issue = rs.getInt("ISSUE");
				int receipt = rs.getInt("RECEIPT");
				String type = rs.getString("TYPE");
				String note = rs.getString("NOTE");
				String done = rs.getString("DONE");
				int unit_price = rs.getInt("UNIT_PRICE");
				int price = rs.getInt("PRICE");
				double defect_rate = rs.getDouble("DEFECT_RATE");
				Date last_update=rs.getDate("LAST_UPDATE");
				
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				//Voucherインスタンスに格納
				Voucher voucher=new Voucher(id,date,parts_id,parts_name,
						issue,receipt,type,note,done,unit_price,price,
						defect_rate,last_update);
				
				//Listに追加
				voucherList.add(voucher);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return voucherList;
	}
	
	//PARTS_ID(部品ID)、日にち、かつ在庫「未反映」で検索
		public List<Voucher> getUndoneVoucher(String parts_id,Date now,Date future){
			List<Voucher>voucherList=new ArrayList<>();
			try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
				
				String sql="SELECT * FROM VOUCHER WHERE PARTS_ID=? AND DONE='未反映' AND DATE > ? AND DATE <= ? ";
				PreparedStatement pStmt=conn.prepareStatement(sql);
				pStmt.setString(1,parts_id );
				pStmt.setDate(2, now);
				pStmt.setDate(3, future);
				
				ResultSet rs=pStmt.executeQuery();
				
				while(rs.next()) {
					String id = rs.getString("ID");
					Date date = rs.getDate("DATE");
					String parts_name = rs.getString("PARTS_NAME");
					int issue = rs.getInt("ISSUE");
					int receipt = rs.getInt("RECEIPT");
					String type = rs.getString("TYPE");
					String note = rs.getString("NOTE");
					String done = rs.getString("DONE");
					int unit_price = rs.getInt("UNIT_PRICE");
					int price = rs.getInt("PRICE");
					double defect_rate = rs.getDouble("DEFECT_RATE");
					Date last_update=rs.getDate("LAST_UPDATE");
					
					//String img=rs.getString("IMG");
					//System.out.println(name);
					
					//Voucherインスタンスに格納
					Voucher voucher=new Voucher(id,date,parts_id,parts_name,
							issue,receipt,type,note,done,unit_price,price,
							defect_rate,last_update);
					
					//Listに追加
					voucherList.add(voucher);
				}
			}catch(SQLException e) {
				e.printStackTrace();
				return null;
			}
			return voucherList;
		}
	
	//DATE(日付(在庫が動く日))で検索
	//now～futureの期間で検索
	public List<Voucher> getVoucher(Date now,Date future){
		List<Voucher>voucherList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM VOUCHER WHERE DATE > ? AND DATE <= ?";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setDate(1, now);
			pStmt.setDate(2, future);
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id = rs.getString("ID");
				Date date = rs.getDate("DATE");
				String parts_id = rs.getString("PARTS_ID");
				String parts_name = rs.getString("PARTS_NAME");
				int issue = rs.getInt("ISSUE");
				int receipt = rs.getInt("RECEIPT");
				String type = rs.getString("TYPE");
				String note = rs.getString("NOTE");
				String done = rs.getString("DONE");
				int unit_price = rs.getInt("UNIT_PRICE");
				int price = rs.getInt("PRICE");
				double defect_rate = rs.getDouble("DEFECT_RATE");
				Date last_update=rs.getDate("LAST_UPDATE");
				
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				//Voucherインスタンスに格納
				Voucher voucher=new Voucher(id,date,parts_id,parts_name,
						issue,receipt,type,note,done,unit_price,price,
						defect_rate,last_update);
				
				//Listに追加
				voucherList.add(voucher);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return voucherList;
	}
	//DATE(日付(在庫が動く日))と部品IDで検索
	//now～futureの期間のｓ(部品ID)の伝票
	public List<Voucher> getVoucher(Date now,Date future,String s){
		List<Voucher>voucherList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM VOUCHER WHERE DATE > ? AND DATE <= ? AND PARTS_ID=? ORDER BY DATE";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setDate(1, now);
			pStmt.setDate(2, future);
			pStmt.setString(3, s);
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id=rs.getString("ID");
				Date date=rs.getDate("DATE");
				String parts_id=rs.getString("PARTS_ID");
				String parts_name=rs.getString("PARTS_NAME");
				int issue=rs.getInt("ISSUE");
				int receipt=rs.getInt("RECEIPT");
				String type=rs.getString("TYPE");
				String note=rs.getString("NOTE");
				String done=rs.getString("DONE");
				int unit_price = rs.getInt("UNIT_PRICE");
				int price = rs.getInt("PRICE");
				double defect_rate = rs.getDouble("DEFECT_RATE");
				Date last_update = rs.getDate("LAST_UPDATE");
				
				Voucher voucher = new Voucher(id,date,parts_id,parts_name,
						issue,receipt,type,note,done,unit_price,price,
						defect_rate,last_update);
				
				voucherList.add(voucher);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return voucherList;
	}

	//レコードの追加
	public boolean create(Voucher voucher) {
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="INSERT INTO VOUCHER (ID,DATE,PARTS_ID,PARTS_NAME,ISSUE,"
					+ "RECEIPT,TYPE,NOTE,DONE,UNIT_PRICE,PRICE,DEFECT_RATE,LAST_UPDATE)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);
					pStmt.setString(1,voucher.getId());
					pStmt.setDate(2,voucher.getDate());
					pStmt.setString(3,voucher.getParts_id());
					pStmt.setString(4,voucher.getParts_name());
					pStmt.setInt(5,voucher.getIssue());
					pStmt.setInt(6,voucher.getReceipt());
					pStmt.setString(7,voucher.getType());
					pStmt.setString(8,voucher.getNote());
					pStmt.setString(9,voucher.getDone());
					pStmt.setInt(10,voucher.getUnit_price());
					pStmt.setInt(11,voucher.getPrice());
					pStmt.setDouble(12,voucher.getDefect_rate());
					pStmt.setDate(13,voucher.getLast_update());
					
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



/*
package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Voucher;

public class VoucherDAO {
	private final String JDBC_URL="jdbc:h2:tcp://localhost/~/stockManegement";
	private final String DB_USER="sa";
	private final String DB_PASS="";
	public List<Voucher> getVoucher(){
		List<Voucher>voucherList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM VOUCHER  ORDER BY DATE";
			PreparedStatement pStmt=conn.prepareStatement(sql);

			
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id=rs.getString("ID");
				Date date=rs.getDate("DATE");
				String goodsName=rs.getString("GOODS_NAME");
				int issue=rs.getInt("ISSUE");
				int receipt=rs.getInt("RECEIPT");
				String description=rs.getString("DESCRIPTION");
				String goodsId=rs.getString("GOODS_ID");
				String done=rs.getString("DONE");
				String type=rs.getString("TYPE");
				//String img=rs.getString("IMG");
				//System.out.println(name);
				Voucher voucher=new Voucher(id,date,goodsName,description,issue, receipt,goodsId,done,type);
				voucherList.add(voucher);
				
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return voucherList;
	}
	public List<Voucher> getVoucher(String id){
		List<Voucher>voucherList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM VOUCHER WHERE GOODS_ID=? ORDER BY DATE";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, id);
			
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String voucherId=rs.getString("ID");
				Date date=rs.getDate("DATE");
				String goodsName=rs.getString("GOODS_NAME");
				int issue=rs.getInt("ISSUE");
				int receipt=rs.getInt("RECEIPT");
				String description=rs.getString("DESCRIPTION");
				String goodsId=rs.getString("GOODS_ID");
				String done=rs.getString("DONE");
				String type=rs.getString("TYPE");
				//String img=rs.getString("IMG");
				//System.out.println(name);
				Voucher voucher=new Voucher(voucherId,date,goodsName,description,issue, receipt,goodsId,done,type);
				voucherList.add(voucher);
				
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return voucherList;
	}
	
	public List<Voucher> getVoucher(Date now){
		List<Voucher>voucherList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM VOUCHER WHERE DATE<=? AND DONE='FALSE' ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setDate(1, now);
			
			String sqlDone="UPDATE VOUCHER SET DONE='TRUE' WHERE ID= ? ";
			PreparedStatement pStmtDone=conn.prepareStatement(sqlDone);
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id=rs.getString("ID");
				Date date=rs.getDate("DATE");
				String goodsName=rs.getString("GOODS_NAME");
				int issue=rs.getInt("ISSUE");
				int receipt=rs.getInt("RECEIPT");
				String description=rs.getString("DESCRIPTION");
				String goodsId=rs.getString("GOODS_ID");
				String done=rs.getString("DONE");
				String type=rs.getString("TYPE");
				Voucher voucher=new Voucher(id,date,goodsName,description,issue, receipt,goodsId,done,type);
				voucherList.add(voucher);
				
				
				//DONE=TRUE 在庫反映済み
				pStmtDone.setString(1,id);
				pStmtDone.executeUpdate();
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return voucherList;
	}
	
	
	
	
	public List<Voucher> getUndoneVoucher(String id){
		List<Voucher>voucherList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM VOUCHER WHERE GOODS_ID=? AND DONE='FALSE' ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1,id );
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String voucherId=rs.getString("ID");
				Date date=rs.getDate("DATE");
				String goodsName=rs.getString("GOODS_NAME");
				int issue=rs.getInt("ISSUE");
				int receipt=rs.getInt("RECEIPT");
				String description=rs.getString("DESCRIPTION");
				String goodsId=rs.getString("GOODS_ID");
				String done=rs.getString("DONE");
				String type=rs.getString("TYPE");
				Voucher voucher=new Voucher(voucherId,date,goodsName,description,issue, receipt,goodsId,done,type);
				voucherList.add(voucher);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return voucherList;
	}
	
	public List<Voucher> getVoucher(Date now,Date future){
		List<Voucher>voucherList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM VOUCHER WHERE DATE > ? AND DATE <= ? ORDER BY DATE";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setDate(1, now);
			pStmt.setDate(2, future);
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id=rs.getString("ID");
				Date date=rs.getDate("DATE");
				String goodsName=rs.getString("GOODS_NAME");
				int issue=rs.getInt("ISSUE");
				int receipt=rs.getInt("RECEIPT");
				String description=rs.getString("DESCRIPTION");
				String goodsId=rs.getString("GOODS_ID");
				String done=rs.getString("DONE");
				String type=rs.getString("TYPE");
				Voucher voucher=new Voucher(id,date,goodsName,description,issue, receipt,goodsId,done,type);
				voucherList.add(voucher);
				
			
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return voucherList;
	}
	public List<Voucher> getVoucher(Date now,Date future,String s){
		List<Voucher>voucherList=new ArrayList<>();
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM VOUCHER WHERE DATE > ? AND DATE <= ? AND GOODS_ID=? ORDER BY DATE";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setDate(1, now);
			pStmt.setDate(2, future);
			pStmt.setString(3, s);
			
			ResultSet rs=pStmt.executeQuery();
			
			while(rs.next()) {
				String id=rs.getString("ID");
				Date date=rs.getDate("DATE");
				String goodsName=rs.getString("GOODS_NAME");
				int issue=rs.getInt("ISSUE");
				int receipt=rs.getInt("RECEIPT");
				String description=rs.getString("DESCRIPTION");
				String goodsId=rs.getString("GOODS_ID");
				String done=rs.getString("DONE");
				String type=rs.getString("TYPE");
				Voucher voucher=new Voucher(id,date,goodsName,description,issue, receipt,goodsId,done,type);
				voucherList.add(voucher);
				
			
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return voucherList;
	}

}
*/