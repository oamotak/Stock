package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Parts;
import model.StockCalenderCalc;

/**
 * Servlet implementation class StockCalender
 */
@WebServlet("/StockCalender")
public class StockCalender extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//カラor更新リクエストがあれば再計算
		ServletContext application=this.getServletContext();
		if(application.getAttribute("stockCalender")==null || request.getParameter("update")!=null) {
			Object[]o;
			if(request.getParameter("update")==null) {
				model.Tests tests=new model.Tests();
				o=tests.getStockCalender();
			}else if(request.getParameter("update").equals("HighSpeedMode")){
				model.Tests tests=new model.Tests();
				o=tests.getStockCalender();
			}else {
				StockCalenderCalc scc=new StockCalenderCalc();
				o=scc.getStockCalender();
			}
			application.setAttribute("stockCalender", o);
		}
		request.setAttribute("calender", application.getAttribute("stockCalender"));
		RequestDispatcher dispatcher=request.getRequestDispatcher("/WEB-INF/jsp/stockCalender.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//アプリケーションスコープのlistをフィルターして返す
		ServletContext application=this.getServletContext();
		if(application.getAttribute("stockCalender")==null) {
			StockCalenderCalc scc=new StockCalenderCalc();
			application.setAttribute("stockCalender",scc.getStockCalender());
		}
		Map<Parts,List<Integer>>partsCalender=(Map<Parts,List<Integer>>)(((Object[])application.getAttribute("stockCalender"))[1]);
		//filter partsName
		if(request.getParameter("partsName")!=null &&request.getParameter("partsName").length()!=0) {
			partsCalender = partsCalender.entrySet().stream()
					.filter(map -> map.getKey().getName().matches(".*"+request.getParameter("partsName")+".*"))
					.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
		}
		//filter partsId
		if(request.getParameter("partsId")!=null &&request.getParameter("partsId").length()!=0) {
			partsCalender = partsCalender.entrySet().stream()
			        .filter(map -> map.getKey().getId().equals(request.getParameter("partsId")))
			        .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
		}
		//スコープセット
		Object[]o={((Object[])application.getAttribute("stockCalender"))[0],partsCalender,((Object[])application.getAttribute("stockCalender"))[2]};
		request.setAttribute("calender",o);
		RequestDispatcher dispatcher=request.getRequestDispatcher("/WEB-INF/jsp/stockCalender.jsp");
		dispatcher.forward(request, response);
		 
	}

}
