package model;

import dao.PartsDAO;

public class PartsRegLogic {
	public String execute(Parts parts) {
		PartsDAO dao=new PartsDAO();
		String msg=dao.setParts(parts);
		return msg;
	}
}
