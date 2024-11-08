package enzocesarano.GestioneEventi.repositories;

import enzocesarano.GestioneEventi.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
