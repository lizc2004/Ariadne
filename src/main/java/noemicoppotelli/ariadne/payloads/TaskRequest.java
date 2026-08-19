package noemicoppotelli.ariadne.payloads;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import noemicoppotelli.ariadne.enums.Priorita;

import java.time.LocalDate;

@Getter
@Setter
public class TaskRequest {

    @NotBlank
    private String titolo;

    @NotBlank
    private String materia;

    @NotNull
    @FutureOrPresent
    private LocalDate scadenza;

    @NotNull
    private Priorita priorita;
}
