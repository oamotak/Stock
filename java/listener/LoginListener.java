package listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import model.Employee;

/**
 * Application Lifecycle Listener implementation class MsgListener
 *
 */
@WebListener
public class LoginListener implements HttpSessionAttributeListener, HttpSessionListener {

    /**
     * Default constructor. 
     */
    public LoginListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent se)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent se)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent se)  { 
         // ログイン履歴
    	HttpSession session=se.getSession();
    	ServletContext application=session.getServletContext();
    	//
    	String loginUser=((Employee)session.getAttribute("emp")).getId()+":"+((Employee)session.getAttribute("emp")).getName();
    	Map<String,String>loginEmp=null;
    	List<String>log=null;
    	Date date = new Date(new Date().getTime());
    	//ログインユーザーになければ追加
  
    		if(application.getAttribute("loginEmp")!=null) {
    			loginEmp=(Map<String, String>) application.getAttribute("loginEmp");
    			if(loginEmp.containsKey(session.getId())){
    						//空
    			}else {
    				log=(List<String>)application.getAttribute("log");
    				log.add(date+loginUser+"ログインしました");
    				loginEmp.put(session.getId(), loginUser);
    				application.setAttribute("loginEmp", loginEmp);
    	    		application.setAttribute("log", log);
    			}
    		}else {
    			log=new ArrayList<>();
    			loginEmp=new HashMap<>();
    			loginEmp.put(session.getId(), loginUser);
        		log.add(date+loginUser+"ログインしました");
        		application.setAttribute("loginEmp", loginEmp);
        		application.setAttribute("log", log);
    		}

    	}
    

	/**
     * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent se)  { 
         // Logout履歴
    	HttpSession session=se.getSession();
    	ServletContext application=session.getServletContext();
    	String logoutUser=((Map<String,String>)application.getAttribute("loginEmp")).get(session.getId());
    	//初期化
    	Map<String,String>loginEmp=null;
    	List<String>log=null;
    	Date date = new Date(new Date().getTime());
    	//
    	if(application.getAttribute("loginEmp")!=null) {
    		loginEmp=(Map<String, String>) application.getAttribute("loginEmp");
    	}else {
    		loginEmp=new HashMap<>();
    	}
    	if(application.getAttribute("log")!=null) {
    		log=(List<String>)application.getAttribute("log");
    	}else {
    		log=new ArrayList<>();
    	}
    	//セット
    	loginEmp.remove(session.getId());
    	log.add(date+logoutUser+"ログアウトしました");
    	application.setAttribute("loginEmp", loginEmp);
    	application.setAttribute("log", log);
    	
    }

	/**
     * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent se)  { 
         // TODO Auto-generated method stub
    }
	
}
