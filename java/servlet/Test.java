package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.StockCalenderCalc;

/**
 * Servlet implementation class Test
 */
@WebServlet("/Test")
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Test() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		long t1 = System.nanoTime();
		model.Tests tests=new model.Tests();
		tests.getStockCalender();
		System.out.println((System.nanoTime()-t1)/1000000+"ミリ秒＜＜HighSpeedMode("+(System.nanoTime()-t1));
		long t2 = System.nanoTime();
		StockCalenderCalc scc=new StockCalenderCalc();
		scc.getStockCalender();
		System.out.println((System.nanoTime()-t2)/1000000+"ミリ秒＜＜NormalMode"+(System.nanoTime()-t2));
		t2 = System.nanoTime();
		scc.getStockCalender();
		System.out.println((System.nanoTime()-t2)/1000000+"ミリ秒＜＜NormalMode"+(System.nanoTime()-t2));
		t1 = System.nanoTime();
		tests.getStockCalender();
		System.out.println((System.nanoTime()-t1)/1000000+"ミリ秒＜＜HighSpeedMode("+(System.nanoTime()-t1));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
