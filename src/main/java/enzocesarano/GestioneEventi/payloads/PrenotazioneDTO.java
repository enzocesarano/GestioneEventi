package enzocesarano.GestioneEventi.payloads;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PrenotazioneDTO(
        @NotNull(message = "L'UUID evento è obbligatorio!")
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$", message = "L'UUID fornito non è valido.")
        String evento) {
}
