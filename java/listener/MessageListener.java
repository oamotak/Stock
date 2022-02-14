package listener;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Application Lifecycle Listener implementation class MessageListener
 *
 */
@WebListener
public class MessageListener implements ServletRequestAttributeListener {

    /**
     * Default constructor. 
     */
    public MessageListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletRequestAttributeListener#attributeRemoved(ServletRequestAttributeEvent)
     */
    public void attributeRemoved(ServletRequestAttributeEvent srae)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletRequestAttributeListener#attributeAdded(ServletRequestAttributeEvent)
     */
    public void attributeAdded(ServletRequestAttributeEvent srae)  { 
         // TODO Auto-generated method stub
    	ServletContext application=srae.getServletContext();
		ServletRequest request=srae.getServletRequest();
		String uri=((HttpServletRequest) request).getRequestURI();
		if(uri.equals("/StockManagementSystem/RestockNote")||uri.equals("/StockManagementSystem/Main")||uri.equals("/StockManagementSystem/MasterRegistration")) {
			try {
    		
				String msg="";
				if(request.getAttribute("msg")!=null && application.getAttribute("log")!=null) {
					List<String>log=(List<String>)application.getAttribute("log");
					msg=(String)request.getAttribute("msg");
					log.add(msg);
					application.setAttribute("log", log);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
    	}
    	
    }

	/**
     * @see ServletRequestAttributeListener#attributeReplaced(ServletRequestAttributeEvent)
     */
    public void attributeReplaced(ServletRequestAttributeEvent srae)  { 
         // TODO Auto-generated method stub
    	
    }
	
}
