package model;




	//PARTSテーブルのSTOCK(在庫数)を更新し、部品在庫カレンダーの取得 修正済み

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

	public class Tests {
		
		public Tests() {}
		public Object[] getStockCalender(){
			
			
			List<Date>dateList=new ArrayList<>();
			//dateList=Collections.synchronizedList(dateList);
			Map<Parts,List<Integer>>partsCalender=new LinkedHashMap<>();
			Object o[]=new Object[3];
			partsCalender=Collections.synchronizedMap(partsCalender);
			//在庫数更新し、全レコードの取得
			CurrentStockCalc csc=new CurrentStockCalc();
			csc.execute();
			List<Parts>partsList=csc.getStock();
			java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
			
			//補充教える
			RestockNoteLogic rnl=new RestockNoteLogic();
			List<Voucher>restockList=new ArrayList<>();
			restockList=Collections.synchronizedList(restockList);
			
			
			
			//部品一つづつ
			if(partsList!=null) {
				Thread th = null;
				try {
					Thread th2=new Test2(sqlDate,dateList);
					th2.start();
					for(Parts parts:partsList) {
						th=new model.Test(parts,sqlDate,partsCalender,o,restockList,rnl);
						th.start();
						
					}
					while(partsCalender.size()<partsList.size()) {
						Thread.sleep(10);
					}
					th2.join();
				} catch (InterruptedException e) {
					// 例外処理
					e.printStackTrace();
				}
				o[0]=dateList;o[1]=partsCalender;o[2]=restockList;
				return o;
			}else {
				return null;
			}
		}
	}


