package model;

public class Lineup {
	private String id;
	private String name;
	private String color1;
	private String color2;
	private String color3;
	private String color4;
	private String color5;
	
	//コンストラクタ
	public Lineup() {}
	public Lineup(String id,String name,String color1,String color2,String color3,String color4,String color5) {
		this.id = id;
		this.name = name;
		this.color1 = color1;
		this.color2 = color2;
		this.color3 = color3;
		this.color4 = color4;
		this.color5 = color5;
	}
	
	//アクセサ
	public String getId() {return id;}
	public String getName() {return name;}
	public String getColor1() {return color1;}
	public String getColor2() {return color2;}
	public String getColor3() {return color3;}
	public String getColor4() {return color4;}
	public String getColor5() {return color5;}
	
	
}

	
	