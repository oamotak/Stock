package servlet;
//伝票登録
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Parts;
import model.PartsSearchLogic;
import model.RegisterVoucherLogic;
import model.Schedule;
import model.ScheduleSearchLogic;
import model.Voucher;
/**
 * Servlet implementation class VoucherRegister
 */
@WebServlet("/VoucherRegister")
public class VoucherRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VoucherRegister() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		
		//セッションスコープ
		HttpSession session = request.getSession();
		
		if(action == null) {
			
			//部品情報取得
			PartsSearchLogic psl = new PartsSearchLogic();
			List<Parts> partslist = psl.execute();
			
			//アプリケーションスコープに保存
			ServletContext application = this.getServletContext();
			application.setAttribute("partslist", partslist);
		
			//部品選択にフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/selectParts.jsp");
			dispatcher.forward(request, response);
		}
		
		if(action.equals("back")){
			
			//部品選択にフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/selectParts.jsp");
			dispatcher.forward(request, response);
		}
		
		if(action.equals("form")) {
			//登録画面にフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/voucherRegisterForm.jsp");
			dispatcher.forward(request, response);
			
		}
		
		//製造予定から伝票登録
		if(action .equals("register2")) {
			//登録した製造予定を取得
			Schedule schedule =(Schedule)session.getAttribute("schedule");
			
			//actionの指定
			action = "plus";
			
			//登録
			RegisterVoucherLogic rvl = new RegisterVoucherLogic();
			List<String> msgList = new ArrayList<>(); 
			msgList = rvl.execute(schedule,action);
			
			for (String msg : msgList) {
				System.out.println(msg);
			}
			
			//セッションスコープ削除
			session.removeAttribute("date");
			session.removeAttribute("schedule");
			//カレンダーにリダイレクト
			response.sendRedirect("/StockManagementSystem/main?test=A");
		}
		
		//マイナス伝票登録
		if(action.equals("minus")) {
			//製造予定のID取得
			String schedule_id = request.getParameter("schedule_id");
			
			//製造予定検索
			ScheduleSearchLogic ssl = new ScheduleSearchLogic();
			Schedule schedule = ssl.execute(schedule_id);
			
			//actionの指定
			action = "minus";
			
			//登録
			RegisterVoucherLogic rvl = new RegisterVoucherLogic();
			List<String> msgList = new ArrayList<>(); 
			msgList = rvl.execute(schedule,action);
			
			for (String msg : msgList) {
				System.out.println(msg);
			}
			
			//削除する製造予定をセッションスコープに保存
			session.setAttribute("schedule", schedule);
			
			//Registerにリダイレクト
			response.sendRedirect("/StockManagementSystem/Register?action=delete");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//セッションスコープ
		HttpSession session = request.getSession();
		
		//リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		
		
		//部品選択後
		if(action.equals("register")) {
			//リクエストパラメータの取得
			String id = request.getParameter("id");
			System.out.println(id);
			
			//部品情報検索
			PartsSearchLogic psl = new PartsSearchLogic();
			Parts parts = psl.execute(id);
			
			//セッションスコープに保存
			session.setAttribute("parts", parts);
			
			//記入画面にフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/voucherRegisterForm.jsp");
			dispatcher.forward(request, response);
		}
		
		//伝票情報記入後
		if(action.equals("check")) {
			
			//リクエストパラメータチェック
			boolean b=false;
			for (String name : (Iterable<String>) request.getParameterNames()::asIterator) {
				if(name==null) {
					b=false;
					break;
				}if(request.getParameter(name).length()==0) {
					b=false;
					break;
				}else {
					b=true;
				}
			}
			
			//エラーメッセージの用意
			String ermsg = null; 	//未入力
			String ermsgP = null;	//unit_price が数字入力でない
			
			//入力情報を入れるVoucherインスタンス
			Voucher voucher = null;
			
			//リクエストパラメータが空の場合
			if(b == false) {ermsg = "未入力の項目があります！";	}
			
			//リクエストパラメータが空でない場合	
			if(b) {
			//不良率初期化
				double defect_rate = 0.0;
				
				
			//伝票を未反映に設定
				String done ="未反映";
				
			//セッションスコープから日付とパーツ名の取得
				Date date = Date.valueOf(request.getParameter("date"));
				session.setAttribute("date", date);
					System.out.println(date);
				Parts parts= (Parts)session.getAttribute("parts");
				String parts_name = parts.getName();
				
			//リクエストパラメータの取得
				
			//部品ID
				String parts_id = request.getParameter("id");
					System.out.println(parts_id);

			//個数
				int quantity = 0;
				
				quantity =Integer.parseInt(request.getParameter("quantity"));
				
			//単価
				int unit_price = 0;
				
				try{
					unit_price = Integer.parseInt(request.getParameter("unit_price"));
				}catch(NumberFormatException e) {
					ermsgP = "数字を入力してください。";
				}
				
				System.out.println(ermsgP);

			//type(在庫の変動の種類)の取得		 
				String type = request.getParameter("type");
					System.out.println(type);
				String note = request.getParameter("note");
					System.out.println(note);
					
			//在庫の変動の種類を判断し、数を入庫・出庫に反映
				int issue = 0;
				int receipt = 0;
					
					if(type.equals("0")) {
						type = "出庫";
						issue = quantity;
					}else if(type.equals("1")) {
						type ="入庫";
						receipt = quantity;
					}else if(type.equals("2")) {
						type ="廃棄";
						issue=quantity;
					}
				
			//合計金額
				int price = 0;
				if(ermsgP == null && type.equals("入庫")) {
					price = quantity * unit_price;
				}
				
			//最終更新日の取得
				Date last_update = new Date(new java.util.Date().getTime());
					System.out.println(last_update);
				
			//id(仮)
				java.util.Date now = new java.util.Date();
				String id = String.valueOf(now.getTime());
					System.out.println(id);
				
			//voucherに保存
				if(ermsgP == null) {
					voucher = new Voucher(id,date,parts_id,parts_name,issue,receipt,type,note,done,unit_price,price,defect_rate,
											last_update);
				}
			}
			
		//メッセージをセッションスコープに保存
			session.setAttribute("voucher", voucher);
			session.setAttribute("ermsg", ermsg);
			session.setAttribute("ermsgP", ermsgP);
			
		//確認画面へフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/voucherCheck.jsp");
			dispatcher.forward(request, response);
		}

		
		if(action.equals("do")) {
			//セッションスコープから取得
			Voucher voucher = (Voucher)session.getAttribute("voucher");
				System.out.println(voucher.getId());
			
			//登録
			RegisterVoucherLogic rvl = new RegisterVoucherLogic();
			String msg = rvl.execute(voucher);
				System.out.println(msg);
			
			//セッションスコープ削除
			session.removeAttribute("date");
			session.removeAttribute("parts");
			session.removeAttribute("voucher");
			session.removeAttribute("ermsg");
			session.removeAttribute("ermsgQ");
			session.removeAttribute("ermsgP");
			
			response.sendRedirect("/StockManagementSystem/VoucherRegister");
		}
		
	}
}
	
