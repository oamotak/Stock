package model;

//VoucherDAOを実行するクラス

import java.util.Date;
import java.util.List;

import dao.VoucherDAO;

public class CollectVoucherCalc {
	
	//すべて
	public List<Voucher> getVoucher(){
		VoucherDAO dao=new VoucherDAO();
		List<Voucher>voucherList=dao.getVoucher();
		return voucherList;
	}
	//指定id すべて
	public List<Voucher> getVoucher(String id){
		VoucherDAO dao=new VoucherDAO();
		List<Voucher>voucherList=dao.getVoucher(id);
		return voucherList;
	}
	
	//期間指定あり 引数util.date
	public List<Voucher> getVoucher(Date a,Date b){
		java.sql.Date datea= new java.sql.Date(a.getTime());
		java.sql.Date dateb= new java.sql.Date(b.getTime());
		VoucherDAO dao=new VoucherDAO();
		List<Voucher>voucherList=dao.getVoucher(datea,dateb);
		return voucherList;
	}
	
	//期間指定あり 引数util.date id指定
		public List<Voucher> getVoucher(Date a,Date b,String id){
			java.sql.Date datea= new java.sql.Date(a.getTime());
			java.sql.Date dateb= new java.sql.Date(b.getTime());
			VoucherDAO dao=new VoucherDAO();
			List<Voucher>voucherList=dao.getVoucher(datea,dateb,id);
			return voucherList;
		}
}

