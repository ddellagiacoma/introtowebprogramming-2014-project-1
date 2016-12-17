package visualizza;

import DB.DBManager;
import DB.Gruppo;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HomeGroups extends HttpServlet {

    int id;
    DBManager manager;
    List<Gruppo> gruppi;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        PrintWriter out = response.getWriter();
        id = Integer.parseInt(request.getParameter("iduser"));
        HttpSession session = request.getSession();

        try {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet HomeGroups</title>");

            //importo i fogli di stile
            out.println("<link href='bootstrap.css' rel='stylesheet'>");
            out.println("<link href='jquery-2.0.3.min' rel='stylesheet'>");
            out.println("</head>");

            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<br>");
            out.println("<br>");

            //inserimento parte grafica, titolo, lista dei gruppi a cui sono iscritto
            out.println("<h2 class='sottotitoli'>Gruppi a cui sono iscritto</h2>");

            out.println("<br>");
            gruppi = this.manager.mygroups(id);
            out.println("<ul>");
            for (int i = 0; i < gruppi.size(); i++) {
                out.println("<li>");
                out.println("<a href=\"/ViewGroup?idgroup=" + gruppi.get(i).getid() + "&iduser=" + id + "\">" + gruppi.get(i).getName() + "</a>");
                out.println("</li>");
            }
            out.println("</ul>");
            out.println("</body>");
            out.println("</html>");
             session.setAttribute("pagina", "HomeGroups?iduser=" + session.getAttribute("idutente"));
        } finally {
            out.close();
        }
    }
 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
