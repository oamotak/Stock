package model;

//PARTSテーブルの部品情報を格納する ９項目 修正済み

import java.io.Serializable;
import java.sql.Date;

public class Parts implements Serializable {
	private String id;			//部品ID
	private String spec_id;		//仕様ID
	private String name;		//部品名
	private String material;	//素材
	private String shape;		//形
	private String color;		//色
	private String note;		//備考
	private int stock;			//在庫
	private Date last_updated;	//更新日
	
	//コンストラクタ
	public Parts() {}
	
	//全項目
	public Parts(String id,String spec_id,String name,
				 String material,String shape,String color,
				 String note,int stock,Date last_updated) {
		this.id = id;
		this.spec_id = spec_id;
		this.name = name;
		this.material = material;
		this.shape = shape;
		this.color = color;
		this.stock=stock;
		this.note = note;
		this.last_updated=last_updated;
	}
	
	//PRODUCTSテーブル検索用
	//部品名・部品ID・仕様ID・素材・形・色
	public Parts(String id,String spec_id,String name,
			 String material,String shape,String color) {
	this.id = id;
	this.spec_id = spec_id;
	this.name = name;
	this.material = material;
	this.shape = shape;
	this.color = color;
	}
	
	//StockCalenderCalc用 在庫数更新
	//部品ID・部品名・在庫数・更新日
	public Parts(String id,String name,int stock,Date last_update) {}
	
	//アクセサ getter
	public String getId() {return this.id;}
	public String getSpec_id() {return spec_id;}
	public String getName() {return this.name;}
	public String getMaterial() {return this.material;}
	public String getShape() {return this.shape;}
	public String getColor() {return this.color;}
	public String getNote() {return this.note;}
	public int getStock() {return this.stock;}
	public Date getLast_Update() {return this.last_updated;}
	
	//アクセサ setter 在庫数を変更する
	public void setStock(int q) {this.stock+=q;}
}

/*
package model;

//PARTSテーブルの部品情報を格納する ９項目 修正済み

import java.io.Serializable;
import java.sql.Date;

public class Parts implements Serializable {
	private String id;			//部品ID
	private String spec_id;		//仕様ID
	private String name;		//部品名
	private String material;	//素材
	private String shape;		//形
	private String color;		//色
	private String note;		//備考
	private int stock;			//在庫
	private Date last_updated;	//更新日
	
	//コンストラクタ
	public Parts() {}
	
	//全項目
	public Parts(String id,String spec_id,String name,
				 String material,String shape,String color,
				 String note,int stock,Date last_updated) {
		this.id = id;
		this.spec_id = spec_id;
		this.name = name;
		this.material = material;
		this.shape = shape;
		this.color = color;
		this.stock=stock;
		this.note = note;
		this.last_updated=last_updated;
	}
	
	//PRODUCTSテーブル検索用
	//部品名・部品ID・仕様ID・素材・形・色
	public Parts(String id,String spec_id,String name,
			 String material,String shape,String color) {
	this.id = id;
	this.spec_id = spec_id;
	this.name = name;
	this.material = material;
	this.shape = shape;
	this.color = color;
	}
	
	//StockCalenderCalc用 在庫数更新
	//部品ID・部品名・在庫数・更新日
	public Parts(String id,String name,int stock,Date last_update) {this.id=id;this.name=name;this.stock=stock;this.last_updated=last_updated;}
	
	//アクセサ getter
	public String getId() {return this.id;}
	public String getSpec_id() {return spec_id;}
	public String getName() {return this.name;}
	public String getMaterial() {return this.material;}
	public String getShape() {return this.shape;}
	public String getColor() {return this.color;}
	public String getNote() {return this.note;}
	public int getStock() {return this.stock;}
	public Date getLast_Update() {return this.last_updated;}
	
	//アクセサ setter 在庫数を変更する
	public void setStock(int q) {this.stock+=q;}
}





/*
package model;

import java.io.Serializable;
import java.sql.Date;

public class Parts implements Serializable {
	private String id;
	private String name;
	private int stock;
	private Date lastUpdated;
	public Parts(String id,String name,int stock,Date lastUpdated) {
		this.id=id;this.name=name;this.stock=stock;this.lastUpdated=lastUpdated;
	}
	public Parts() {}
	
	public String getId() {return this.id;}
	public String getName() {return this.name;}
	public int getStock() {return this.stock;}
	public Date getLastUpdated() {return this.lastUpdated;}
	public void setStock(int q) {this.stock+=q;}
}
*/