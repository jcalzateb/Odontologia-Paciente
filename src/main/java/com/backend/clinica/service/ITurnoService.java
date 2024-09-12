package com.backend.clinica.service;

import com.backend.clinica.dto.entrada.TurnoEntradaDto;
import com.backend.clinica.dto.salida.TurnoSalidaDto;
import com.backend.clinica.exceptions.ResourceNotFoundException;
import com.backend.clinica.exceptions.BadRequestException;

import java.util.List;

public interface ITurnoService {
    TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoDto) throws BadRequestException;
    TurnoSalidaDto buscarTurnoPorId(Long id);
    List<TurnoSalidaDto> listarTurnos();
    void eliminarTurno(Long id) throws ResourceNotFoundException;
    TurnoSalidaDto actualizarTurno(TurnoEntradaDto turnoDto, Long id) throws ResourceNotFoundException, BadRequestException;
}