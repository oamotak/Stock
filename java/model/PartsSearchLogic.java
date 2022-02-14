package model;
import java.util.ArrayList;
//部品情報検索
import java.util.List;

import dao.PartsDAO;

public class PartsSearchLogic {
	
	//全レコード
	public List<Parts> execute() {
		List<Parts> partsList = new ArrayList<>();
		PartsDAO dao = new PartsDAO();
		partsList = dao.getParts();
		return partsList;
	}
	//ID検索
	public Parts execute(String id) {
		PartsDAO dao = new PartsDAO();
		Parts parts = dao.getParts_inf(id);
		return parts;
	}
	
	//製品から必要な部品を検索
	public List<Parts> execute(List<Products> productsList,String color){
		
		PartsDAO dao = new PartsDAO();
		List<Parts> partsList = new ArrayList<>();
		
		String spec_id = null;
		String msg = null;
		Parts parts = null;
		
		//部品のIDを取得
		for(Products products : productsList) {
			spec_id = products.getSpec_id();
		
		//仕様IDと色で部品検索			
			parts = dao.getParts(spec_id, color);
		
			if(parts != null) {
				partsList.add(parts);
			}else {
				msg ="部品が登録されていません。";
					System.out.println(msg);
			}
		}
		
		return partsList;
	}
}
