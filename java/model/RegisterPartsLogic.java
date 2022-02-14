package model;

//PARTSテーブル(部品情報)にレコードを追加する

import dao.PartsDAO;

public class  RegisterPartsLogic {
	public boolean execute(Parts parts) {
		PartsDAO dao = new PartsDAO();
		boolean bo = dao.create(parts); 
		return bo;
	}
}
