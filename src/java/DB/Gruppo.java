package DB;

public class Gruppo {

    int id, idadmin;
    String nome;
    String datacreazione;

    //funzione che ritorna il nome di un gruppo
    public String getName() {

        return nome;

    }
    //funzione che setta il nome di un gruppo

    public void setName(String nome) {

        this.nome = nome;

    }

    //funzione che ritorna l'id di un gruppo
    public int getid() {

        return id;

    }

    //funzione che setta l'id di un gruppo
    public void setid(int id) {

        this.id = id;

    }

    //funzione che ritorna la data di creazione di un gruppo
    public String getdatacreazione() {

        return datacreazione;

    }

    //funzione che setta la data di creazione di un gruppo
    public void setdatacreazione(String datacreazione) {

        this.datacreazione = datacreazione;

    }

    //funzione che ritorna l'idadmin di un gruppo
    public int getidadmin() {

        return idadmin;

    }

    //funzione che setta l'idadmin di un gruppo
    public void setidadmin(int idadmin) {

        this.idadmin = idadmin;

    }

}
