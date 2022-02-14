package dao;

//PRODUCTSテーブルのDAO 新規作成 以下目次

/*
・全レコードの取得

・NAME(製品名)から必要な部品を検索

・部品情報からその部品を使用する製品を検索
	※部品情報＝部品名・部品ID・仕様ID・素材・形・色
	
・レコードの追加
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Parts;
import model.Products;

public class ProductsDAO {
	//接続情報
	private final String JDBC_URL="jdbc:h2:tcp://localhost/~/stock";
	private final String DB_USER="sa";
	private final String DB_PASS="";
	
	//全レコードの取得
	public List<Products> getProducts(){
		List<Products> productsList = new ArrayList<>();
			
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM PRODUCTS";
			PreparedStatement pStmt=conn.prepareStatement(sql);
				
			ResultSet rs=pStmt.executeQuery();
				
			while(rs.next()) {
				String id = rs.getString("ID");
				String name = rs.getString("NAME");
				int price = rs.getInt("PRICE");
				String spec_id = rs.getString("SPEC_ID");
				String parts_name = rs.getString("PARTS_NAME");
				String material = rs.getString("MATERIAL");
				String shape = rs.getString("SHAPE");
				int usage = rs.getInt("USAGE");
				String color1 = rs.getString("COLOR1");	
				String color2 = rs.getString("COLOR2");
				String color3 = rs.getString("COLOR3");
				String color4 = rs.getString("COLOR4");
				String color5 = rs.getString("COLOR5");
				
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				//Productsインスタンスに格納
				Products products = new Products(id,name,price,
						spec_id,parts_name,material,shape,usage,
						color1,color2,color3,color4,color5);
					
				//Listに追加
				productsList.add(products);
			}
		}catch(SQLException e) {
				e.printStackTrace();
				return null;
	}
		return productsList;
	}
	
	//NAME(製品名)から必要な部品を検索
	public List<Products> getParts(String name,String color){
		List<Products> productsList = new ArrayList<>();
			
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM PRODUCTS WHERE NAME = ? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
			pStmt.setString(1, name);
			
			ResultSet rs=pStmt.executeQuery();
				
			while(rs.next()) {
				String id = rs.getString("ID");
				int price = rs.getInt("PRICE");
				String spec_id = rs.getString("SPEC_ID");
				String parts_name = rs.getString("PARTS_NAME");
				String material = rs.getString("MATERIAL");
				String shape = rs.getString("SHAPE");
				int usage = rs.getInt("USAGE");
				String color1 = rs.getString("COLOR1");	
				String color2 = rs.getString("COLOR2");
				String color3 = rs.getString("COLOR3");
				String color4 = rs.getString("COLOR4");
				String color5 = rs.getString("COLOR5");
				
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				//Productsインスタンスに格納
				Products products = new Products(id,name,price,
						spec_id,parts_name,material,shape,usage,
						color1,color2,color3,color4,color5);
					
				//Listに追加
				productsList.add(products);
			}
		}catch(SQLException e) {
				e.printStackTrace();
				return null;
	}
		return productsList;
	}
	
	//部品情報からその部品を使用する製品を検索
	//部品情報＝部品名・部品ID・仕様ID・素材・形・色
	public List<Products> getProducts(Parts parts){
		List<Products> productsList = new ArrayList<>();
			String parts_name = parts.getName();
			String spec_id = parts.getSpec_id();
			String material = parts.getMaterial();
			String shape = parts.getShape();
			String color = parts.getColor();
		
		try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
			String sql="SELECT * FROM PRODUCTS WHERE PARTS_NAME = ? AND SPEC_ID = ?"
					+ "AND MATERIAL = ? AND SHAPE = ? "
					+ "AND COLOR1 = ? OR COLOR2 = ? OR COLOR3 = ? OR COLOR4 = ? OR COLOR5 = ? ";
			PreparedStatement pStmt=conn.prepareStatement(sql);
				pStmt.setString(1, parts_name);
				pStmt.setString(2, spec_id);
				pStmt.setString(3, material);
				pStmt.setString(4, shape);
				pStmt.setString(5, color);
				pStmt.setString(6, color);
				pStmt.setString(7, color);
				pStmt.setString(8, color);
				pStmt.setString(9, color);
			
			ResultSet rs=pStmt.executeQuery();
				
			while(rs.next()) {
				String id = rs.getString("ID");
				String name = rs.getString("NAME");
				int price = rs.getInt("PRICE");
				int usage = rs.getInt("USAGE");
				String color1 = rs.getString("COLOR1");	
				String color2 = rs.getString("COLOR2");
				String color3 = rs.getString("COLOR3");
				String color4 = rs.getString("COLOR4");
				String color5 = rs.getString("COLOR5");
				
				//String img=rs.getString("IMG");
				//System.out.println(name);
				
				//Productsインスタンスに格納
				Products products = new Products(id,name,price,
						spec_id,parts_name,material,shape,usage,
						color1,color2,color3,color4,color5);
					
				//Listに追加
				productsList.add(products);
			}
		}catch(SQLException e) {
				e.printStackTrace();
				return null;
	}
		return productsList;
	}
	//レコードの追加
		public boolean create(Products products) {
			
			try(Connection conn=DriverManager.getConnection(JDBC_URL,DB_USER,DB_PASS)){
				String sql="INSERT INTO PRODUCTS (ID,NAME,PRICE,SPEC_ID,PARTS_NAME,MATERIAL,"
						+ "SHAPE,USAGE,COLOR1,COLOR2,COLOR3,COLOR4,COLOR5)"
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement pStmt = conn.prepareStatement(sql);
						pStmt.setString(1,products.getId());
						pStmt.setString(2,products.getName());
						pStmt.setInt(3,products.getPrice());
						pStmt.setString(4,products.getSpec_id());
						pStmt.setString(5,products.getParts_name());
						pStmt.setString(6,products.getMaterial());
						pStmt.setString(7,products.getShape());
						pStmt.setInt(8,products.getUsage());
						pStmt.setString(9,products.getColor1());
						pStmt.setString(10,products.getColor2());
						pStmt.setString(11,products.getColor3());
						pStmt.setString(12,products.getColor4());
						pStmt.setString(13,products.getColor5());
						
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