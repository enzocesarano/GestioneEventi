package enzocesarano.GestioneEventi.payloads;

import jakarta.validation.constraints.*;

public record UtenteDTO(@NotEmpty(message = "Il nome è obbligatorio!")
                        @Size(min = 2, max = 20, message = "Il nome deve contentere dai 2 ai 20 caratteri")
                        String nome,
                        @NotEmpty(message = "Il cognome è obbligatorio!")
                        @Size(min = 2, max = 20, message = "Il nome deve contentere dai 2 ai 20 caratteri")
                        String cognome,
                        @NotEmpty(message = "L'email è obbligatoria!")
                        @Email(message = "L'email non è un indirizzo corretto!")
                        String email,
                        @NotEmpty(message = "Il cognome è obbligatorio!")
                        @Size(min = 2, max = 20, message = "Il nome deve contentere dai 2 ai 20 caratteri")
                        String username,
                        @NotEmpty(message = "Il campo password non può essere vuoto!")
                        @NotNull
                        String password,
                        @NotNull
                        @Pattern(regexp = "^(UTENTE_NORMALE|ORGANIZZATORE_EVENTI)$", message = "Il ruolo deve essere UTENTE_NORMALE o ORGANIZZATORE_EVENTI")
                        String ruolo) {
}
