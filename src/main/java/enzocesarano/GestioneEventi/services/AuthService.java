package enzocesarano.GestioneEventi.services;

import enzocesarano.GestioneEventi.entities.Utente;
import enzocesarano.GestioneEventi.exceptions.UnauthorizedException;
import enzocesarano.GestioneEventi.payloads.UtenteLoginDTO;
import enzocesarano.GestioneEventi.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UtenteService utenteService;

    @Autowired
    private JWT jwt;

    public String checkCredentialsAndGenerateToken(UtenteLoginDTO payload) {
        Utente utente = this.utenteService.findByEmail(payload.email());
        if (utente.getPassword().equals(payload.password())) {
            String accessToken = jwt.createToken(utente);
            return accessToken;
        } else {
            throw new UnauthorizedException("Credenziali errate!");
        }
    }

}