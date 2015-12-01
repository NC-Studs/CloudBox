/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kira
 */
@WebFilter(filterName = "LoggingFilter",
        urlPatterns = {"/*"}
//        ,
//initParams = {
//    @WebInitParam(name = "mood", value = "awake")}
)
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

//        System.out.println("fdsfs");
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        Bean session = (Bean) req.getSession().getAttribute("bean1");
        String url = req.getRequestURI();


        chain.doFilter(request, response);

//        if (session == null || !session.isLogged) {
//            System.out.println("null and isnt logged");
//            
//            if (url.indexOf("secure/") >= 0 || url.indexOf("logout.xhtml") >= 0) {
//                resp.sendRedirect(req.getServletContext().getContextPath() + "/login.xhtml");
//            } else {
//                chain.doFilter(request, response);
//            }
//        } else if (session.getPass() != null) {
//            System.out.println("pass not null");
//            if (url.indexOf("secure/") >= 0) {
//                if (session.isLogged) {
//                    chain.doFilter(request, response);
//                } else {
//                    req.getSession().invalidate();
//                    session.setShowErrorMessage(false);
//                    req.getSession().removeAttribute("bean1");
//                    resp.sendRedirect(req.getServletContext().getContextPath() + "/login.xhtml");
//                }
//            } 
//            else if ((url.indexOf("login.xhtml") >= 0) && session.isLogged) {
//                resp.sendRedirect(req.getServletContext().getContextPath() + "/secure/");
//            } else if (url.indexOf("logout.xhtml") >= 0) {
//
//                session.setShowErrorMessage(false);
//                req.getSession().invalidate();
//                req.getSession().removeAttribute("bean1");
//                resp.sendRedirect(req.getServletContext().getContextPath() + "/login.xhtml");
//            } else {
//                
//                chain.doFilter(request, response);
//            }
//        } else if (!session.isLogged) {
//            System.out.println("isnt logged");
//            req.getSession().invalidate();
//            session.setShowErrorMessage(false);
//            req.getSession().removeAttribute("bean1");
//            if (url.indexOf("secure/") >= 0 || url.indexOf("logout.xhtml") >= 0) {
//                resp.sendRedirect(req.getServletContext().getContextPath() + "/login.xhtml");
//            } else {
//                chain.doFilter(request, response);
//            }
//        }
    }

    @Override
    public void destroy() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
