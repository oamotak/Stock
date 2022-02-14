package model;

//PartsDAOを実行するクラス 以下目次
/*
 ・ 在庫数の更新
 ・全レコードの取得
 ・部品名に「seach」を含むものを検索
 */

import java.util.List;

import dao.PartsDAO;
import dao.VoucherDAO;

public class CurrentStockCalc {
	
	//在庫数の更新
	public void execute(){
		//今日までの在庫未反映伝票所得
		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
		VoucherDAO vdao = new VoucherDAO();
		List<Voucher>voucherList = vdao.getVoucher(sqlDate);
		//現在時刻までのDone＝'未反映'伝票を在庫に更新させる
		PartsDAO pdao=new PartsDAO();
		if(voucherList!=null) {
		pdao.setUpdates(voucherList, sqlDate);
		}
		//pdao.setUpdates(sqlDate);
	}
	
	//全レコードの取得
	public List<Parts> getStock() {
		PartsDAO dao=new PartsDAO();
		List<Parts>partsList=dao.getParts();
		return partsList;
	}
	
	//部品名に「seach」を含むものを検索
	public List<Parts> getStock(String search) {
		PartsDAO dao=new PartsDAO();
		List<Parts>partsList=dao.getParts("%"+search+"%");
		return partsList;
	}
}


