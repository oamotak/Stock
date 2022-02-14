package model;

//SCHEDULEテーブルの製造予定表の情報を格納 新規作成

import java.io.Serializable;
import java.sql.Date;

public class Schedule implements Serializable{
	private Date date ;		//製造日
	private String id;		//製品ID
	private String name;	//作る製品
	private String color;	//作る色
	private int quantity;	//作る数
	private String schedule_id; //製造予定ID※重複不可
	
	
	//コンストラクタ
	public Schedule() {}
	
	public Schedule(Date date,String id,String name,String color,int quantity,String schedule_id) {
		this.date = date;
		this.id = id;
		this.name = name;
		this.color = color;
		this.quantity= quantity;
		this.schedule_id = schedule_id;
		//this.bool = bool;
	}
	
	
	//アクセサ getter
	public Date getDate() {return date;}
	public String getName() {return name;}
	public String getColor() {return color;}
	public String getId() {return id;}
	public int getQuantity() {return quantity;}
	public String getSchedule_id() {return schedule_id;}
}
