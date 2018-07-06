
**Sagrada: Prova Finale di Ingegneria del Software 2018**

**Descrizione**

La prova finale consiste nella realizzazione della versione digitale del gioco in scatola “Sagrada”: le regole prevedono che ogni giocatore completi una vetrata della Sagrada Familia aggiungendo dei dadi alla propria plancia nel rispetto di determinate restrizioni.


**Requisiti**
- Game specific: sono state implementate le regole complete (12 Carte Utensile)
- Game agnostic: implementazione di un sistema distribuito composto da un singolo server in grado di gestire una partita alla volta e multipli client (uno per giocatore) che possono partecipare ad una sola partita alla volta. Utilizzo del pattern MVC (Model-View-Controller) per progettare l’intero sistema.

È stata implementata la comunicazione client-server sia via Socket  che via RMI, il server supporta partite in cui i giocatori utilizzano tecnologie diverse e il client può scegliere, all’avvio, quale tecnologia usare.
Il client, inoltre, può selezionare che tipo di interfaccia usare (CLI o GUI).

Si assume che ogni giocatore che voglia partecipare ad una partita conosca l’indirizzo IP o lo URL del server.
Quando un giocatore si connette:
- Se non ci sono partite in fase di avvio, viene creata una nuova partita, altrimenti l’utente entra automaticamente a far parte della partita in fase di avvio.
- Se c’è una partita in fase di avvio, il giocatore viene automaticamente aggiunto alla partita.
- La partita inizia non appena si raggiungono i 4 giocatori. Quando 2 giocatori si connettono a una partita viene inizializzato un timer di N secondi, caricato da un file di configurazione presente lato server. Se non si raggiungono 4 giocatori entro il timeout, la partita inizia comunque con il numero di giocatori raggiunto, a patto che questo sia >= 2. Se prima del timeout il numero di giocatori in attesa scende sotto i due, il timer viene resettato.


**Funzionalità avanzate**

- Partite Multiple: è stato realizzato il server in modo che possa gestire più partite contemporaneamente, dopo la procedura di creazione della prima partita, i giocatori che accederanno al server verranno gestiti in una sala d’attesa per creare una seconda partita e così via.
- Carte Schema Dinamiche: realizzato un sistema di caricamento da file di mappe di gioco personalizzate. 

**Autori**

- Alessandro Nichelini
- Stefano Martina
- Valentina Maggioni
