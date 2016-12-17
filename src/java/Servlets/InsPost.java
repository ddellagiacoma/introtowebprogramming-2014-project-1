package Servlets;

import DB.DBManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InsPost extends HttpServlet {

    int idgroup;
    int iduser, idadmin, idpost;
    String testo;
    DBManager manager;
    List<String> nomefile;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        PrintWriter out = response.getWriter();

        idgroup = Integer.parseInt(request.getParameter("idgroup"));
        idadmin = Integer.parseInt(request.getParameter("idadmin"));
        iduser = Integer.parseInt(request.getParameter("iduser"));
        testo = request.getParameter("InputPost");
        idpost = Integer.parseInt(request.getParameter("idpost"));

        try {

            nomefile = this.manager.prendinomefile(idpost);
            if (!nomefile.isEmpty()) {
                testo = testo + "<br>File: ";
                for (int i = 0; i < nomefile.size(); i++) {
                    testo = testo + "<a href=\"Download?nomefile=" + nomefile.get(i) + "\">" + nomefile.get(i) + "</a>   ";
                }
            }

            // gestione dell'inserimento di un post all'interno di un gruppo
           this.manager.inseriscipost(iduser, idgroup, testo);
            response.sendRedirect("/ViewGroup?idgroup=" + idgroup + "&iduser=" + iduser);

        } catch (IOException e) {
            e.printStackTrace();

        } 
    }

    // funzione che controlla se nel database è presente un file con lo stesso nome inserito dall'utente per fare
    // in modo che la risorsa sia scaricabile
    
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
