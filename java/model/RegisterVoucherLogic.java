package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

//VOUCHERテーブル（伝票情報）にレコードを追加する

import dao.VoucherDAO;

public class RegisterVoucherLogic {
	public String execute(Voucher voucher) {
		VoucherDAO dao = new VoucherDAO();
		boolean bl = dao.create(voucher);
		
		String msg = null; 
		
		if(bl) { msg = "登録しました。"; }
		else { msg="失敗しました。"; }
		
		return msg; 
	}
	
	//予定表からの登録
	public List<String> execute(Schedule schedule,String action) {
		
		//予定表情報取得
		Date date = schedule.getDate();
		String name = schedule.getName();
		String color = schedule.getColor();
		int quantity = schedule.getQuantity();
		
		
		//商品テーブルを名前で検索
		//必要な部品を取得
		ProductsSearchLogic psl = new ProductsSearchLogic();
		List<Products> productsList = new ArrayList<>();
		productsList = psl.execute(name, color);
		
		//仕様IDから部品情報を取得
		PartsSearchLogic psl2 = new PartsSearchLogic();
		List<Parts> partsList = new ArrayList<>();
		partsList = psl2.execute(productsList, color);
		
		//結果表示初期化
		String msg = null;
		List<String> msgList = new ArrayList<>();
		
		//各項目初期化
		int receipt = 0;
		int unit_price = 0;
		int price = 0;
		int defect_rate = 0;
		int issue = 0;
		String type = "出庫";
		String done = "未反映";
		String parts_name = null;
		String parts_id = null;
		Voucher voucher = null;
		
		
		//備考
		String note = name + color +"を"+ quantity + "個生産" ;
		
		//最終更新日の取得
		Date last_update = new Date(new java.util.Date().getTime());
		System.out.println(last_update);
		
		//マイナス変換
		if(action.equals("minus")) {
			quantity = -quantity;
			note = name + color +"を"+ quantity + "個生産予定を取り消し"; 
		}
		
		VoucherDAO dao = new VoucherDAO();
		
		//productslistとpartsListを照合
		//伝票情報入力
		for(Products products : productsList) {
			String spec_id = products.getSpec_id();
				System.out.println(spec_id);
			for(Parts parts : partsList) {
				String spec_id2 = parts.getSpec_id();
					System.out.println(spec_id);
				if(spec_id.equals(spec_id2)) {
					
					//必要数計算
					issue = quantity * products.getUsage();
					parts_id = parts.getId();
					parts_name = parts.getName();
					
					//id(仮)
					java.util.Date now = new java.util.Date();
					String id = String.valueOf(now.getTime());
					System.out.println(id);
					
					voucher = new Voucher(id,date,parts_id,parts_name,issue,receipt,type,note,done,unit_price,price,defect_rate,
							last_update);
					
					boolean bl = dao.create(voucher);
								
					if(bl) { msg = "登録しました。"; }
					else { msg="失敗しました。"; }
					
					msgList.add(msg);
					break;
				}
			}
		}
		return msgList; 
	}
}
