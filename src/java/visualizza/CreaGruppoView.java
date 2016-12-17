package visualizza;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CreaGruppoView extends HttpServlet {

    int id;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        id = Integer.parseInt(request.getParameter("iduser"));

        try {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CreaGruppoView</title>");

            //importo i fogli di stile
            out.println("<link href='bootstrap.css' rel='stylesheet'>");
            out.println("<link href='creagruppo.css' rel='stylesheet'>");
            out.println("<link href='jquery-2.0.3.min' rel='stylesheet'>");
            out.println("</head>");

            out.println("<body>");
            out.println("<div class='container'>");

            //inserimento parte grafica, titolo, textbox, bottone
            out.println("<h2 class='sottotitoli'>Crea gruppo</h2>");
            out.println("<form action=\"/CreaGr?iduser=" + id + "\"method=\"POST\" class='crea'>");
            out.println("<input type=\"text\" placeholder='Nome del gruppo' class='form-control' autocomplete=\"Off\" name=\"InputNomeGruppo\" required><br><br>");
            out.println("<input type=\"submit\" value=\"Submit\" name=\"BtnLog\" class='btn btn-lg btn-primary btn-block'></form>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        session.setAttribute("pagina", "CreaGruppoView?iduser=" + session.getAttribute("idutente"));
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
