package enzocesarano.GestioneEventi.controllers;

import enzocesarano.GestioneEventi.entities.Evento;
import enzocesarano.GestioneEventi.entities.Prenotazione;
import enzocesarano.GestioneEventi.entities.Utente;
import enzocesarano.GestioneEventi.exceptions.BadRequestException;
import enzocesarano.GestioneEventi.payloads.EventoDTO;
import enzocesarano.GestioneEventi.payloads.PrenotazioneDTO;
import enzocesarano.GestioneEventi.payloads.UtenteDTO;
import enzocesarano.GestioneEventi.services.EventoService;
import enzocesarano.GestioneEventi.services.PrenotazioneService;
import enzocesarano.GestioneEventi.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/utenti")
public class UtenteController {
    @Autowired
    private UtenteService utenteService;

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private EventoService eventoService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Utente> findAllUtenti(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy) {
        return this.utenteService.findAll(page, size, sortBy);
    }

    @GetMapping("/{id_utente}")
    @ResponseStatus(HttpStatus.OK)
    public Utente getUtente(@PathVariable UUID id_utente) {
        Utente utente = this.utenteService.findById(id_utente);
        return utente;
    }

    @PutMapping("/{id_utente}")
    @ResponseStatus(HttpStatus.OK)
    public Utente putUtente(@PathVariable UUID id_utente, @RequestBody @Validated UtenteDTO utente, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        Utente updatedUtente = this.utenteService.findByIdAndUpdate(id_utente, utente);
        return updatedUtente;
    }

    @DeleteMapping("/{id_utente}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUtente(@PathVariable UUID id_utente) {
        this.utenteService.deleteUtente(id_utente);
    }

    @GetMapping("/{id_utente}/prenotazioni")
    @ResponseStatus(HttpStatus.OK)
    public List<Prenotazione> getPrenotazioniByUtente(@PathVariable UUID id_utente) {
        List<Prenotazione> prenotazioni = this.utenteService.findPrenotazioniByUtenteId(id_utente);
        return prenotazioni;
    }

    @PostMapping("/{id_utente}/prenotazioni")
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione postPrenotazione(@RequestBody @Validated PrenotazioneDTO payload, @PathVariable UUID id_utente, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        return this.prenotazioneService.savePrenotazione(payload, id_utente);
    }

    @GetMapping("/{id_utente}/eventi")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTI')")
    public Page<Evento> findAllEventiPerUtente(
            @PathVariable UUID id_utente,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataEvento") String sortBy) {
        return this.eventoService.findAllByOrganizzatore(id_utente, page, size, sortBy);
    }

    @GetMapping("/{id_utente}/eventi/{id_evento}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTI')")
    public Evento getEvento(@PathVariable UUID id_evento) {
        return this.eventoService.findById(id_evento);
    }

    @PostMapping("/{id_utente}/eventi")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTI')")
    public Evento postEvento(@RequestBody @Validated EventoDTO payload, @PathVariable UUID id_utente, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        return this.eventoService.saveEvento(payload, id_utente);
    }

    @PutMapping("/{id_utente}/eventi/{id_evento}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTI')")
    public Evento putEvento(@PathVariable UUID id_evento, @RequestBody @Validated EventoDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        return this.eventoService.findByIdAndUpdate(id_evento, payload);
    }

    @DeleteMapping("/{id_utente}/eventi/{id_evento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTI')")
    public void deleteEvento(@PathVariable UUID id_evento) {
        this.eventoService.deleteEvento(id_evento);
    }
}
