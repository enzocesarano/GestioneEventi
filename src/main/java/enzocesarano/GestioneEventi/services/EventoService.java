package enzocesarano.GestioneEventi.services;

import enzocesarano.GestioneEventi.entities.Evento;
import enzocesarano.GestioneEventi.entities.Utente;
import enzocesarano.GestioneEventi.exceptions.NotFoundException;
import enzocesarano.GestioneEventi.exceptions.UnauthorizedException;
import enzocesarano.GestioneEventi.payloads.EventoDTO;
import enzocesarano.GestioneEventi.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UtenteService utenteService;


    public Page<Evento> findAll(int page, int size, String sortBy) {
        if (size > 20) size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.eventoRepository.findAll(pageable);
    }

    public Evento findById(UUID id_evento) {
        return this.eventoRepository.findById(id_evento).orElseThrow(() -> new NotFoundException(id_evento));
    }

    public Evento saveEvento(EventoDTO payload, Utente currentUtente) {
        Utente utente = this.utenteService.findById(currentUtente.getId_utente());
        Evento newEvento = new Evento(payload.titolo(), payload.descrizione(), payload.data_evento(), payload.luogo_evento(), payload.posti_disponibili());
        newEvento.setOrganizzatore(utente);
        return this.eventoRepository.save(newEvento);
    }

    public Evento findByIdAndUpdate(UUID id_evento, EventoDTO payload, Utente currentAuthenticatedUtente) {
        Evento evento = this.findById(id_evento);
        if (!evento.getOrganizzatore().getId_utente().equals(currentAuthenticatedUtente.getId_utente())) {
            throw new UnauthorizedException("Non hai i permessi per modificare questo evento!");
        }
        evento.setTitolo(payload.titolo());
        evento.setDescrizione(payload.descrizione());
        evento.setData_evento(payload.data_evento());
        evento.setLuogo_evento(payload.luogo_evento());
        evento.setPosti_disponibili(payload.posti_disponibili());

        return this.eventoRepository.save(evento);
    }

    public void deleteEvento(UUID id_evento, Utente currentAuthenticatedUtente) {
        Evento evento = this.findById(id_evento);
        if (!evento.getOrganizzatore().getId_utente().equals(currentAuthenticatedUtente.getId_utente())) {
            throw new UnauthorizedException("Non hai i permessi per modificare questo evento!");
        }
        this.eventoRepository.delete(evento);
    }

    public Page<Evento> findAllByOrganizzatore(Utente currentAuthenticatedUtente, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return eventoRepository.findByOrganizzatore(currentAuthenticatedUtente, pageable);
    }
}
