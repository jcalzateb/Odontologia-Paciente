package com.backend.clinica.dto.entrada;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;

public class TurnoEntradaDto {
    @NotNull(message = "Debe especificarse el ID del paciente")
    @Valid
    private Long pacienteId;
    @NotNull(message = "Debe especificarse el ID del odontólogo")
    @Valid
    private Long odontologoId;
    @FutureOrPresent(message = "La fecha del turno no puede ser anterior a hoy")
    @NotNull(message = "Debe especificarse la fecha de ingreso del paciente")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaTurno;
    public TurnoEntradaDto() {
    }
    public TurnoEntradaDto(Long pacienteId, Long odontologoId, LocalDate fechaTurno) {
        this.pacienteId = pacienteId;
        this.odontologoId = odontologoId;
        this.fechaTurno = fechaTurno;
    }
    public Long getPacienteId() {
        return pacienteId;
    }
    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }
    public Long getOdontologoId() {
        return odontologoId;
    }
    public void setOdontologoId(Long odontologoId) {
        this.odontologoId = odontologoId;
    }
    public LocalDate getFechaTurno() {
        return fechaTurno;
    }
    public void setFechaTurno(LocalDate fechaTurno) {
        this.fechaTurno = fechaTurno;
    }
}
