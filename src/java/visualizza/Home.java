package visualizza;

import DB.DBManager;
import DB.Gruppo;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Home extends HttpServlet {

    int id;
    String avatar;
    DBManager manager;
    List<Gruppo> gruppiinviti;
    ArrayList<String> aggiornamenti
            = new ArrayList<String>();
    String amministratore;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.text.ParseException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        response.setContentType("text/html;charset=UTF-8");
        Cookie[] cookies = request.getCookies();
        Cookie cookie;
        HttpSession session = request.getSession();
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        PrintWriter out = response.getWriter();
session.setAttribute("pagina", "Home?iduser=" + session.getAttribute("idutente"));
        id = Integer.parseInt(request.getParameter("iduser"));

        try {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Home</title>");

            //importo i fogli di stile
            out.println("<link href='bootstrap.css' rel='stylesheet'>");
            out.println("<link href='home.css' rel='stylesheet'>");
            out.println("<link href='jquery-2.0.3.min' rel='stylesheet'>");

            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            cookie = cookies[2];
            //inserimento parte grafica, data ultimo accesso, presa da cookie
            out.println("Ultimo accesso " + cookie.getValue() + "<br>");

            //conversione data del cookie in timestamp per inserirlo nel db
            final String OLD_FORMAT = "dd/MM/yyyy HH:mm:ss";
            final String NEW_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
            String oldDateString = cookie.getValue().toString();
            String newDateString;

            DateFormat formatter = new SimpleDateFormat(OLD_FORMAT);
            java.util.Date d = formatter.parse(oldDateString);
            ((SimpleDateFormat) formatter).applyPattern(NEW_FORMAT);
            newDateString = formatter.format(d);

            Timestamp ts = Timestamp.valueOf(newDateString);

         
            //inserimento parte grafica, immagine dell'avatar
            avatar = this.manager.trovaavatar(id);
            out.println("<img src=\"" + avatar + "\" width=\"150\" height=\"150\" class=\"img-thumbnail\"/>");

            //inserimento parte grafica, bottone per andare alla creazione di un gruppo
            out.println("<form action=\"/CreaGruppoView?iduser=" + id + "\"method=\"POST\" class='creagruppo'>");
            out.println("<input type=\"submit\" class='btn btn-lg btn-primary btn-block' value=\"Crea Gruppo\" name=\"BtnCreaGruppo\"></form>");

            //inserimento parte grafica, bottone per visualizzare i gruppi a cui si è iscritti
            out.println("<form action=\"/HomeGroups?iduser=" + id + "\"method=\"POST\" class='imieigruppi'>");
            out.println("<input type=\"submit\" class='btn btn-lg btn-primary btn-block' value=\"I miei gruppi\" name=\"BtnHomeGroups\"></form>");

            // struttura try-catch che controlla se ci sono stati aggiornamenti nei gruppi a cui l'utente è iscritto
            // nel periodo successivo all'ultimo login precedente a quello appena effettuato
            try {
                ResultSet rs;
                String query = "SELECT DISTINCT NOME FROM GRUPPO, POST, UTENTE WHERE POST.DATA>=UTENTE.ULTIMOLOGIN AND UTENTE.ID=POST.IDUTENTE AND GRUPPO.ID=POST.IDGRUPPO AND UTENTE.ID=?";
                PreparedStatement st = DBManager.db.prepareStatement(query);
                st.setInt(1, id);

                rs = st.executeQuery();
                while (rs.next()) {
                    aggiornamenti.add(rs.getString(1));
                }

                //inserimento parte grafica, in base alla presenza o meno di aggiornamenti inserisco il titolo adeguato
                // e in caso di aggiornamenti un lista con i gruppi interessati
                if (aggiornamenti.isEmpty()) {
                    out.println("<h4 class='sottotitoli'>Nessun aggiornamento dall'ultimo login</h4>");
                    out.println("<br>");

                } else {
                    out.println("<h3 class='sottotitoli'>Gruppi aggiornati dall'ultimo login</h3>");
                    out.println("<br>");

                    out.println("<ul>");

                    for (int i = 0; i < aggiornamenti.size(); i++) {
                        out.println("<li>");
                        out.println(aggiornamenti.get(i));
                        out.println("</li>");
                    }
                    out.println("</ul>");
                    aggiornamenti.clear();
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
            out.println("<br>");

            // struttura try-catch che setta l'ultimo login dell'utente come quello appena effettuato
           this.manager.setultimologin(ts, id);

            //gestione inviti ai gruppi
            //inserimento parte grafica, sottotitolo
            gruppiinviti = this.manager.invitigruppo(id);
            out.println("<h3 class='sottotitoli'>I miei inviti</h3>");

            //inserimento parte grafica, in base alla presenza o meno di inviti inserisco la risposta adeguata
            if (gruppiinviti.isEmpty()) {
                out.println("Nessun invito");

            } else {
                //inserimento parte grafica, nel caso di aggiornamenti inserisco una tabella con 
                // messaggio che indica da chi sei stato invitato e per quale gruppo
                // bottone per accettare
                // bottone per rifiutare
                out.println("<table border=\"0\" class='table'>");
                for (int i = 0; i < gruppiinviti.size(); i++) {
                    out.println("<tr>");
                    out.println("<td>");
                    out.println("Sei stato invitato nel gruppo " + gruppiinviti.get(i).getName());

                    amministratore = this.manager.amministratore(gruppiinviti.get(i).getid());

                    out.println("da " + amministratore);

                    out.println("</td>");
                    out.println("<td>");
                    out.println("<form action=\"/AccettaInvito?iduser=" + id + "&idgroup=" + gruppiinviti.get(i).getid() + "\"method=\"POST\">");
                    out.println("<input type=\"submit\" value=\"Accetta\" class='btn btn-lg btn-block' name=\"BtnAccetta\">");
                    out.println("</form>");
                    out.println("</td>");
                    out.println("<td>");
                    out.println("<form action=\"/RifiutaInvito?iduser=" + id + "&idgroup=" + gruppiinviti.get(i).getid() + "\"method=\"POST\">");
                    out.println("<input type=\"submit\" value=\"Rifiuta\" class='btn btn-lg btn-block' name=\"BtnRifiuta\"></form>");
                    out.println("</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }

            //inserimento parte grafica, bottone per la scelta di un nuovo avatar e bottone per la sua modifica
            out.println("<form action=\"UploadAvatar?iduser=" + id + "&avatar=" + avatar + "\"method='post' class='upload' enctype='multipart/form-data'>");
            out.println("<input type='file' class='btn btn-lg btn-primary btn-block' name='avatar'/>");
            out.println("<input type='submit' class='btn btn-lg btn-primary btn-block' value='Upload Avatar' />");
            out.println("</form>");
            out.println("<br>");
            //inserimento parte grafica, link che fa uscire dal proprio profilo l'utente
            out.println("<a href='/LogOut'>Log Out</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        
        } finally {
            out.close();
        }

    }

    // funzione che restituisce l'amministratore di un gruppo da cui si ha avuto un invito
   

    // funzione che ritorna la lista dei gruppi dai quali si ha avuto un invito
   
    // funzione che ritorna l'avatar dell'utente
    
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
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
