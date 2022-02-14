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
import model.LoginLogic;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//loginしていたら/Mainへ、そうでなければjspへ
		HttpSession session=request.getSession();
		Employee emp=(Employee)session.getAttribute("emp");
		if(emp!=null) {
			response.sendRedirect("/StockManagementSystem/Main"); 
		}else {
			RequestDispatcher dispatcher=request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//loginformからうけつけるところDB->doGetに渡す
		//id,pass所得
		String id=request.getParameter("id");String pass=request.getParameter("pass");
		LoginLogic lolo=new LoginLogic();
		Employee emp=lolo.execute(id, pass);
		//sessionにセット
		if(emp!=null) {HttpSession session=request.getSession();session.setAttribute("emp", emp);}
		request.setAttribute("msg", "IDもしくはPASSWORDが間違っています");
		//dogetで判断
		doGet(request, response);
		
	}

}
