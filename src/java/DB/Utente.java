package DB;

import java.io.Serializable;

public class Utente implements Serializable {

    private String username;
    private String password;
    private int id;
    //funzione che ritorna l'username

    public String getName() {

        return username;

    }
    //funzione che setta l'username

    public void setName(String username) {

        this.username = username;

    }
    //funzione che ritorna la password

    public String getpswd() {

        return password;

    }
    //funzione che setta la password

    public void setPswd(String password) {

        this.password = password;

    }
    //funzione che ritorna l'id

    public int getid() {

        return id;

    }
    //funzione che setta l'id

    public void setid(int id) {

        this.id = id;

    }

}
