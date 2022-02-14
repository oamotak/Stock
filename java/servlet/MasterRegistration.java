package servlet;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.EmpRegLogic;
import model.Parts;
import model.PartsRegLogic;

/**
 * Servlet implementation class MasterRegistration
 */
@WebServlet("/MasterRegistration")
public class MasterRegistration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MasterRegistration() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher dispatcher=request.getRequestDispatcher("/WEB-INF/jsp/empReg.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String msg="登録します";
		try {
			//emp登録
			if(request.getParameter("reg").equals("emp")) {
				String id=request.getParameter("id");
				String name=request.getParameter("name");
				String pass=request.getParameter("pass");
				request.getParameterMap();
				EmpRegLogic erl=new EmpRegLogic();
				msg=erl.execute(name, id, pass);

			}
			//parts登録
			for (String name : (Iterable<String>) request.getParameterNames()::asIterator) {
				if(request.getParameter(name).length()==0) {
					throw new Exception();
				}
			}
			if(request.getParameter("reg").equals("parts")) {
				String id=request.getParameter("id");
				String spec_id=request.getParameter("spec_id");
				String name=request.getParameter("name");
				String material=request.getParameter("material");
				String shape=request.getParameter("shape");
				String color=request.getParameter("color");
				String note=request.getParameter("note");
				int stock=Integer.parseInt(request.getParameter("stock"));
				Date last_updated=new java.sql.Date(new java.util.Date().getTime());
				Parts parts=new Parts(id,spec_id,name,
						 material,shape,color,
						 note,stock,last_updated);
				PartsRegLogic prl=new PartsRegLogic();
				msg=prl.execute(parts);
			}
		}catch(Exception e) {
			msg="入力値が不正です";
		}
		request.setAttribute("msg", msg);
		RequestDispatcher dispatcher=request.getRequestDispatcher("/WEB-INF/jsp/empReg.jsp");
		dispatcher.forward(request, response);
	}

}
