package Servlets;

import DB.DBManager;
import DB.Utente;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreaPDF extends HttpServlet {

    DBManager manager;
    int idgroup;
    String nome;
    List<Utente> user;
    ArrayList<Timestamp> dataultimopost
            = new ArrayList<Timestamp>();
    ArrayList<Integer> numeropost
            = new ArrayList<Integer>();
    ArrayList<String> stringaimg = new ArrayList<String>();

    int iduser;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        idgroup = Integer.parseInt(request.getParameter("idgroup"));
        iduser = Integer.parseInt(request.getParameter("iduser"));

        try {
            Document document = new Document();

            try {

                ResultSet rs;
                String query = "SELECT NOME FROM GRUPPO WHERE ID=?";
                PreparedStatement st = DBManager.db.prepareStatement(query);

                st.setInt(1, idgroup);
                rs = st.executeQuery();
                while (rs.next()) {
                    nome = rs.getString(1);
                }
                rs.close();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String url = request.getServletContext().getRealPath("") + "\\pdf\\";
            String percorso = url + nome + ".pdf";

            File folder = new File(url);

            if (!folder.isDirectory()) {

                folder.mkdir();

            }

            PdfWriter.getInstance(document, new FileOutputStream(percorso));
            document.open();

            user = listautenti();

            Anchor a = new Anchor(nome, FontFactory.getFont(FontFactory.COURIER_BOLD, 16));
            document.add(a);

            ResultSet rs;
            String c;
            String img;
            img = request.getServletContext().getRealPath("") + "\\";

            for (int i = 0; i < user.size(); i++) {

                Anchor anchorTarget = new Anchor(user.get(i).getName(), FontFactory.getFont(FontFactory.COURIER, 14));
                anchorTarget.setName("BackToTop");
                Paragraph paragraph1 = new Paragraph();
                paragraph1.setSpacingBefore(50);
                paragraph1.add(anchorTarget);
                document.add(paragraph1);

                try {

                    rs = null;
                    String query = "SELECT COUNT(*) FROM POST WHERE IDGRUPPO=? AND IDUTENTE=?";
                    PreparedStatement st = DBManager.db.prepareStatement(query);
                    st.setInt(1, idgroup);
                    st.setInt(2, user.get(i).getid());
                    rs = st.executeQuery();
                    while (rs.next()) {
                        numeropost.add(i, rs.getInt(1));
                    }

                    document.add(new Paragraph("Numero post fatti: " + numeropost.get(i),
                            FontFactory.getFont(FontFactory.COURIER, 14)));

                    rs.close();
                    st.close();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (numeropost.get(i) != 0) {
                    try {
                        rs = null;
                        String query = "SELECT MAX(DATA) FROM POST WHERE IDGRUPPO=? AND IDUTENTE=?";
                        PreparedStatement st = DBManager.db.prepareStatement(query);
                        st.setInt(1, idgroup);
                        st.setInt(2, user.get(i).getid());
                        rs = st.executeQuery();

                        while (rs.next()) {
                            dataultimopost.add(i, rs.getTimestamp(1));
                        }

                        document.add(new Paragraph("Ultimo post scritto: " + dataultimopost.get(i),
                                FontFactory.getFont(FontFactory.COURIER, 14)));

                        rs.close();
                        st.close();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    String query = "SELECT AVATAR FROM UTENTE, GRUPPO_UTENTE WHERE IDGRUPPO=? AND UTENTE.ID=IDUTENTE AND IDUTENTE=?";
                    PreparedStatement st = DBManager.db.prepareStatement(query);
                    st.setInt(1, idgroup);
                    st.setInt(2, user.get(i).getid());
                    rs = st.executeQuery();

                    while (rs.next()) {
                        stringaimg.add(i, rs.getString(1));
                    }

                    c = img + stringaimg.get(i);
                    Image immagine = Image.getInstance(c);
                    immagine.scaleAbsolute(100, 100);
                    document.add(immagine);
                    rs.close();
                    st.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            document.close();
            stringaimg.clear();
            numeropost.clear();
            dataultimopost.clear();
            user.clear();

        } catch (DocumentException de) {

            System.err.println(de.getMessage());

        } catch (IOException ioe) {

            System.err.println(ioe.getMessage());

        }

        response.sendRedirect("/ImpostazioniAdmin?idgroup=" + idgroup + "&iduser=" + iduser);
    }

    public List<Utente> listautenti() {
        List<Utente> utenti = new ArrayList<Utente>();
        try {
            ResultSet rs;
            String query = "SELECT USERNAME, UTENTE.ID FROM UTENTE, GRUPPO_UTENTE WHERE IDGRUPPO=? AND UTENTE.ID=GRUPPO_UTENTE.IDUTENTE";

            PreparedStatement st = DBManager.db.prepareStatement(query);

            st.setInt(1, idgroup);
            rs = st.executeQuery();

            while (rs.next()) {
                Utente u = new Utente();
                u.setName(rs.getString(1));
                u.setid(rs.getInt(2));
                utenti.add(u);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
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
