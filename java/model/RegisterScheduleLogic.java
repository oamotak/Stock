package model;

//SCHEDULEテーブル(製造予定表)にレコードを追加する

import dao.ScheduleDAO;


public class RegisterScheduleLogic {
	public String create(Schedule schedule) {
		ScheduleDAO dao = new ScheduleDAO();
		boolean bl = dao.create(schedule);
		
		String msg = null; 
		
		if(bl) { msg = "登録しました。"; }
		else { msg="失敗しました。"; }
		
		return msg; 
	}

	public boolean delete(String schedule_id) {
		ScheduleDAO dao = new ScheduleDAO();
		boolean bl =dao.delete(schedule_id);
		return bl;
	}
}
