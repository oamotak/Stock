package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import dao.VoucherDAO;

	public class Test extends java.lang.Thread{
		Parts parts;
		Date sqlDate;
		Map<Parts,List<Integer>>partsCalender;
		Object []o;
		List<Voucher>restockList;
		RestockNoteLogic rnl;
		int count;
		List<Date>dateList;
		public Test(Parts parts,Date sqlDate,Map<Parts,List<Integer>>partsCalender,Object []o,List<Voucher>restockList,RestockNoteLogic rnl) {
			this.parts=parts;this.o=o;this.sqlDate=sqlDate;
			this.partsCalender=partsCalender;
			this.restockList=restockList;
			this.rnl=rnl;
		}
		public void run() {
			
			List<Integer>list=new ArrayList<>();
			sqlDate = new java.sql.Date(new java.util.Date().getTime());
			Calendar c = Calendar.getInstance();
			VoucherDAO vdao=new VoucherDAO();
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
				
				//次の日へ
				sqlDate=future;
			}
			partsCalender.put(parts, list);
			//System.out.println(getName()+"完了");
		}
	}

