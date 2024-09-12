package com.backend.clinica.service.impl;

import com.backend.clinica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinica.dto.salida.OdontologoSalidaDto;
import com.backend.clinica.entity.Odontologo;
import com.backend.clinica.exceptions.ResourceNotFoundException;
import com.backend.clinica.repository.OdontologoRepository;
import com.backend.clinica.service.IOdontologoService;
import com.backend.clinica.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OdontologoService implements IOdontologoService {

    private final Logger LOGGER = LoggerFactory.getLogger(OdontologoService.class);
    private final OdontologoRepository odontologoRepository;
    private final ModelMapper modelMapper;
    public OdontologoService(OdontologoRepository odontologoRepository, ModelMapper modelMapper) {
        this.odontologoRepository = odontologoRepository;
        this.modelMapper = modelMapper;
        configureMapping();
    }
    @Override
    public OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologoDto) {
        LOGGER.info("OdontologoEntradaDto: {}", JsonPrinter.toString(odontologoDto));
        Odontologo entidadOdontologo = modelMapper.map(odontologoDto, Odontologo.class);
        LOGGER.info("EntidadOdontologo: {}", JsonPrinter.toString(entidadOdontologo));
        Odontologo odontologoRegistrado = odontologoRepository.save(entidadOdontologo);
        LOGGER.info("OdontologoRegistrado: {}", JsonPrinter.toString(odontologoRegistrado));
        OdontologoSalidaDto odontologoSalidaDto = modelMapper.map(odontologoRegistrado, OdontologoSalidaDto.class);
        LOGGER.info("OdontologoSalidaDto: {}", JsonPrinter.toString(odontologoSalidaDto));
        return odontologoSalidaDto;
    }
    @Override
    public OdontologoSalidaDto buscarOdontologoPorId(Long id) {
        Odontologo odontologoBuscado = odontologoRepository.findById(id).orElse(null);
        LOGGER.info("Odontologo buscado: {}", JsonPrinter.toString(odontologoBuscado));
        if (odontologoBuscado == null) {
            LOGGER.error("No se ha encontrado el odontólogo con id {}", id);
            return null;
        }
        return modelMapper.map(odontologoBuscado, OdontologoSalidaDto.class);
    }
    public List<OdontologoSalidaDto> listarOdontologos() {
        List<OdontologoSalidaDto> odontologoSalidaDtos = odontologoRepository.findAll()
                .stream()
                .map(odontologo -> modelMapper.map(odontologo, OdontologoSalidaDto.class))
                .toList();
        LOGGER.info("Listado de todos los odontólogos: {}", JsonPrinter.toString(odontologoSalidaDtos));
        return odontologoSalidaDtos;
    }
    public void eliminarOdontologo(Long id) throws ResourceNotFoundException {
        if (!odontologoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el odontólogo con id " + id);
        }else {
            odontologoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el odontólogo con id {}", id);
        }
    }

    @Override
    public OdontologoSalidaDto actualizarOdontologo(OdontologoEntradaDto odontologoDto, Long id) throws ResourceNotFoundException {
        Odontologo odontologoAActualizar = odontologoRepository.findById(id).orElse(null);
        OdontologoSalidaDto odontologoSalidaDto;
        if (odontologoAActualizar == null) {
            throw new ResourceNotFoundException("No fue posible actualizar el odontólogo porque no se encuentra en nuestra base de datos");
        }else {
            modelMapper.map(odontologoDto, odontologoAActualizar);
            odontologoRepository.save(odontologoAActualizar);
            odontologoSalidaDto = modelMapper.map(odontologoAActualizar, OdontologoSalidaDto.class);
            LOGGER.warn("Odontólogo actualizado: {}", JsonPrinter.toString(odontologoSalidaDto));
        }
        return odontologoSalidaDto;
    }
    private void configureMapping() {
        modelMapper.typeMap(OdontologoEntradaDto.class, Odontologo.class)
                .addMappings(mapper -> {
                    mapper.map(OdontologoEntradaDto::getMatricula, Odontologo::setMatricula);
                    mapper.map(OdontologoEntradaDto::getNombre, Odontologo::setNombre);
                    mapper.map(OdontologoEntradaDto::getApellido, Odontologo::setApellido);
                });
        modelMapper.typeMap(Odontologo.class, OdontologoSalidaDto.class)
                .addMappings(mapper -> {
                    mapper.map(Odontologo::getId, OdontologoSalidaDto::setId);
                    mapper.map(Odontologo::getMatricula, OdontologoSalidaDto::setMatricula);
                    mapper.map(Odontologo::getNombre, OdontologoSalidaDto::setNombre);
                    mapper.map(Odontologo::getApellido, OdontologoSalidaDto::setApellido);
                });
    }

}
