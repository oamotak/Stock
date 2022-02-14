package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.CurrentStockCalc;
import model.Parts;

/**
 * Servlet implementation class PartsStock
 */
@WebServlet("/PartsStock")
public class PartsStock extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//現在時刻までの未反映伝票を在庫に更新させる
		CurrentStockCalc csc=new CurrentStockCalc();
		csc.execute();
		//在庫所得
		List<Parts>partsList=csc.getStock();
		request.setAttribute("partsList", partsList);
		RequestDispatcher dispatcher=request.getRequestDispatcher("/WEB-INF/jsp/stock.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 検索boxのほう
		String search=request.getParameter("search");
		
		CurrentStockCalc csc=new CurrentStockCalc();
		
		List<Parts>partsList=csc.getStock(search);
		request.setAttribute("partsList", partsList);
		
		if(search.length()==0) {search="全部品";}
		request.setAttribute("search", search);
		RequestDispatcher dispatcher=request.getRequestDispatcher("/WEB-INF/jsp/stock.jsp");
		dispatcher.forward(request, response);
	}

}
