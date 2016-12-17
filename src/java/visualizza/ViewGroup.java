package visualizza;

import DB.DBManager;
import DB.Post;
import DB.Utente;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ViewGroup extends HttpServlet {

    int idgroup;
    int iduser;
    int idadmin;
    boolean admin;
    DBManager manager;
    String nomeGruppo;
    List<Post> posts;
    List<Utente> utenti;
    String avatar;
    String nomeutentepost;
    String sottostringa;
    int maxidpost, autenticazionegruppo, autenticazioneparam;
    String linkfile;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        idgroup = Integer.parseInt(request.getParameter("idgroup"));
        iduser = Integer.parseInt(request.getParameter("iduser"));
        idadmin = manager.idadmin(idgroup);
        admin = idadmin == iduser;
        nomeGruppo=manager.prendinome(idgroup);
        try {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ViewGroup</title>");

            //importo i fogli di stile
            out.println("<link href='bootstrap.css' rel='stylesheet'>");
            out.println("<link href='viewgroup.css' rel='stylesheet'>");
            out.println("<link href='jquery-2.0.3.min' rel='stylesheet'>");
            out.println("</head>");

            out.println("<body>");
            out.println("<div class='container'>");

            //inserimento parte grafica, titolo e tabella dove sono inseriti i post degli utenti
            // nella tabella vengono inseriti in ordine:
            // data e ora del messaggio
            // avatar dell'utente
            // nome dell'utente
            // testo del post
            out.println("<h1 class='sottotitoli'>" + nomeGruppo + "</h1>");
            maxidpost = manager.prendiidpost();
            out.println("<form action=\"/InsPost?idgroup=" + idgroup + "&iduser=" + iduser + "&idadmin=" + idadmin + "&idpost=" + maxidpost + "\"method=\"POST\">");
            posts = manager.mypost(idgroup);
            out.println("<table border=\"0\" class='table table-condensed table-hover'>");
            for (int i = 0; i < posts.size(); i++) {
                out.println("<tr>");
                out.println("<td>");
                out.println("<FONT SIZE=\"1\">" + posts.get(i).getData() + "</font>");
                out.println("</td>");
                out.println("<td>");
                avatar = manager.trovaavatar(posts.get(i).getIdUtente());
                out.println("<img src=\"" + avatar + "\" width=\"50\" height=\"50\" />");
                out.println("</td>");
                out.println("<td>");
                out.println("<b>");
                nomeutentepost = manager.trovanomepost(posts.get(i).getIdUtente());
                out.println(nomeutentepost);
                out.println("<b>");
                out.println("</td>");
                out.println("<td>");

                // gestione delle stringhe con collegamenti a file già caricati o ad indirizzi internet esterni al sito 
                String stringamod = posts.get(i).getTesto();
                Pattern pattern = Pattern.compile("(.*?)\\$\\$(.+?)\\$\\$(.*?)");
                Pattern patternweb = Pattern.compile("\\(?\\b((ht|f)tp(s?)://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]");
                Matcher matcherweb = patternweb.matcher(stringamod);
                while (matcherweb.find()) {
                    stringamod = posts.get(i).getTesto().substring(0, matcherweb.start()) + "<a href=\"" + matcherweb.group() + "\">" + matcherweb.group() + "</a>" + posts.get(i).getTesto().substring(matcherweb.end(), posts.get(i).getTesto().length());
                }
                Matcher matcher = pattern.matcher(stringamod);
                int n = 0;
                while (matcher.find()) {
                    linkfile = matcher.group(2);
                    if (manager.cercanomefile(linkfile)) {
                        out.println(matcher.group(1) + "<a href=\"Download?nomefile=" + linkfile + "\">" + linkfile + "</a>");
                        n = matcher.end(2);
                    } else {
                        out.println(matcher.group(1) + "<a href=\"http://www." + matcher.group(2) + "\">" + matcher.group(2) + "</a>" + matcher.group(3));
                        n = matcher.end(2);
                    }
                }
                if (n != 0) {
                    n = n + 2;
                }
                sottostringa = stringamod.substring(n, stringamod.length());
                out.println(sottostringa);
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</table>");

            //inserimento parte grafica, tabella
            // textbox per l'inserimento del testo di un nuovo post e bottone per inserirlo
            // bottone per scegliere il file da allegare al post
            // bottone per salvare il file, che verrà poi allegato al post
            out.println("<table border='0' class ='table table-condensed' >");
            out.println("<tr>");
            out.println("<td>");
            out.println("<input placeholder='Inserisci il testo del tuo post' class=\"form-control\" type=\"text\" autocomplete=\"Off\" name=\"InputPost\" >");
            out.println("<br>");
            out.println("<input type=\"submit\" value=\"Inserisci post\" name=\"BtnInserisciPost\" class='btn btn-lg btn-primary btn-block'></form>");
            out.println("</td>");
            out.println("<td>");
            out.println("<form action=\"Upload?iduser=" + iduser + "&idgroup=" + idgroup + "&idpost=" + maxidpost + "&idadmin=" + idadmin + "\"method='post' enctype='multipart/form-data'>");
            out.println("<input class='btn btn-lg btn-block' type='file' name='file'/>");
            out.println("</td>");
            out.println("<td>");
            out.println("<input class='btn btn-lg btn-block' type='submit' value='Upload File'\">");
            out.println("</form>");
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("<br>");

            //inserimento parte grafica, sottotitolo, lista degli utenti che partecipano al gruppo, escluso l'utente loggato
            out.println("<h4 class='sottotitoli'>Utenti partecipanti alla discussione</h4>");

            utenti = manager.utentipartecipanti(idgroup, iduser);
            out.println("<ul>");
            for (int i = 0; i < utenti.size(); i++) {
                out.println("<li>");
                out.println(utenti.get(i).getName());
                out.println("</li>");

            }
            out.println("</ul>");

            //inserimento parte grafica, controllo se l'utente è l'amministratore, in tal caso aggiungo un bottone che
            //lo porta alle impostazioni per modificare il gruppo 
            if (admin == true) {
                out.println("<form action=\"/ImpostazioniAdmin?idgroup=" + idgroup + "&iduser=" + iduser + "\"method=\"POST\" class='spaziosotto'>");
                out.println("<input type=\"submit\" class='btn btn-lg  btn-primary' value=\"Impostazioni Amministratore\" name=\"BtnImpostazioniAdmin\"></form>");
            }

            //inserimento parte grafica, bottone per tornare alla home page
            out.println("<form action=\"Home?iduser=" + iduser + "\"method=\"POST\" >");
            out.println("<input type='submit' value='Home' class='btn btn-primary btn-lg' class='spaziosotto'/>");
            out.println("</form>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        session.setAttribute("pagina", "ViewGroup?idgroup=" + idgroup + "&iduser=" + session.getAttribute("idutente"));
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
