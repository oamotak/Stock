package model;

import java.util.ArrayList;
import java.util.List;

import dao.ProductsDAO;

public class ProductsSearchLogic {
	public List<Products> execute(String name,String color){
		ProductsDAO dao = new ProductsDAO();
		List<Products> partslist = new ArrayList<>();
		partslist = dao.getParts(name, color);
		
		return partslist;
	}
}
