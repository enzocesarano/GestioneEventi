package enzocesarano.GestioneEventi.payloads;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EventoDTO(
        @NotEmpty(message = "Il titolo è obbligatorio!")
        @Size(min = 3, max = 15, message = "Il titolo deve contenere dai 3 ai 15 caratteri")
        String titolo,

        @NotEmpty(message = "La descrizione è obbligatoria!")
        @Size(min = 10, max = 100, message = "La descrizione deve contenere dai 10 ai 100 caratteri")
        String descrizione,

        @NotNull(message = "La data dell'evento è obbligatoria!")
        @FutureOrPresent(message = "La data dell'evento non può essere nel passato")
        LocalDate data_evento,

        @NotEmpty(message = "Il luogo dell'evento è obbligatorio!")
        @Size(min = 3, max = 100, message = "Il luogo dell'evento deve contenere dai 3 ai 100 caratteri")
        String luogo_evento,

        @Positive(message = "Il numero di posti disponibili deve essere positivo")
        int posti_disponibili) {
    
}
