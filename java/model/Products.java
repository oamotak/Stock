package model;

//PRODUCTSテーブルの製品情報を格納 13項目 新規作成 

import java.io.Serializable;

public class Products implements Serializable{
	private String id;			//製品ID
	private String name;		//製品名
	private int price;			//製品価格
	private String spec_id;		//仕様ID
	private String parts_name;	//部品名
	private String material;	//素材
	private String shape;		//形
	private int usage;			//使用数
	private String color1;		//カラー展開１
	private String color2;		//カラー展開２
	private String color3;		//カラー展開３
	private String color4;		//カラー展開４
	private String color5;		//カラー展開５

	//コンストラクタ
	public Products() {}
	
	public Products(String id,String name,int price,String spec_id,
			String parts_name,String material,String shape,int usage,
			String color1,String color2,String color3,String color4,String color5) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.spec_id =spec_id;
		this.parts_name = parts_name;
		this.material = material;
		this.shape = shape;
		this.usage = usage;
		this.color1 = color1;
		this.color2 = color2;
		this.color3 = color3;
		this.color4 = color4;
		this.color5 = color5;
	}
	
	//アクセサ　getter
	public String getId() {return this.id;}
	public String getName() {return this.name;}
	public int getPrice() {return this.price;}
	public String getSpec_id() {return this.spec_id;}
	public String getParts_name() {return this.parts_name;}
	public String getMaterial() {return this.material;}
	public String getShape() {return this.shape;}
	public int getUsage() {return this.usage;}
	public String getColor1() {return this.color1;}
	public String getColor2() {return this.color2;}
	public String getColor3() {return this.color3;}
	public String getColor4() {return this.color4;}
	public String getColor5() {return this.color5;}
}
