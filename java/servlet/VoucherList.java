package servlet;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.CollectVoucherCalc;
import model.Voucher;

/**
 * Servlet implementation class Voucher
 */
@WebServlet("/VoucherList")
public class VoucherList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//DBから伝票検索、セッション保管後doPostの（ｘfilter）、ページ付けをする
		//parameter受け取り
		String goodsId=null;Date datea=null;Date dateb=null;
		if(request.getParameter("id")!=null && request.getParameter("id").length()!=0) {goodsId=request.getParameter("id");}
		if(request.getParameter("datea")!=null && request.getParameter("dateb")!=null && request.getParameter("datea").length()!=0 && request.getParameter("dateb").length()!=0) {
			datea=Date.valueOf(request.getParameter("datea"));
			dateb=Date.valueOf(request.getParameter("dateb"));	

		}
		
		//伝票所得
		List<Voucher>voucherList=new ArrayList<>();
		CollectVoucherCalc cvc=new CollectVoucherCalc();
		String msg="";int n=0;
		if(goodsId!=null && datea!=null && dateb!=null) {
			voucherList=cvc.getVoucher(datea, dateb, goodsId);
			msg= "ID:"+goodsId+"　期間"+datea+"～"+dateb+"の検索結果";
		}else if(datea!=null && dateb!=null) {
			voucherList=cvc.getVoucher(datea, dateb);
			msg= "全部品"+"　期間"+datea+"～"+dateb+"の検索結果";
		}else if(goodsId!=null) {
			voucherList=cvc.getVoucher(goodsId);
			msg= "ID:"+goodsId+"　全期間"+"の検索結果";
		}else {
			voucherList=cvc.getVoucher();
			msg= "全部品　全期間"+"の検索結果";
		}
		
		HttpSession session=request.getSession();
		if(voucherList!=null) {n=voucherList.size();}
	    session.setAttribute("msgVoucher",msg+"　　"+n+"件");
		//スコープ保存
		session.setAttribute("voucherList",voucherList);
		//doPostのfilterかける
		doPost(request,response);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//セッションスコープのデータを絞り込み>>requestSet
		HttpSession session=request.getSession();
		List<Voucher>vlist=(List<Voucher>)session.getAttribute("voucherList");
		String msg=(String)session.getAttribute("msgVoucher")+">>フィルター";
		//TYPE filter
		if(request.getParameter("type")!=null) {
			if(request.getParameter("type").length()!=0) {
				vlist=vlist.stream()
						.filter(v->v.getType().equals(request.getParameter("type")))
						.collect(Collectors.toList());
				msg+=">>種類："+request.getParameter("type");
				request.setAttribute("type", request.getParameter("type"));
			}
		}
		//PARTS_NAME filter
		if(request.getParameter("name")!=null) {
			if(request.getParameter("name").length()!=0) {
				vlist=vlist.stream()
						.filter(v->v.getParts_name().matches(".*"+request.getParameter("name")+".*"))
						.collect(Collectors.toList());
				msg+=">>部品名："+request.getParameter("name")+"を含む";
				request.setAttribute("name", request.getParameter("name"));
			}
		}
		
		//DISCRIPTION filter
		/*
		if(request.getParameter("")!=null) {
			vlist=vlist.stream()
					.filter(v->v.getType().equals(request.getParameter("")))
					.collect(Collectors.toList());
			msg+=""+request.getParameter("")+"";
		}
		*/
		//counter
		request.setAttribute("count", vlist.stream().count());
		msg+=">>"+vlist.size()+"件";
		//sort by date
		vlist=vlist.stream()
				.sorted((v1,v2)->v1.getDate().compareTo(v2.getDate()))
				.collect(Collectors.toList());
		//pagination 20items/Page
		if(request.getParameter("page")!=null) {
			vlist=vlist.stream()
					.skip((Integer.parseInt(request.getParameter("page"))-1)*20)
					.limit(20)
					.collect(Collectors.toList());
					msg+="Page:"+request.getParameter("page");
		}else {
			vlist=vlist.stream()
					.limit(20)
					.collect(Collectors.toList());
					msg+="Page:1";
		}
		//scope
		request.setAttribute("voucherList", vlist);
		request.setAttribute("msg",msg);
		RequestDispatcher dispatcher=request.getRequestDispatcher("/WEB-INF/jsp/voucher.jsp");
		dispatcher.forward(request, response);

	}

}
