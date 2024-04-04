package de.bassmech.findra.web.auth;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.bassmech.findra.web.util.statics.UrlFilterType;
import de.bassmech.findra.web.view.HeaderView;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.xhtml"})
public class AuthFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(AuthFilter.class);
	
    public AuthFilter() {
    }
 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
         
    }
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
         try {
            // check whether session variable is set
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            HttpSession ses = req.getSession(false);
            //  allow user to proceed if url is login.xhtml or user logged in or user is accessing any page in //public folder
            String reqURI = req.getRequestURI();
            if ( reqURI.indexOf("/login.xhtml") >= 0 
            		|| (ses != null && ses.getAttribute("clientUuid") != null)
                    || reqURI.indexOf("/public/") >= 0 
                    //|| reqURI.contains("javax.faces.resource")
                    || reqURI.contains("jakarta.faces.resource"))
                   chain.doFilter(request, response);
            else   // user didn't log in but asking for a page that is not allowed so take user to login page
                   res.sendRedirect(getFullPath(req.getContextPath(), UrlFilterType.LOGIN));  // Anonymous user. Redirect to login page
      }
     catch(Throwable e) {
    	 //TODO logger
    	 logger.error("error on authfilter", e);
     }
    } //doFilter
 
    private String getFullPath(String contextPath, UrlFilterType target) {
    	return contextPath + target.getFullUrl(); 
    }
    
    @Override
    public void destroy() {
         
    }
}
