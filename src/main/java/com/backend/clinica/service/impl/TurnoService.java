package com.backend.clinica.service.impl;

import com.backend.clinica.dto.entrada.TurnoEntradaDto;
import com.backend.clinica.dto.salida.TurnoSalidaDto;
import com.backend.clinica.entity.Odontologo;
import com.backend.clinica.entity.Paciente;
import com.backend.clinica.entity.Turno;
import com.backend.clinica.exceptions.BadRequestException;
import com.backend.clinica.exceptions.ResourceNotFoundException;
import com.backend.clinica.repository.TurnoRepository;
import com.backend.clinica.service.ITurnoService;
import com.backend.clinica.service.impl.PacienteService;
import com.backend.clinica.service.impl.OdontologoService;
import com.backend.clinica.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnoService implements ITurnoService {
    private final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);
    private final ModelMapper modelMapper;
    private final TurnoRepository turnoRepository;
    private final PacienteService pacienteService;
    private final OdontologoService odontologoService;

    public TurnoService(ModelMapper modelMapper, TurnoRepository turnoRepository, PacienteService pacienteService, OdontologoService odontologoService) {
        this.modelMapper = modelMapper;
        this.turnoRepository = turnoRepository;
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
        configureMappings();
    }
    @Override
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoDto) {
        LOGGER.info("TurnoEntradaDto: {}", JsonPrinter.toString(turnoDto));
        Paciente paciente = modelMapper.map(pacienteService.buscarPacientePorId(turnoDto.getPacienteId()), Paciente.class);
        Odontologo odontologo = modelMapper.map(odontologoService.buscarOdontologoPorId(turnoDto.getOdontologoId()), Odontologo.class);

        if (paciente == null || odontologo == null) {
            throw new BadRequestException("El paciente u odontólogo no existe");
        }
        Turno turno = new Turno();
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologo);
        turno.setFechaTurno(turnoDto.getFechaTurno());
        Turno turnoRegistrado = turnoRepository.save(turno);
        LOGGER.info("TurnoRegistrado: {}", JsonPrinter.toString(turnoRegistrado));

        return modelMapper.map(turnoRegistrado, TurnoSalidaDto.class);
    }
    @Override
    public TurnoSalidaDto buscarTurnoPorId(Long id) {
        Turno turnoBuscado = turnoRepository.findById(id).orElse(null);
        LOGGER.info("Turno buscado: {}", JsonPrinter.toString(turnoBuscado));
        TurnoSalidaDto turnoEncontrado = null;
        if(turnoBuscado != null){
            turnoEncontrado = modelMapper.map(turnoBuscado, TurnoSalidaDto.class);
            LOGGER.info("Paciente encontrado: {}", JsonPrinter.toString(turnoEncontrado));
        } else LOGGER.error("No se ha encontrado el paciente con id {}", id);

        return turnoEncontrado;
    }
    public List<TurnoSalidaDto> listarTurnos() {
        List<TurnoSalidaDto> turnoSalidaDtos = turnoRepository.findAll()
                .stream()
                .map(turno -> modelMapper.map(turno, TurnoSalidaDto.class))
                .toList();
        LOGGER.info("Listado de todos los turnos: {}", JsonPrinter.toString(turnoSalidaDtos));
        return turnoSalidaDtos;
    }
    public void eliminarTurno(Long id) throws ResourceNotFoundException {
        if (!turnoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el turno con id " + id);
        }else {
            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el turno con id {}", id);
        }
    }
    @Override
    public TurnoSalidaDto actualizarTurno(TurnoEntradaDto turnoDto, Long id) throws ResourceNotFoundException {
        Turno turnoAActualizar = turnoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No fue posible actualizar el turno porque no se encuentra en la base de datos"));
        Paciente paciente = modelMapper.map(pacienteService.buscarPacientePorId(turnoDto.getPacienteId()), Paciente.class);
        Odontologo odontologo = modelMapper.map(odontologoService.buscarOdontologoPorId(turnoDto.getOdontologoId()), Odontologo.class);

        if (paciente == null || odontologo == null) {
            throw new BadRequestException("El paciente u odontólogo no existe");
        }

        turnoAActualizar.setPaciente(paciente);
        turnoAActualizar.setOdontologo(odontologo);
        turnoAActualizar.setFechaTurno(turnoDto.getFechaTurno());

        Turno turnoActualizado = turnoRepository.save(turnoAActualizar);
        LOGGER.warn("Turno actualizado: {}", JsonPrinter.toString(turnoActualizado));

        return modelMapper.map(turnoActualizado, TurnoSalidaDto.class);
    }
    private void configureMappings() {
        modelMapper.typeMap(TurnoEntradaDto.class, Turno.class)
                .addMappings(mapper -> {
                    mapper.map(TurnoEntradaDto::getPacienteId, Turno::setPaciente);
                    mapper.map(TurnoEntradaDto::getOdontologoId, Turno::setOdontologo);
                });

        modelMapper.typeMap(Turno.class, TurnoSalidaDto.class)
                .addMappings(mapper -> {
                    mapper.map(Turno::getPaciente, TurnoSalidaDto::setPacienteSalidaDto);
                    mapper.map(Turno::getOdontologo, TurnoSalidaDto::setOdontologoSalidaDto);
                });
    }

}
