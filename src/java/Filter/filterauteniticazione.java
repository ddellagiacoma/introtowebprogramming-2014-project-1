package Filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class filterauteniticazione implements Filter {

    private FilterConfig filterConfig = null;

    public filterauteniticazione() {
    }
    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    int id;
    int provaid;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
    
        id = Integer.parseInt(req.getParameter("iduser"));
        try {
            provaid = (Integer) session.getAttribute("idutente");
        } catch (Exception e) {
            log("Unauthorized access request");
            res.sendRedirect((String) session.getAttribute("pagina"));

            //RequestDispatcher rd = request.getRequestDispatcher("/index.html");
            //rd.forward(request, response);
        }

        if (id == provaid) {
            chain.doFilter(request, response);
        } else {
            log("Unauthorized access request");
            res.sendRedirect((String) session.getAttribute("pagina"));

            //res.sendRedirect("/index.html");
        }

    }

    @Override
    public void destroy() {
    }

    /**
     * Init method for this filter
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;

    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
}