package Servlets;

import DB.DBManager;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadAvatar extends HttpServlet {

    int idutente;
    String avatar;
    String file;
    private String dirName;
    DBManager manager;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");

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
        throw new ServletException("GET method used with "
                + getClass().getName() + ": POST method required.");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        dirName = request.getServletContext().getRealPath("") + "\\avatar";
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        PrintWriter out = response.getWriter();
        idutente = Integer.parseInt(request.getParameter("iduser"));
        avatar = (request.getParameter("avatar"));

        response.setContentType("text/plain");

        try {

            MultipartRequest multi
                    = new MultipartRequest(request, dirName, 10 * 1024 * 1024,
                            "ISO-8859-1", new DefaultFileRenamePolicy());

            Enumeration files = multi.getFileNames();

            int rs;
            while (files.hasMoreElements()) {

                String name = (String) files.nextElement();
                String filename = multi.getFilesystemName(name);

                avatar = "avatar/" + filename;

                File f = multi.getFile(name);

                String estensione = filename;
                int index = estensione.indexOf(".");
                estensione = filename.substring(index);
                if (".jpg".equals(estensione)) {
                    String query = "UPDATE UTENTE SET AVATAR=? WHERE ID=?";
                    PreparedStatement st = manager.db.prepareStatement(query);
                    st.setString(1, avatar);
                    st.setInt(2, idutente);

                    rs = st.executeUpdate();
                    out.println(idutente);
                    out.println(avatar);

                } else {
                    f.delete();

                }
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
        response.sendRedirect("/Home?iduser=" + idutente);
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
