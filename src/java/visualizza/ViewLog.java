package visualizza;


import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ViewLog extends HttpServlet {

    String user;
    String pswd;
    Cookie cookieuser = new Cookie("user", "");

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(true);

        //setto cookie
        response.addCookie(cookieuser);
        cookieuser.setValue(request.getParameter("Inputname"));
        cookieuser.setMaxAge(-1);

        
        PrintWriter out = response.getWriter();
        try {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='utf-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Progetto web</title>");
                
            //importo i fogli di stile
            out.println("<link href='bootstrap.css' rel='stylesheet'>");
            out.println("<link href='signin.css' rel='stylesheet'>");
            out.println("<link href='jquery-2.0.3.min' rel='stylesheet'>");
            out.println("</head>");
            
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<form class='form-signin' action='/Logged'>");
            //inserimento parte grafica, titolo, textbox e bottone per il login
            out.println("<h2 class='form-signin-heading'>Log In</h2>");
            out.println("<input name='Inputname' type='text' class='form-control' placeholder='Username' required autofocus>");
            out.println("<input name='InputPassword' type='password' class='form-control' placeholder='Password' required>");
            out.println("<button name='BtnLog' class='btn btn-lg btn-primary btn-block' type='submit'>Entra</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

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
