package visualizza;

import DB.DBManager;
import DB.Utente;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ImpostazioniAdmin extends HttpServlet {

    int idgroup;
    int iduser;
    int idadmin;

    DBManager manager;
    List<Utente> utenti, utentiinviti;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        idgroup = Integer.parseInt(request.getParameter("idgroup"));
        iduser = Integer.parseInt(request.getParameter("iduser"));
        idadmin = manager.idadmin(idgroup);

        if (idadmin != iduser) {
            RequestDispatcher rd = request.getRequestDispatcher("/ViewGroup?idgroup=" + idgroup + "&iduser=" + iduser);
            rd.forward(request, response);
        }

        try {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ImpostazioniAdmin</title>");

            //importo i fogli di stile
            out.println("<link href='bootstrap.css' rel='stylesheet'>");
            out.println("<link href='impostadmin.css' rel='stylesheet'>");
            out.println("<link href='jquery-2.0.3.min' rel='stylesheet'>");

            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");

            //inserimento parte grafica, titolo, tabella (utenti del gruppo, bottone per eliminarlo)
            out.println("<h1 class='sottotitoli'>Utenti presenti nel gruppo</h1><br>");
            out.println("<table border=\"0\" class='table table-condensed table-hover'>");
            utenti = manager.utentipartecipanti(idgroup,iduser);
            for (int i = 0; i < utenti.size(); i++) {
                out.println("<tr>");
                out.println("<td align='center'>");
                out.println(utenti.get(i).getName());
                out.println("</td>");
                out.println("<td align='center'>");
                out.println("<form action=\"/EliminaUtentiGruppo?idgroup=" + idgroup + "&ideliminato=" + utenti.get(i).getid() + "&iduser=" + iduser + "\"method=\"POST\">");
                out.println("<input class='btn btn-lg btn-block' type=\"submit\" value=\"Elimina dal gruppo\" name=\"BtnEliminadagruppo\"></form>");
                out.println("</td>");

                out.println("</tr>");

            }
            out.println("</table>");

            //inserimento parte grafica, titolo, tabella (utenti del sistema ma non del gruppo, bottone per aggiungerli)
            out.println("<h2 class='sottotitoli'>Invita un nuovo utente</h2><br>");
            out.println("<table border=\"0\" class='table table-condensed table-hover'>");
            utentiinviti = manager.utentiinviti(idgroup);

            for (int i = 0; i < utentiinviti.size(); i++) {
                out.println("<tr>");
                out.println("<td align='center'>");
                out.println(utentiinviti.get(i).getName());
                out.println("</td>");
                out.println("<td align='center'>");
                out.println("<form action=\"/InvitaUtenti?idgroup=" + idgroup + "&iduser=" + utentiinviti.get(i).getid() + "&idadmin=" + iduser + "\"method=\"POST\">");
                out.println("<input class='btn btn-lg btn-block' type=\"submit\" value=\"Invita nel gruppo\" name=\"BtnInvita\"></form>");
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("<br>");
            out.println("<br>");

            //inserimento parte grafica, textbox e bottone per rinominare il gruppo
            out.println("<table border=\"0\">");
            out.println("<form action=\"/Rinomina?idgroup=" + idgroup + "&iduser=" + iduser + "\"method=\"POST\" >");
            out.println("<tr><td><h3 class='sottotitoli'>Rinomina il gruppo</h3></td></tr>");
            out.println("<tr><td>");
            out.println("<input type=\"text\" placeholder='Nome del gruppo' autocomplete=\"Off\" required name=\"InputRename\" class=\"form-control\">");
            out.println("</td><td>");
            out.println("<input type=\"submit\" value=\"Rinomina\" class='btn btn-lg btn-block btn-primary' name=\"BtnRename\">");
            out.println("</td></tr></table></form>");

            //inserimento parte grafica, bottone per la creazione del pdf
            out.println("<form action=\"/CreaPDF?idgroup=" + idgroup + "&iduser=" + iduser + "\"method=\"POST\" class='spaziosotto'>");
            out.println("<input type=\"submit\" class='btn btn-lg btn-primary' value=\"Crea Pdf\" name=\"BtnPdf\">");
            out.println("</form>");

            //inserimento parte grafica, bottone per tornare alla pagina del gruppo
            out.println("<form action=\"/ViewGroup?idgroup=" + idgroup + "&iduser=" + iduser + "\"method=\"POST\">");
            out.println("<input type=\"submit\" class='btn btn-lg btn-primary' value=\"Torna al gruppo\" name=\"BtnOk\" class='spaziosotto'></form>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        session.setAttribute("pagina", "ImpostazioniAdmin?idgroup=" + idgroup + "&iduser=" + session.getAttribute("idutente"));
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
