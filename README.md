# Introduzione alla Programmazione per il web

## SERVLET

Le classi del progetto sono suddivise all’interno dei seguenti package:

*	Filter – contiene le servlet per gestire i filtri
*	DB – contiene le servlet per comunicare con il database
*	Listeners – contiene la servlet per aprire la conessione al database
*	Visualizza – contiene le servlet “grafiche”, ovvero che contengono le istruzioni HTML
*	Servlets – contiene le servlet usate dal processo che non rientrano in nessuna delle precedenti categorie

All’interno di questi package sono contenute le seguenti servlet; nel seguito ne viene descritto il funzionamento, ne viene spiegata la scelta di programmazione eﬀettuata; tra parentesi viene mostrato in quale package sono contenute. ViewLog(visualizza): è la servlet che viene utilizzata per mostrare il login, che viene effettuato tramite username e password. Premendo il tasto “Entra” viene invocata la servlet Logged.

* Logged(servlets): controlla che il login effettuato sia corretto e setta i valori al cookie.
* Home(visualizza): contiene la pagina principale del forum, in alto viene visualizzato l’ultimo accesso dell’utente, tramite cookie, e il suo avatar, poi i bottoni per la creazione di un nuovo gruppo(CreaGruppoView) e per mostrare i gruppi a cui si è iscritti(HomeGroups); più in basso è presente il quick display con gli aggiornamenti dall’ultima visita ai propri gruppi e gli inviti ricevuti, con la possibilità di accettare(AccettaInvito) o di rifiutare(RifiutaInvito), il tutto tramite due pulsanti. Nella parte inferiore troviamo i pulsanti per gestire l’upload di un nuovo avatar e un link per fare il logout.

* CreaGruppoView(visualizza): premendo il tasto Crea Gruppo si viene indirizzati a questa servlet che mostra solamente una TextBox e un bottone per inserire un nuovo gruppo, il quale richiama la CreaGr.

* CreaGr(servlets): dopo aver controllato che il parametro inserito nella TextBox non sia nullo, in tal caso viene mostrata nuovamente la CreaGruppo, viene invocata una query che inserisce il gruppo e la relazione tra lo stesso e il creatore all’interno del database, viene poi visualizzata la ViewGroup.

* HomeGroups(visualizza): premendo dalla Home il tasto “I miei gruppi” si viene indirizzata a questa servlet che mostra in un elenco puntato i gruppi a cui l’utente è iscritto.

* ViewGroup(visualizza): questa servlet mostra in alto il nome del gruppo scelto, poi inseriti in una tabella sono presenti i seguenti dati: data e ora (in formato TimeStamp, avatar, utente e testo del post. Più sotto sono presenti la TextBox, e i bottoni per inserire il post(InsPost) e gli eventuali file(UploadFile) caricati dall’utente all’interno della discussione. Sul fondo della pagina sono visualizzati gli utenti partecipanti alla discussione e due pulsanti, uno che manda alla gestione del gruppo, visibile solamente per l’amministratore e un per tornare alla Home. Nel momento di caricare il testo dei post viene effettuato il parsing, sia per i file già caricati, sia per gli indirizzi esterni.
* InsPost(servlets): inserisce il post(testo, data e ora, etc) all’interno del database. Nel caso del caricamento di uno o più files alla fine del messaggio aggiunge del testo dove mostra tutti i file caricati per quel post e richiama la ViewGroup.

* Upload(servlets): crea una nuova cartella “File” nella directory build del progetto, qualora non sia già presente, salva all’interno il file caricato ed inserisce nel database il percorso.

* Download(servlets): cliccando sul nome del file, quest’ultimo viene salvato nella directory predefinita dei download.

* ImpostazioniAdmin(visualizza): è la pagina riassuntiva del gruppo che può essere aperta solamente dall’amministratore, in cui si possono vedere gli utenti presenti nel gruppo, con la possibilità di eliminarli tramite bottone(EliminaUtentiGruppi), gli utenti che possono essere invitati(InvitaUtenti), una TextBox con relativo bottone per rinominare il gruppo(Rinomina), un tasto per creare il PDF riassuntivo(CreaPDF) del gruppo e un tasto per tornare alla ViewGroup.

* EliminaUtentiGruppi(servlets): modifica il campo “Eliminato” all’interno del record nel database, così da escludere l’utente dalla discussione.

* InvitaUtenti(servlets): esegue la query che aggiunge alla tabella “Invito” un record, in attesa della risposta dell’utente invitato.

* Rinomina(servlets): aggiorna il record relativo al gruppo nel database

* CreaPDF(servlets): crea una nuova cartella “PDF” nella directory build del progetto, qualora non sia già presente, e salva il file in cui sono contenuti, gli utenti partecipanti al gruppi con avatar, il numero dei post e la data dell’ultimo commento inserito.

* AccettaInvito(servlets): setta il campo booleano “Accettato” come true ed inserisce la relazione tra utente e gruppo.

* RifiutaInvito(servlets): cancella dalla tabella “Invito” il record ed impedisce un nuovo invito.

* UploadAvatar(servlets): come l’Upload, solamente che crea una cartella “Avatar”, nel caso non esista, e controlla che il tipo del file caricato sia JPG.

* LogOut(servlets): torna alla ViewLog e chiude la sessione.

* DBManager(DB): servlet che contiene tutte le query necessarie.

* Gruppo-Post-Utente(DB): servlet che rimappano le tabelle del database.

* FilterGroup(filter): chiama una query in cui controlla che l’utente faccia parte del gruppo passato come parametro dall’URL, in caso contrario chiama una pagina presa come attributo della sessione(la pagina precedente).

* FilterLogin(filter): controlla se la sessione è attiva e l’utente è loggato, in caso contrario rimanda alla ViewLog.

* filterautenticazione(filter): chiama una query in cui controlla se l’utente passato come parametro dall’URL sia uguale a un attributo della sessione settato al login, in caso contrario chiama una pagina presa come attributo della sessione(la pagina precedente).

* WebappContextListener(listeners): è un listener che ha il compito di accedere e di chiudere il database.
