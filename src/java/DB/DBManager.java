package DB;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class DBManager implements Serializable {

    //La connessione col Database
    public static Connection db; 

    public DBManager(String dburl) throws SQLException {
        try {

            Class.forName("org.apache.derby.jdbc.ClientDriver", true, getClass().getClassLoader());

            db = DriverManager.getConnection(dburl);

           

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() throws SQLException {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");

        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }
//funzione che controlla se esiste utente con password assegnati
    public Utente autenticazione(String username, String password) throws SQLException {

        ResultSet rs;

        try {

            String query = "SELECT ID,USERNAME,PASSWORD FROM UTENTE WHERE USERNAME=? AND PASSWORD=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setString(1, username);
            st.setString(2, password);

            rs = st.executeQuery();

            try {

                if (rs.next()) {
                    Utente utente = new Utente();
                    utente.setName(rs.getString(2));
                    utente.setPswd(rs.getString(3));
                    utente.setid(rs.getInt(1));
                    return utente;

                } else {
                    return null;
                }
            } finally {
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
//funzione usata nel filto che controlla se un utente fa parte o meno di un gruppo
    public static int controllogruppo(int idgruppo, int idutente) {

        ResultSet rs;
        try {
            String query = "SELECT ID FROM GRUPPO_UTENTE WHERE IDGRUPPO=? AND IDUTENTE=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgruppo);
            st.setInt(2, idutente);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return -1;
        }

    }
    
    
    //funzione che seleziona gli inviti ad un gruppo di un utente
     public List<Gruppo> invitigruppo(int id) {
        List<Gruppo> mygroups = new ArrayList<Gruppo>();
        ResultSet rs;
        try {
            String query = "SELECT GRUPPO.ID,GRUPPO.NOME,GRUPPO.IDPROPR,GRUPPO.DATACREAZ "
                    + "FROM GRUPPO JOIN INVITO ON GRUPPO.ID=IDGRUPPO "
                    + "WHERE IDUTENTE=? AND ACCETTATO=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, id);
            st.setBoolean(2, false);
            rs = st.executeQuery();
            while (rs.next()) {
                Gruppo g = new Gruppo();
                g.setName(rs.getString(2));
                g.setdatacreazione(rs.getString(4));
                g.setid(rs.getInt(1));
                g.setidadmin(rs.getInt(3));
                mygroups.add(g);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return mygroups;
    }
//inserisce un utente nella tabella gruppo_utente
      public void inserisciGruppoUtente(int idgroup,int iduser) {

        try {

            String query = "INSERT INTO GRUPPO_UTENTE(IDGRUPPO,IDUTENTE) VALUES(?,?)";
            PreparedStatement st =db.prepareStatement(query);
            st.setInt(1, idgroup);
            st.setInt(2, iduser);
            st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        

    }
//setta il campo booleano true all invito accettato
    public void setaccettato(int idgroup,int iduser) {

        try {

            String query = "UPDATE INVITO SET ACCETTATO=? WHERE IDGRUPPO=? AND IDUTENTE=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setBoolean(1, true);
            st.setInt(2, idgroup);
            st.setInt(3, iduser);
            st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    //funzione che seleziona il massimo id tra i gruppi
        
        //funzione che inserisce un gruppo appena creato
         public void inserisciGruppo(String nomeGruppo,int id) {
        int rs;
        try {

            String query = "INSERT INTO GRUPPO(NOME,IDPROPR,DATACREAZ) VALUES(?,?,CURRENT_DATE)";
            PreparedStatement st = db.prepareStatement(query);
            st.setString(1, nomeGruppo);
            st.setInt(2, id);
            rs = st.executeUpdate();
            st.close();
} catch (SQLException e) {
            e.printStackTrace();
        }

    }
         //funzione che elimina un utente da un gruppo
         public void eliminautente(int idgroup,int ideliminato){
    try {
            String query = "UPDATE GRUPPO_UTENTE SET ELIMINATO=TRUE WHERE IDGRUPPO=? AND IDUTENTE=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgroup);
            st.setInt(2, ideliminato);
            st.executeUpdate();
    }catch(Exception e){e.printStackTrace();}}
         
         //funzione che setta l ultimo log in fatto
public void setultimologin(Timestamp ts,int id){
 try {

                String query = "UPDATE UTENTE SET ULTIMOLOGIN=? WHERE ID=?";
                PreparedStatement st =db.prepareStatement(query);
                st.setTimestamp(1, ts);
                st.setInt(2, id);

                st.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();

            }
}
//funzione che ritorna l amministratore di un gruppo
 public String amministratore(int idgroup) {
        ResultSet rs;
        try {
            String query = "SELECT USERNAME "
                    + "FROM UTENTE JOIN GRUPPO ON GRUPPO.IDPROPR=UTENTE.ID "
                    + "WHERE GRUPPO.ID=?";
            PreparedStatement st =db.prepareStatement(query);
            st.setInt(1, idgroup);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                return "errore";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "errore";
        }

    }
  //funzione che ritorna l avatar dell utente
public String trovaavatar(int id) {
        ResultSet rs;
        try {
            String query = "SELECT AVATAR FROM UTENTE WHERE ID=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {

                return rs.getString(1);
            } else {
                return "errore";

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "errore";
        }
    }
//funzione che ritorna la lista di gruppi in cui l utente è presente
   public List<Gruppo> mygroups(int id) {
        List<Gruppo> mygroups = new ArrayList<Gruppo>();
        ResultSet rs;
        try {
            String query = "SELECT GRUPPO.ID,GRUPPO.NOME,GRUPPO.IDPROPR,GRUPPO.DATACREAZ "
                    + "FROM GRUPPO JOIN GRUPPO_UTENTE ON GRUPPO.ID=IDGRUPPO "
                    + "WHERE GRUPPO_UTENTE.IDUTENTE=? AND GRUPPO_UTENTE.ELIMINATO=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1,id);
            st.setBoolean(2, false);
            rs = st.executeQuery();
            while (rs.next()) {
                Gruppo g = new Gruppo();
                g.setName(rs.getString(2));
                g.setdatacreazione(rs.getString(4));
                g.setid(rs.getInt(1));
                g.setidadmin(rs.getInt(3));
                mygroups.add(g);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return mygroups;
    }
   //funzione che inserisce il post nel db
   public void inseriscipost(int iduser,int idgroup,String testo){
        try{
       int rs;
            String query = "INSERT INTO POST(DATA,IDUTENTE,IDGRUPPO,TESTO) VALUES(CURRENT_TIMESTAMP,?,?,?)";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, iduser);
            st.setInt(2, idgroup);
            st.setString(3, testo);
            rs = st.executeUpdate();
        }catch(Exception e){e.printStackTrace();}
        
   }
   
   //funzione che ritorna un alista di nomi dei file presenti in uun post
   public List<String> prendinomefile(int idpost) {
        List<String> prendinomefile = new ArrayList<String>();
        ResultSet rs;
        try {
            String query = "SELECT NOME FROM POST_FILE WHERE IDPOST=? ";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idpost);

            rs = st.executeQuery();
            while (rs.next()) {
                String s;
                s = rs.getString(1);
                prendinomefile.add(s);
            }

            rs.close();
            st.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return prendinomefile;
    }
   //funzione che manda l invito all utente
   public void invitautente(int idgroup,int idutente){
   try {
            String query = "INSERT INTO INVITO(IDGRUPPO,IDUTENTE) VALUES(?,?)";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgroup);
            st.setInt(2, idutente);
            st.executeUpdate();
            st.close();
   }catch(Exception e){e.printStackTrace();}
       
   }
//funzione per rifiutare invito
public void rifiutainvito(int idgroup,int iduser){
     try {
 int rs;
            String query = "DELETE FROM INVITO WHERE IDGRUPPO=? AND IDUTENTE=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgroup);
            st.setInt(2, iduser);
            rs = st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
}
//funzione che rinomina il gruppo dato ujn nome nuovo
public void rinominagruppo(String newname, int id){
    try {
            String query = "UPDATE GRUPPO SET NOME=? WHERE ID=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setString(1, newname);
            st.setInt(2, id);
            st.executeUpdate();
            st.close();

            
            
        } catch (Exception e) {
            e.printStackTrace();

        }
}
//trova il massimo id nei file
  public int trovaidfile() {
        try {
            ResultSet rs2;
            String query2 = "SELECT MAX(ID) FROM POST_FILE ";
            PreparedStatement st2 = db.prepareStatement(query2);

            rs2 = st2.executeQuery();
            if (rs2.next()) {
                return Integer.parseInt(rs2.getString(1)) + 1;
            } else {
                return 1;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }

    }
  //funzione che ritorna tutti gli utenti da poter invitare nel gruppo
   public List<Utente> utentiinviti(int idgruppo) {
        List<Utente> utenti = new ArrayList<Utente>();
        ResultSet rs;
        try {
            String query = "SELECT ID,USERNAME,PASSWORD FROM UTENTE WHERE ID NOT IN"
                    + "(SELECT IDUTENTE FROM GRUPPO_UTENTE WHERE IDGRUPPO=? AND ELIMINATO=?) AND ID NOT IN"
                    + "(SELECT IDUTENTE FROM INVITO WHERE IDGRUPPO=? )";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgruppo);
            st.setBoolean(2, false);
            st.setInt(3, idgruppo);
            rs = st.executeQuery();
            while (rs.next()) {
                Utente u = new Utente();
                u.setName(rs.getString(2));
                u.setid(rs.getInt(1));
                u.setPswd(rs.getString(3));

                utenti.add(u);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return utenti;
    }
   //funzione che ritonra tutti gli utenti presenti nel gruppo
public List<Utente> utentipartecipanti(int idgruppo,int iduser) {
        List<Utente> utentipartecipanti = new ArrayList<Utente>();
        ResultSet rs;
        try {
            String query = "SELECT USERNAME,UTENTE.ID,PASSWORD FROM UTENTE,GRUPPO_UTENTE WHERE IDUTENTE=UTENTE.ID AND IDGRUPPO=? AND ELIMINATO=? AND IDUTENTE!=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgruppo);
            st.setBoolean(2, false);
            st.setInt(3, iduser);
            rs = st.executeQuery();
            while (rs.next()) {
                Utente u = new Utente();
                u.setName(rs.getString(1));
                u.setid(rs.getInt(2));
                u.setPswd(rs.getString(3));

                utentipartecipanti.add(u);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return utentipartecipanti;

    }
//funzione che ritorna l id dell amministratore di un gruppo
 public int idadmin(int idgroup) {
        try {
            ResultSet rs2;
            String query2 = "SELECT IDPROPR FROM GRUPPO WHERE ID=?";
            PreparedStatement st2 =db.prepareStatement(query2);

            st2.setInt(1, idgroup);
            rs2 = st2.executeQuery();
            if (rs2.next()) {
                return Integer.parseInt(rs2.getString(1));
            } else {
                return 0;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }
 //prende il nome sapendo l id di un gruppo
  public String prendinome(int idgruppo) {
        ResultSet rs;
        try {
            String query = "SELECT NOME FROM GRUPPO WHERE ID=?";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgruppo);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } 
            st.close();}
       catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
  
  //ritorna la lista di tutti i post di un gruppo
  public List<Post> mypost(int idgruppo) {
        List<Post> mypost = new ArrayList<Post>();
        ResultSet rs;
        try {
            String query = "SELECT ID,DATA,IDUTENTE,TESTO FROM POST WHERE IDGRUPPO=? ORDER BY DATA";
            PreparedStatement st = db.prepareStatement(query);
            st.setInt(1, idgruppo);
            rs = st.executeQuery();
            while (rs.next()) {
                Post p = new Post();
                p.setId(rs.getInt(1));
                p.setData(rs.getString(2));
                p.setIdGruppo(idgruppo);
                p.setIdUtente(rs.getInt(3));
                p.setTesto(rs.getString(4));
                mypost.add(p);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mypost;
    }
//ritorna il nome dell utente che ha inserito il post
  public String trovanomepost(int idutentepost) {
        ResultSet rs;
        try {
            String query = "SELECT USERNAME FROM UTENTE WHERE ID=?";
            PreparedStatement st = DBManager.db.prepareStatement(query);
            st.setInt(1, idutentepost);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                return "errore";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "errore";
        }
    }
  //torna l id massimo del post
    public int prendiidpost() {
        try {
            ResultSet rs2;
            String query2 = "SELECT MAX(ID) FROM POST";
            PreparedStatement st2 = DBManager.db.prepareStatement(query2);
            rs2 = st2.executeQuery();
            if (rs2.next()) {
                return Integer.parseInt(rs2.getString(1)) + 1;
            } else {
                return 0;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    //trova il nome di un file
public boolean cercanomefile(String linkfile) {
        try {
            ResultSet rs2;
            String query2 = "SELECT ID FROM POST_FILE WHERE NOME=?";
            PreparedStatement st2 = db.prepareStatement(query2);
            st2.setString(1, linkfile);
            rs2 = st2.executeQuery();
            if (rs2.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
public int trovaidgruppo(String nomeGruppo){
    try {
            ResultSet rs2;
            String query2 = "SELECT ID FROM GRUPPO WHERE NOME=?";
            PreparedStatement st2 =db.prepareStatement(query2);
            st2.setString(1, nomeGruppo);
            rs2 = st2.executeQuery();
            if (rs2.next()) {
                return rs2.getInt(1);
            } 
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    return 0;}
}
