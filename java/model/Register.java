package model;

import dao.PartsDAO;
import dao.ScheduleDAO;

public class Register {
	
	//PARTSテーブル(部品情報)にレコードを追加する
	public boolean parts(Parts parts) {
		PartsDAO dao = new PartsDAO();
		boolean bo = dao.create(parts); 
		return bo;
	}
	
	//SCHEDULEテーブル(製造予定表)にレコードを追加する
	public boolean scedule(Schedule schedule) {
		ScheduleDAO dao = new ScheduleDAO();
		boolean bo = dao.create(schedule);
		return bo;
	}
}
