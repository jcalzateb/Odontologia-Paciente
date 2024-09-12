package com.backend.clinica.dto.salida;

import java.time.LocalDate;

public class TurnoSalidaDto {
    private Long id;
    private PacienteSalidaDto pacienteSalidaDto;
    private OdontologoSalidaDto odontologoSalidaDto;
    private LocalDate fechaTurno;
    public TurnoSalidaDto() {
    }
    public TurnoSalidaDto(Long id, PacienteSalidaDto pacienteSalidaDto, OdontologoSalidaDto odontologoSalidaDto, LocalDate fechaTurno) {
        this.id = id;
        this.pacienteSalidaDto = pacienteSalidaDto;
        this.odontologoSalidaDto = odontologoSalidaDto;
        this.fechaTurno = fechaTurno;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public PacienteSalidaDto getPacienteSalidaDto() {
        return pacienteSalidaDto;
    }
    public void setPacienteSalidaDto(PacienteSalidaDto pacienteSalidaDto) {
        this.pacienteSalidaDto = pacienteSalidaDto;
    }
    public OdontologoSalidaDto getOdontologoSalidaDto() {
        return odontologoSalidaDto;
    }
    public void setOdontologoSalidaDto(OdontologoSalidaDto odontologoSalidaDto) {
        this.odontologoSalidaDto = odontologoSalidaDto;
    }
    public LocalDate getFechaTurno() {
        return fechaTurno;
    }
    public void setFechaTurno(LocalDate fechaTurno) {
        this.fechaTurno = fechaTurno;
    }
}
