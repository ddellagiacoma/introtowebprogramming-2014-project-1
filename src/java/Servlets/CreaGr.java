package Servlets;

import DB.DBManager;
import DB.Gruppo;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreaGr extends HttpServlet {

    int giorno, mese, anno, id, idgruppo;
    String data;
    String nomeGruppo;
    DBManager manager;
    Gruppo g;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        id = Integer.parseInt(request.getParameter("iduser"));
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");

        nomeGruppo = request.getParameter("InputNomeGruppo");

        if ("".equals(nomeGruppo)) {
            response.sendRedirect("/CreaGruppoView");
        } else {
// funzione che ritorna l'id dell'ultimo gruppo inserito, cosi da inserire il nuovo gruppo con id=(idtrovato+1)


            // funzione che inserisce il gruppo appena creato all'interno del database

            this.manager.inserisciGruppo(nomeGruppo, id);
            // funzione che inserisce il creatore del gruppo come amministratore, e quindi partecipante, al gruppo stesso
            idgruppo = manager.trovaidgruppo(nomeGruppo);
            this.manager.inserisciGruppoUtente(idgruppo, id);
            // la successiva pagina visualizzata è la pagina del gruppo appena creato
            response.sendRedirect("/ViewGroup?idgroup=" + idgruppo + "&iduser=" + id);

        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
