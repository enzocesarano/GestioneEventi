package enzocesarano.GestioneEventi.services;

import enzocesarano.GestioneEventi.entities.Evento;
import enzocesarano.GestioneEventi.entities.Prenotazione;
import enzocesarano.GestioneEventi.entities.Utente;
import enzocesarano.GestioneEventi.exceptions.BadRequestException;
import enzocesarano.GestioneEventi.exceptions.NotFoundException;
import enzocesarano.GestioneEventi.payloads.PrenotazioneDTO;
import enzocesarano.GestioneEventi.repositories.PrenotazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PrenotazioneService {

    @Autowired
    PrenotazioneRepository prenotazioneRepository;

    @Autowired
    UtenteService utenteService;

    @Autowired
    EventoService eventoService;

    public Page<Prenotazione> findAll(int page, int size, String sortBy) {
        if (size > 20) size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione findById(UUID id_prenotazione) {
        return this.prenotazioneRepository.findById(id_prenotazione).orElseThrow(() -> new NotFoundException(id_prenotazione));
    }

    public Prenotazione savePrenotazione(PrenotazioneDTO payload, Utente currentAuthenticatedUtente) {
        Evento evento = this.eventoService.findById(UUID.fromString(payload.id_evento()));
        Utente utente = this.utenteService.findById(currentAuthenticatedUtente.getId_utente());
        if (this.prenotazioneRepository.existsByUtenteAndEvento(utente, evento)) {
            throw new BadRequestException("Hai già una prenotazione per questo evento.");
        }
        Prenotazione newPrenotazione = new Prenotazione(utente, evento);
        return this.prenotazioneRepository.save(newPrenotazione);
    }

    public void deletePrenotazione(UUID id_prenotazione) {
        Prenotazione prenotazione = this.findById(id_prenotazione);
        this.prenotazioneRepository.delete(prenotazione);
    }

}
