package Filter;

import DB.DBManager;
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

public class FilterGruppo implements Filter {
    private FilterConfig filterConfig = null;
    public FilterGruppo() {
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
    int iduser, idgroup, autenticazionegruppo;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
                HttpSession session = req.getSession();
        iduser = Integer.parseInt(req.getParameter("iduser"));
        idgroup = Integer.parseInt(req.getParameter("idgroup"));
        autenticazionegruppo = DBManager.controllogruppo(idgroup, iduser);
        if (autenticazionegruppo != -1) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect((String) session.getAttribute("pagina"));
            //res.sendRedirect("/Home?iduser=" + iduser);
        }
        //chain.doFilter(request, response);
    }
    @Override
    public void destroy() {
    }
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
}