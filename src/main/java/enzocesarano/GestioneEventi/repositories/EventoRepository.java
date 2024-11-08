package enzocesarano.GestioneEventi.repositories;

import enzocesarano.GestioneEventi.entities.Evento;
import enzocesarano.GestioneEventi.entities.Utente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventoRepository extends JpaRepository<Evento, UUID> {
    Page<Evento> findByOrganizzatore(Utente utente, Pageable pageable);
}
