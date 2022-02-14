package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Employee;

/**
 * Servlet implementation class Main
 */
@WebServlet("/Main")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher dispatcher;
		if(request.getParameter("menu")!=null) {
			if(request.getParameter("menu").equals("msg")) {
				dispatcher=request.getRequestDispatcher("/WEB-INF/jsp/log.jsp");
			}else if(request.getAttribute("menu").equals("msg")) {
				dispatcher=request.getRequestDispatcher("/WEB-INF/jsp/log.jsp");
			}else {
				dispatcher=request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
			}
		}else {
			dispatcher=request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
		}
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session=request.getSession();
		request.setAttribute("msg",((Employee)session.getAttribute("emp")).getName()+">>"+request.getParameter("msg"));
		doGet(request, response);
	}

}
