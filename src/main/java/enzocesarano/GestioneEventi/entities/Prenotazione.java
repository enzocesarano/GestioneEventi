package enzocesarano.GestioneEventi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_prenotazione;

    private LocalDate data_prenotazione;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "id_evento")
    private Evento evento;

    public Prenotazione(LocalDate data_prenotazione, Utente utente, Evento evento) {
        this.data_prenotazione = data_prenotazione;
        this.utente = utente;
        this.evento = evento;
    }
}
