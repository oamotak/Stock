package servlet;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.RegisterVoucherLogic;
import model.StockCalenderCalc;
import model.Voucher;

/**
 * Servlet implementation class RestockNote
 */
@WebServlet("/RestockNote")
public class RestockNote extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestockNote() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// jspに飛ばす
		RequestDispatcher dispatcher=request.getRequestDispatcher("/WEB-INF/jsp/restockNote.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//更新のみor注文＆更新
		boolean b=false;
		String msg="更新しました";
		//parameter なしの場合for無視
		for (String name : (Iterable<String>) request.getParameterNames()::asIterator) {
			if(request.getParameter(name).length()==0) {
				b=false;
				msg="すべての項目を入力してください";
				break;
			}else {
				b=true;
			}
		}
		//パラメーターがすべてそろっていたらセット
		if(b) {
			try {
				String id=request.getParameter("id");
				java.sql.Date date=Date.valueOf(request.getParameter("date"));
				String parts_id=request.getParameter("partsId");
				String parts_name=request.getParameter("name");
				int issue=0;
				int receipt=Integer.parseInt(request.getParameter("receipt"));
				String type=request.getParameter("type");
				String note=request.getParameter("note");
				String done="未反映";
				int unit_price=Integer.parseInt(request.getParameter("unitPrice"));
				int price=Integer.parseInt(request.getParameter("price"));
				double defect_rate=0;
				Date last_update=new java.sql.Date(new java.util.Date().getTime());
				//入力値チェック
				if(receipt<=0||unit_price<0||price!=unit_price*receipt) {
					throw new Exception();
				}
				//伝票作成
				Voucher newVoucher=new Voucher(id,date,parts_id,parts_name,issue,receipt,type, note,done,unit_price,price,defect_rate,last_update);
				RegisterVoucherLogic rvl = new RegisterVoucherLogic();
				msg = "伝票番号"+id+rvl.execute(newVoucher);
			}catch(Exception e) {
				msg="入力値が不正です";
			}
		}
		//restockNote再計算,applicationにset
		StockCalenderCalc scc=new StockCalenderCalc();
		Object o[]=scc.getStockCalender();
		ServletContext application=this.getServletContext();
		application.setAttribute("stockCalender", o);
		request.setAttribute("msg",msg);
		//戻る
		doGet(request, response);
	}

}
