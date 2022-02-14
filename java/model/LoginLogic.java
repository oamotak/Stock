package model;

import dao.EmployeeDAO;

public class LoginLogic {

	public Employee execute(String id,String pass) {
		EmployeeDAO dao=new EmployeeDAO();
		Employee emp=dao.getEmployee(id, pass);
		return emp;
	}
	
}
