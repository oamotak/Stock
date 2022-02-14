package model;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class Test2 extends Thread{
	Date sqlDate;
	List<Date>dateList;
	
	public void run() {
		for(int i=0;i<=30;i++) {
			Calendar c = Calendar.getInstance();
			dateList.add(sqlDate);
			//一日おきに調べる
			c.setTime(sqlDate);
			c.add(Calendar.DATE, 1);
			Date future=new java.sql.Date(c.getTimeInMillis());
			//次の日へ
			sqlDate=future;
		}
	}
	public Test2(Date sqlDate,List<Date>dateList) {
		this.sqlDate=sqlDate;this.dateList=dateList;
	}
}
