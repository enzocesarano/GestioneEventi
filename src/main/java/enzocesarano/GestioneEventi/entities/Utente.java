package enzocesarano.GestioneEventi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import enzocesarano.GestioneEventi.entities.Enum.RoleUtente;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Utente {

    @OneToMany(mappedBy = "utente", cascade = CascadeType.REMOVE)
    @JsonBackReference
    List<Prenotazione> prenotazioni;
    @JsonBackReference
    @OneToMany(mappedBy = "organizzatore", cascade = CascadeType.REMOVE)
    List<Evento> eventi;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)

    private UUID id;
    private String nome;
    private String cognome;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private RoleUtente ruolo;

    public Utente(String nome, String cognome, String username, String password, RoleUtente ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
    }
}
