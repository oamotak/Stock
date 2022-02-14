package model;

import dao.EmployeeDAO;

public class EmpRegLogic {

	public String execute(String name,String id,String pass) {
		EmployeeDAO dao=new EmployeeDAO();
		String msg=dao.setEmployee(name, id, pass);
		return msg;
	}
	
}
