package com.backend.clinica.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import com.backend.clinica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinica.dto.salida.OdontologoSalidaDto;
import com.backend.clinica.entity.Odontologo;
import com.backend.clinica.exceptions.ResourceNotFoundException;
import com.backend.clinica.repository.OdontologoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class OdontologoServiceTest {

    private static OdontologoService odontologoService;
    private static OdontologoRepository odontologoRepositoryMock;
    private static ModelMapper modelMapper;
    private static Odontologo odontologo;
    private static OdontologoEntradaDto odontologoEntradaDto;

    @BeforeAll
    static void setUp() {
        odontologoRepositoryMock = mock(OdontologoRepository.class);
        modelMapper = new ModelMapper();
        odontologoService = new OdontologoService(odontologoRepositoryMock, modelMapper);

        odontologo = new Odontologo(1L, "1234", "Pedro", "Pica");
        odontologoEntradaDto = new OdontologoEntradaDto( "1234","Pica", "Pedro");
    }

    @Test
    void deberiaRegistrarOdontologo() {
        when(odontologoRepositoryMock.save(any(Odontologo.class))).thenReturn(odontologo);

        OdontologoSalidaDto odontologoSalidaDto = odontologoService.registrarOdontologo(odontologoEntradaDto);
        assertNotNull(odontologoSalidaDto);
        assertEquals("Pedro", odontologoSalidaDto.getNombre());
        verify(odontologoRepositoryMock, times(1)).save(any(Odontologo.class));
    }
    @Test
    void deberiaListarOdontologos() {
        when(odontologoRepositoryMock.findAll()).thenReturn(List.of(odontologo));

        List<OdontologoSalidaDto> odontologos = odontologoService.listarOdontologos();
        assertFalse(odontologos.isEmpty());
        assertEquals(1, odontologos.size());
        verify(odontologoRepositoryMock, times(1)).findAll();
    }

    @Test
    void deberiaEliminarOdontologo() throws ResourceNotFoundException {
        when(odontologoRepositoryMock.existsById(1L)).thenReturn(true);
        doNothing().when(odontologoRepositoryMock).deleteById(1L);
        odontologoService.eliminarOdontologo(1L);
        verify(odontologoRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    void deberiaLanzarResourceNotFoundExceptionAlEliminarOdontologoInexistente() {
        when(odontologoRepositoryMock.existsById(2L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> odontologoService.eliminarOdontologo(2L));
        verify(odontologoRepositoryMock, times(0)).deleteById(1L);
    }

    @Test
    void deberiaActualizarOdontologo() {
        when(odontologoRepositoryMock.findById(1L)).thenReturn(Optional.of(odontologo));
        when(odontologoRepositoryMock.save(any(Odontologo.class))).thenReturn(odontologo);
        OdontologoSalidaDto odontologoActualizado = odontologoService.actualizarOdontologo(odontologoEntradaDto, 1L);
        assertNotNull(odontologoActualizado);
        assertEquals("Pedro", odontologoActualizado.getNombre());
        verify(odontologoRepositoryMock, times(1)).save(any(Odontologo.class));
    }
}