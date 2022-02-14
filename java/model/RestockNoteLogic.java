package model;

import java.sql.Date;
import java.util.List;

public class RestockNoteLogic {
	public void excute(Parts parts,Date date,List<Voucher>vlist){
	 
	 int stock=parts.getStock();
	 int receipt;
	 
	 
	 for(Voucher v:vlist) {
		 if(v.getParts_id().equals(parts.getId())) {
			 stock+=v.getReceipt();
		 }
	 }
	 if(stock<0) {
		 receipt=stock*-1;
		 Voucher voucher=new Voucher("auto:"+new java.util.Date().getTime(),date,parts.getId(),parts.getName(),0,receipt,"入庫","自動補充","未反映",0,0,0,date);
		 vlist.add(voucher);
	 }
	}
}
