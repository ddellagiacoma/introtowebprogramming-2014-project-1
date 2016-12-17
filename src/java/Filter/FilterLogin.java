package Filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class FilterLogin implements Filter {

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private final FilterConfig filterConfig = null;
    private ServletContext context;

    /**
     *
     *
     * @param filterConfig
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
        this.context.log("SessionFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);
        String login;

        if (session == null) {
            RequestDispatcher rd = request.getRequestDispatcher("/index.html");
            rd.forward(request, response);
        } else {
            login = (String) session.getAttribute("login");

            if (!"true".equals(login)) {
                this.context.log("Unauthorized access request");

                RequestDispatcher rd = request.getRequestDispatcher("/index.html");
                rd.forward(request, response);
            } else {

                chain.doFilter(request, response);
            }
        }
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

    @Override
    public void destroy() {

    }

  
}
