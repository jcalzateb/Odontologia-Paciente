package com.backend.clinica.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import com.backend.clinica.dto.entrada.TurnoEntradaDto;
import com.backend.clinica.dto.salida.OdontologoSalidaDto;
import com.backend.clinica.dto.salida.PacienteSalidaDto;
import com.backend.clinica.dto.salida.TurnoSalidaDto;
import com.backend.clinica.entity.Domicilio;
import com.backend.clinica.entity.Odontologo;
import com.backend.clinica.entity.Paciente;
import com.backend.clinica.entity.Turno;
import com.backend.clinica.exceptions.BadRequestException;
import com.backend.clinica.exceptions.ResourceNotFoundException;
import com.backend.clinica.repository.TurnoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TurnoServiceTest {
    private static TurnoService turnoService;
    private static TurnoRepository turnoRepositoryMock;
    private static ModelMapper modelMapper;
    private static Turno turno;
    private static TurnoEntradaDto turnoEntradaDto;
    private static Odontologo odontologo;
    private static Paciente paciente;
    private static PacienteService pacienteServiceMock;
    private static OdontologoService odontologoServiceMock;


    @BeforeAll
    static void setUp() {
        paciente = new Paciente(1L, "Juan", "Luis", 123456, LocalDate.of(2023, 5, 12),
                new Domicilio(1L, "Patio", 654321, "norte", "Valle"));
        odontologo = new Odontologo(2L, "1234", "Pedro", "Pica");
        turnoRepositoryMock = mock(TurnoRepository.class);
        pacienteServiceMock = mock(PacienteService.class);
        odontologoServiceMock = mock(OdontologoService.class);
        modelMapper = new ModelMapper();
        turnoService = new TurnoService(modelMapper, turnoRepositoryMock, pacienteServiceMock, odontologoServiceMock);

        turno = new Turno(1L, paciente, odontologo, LocalDate.of(2023, 9, 15));
        turnoEntradaDto = new TurnoEntradaDto();
        turnoEntradaDto.setPacienteId(1L);
        turnoEntradaDto.setOdontologoId(2L);
        turnoEntradaDto.setFechaTurno(LocalDate.of(2023, 9, 15));
    }

    @Test
    void deberiaRegistrarTurno() {
        when(pacienteServiceMock.buscarPacientePorId(1L)).thenReturn(modelMapper.map(paciente, PacienteSalidaDto.class));
        when(odontologoServiceMock.buscarOdontologoPorId(2L)).thenReturn(modelMapper.map(odontologo, OdontologoSalidaDto.class));
        when(turnoRepositoryMock.save(any(Turno.class))).thenReturn(turno);

        TurnoSalidaDto salidaDto = turnoService.registrarTurno(turnoEntradaDto);
        assertNotNull(salidaDto);
        assertEquals(turno.getFechaTurno(), salidaDto.getFechaTurno());
        verify(turnoRepositoryMock, times(1)).save(any(Turno.class));
    }

    @Test
    void deberiaLanzarBadRequestExceptionCuandoPacienteNoExiste() {
        when(pacienteServiceMock.buscarPacientePorId(1L)).thenReturn(null);
        assertThrows(BadRequestException.class, () -> turnoService.registrarTurno(turnoEntradaDto));
        verify(turnoRepositoryMock, times(0)).save(any(Turno.class));
    }

    @Test
    void deberiaListarTurnos() {
        when(turnoRepositoryMock.findAll()).thenReturn(List.of(turno));

        List<TurnoSalidaDto> turnos = turnoService.listarTurnos();
        assertFalse(turnos.isEmpty());
        assertEquals(1, turnos.size());
        verify(turnoRepositoryMock, times(1)).findAll();
    }

    @Test
    void deberiaEliminarTurno() throws ResourceNotFoundException {
        when(turnoRepositoryMock.existsById(1L)).thenReturn(true);
        doNothing().when(turnoRepositoryMock).deleteById(1L);
        turnoService.eliminarTurno(1L);
        verify(turnoRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    void deberiaLanzarResourceNotFoundExceptionAlEliminarTurnoNoExistente() {
        when(turnoRepositoryMock.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> turnoService.eliminarTurno(1L));
        verify(turnoRepositoryMock, times(0)).deleteById(1L);
    }

    @Test
    void deberiaActualizarTurno() {
        when(turnoRepositoryMock.findById(1L)).thenReturn(Optional.of(turno));
        when(turnoRepositoryMock.save(any(Turno.class))).thenReturn(turno);

        TurnoSalidaDto actualizado = turnoService.actualizarTurno(turnoEntradaDto, 1L);
        assertNotNull(actualizado);
        verify(turnoRepositoryMock, times(1)).save(any(Turno.class));
    }
}