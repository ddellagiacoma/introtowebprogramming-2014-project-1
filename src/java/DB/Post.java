package DB;

public class Post {

    int id, idutente, idgruppo;
    String data, testo;

    //funzione che ritorna il testo di un post
    public String getTesto() {

        return testo;

    }

    //funzione che setta il testo di un post
    public void setTesto(String testo) {

        this.testo = testo;

    }

    //funzione che ritorna la data di un post
    public String getData() {

        return data;

    }

    //funzione che setta la data di un post
    public void setData(String data) {

        this.data = data;

    }

    //funzione che ritorna l'id di un post
    public int getId() {

        return id;

    }
    //funzione che setta l'id di un post

    public void setId(int id) {

        this.id = id;

    }
    //funzione che ritorna l'idutente di un post

    public int getIdUtente() {

        return idutente;

    }
    //funzione che setta l'idutente di un post

    public void setIdUtente(int idutente) {

        this.idutente = idutente;

    }
    //funzione che ritorna l'idgruppo di un post

    public int getIdGruppo() {

        return idgruppo;

    }
    //funzione che setta l'idgruppo di un post

    public void setIdGruppo(int idgruppo) {

        this.idgruppo = idgruppo;

    }

}
