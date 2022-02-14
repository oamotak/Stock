package model;

//PARTSテーブルのSTOCK(在庫数)を更新し、部品在庫カレンダーの取得 修正済み

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dao.VoucherDAO;

public class StockCalenderCalc {

	public Object[] getStockCalender(){
		
		
		List<Date>dateList=new ArrayList<>();
		Map<Parts,List<Integer>>partsCalender=new LinkedHashMap<>();
		Object o[]=new Object[3];
	
		//在庫数更新し、全レコードの取得
		CurrentStockCalc csc=new CurrentStockCalc();
		csc.execute();
		List<Parts>partsList=csc.getStock();
		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
		VoucherDAO vdao=new VoucherDAO();
		//補充教える
		RestockNoteLogic rnl=new RestockNoteLogic();
		List<Voucher>restockList=new ArrayList<>();
		
		
		//部品一つづつ
		if(partsList!=null) {
			for(Parts parts:partsList) {
				//1日おきのある部品の在庫数,sqldate初期化
				List<Integer>list=new ArrayList<>();
				sqlDate = new java.sql.Date(new java.util.Date().getTime());
				Calendar c = Calendar.getInstance();
				for(int i=0;i<=30;i++) {
					//一日おきに調べる
					c.setTime(sqlDate);
					c.add(Calendar.DATE, 1);
					Date future=new java.sql.Date(c.getTimeInMillis());
				
					//現在の在庫入れ込み（最後の1日は入らない）
					list.add(parts.getStock());
					
					//在庫足りなければrnlへ
					if(parts.getStock()<0) {rnl.excute(parts, sqlDate,restockList);}
				
					//次の日までの伝票所得 iｆあれば計算
					List<Voucher>vlist=vdao.getUndoneVoucher( parts.getId(),sqlDate, future);
					if(vlist!=null) {
						for(Voucher v:vlist) {
							parts.setStock(v.getReceipt()-v.getIssue());
						}
					}
					//初回のみ日付のリストつくる
					if(parts==partsList.get(0)) {dateList.add(sqlDate);}
					//次の日へ
					sqlDate=future;
				}
				partsCalender.put(parts, list);
			}
			o[0]=dateList;o[1]=partsCalender;o[2]=restockList;
			return o;
		}else {
			return null;
		}
	}
}

/*

package model;

//PARTSテーブルのSTOCK(在庫数)を更新し、部品在庫カレンダーの取得 修正済み

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dao.VoucherDAO;

public class StockCalenderCalc {

	public Map<Date,List<Parts>> getStockCalender(){
		
		List<Parts> partsList=new ArrayList<>();
		
		Map stockCalender=new LinkedHashMap<Date,List<Parts>>();
		
		//在庫数更新し、全レコードの取得
		CurrentStockCalc csc=new CurrentStockCalc();
		csc.execute();
		partsList=csc.getStock();
		
		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
		VoucherDAO vdao=new VoucherDAO();
		
		//当日の在庫入力
		List<Parts> pl = new ArrayList<>();
		
		//partsList内の全レコードの
		//(部品ID・部品名・在庫数・更新日)をplへ追加
		for(Parts parts:partsList) {
			Parts p = new Parts(parts.getId(),parts.getName(),parts.getStock(),parts.getLast_Update());
			pl.add(p);
		}
		stockCalender.put(sqlDate, pl);
		
		
		
		//次の日以降の在庫変動入力
		int count=0;
		while(count<30) {
			
			Calendar c = Calendar.getInstance();
	        c.setTime(sqlDate);
	        c.add(Calendar.DATE, 1);
	        Date future=new java.sql.Date(c.getTimeInMillis());
	        List<Parts>fp=new ArrayList<>();
	        
	        //次の日の伝票リスト所得
			List<Voucher>voucherList=vdao.getVoucher(sqlDate,future);
			
			//在庫セット
			//伝票情報の取得(部品ID・出庫数・入庫数)
			for(Voucher voucher:voucherList) {
				String parts_id = voucher.getParts_id();
				int issue = voucher.getIssue();
				int receipt = voucher.getReceipt();
				
				//部品IDの取得
				for(Parts parts:partsList) {
					String id = parts.getId();
					
					//IDが一致したら(入庫数ー出庫数)を在庫に足す
					if(id.equals(parts_id)) {
						parts.setStock((receipt-issue));
					}
				}
			}
			
			//partsList内の全レコードの
			//(部品ID・部品名・在庫数・更新日)をfpリストへ追加
			for(Parts parts:partsList) {
				Parts p=new Parts(parts.getId(),parts.getName(),parts.getStock(),parts.getLast_Update());
				fp.add(p);
			}
			
			stockCalender.put(future,fp);
			
			//次の日
			sqlDate=new java.sql.Date(c.getTimeInMillis());
			count++;
		}
		
		return stockCalender;
	}
}
*/
/*
package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dao.VoucherDAO;

public class StockCalenderCalc {

	public Map<Date,List<Parts>> getStockCalender(){
		List<Parts> partsList=new ArrayList<>();
		Map stockCalender=new LinkedHashMap<Date,List<Parts>>();
		
		
		//在庫所得
		CurrentStockCalc csc=new CurrentStockCalc();
		csc.execute();
		partsList=csc.getStock();
		
		
		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
		VoucherDAO vdao=new VoucherDAO();
		
		//当日の在庫入力
		List<Parts>pl=new ArrayList<>();
		for(Parts parts:partsList) {
			Parts p=new Parts(parts.getId(),parts.getName(),parts.getStock(),parts.getLastUpdated());
			pl.add(p);
		}
		stockCalender.put(sqlDate, pl);
		
		
		//次の日以降の在庫変動入力
		int count=0;
		while(count<30) {
			
			Calendar c = Calendar.getInstance();
	        c.setTime(sqlDate);
	        c.add(Calendar.DATE, 1);
	        Date future=new java.sql.Date(c.getTimeInMillis());
	        List<Parts>fp=new ArrayList<>();
	        
	        //次の日の伝票リスト所得
			List<Voucher>voucherList=vdao.getVoucher(sqlDate,future);
			//在庫セット
			for(Voucher voucher:voucherList) {
				String goodsId=voucher.getGoodsId();
				int issue=voucher.getIssue();
				int receipt=voucher.getReceipt();
				
				for(Parts parts:partsList) {
					String partsId=parts.getId();
					if(partsId.equals(goodsId)) {
						parts.setStock((receipt-issue));
					}
				}
			}
			for(Parts parts:partsList) {
				Parts p=new Parts(parts.getId(),parts.getName(),parts.getStock(),parts.getLastUpdated());
				fp.add(p);
			}
			
			stockCalender.put(future,fp);
			//次の日
			sqlDate=new java.sql.Date(c.getTimeInMillis());
			
			System.out.println(sqlDate+"sql");
			System.out.println(future+"+1");
			count++;
		
		}
		
		
		
		return stockCalender;
	}
}
*/