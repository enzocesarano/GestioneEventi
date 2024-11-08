package enzocesarano.GestioneEventi.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id_evento;

    private String titolo;
    private String descrizione;
    private LocalDate data_evento;
    private String luogo_evento;
    private int posti_disponibili;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente organizzatore;

    public Evento(String titolo, String descrizione, LocalDate data_evento, String luogo_evento, int posti_disponibili) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data_evento = data_evento;
        this.luogo_evento = luogo_evento;
        this.posti_disponibili = posti_disponibili;
    }
}