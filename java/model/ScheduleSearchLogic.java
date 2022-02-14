package model;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import dao.ScheduleDAO;

public class ScheduleSearchLogic {
	
	//全レコード取得
	public List<Schedule> execute(){
		ScheduleDAO dao = new ScheduleDAO();
		List<Schedule> scheduleList = dao.scheduleALL();
		
		return scheduleList;
	}
	
	//Map作成
	//全日付を取得(SetTreeで受け取り)
	public Map<Date,List<Schedule>> executeGetMap(){
		ScheduleDAO dao = new ScheduleDAO();
		
		//日付を取得
		Set<Date> dateList = new TreeSet<>(); 
		dateList = dao.getDate();
		
		//全レコードを取得
		List<Schedule> scheduleAll = new ArrayList<>();  
		scheduleAll = dao.scheduleALL();
		
		Map<Date,List<Schedule>> scheduleList = new TreeMap<>();
		
		//日付とスケジュールの照合
		for (Date d : dateList) {
			
			scheduleList.put(d, new ArrayList<Schedule>());
			
			for (Schedule s:scheduleAll) {
				Date date = s.getDate();
				
				if(date.equals(d)) {
					scheduleList.get(d).add(s);
					
				}
			}
		}

		return scheduleList;
				
	}
	
	//schedule_idから検索
	public Schedule execute(String schedule_id) {
		ScheduleDAO dao = new ScheduleDAO();
		Schedule schedule = null;
		schedule = dao.Getschedule(schedule_id);
		
		return schedule;
	}
	
}
