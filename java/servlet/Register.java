package servlet;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Lineup;
import model.LineupSearchLogic;
import model.RegisterScheduleLogic;
import model.Schedule;
/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//リクエストパラメータから日付の取得
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		System.out.println(action);
		
		//セッションスコープ
		HttpSession session = request.getSession();

		//Lineupテーブルから全レコードを取得
		LineupSearchLogic lineupSearchLogic = new LineupSearchLogic();
		List<Lineup> lineup = lineupSearchLogic.execute();
		
		//アプリケーションスコープに保存
		ServletContext application = this.getServletContext(); 
		application.setAttribute("lineupList", lineup);
		
		
		if(action == null) {
			
			//リクエストパラメータから日付を取得
			String year = request.getParameter("year");
			String month = request.getParameter("month");
			String day = request.getParameter("day");
			System.out.println(year + "-" + month + "-" + day);
			String date = year + "-" + month + "-" + day;
			
			Date sqlDate= Date.valueOf(date);
			session.setAttribute("date",sqlDate);
			
			//製品選択画面へフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/selectLineup.jsp");
			dispatcher.forward(request, response);
		}else {
		
		if(action.equals("search")) {

			//製品選択画面にフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/selectLineup.jsp");
			dispatcher.forward(request, response);
		}
		
		if(action.equals("form")) {
			//登録画面にフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/scheduleRegister.jsp");
			dispatcher.forward(request, response);
			
		}
		
		
		//予定削除
		if(action.equals("delete")) {
			
			//セッションスコープから削除する製造予定のschedule_idを取得
			Schedule schedule = (Schedule)session.getAttribute("schedule");
			String schedule_id = schedule.getSchedule_id();
				System.out.println(schedule_id);
			
			//削除実行
			RegisterScheduleLogic rsl = new RegisterScheduleLogic();
			boolean bo = rsl.delete(schedule_id);
			
			if(bo) {
				String msg = "削除しました。";
				System.out.println(msg);
			}else {
				String msg = "削除に失敗しました。";
				System.out.println(msg);
				
			}
			
			//セッションスコープ削除
			session.removeAttribute("Schedule");
			
			
			//カレンダーにリダイレクト
			response.sendRedirect("/StockManagementSystem/main?test=A");
		}
	}
}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//セッションスコープ
		HttpSession session = request.getSession();
		Date date = (Date)session.getAttribute("date");
		
		Lineup lineup = null;
		
		//リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		System.out.println(action);
		
		if(action.equals("search")){
			String name = request.getParameter("name");
		
			//名前で検索
			LineupSearchLogic lineupSerachLogic = new LineupSearchLogic();
			lineup = lineupSerachLogic.search(name);
		
			//セッションスコープに保存
			session.setAttribute("lineup", lineup);
		
			System.out.println(name);
		
			//登録画面にフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/scheduleRegister.jsp");
			dispatcher.forward(request, response);
		}
		
		//入力画面からの情報を受け取り、scheduleインスタンスに保存
		if(action.equals("check")) {
			
			//セッションスコープから名前とID
			lineup = (Lineup)session.getAttribute("lineup");
			String id= lineup.getId();
			String name = lineup.getName();
			
			//リクエストパラメータの取得
			String color = request.getParameter("color");
			
			//quantityを数字変換
			int quantity = 0;
			String ermsg = null;
			try{ quantity = Integer.parseInt(request.getParameter("quantity"));	
			}catch (NumberFormatException e){
				ermsg = "個数を選択して下さい。";
			}
			
				System.out.println(ermsg);
				
			session.setAttribute("ermsg", ermsg);
			
			//問題ない場合、情報をscheduleインスタンスに保存
			if(ermsg == null) {
				
				//schedule_id作成
				java.util.Date now = new java.util.Date();
				String schedule_id = String.valueOf(now.getTime());
					System.out.println(schedule_id);
				
				Schedule schedule = new Schedule(date,id,name,color,quantity,schedule_id);
				session.setAttribute("schedule",schedule);
			}
			
			//確認画面にフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/scheduleCheck.jsp");
			dispatcher.forward(request, response);
		}	
		
			
		if(action.equals("yes")) {
			
			//データベースに登録
			Schedule schedule = (Schedule)session.getAttribute("schedule");
			RegisterScheduleLogic rsl = new RegisterScheduleLogic(); 
			String msg = rsl.create(schedule);
				System.out.println(msg);
				
		
			//VoucherRegisterにリダイレクト
			response.sendRedirect("/StockManagementSystem/VoucherRegister?action=register2");
		}

	}
}
