package model;


import java.util.ArrayList;
import java.util.List;

import dao.LineupDAO;

public class LineupSearchLogic {
	//全レコードの取得
	public List<Lineup> execute() {
		List<Lineup> lineupList = new ArrayList<>(); 
		LineupDAO dao = new LineupDAO();
		lineupList = dao.getLineup();
		return lineupList;
	}
	//名前で検索
	public Lineup search(String name){
		LineupDAO dao = new LineupDAO();
		Lineup lineup = dao.search(name);
		return lineup;
	}
}
