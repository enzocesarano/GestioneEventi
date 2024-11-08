package enzocesarano.GestioneEventi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import enzocesarano.GestioneEventi.entities.Enum.RoleUtente;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"password", "role", "accountNonLocked", "credentialsNonExpired", "accountNonExpired", "authorities", "enabled"})
public class Utente implements UserDetails {

    @OneToMany(mappedBy = "utente", cascade = CascadeType.REMOVE)
    @JsonBackReference
    List<Prenotazione> prenotazioni;
    @JsonBackReference
    @OneToMany(mappedBy = "organizzatore", cascade = CascadeType.REMOVE)
    List<Evento> eventi;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)

    private UUID id_utente;
    private String nome;
    private String cognome;
    private String email;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private RoleUtente ruolo;

    public Utente(String nome, String cognome, String email, String username, String password, RoleUtente ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.ruolo.name()));
    }
    
}
