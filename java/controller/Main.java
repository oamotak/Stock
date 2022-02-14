
//カレンダーへフォワードji
package controller;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.MyCalendar;
import model.MyCalendarLogic;
import model.Schedule;
import model.ScheduleSearchLogic;
@WebServlet("/main")
public class Main extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//アプリケーションスコープ
		ServletContext application = this.getServletContext();
		
		//リクエストパラメータの取得
		String s_year=request.getParameter("year");
		String s_month=request.getParameter("month");
		
		//MyCalendarLogicインスタンス生成
		MyCalendarLogic logic=new MyCalendarLogic();
		MyCalendar mc=null;
		if(s_year != null && s_month != null) {
			int year =Integer.parseInt(s_year);
			int month=Integer.parseInt(s_month);
			if(month==0) {
				month=12;
				year--;
			}
			if(month==13) {
				month=1;
				year++;
			}
			//年と月のクエリパラメーターが来ている場合にはその年月でカレンダーを生成する
			mc=logic.createMyCalendar(year,month);
		}else {
			//クエリパラメータが来ていないときは実行日時のカレンダーを生成する。
			mc=logic.createMyCalendar();
		}
		
		
		//リクエストスコープに格納
		request.setAttribute("mc", mc);
		
				
		//viewにフォワード
		String ans = request.getParameter("test");
		
		
		//製造予定
		if(ans.equals("A")) {
			
			request.setAttribute("anker", "A");
			
			//Scheduleの全レコードを取得
			Map<Date,List<Schedule>> scheduleList = new TreeMap<>();
			ScheduleSearchLogic ssl = new ScheduleSearchLogic();
			scheduleList = ssl.executeGetMap();
			
			//アプリケーションスコープに保存
			application.setAttribute("scheduleList", scheduleList);

			
			RequestDispatcher rd=request.getRequestDispatcher("/WEB-INF/view1/Main.jsp");
			rd.forward(request, response);
		}	
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}