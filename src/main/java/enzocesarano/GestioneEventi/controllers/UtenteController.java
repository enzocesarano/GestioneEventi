package enzocesarano.GestioneEventi.controllers;

import enzocesarano.GestioneEventi.entities.Evento;
import enzocesarano.GestioneEventi.entities.Prenotazione;
import enzocesarano.GestioneEventi.entities.Utente;
import enzocesarano.GestioneEventi.exceptions.BadRequestException;
import enzocesarano.GestioneEventi.exceptions.NotFoundException;
import enzocesarano.GestioneEventi.exceptions.UnauthorizedException;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/me")
    public Utente getProfile(@AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        return currentAuthenticatedUtente;
    }

    @PutMapping("/me")
    public Utente updateProfile(@AuthenticationPrincipal Utente currentAuthenticatedUtente, @RequestBody @Validated UtenteDTO body) {
        return this.utenteService.findByIdAndUpdate(currentAuthenticatedUtente.getId_utente(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        this.utenteService.deleteUtente(currentAuthenticatedUtente.getId_utente());
    }


    @GetMapping("/me/prenotazioni")
    @ResponseStatus(HttpStatus.OK)
    public List<Prenotazione> getPrenotazioniByUtente(@AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        List<Prenotazione> prenotazioni = this.utenteService.findPrenotazioniByUtenteId(currentAuthenticatedUtente.getId_utente());
        return prenotazioni;
    }

    @PostMapping("/me/prenotazioni")
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione postPrenotazione(@RequestBody @Validated PrenotazioneDTO payload, @AuthenticationPrincipal Utente currentAuthenticatedUtente, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        return this.prenotazioneService.savePrenotazione(payload, currentAuthenticatedUtente.getId_utente());
    }

    @GetMapping("/me/eventi")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTI')")
    public Page<Evento> findAllEventiPerUtente(
            @AuthenticationPrincipal Utente currentAuthenticatedUtente,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataEvento") String sortBy) {
        return this.eventoService.findAllByOrganizzatore(currentAuthenticatedUtente.getId_utente(), page, size, sortBy);
    }

    @GetMapping("/me/eventi/{id_evento}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ORGANIZZATORE_EVENTI')")
    public Evento getEvento(@AuthenticationPrincipal Utente currentAuthenticatedUtente, @PathVariable UUID id_evento) {
        Evento evento = this.eventoService.findById(id_evento);
        if (evento == null || !evento.getOrganizzatore().equals(currentAuthenticatedUtente)) {
            throw new NotFoundException("Evento non trovato o non appartiene all'utente!");
        }
        return evento;
    }

    @PostMapping("/me/eventi")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTI')")
    public Evento postEvento(@RequestBody @Validated EventoDTO payload, @AuthenticationPrincipal Utente currentAuthenticatedUtente, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        return this.eventoService.saveEvento(payload, currentAuthenticatedUtente.getId_utente());
    }

    @PutMapping("/me/eventi/{id_evento}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ORGANIZZATORE_EVENTI')")
    public Evento putEvento(@AuthenticationPrincipal Utente currentAuthenticatedUtente,
                            @PathVariable UUID id_evento,
                            @RequestBody @Validated EventoDTO payload,
                            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        Evento evento = this.eventoService.findById(id_evento);
        if (evento == null) {
            throw new NotFoundException("Evento non trovato!");
        }

        if (!evento.getOrganizzatore().equals(currentAuthenticatedUtente)) {
            throw new UnauthorizedException("Non hai i permessi per aggiornare questo evento!");
        }
        return this.eventoService.findByIdAndUpdate(id_evento, payload);
    }

    @DeleteMapping("/me/eventi/{id_evento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ORGANIZZATORE_EVENTI')")
    public void deleteEvento(@AuthenticationPrincipal Utente currentAuthenticatedUtente,
                             @PathVariable UUID id_evento) {
        Evento evento = this.eventoService.findById(id_evento);
        if (evento == null) {
            throw new NotFoundException("Evento non trovato!");
        }
        if (!evento.getOrganizzatore().equals(currentAuthenticatedUtente)) {
            throw new UnauthorizedException("Non hai i permessi per eliminare questo evento!");
        }
        this.eventoService.deleteEvento(id_evento);
    }
}
