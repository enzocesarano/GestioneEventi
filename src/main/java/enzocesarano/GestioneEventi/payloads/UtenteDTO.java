package enzocesarano.GestioneEventi.payloads;

import enzocesarano.GestioneEventi.entities.Enum.RoleUtente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
                        RoleUtente ruolo) {
}
