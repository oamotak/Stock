package model;


//VOUCHERテーブルの伝票情報を格納 13項目//修正済み

import java.io.Serializable;
import java.sql.Date;

public class Voucher implements Serializable{
	private String id;			//伝票ID
	private Date date;			//在庫が実際に動く日付
	private String parts_id;	//部品ID
	private String parts_name; //部品名
	private int issue;			//出庫数（使用数）
	private int receipt;		//入庫数
	private String type;		//在庫変動の内容
	private String note;		//説明
	private String done;		//反映済みか否か
	private int unit_price;		//部品単価
	private int price;			//総額（入庫時のみ）
	private double defect_rate;//不良率
	private Date last_update;	//更新日（伝票記入日）
	
	//コンストラクタ
	public Voucher() {}
	public Voucher(String id,Date date,String parts_id,
					String parts_name,int issue,int receipt,
					String type,String note,String done,
					int unit_price,int price,double defect_rate,
					Date last_update) {
		this.id = id;
		this.date = date;
		this.parts_id = parts_id;
		this.parts_name = parts_name;
		this.issue = issue;
		this.receipt=receipt;
		this.type=type;
		this.note = note;
		this.done = done;
		this.unit_price = unit_price;
		this.price = price;
		this.defect_rate = defect_rate;
		this.last_update = last_update;
		
	}
	//アクセサ getter
	public String getId() {return this.id;}
	public Date getDate() {return this.date;}
	public String getParts_id() {return this.parts_id;}
	public String getParts_name() {return this.parts_name;}
	public int getIssue() {return this.issue;}
	public int getReceipt() {return this.receipt;}
	public String getType() {return this.type;}
	public String getNote() {return this.note;}
	public String getDone() {return this.done;}
	public int getUnit_price() {return this.unit_price;}
	public int getPrice() {return this.price;}
	public double getDefect_rate() {return defect_rate;}
	public Date getLast_update() {return this.last_update;}
}



/*
package model;

import java.io.Serializable;
import java.sql.Date;

public class Voucher implements Serializable{
	private String voucherId;
	private Date date;
	private String goodsName;
	private String goodsID;
	private String description;
	private int issue;
	private int receipt;
	//未実装
	private String type;
	private String done;
	
	public Voucher() {}
	public Voucher(String voucherId,Date date,String goodsName,String description,int issue,int receipt,String goodsId,String done,String type) {
		this.voucherId=voucherId;this.date=date;this.goodsName=goodsName;this.description=description;this.issue=issue;this.receipt=receipt;this.goodsID=goodsId;this.done=done;this.type=type;
	}
	public String getVoucherId() {return this.voucherId;}
	public String getGoodsName() {return this.goodsName;}
	public String getDiscription() {return this.description;}
	public Date getDate() {return this.date;}
	public int getIssue() {return this.issue;}
	public int getReceipt() {return this.receipt;}
	public String getGoodsId() {return this.goodsID;}
	public String getType() {return this.type;}
	public String getDone() {return this.done;}

}
*/