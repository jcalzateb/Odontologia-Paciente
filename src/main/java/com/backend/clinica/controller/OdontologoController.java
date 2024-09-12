package com.backend.clinica.controller;

import com.backend.clinica.dto.entrada.OdontologoEntradaDto;
import com.backend.clinica.dto.salida.OdontologoSalidaDto;

import com.backend.clinica.exceptions.ResourceNotFoundException;
import com.backend.clinica.service.IOdontologoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController {
    private final IOdontologoService  odontologoService;
    public OdontologoController(IOdontologoService  odontologoService) {
        this.odontologoService = odontologoService;
    }
    @PostMapping("/registrar")
    public ResponseEntity<OdontologoSalidaDto> registrarOdontologo(@RequestBody @Valid OdontologoEntradaDto odontologoEntradaDto) {
        OdontologoSalidaDto odontologoSalidaDto = odontologoService.registrarOdontologo(odontologoEntradaDto);
        return new ResponseEntity<>(odontologoSalidaDto, HttpStatus.CREATED);
    }
    @GetMapping("/listar")
    public ResponseEntity<List<OdontologoSalidaDto>> listarOdontologos() {
        return new ResponseEntity<>(odontologoService.listarOdontologos(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<OdontologoSalidaDto> buscarOdontologoPorId(@PathVariable Long id) {
        OdontologoSalidaDto odontologoSalidaDto = odontologoService.buscarOdontologoPorId(id);
        if (odontologoSalidaDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retorna 404 si no se encuentra
        }
        return new ResponseEntity<>(odontologoSalidaDto, HttpStatus.OK);
    }
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<OdontologoSalidaDto> actualizarOdontologo(@RequestBody @Valid OdontologoEntradaDto odontologo, @PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(odontologoService.actualizarOdontologo(odontologo, id), HttpStatus.OK);
    }
    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarOdontologo(@RequestParam Long id) throws ResourceNotFoundException {
        odontologoService.eliminarOdontologo(id);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Odontólogo eliminado correctamente");
    }
}
