package com.backend.clinica.service;

import com.backend.clinica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinica.dto.salida.OdontologoSalidaDto;
import com.backend.clinica.exceptions.ResourceNotFoundException;
import java.util.List;

public interface IOdontologoService {
    OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologoDto);

    OdontologoSalidaDto buscarOdontologoPorId(Long id);

    List<OdontologoSalidaDto> listarOdontologos();

    void eliminarOdontologo(Long id) throws ResourceNotFoundException;

    OdontologoSalidaDto actualizarOdontologo(OdontologoEntradaDto odontologoDto, Long id) throws ResourceNotFoundException;

}
