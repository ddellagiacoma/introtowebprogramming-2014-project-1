package Servlets;

import DB.DBManager;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EliminaUtentiGruppo extends HttpServlet {

    int idgroup, ideliminato, iduser;
    DBManager manager;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        idgroup = Integer.parseInt(request.getParameter("idgroup"));
        ideliminato = Integer.parseInt(request.getParameter("ideliminato"));
        iduser = Integer.parseInt(request.getParameter("iduser"));

        // struttura try-catch che aggiorna il database nel caso un utente venga eliminato da un gruppo
       try{
           
       this.manager.eliminautente(idgroup, ideliminato);
            // la successiva pagina visualizzata è quella delle impostazioni dell'admin
            response.sendRedirect("/ImpostazioniAdmin?idgroup=" + idgroup + "&iduser=" + iduser);
        } catch (IOException e) {
            e.printStackTrace();

       

    }}

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
