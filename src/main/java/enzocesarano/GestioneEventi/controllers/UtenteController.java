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
        List<Prenotazione> prenotazioni = this.utenteService.findPrenotazioniByUtenteId(currentAuthenticatedUtente);
        return prenotazioni;
    }

    @PostMapping("/me/prenotazioni")
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione postPrenotazione(@RequestBody @Validated PrenotazioneDTO payload, @AuthenticationPrincipal Utente currentAuthenticatedUtente, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        return this.prenotazioneService.savePrenotazione(payload, currentAuthenticatedUtente);
    }

    @GetMapping("/me/eventi")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTI')")
    public Page<Evento> findAllEventiPerUtente(
            @AuthenticationPrincipal Utente currentAuthenticatedUtente,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "titolo") String sortBy) {
        return this.eventoService.findAllByOrganizzatore(currentAuthenticatedUtente, page, size, sortBy);
    }

    @GetMapping("/me/eventi/{id_evento}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTI')")
    public Evento getEvento(@PathVariable UUID id_evento, @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        return this.eventoService.findById(id_evento);
    }

    @PostMapping("/me/eventi")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTI')")
    public Evento postEvento(@RequestBody @Validated EventoDTO payload, @AuthenticationPrincipal Utente currentAuthenticatedUtente, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        return this.eventoService.saveEvento(payload, currentAuthenticatedUtente);
    }

    @PutMapping("/me/eventi/{id_evento}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTI')")
    public Evento putEvento(@PathVariable UUID id_evento, @RequestBody @Validated EventoDTO payload, BindingResult validationResult, @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }
        return this.eventoService.findByIdAndUpdate(id_evento, payload, currentAuthenticatedUtente);
    }

    @DeleteMapping("/me/eventi/{id_evento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTI')")
    public void deleteEvento(@PathVariable UUID id_evento, @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        this.eventoService.deleteEvento(id_evento, currentAuthenticatedUtente);
    }
}
