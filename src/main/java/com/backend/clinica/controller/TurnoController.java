package com.backend.clinica.controller;

import com.backend.clinica.dto.entrada.TurnoEntradaDto;
import com.backend.clinica.dto.salida.TurnoSalidaDto;
import com.backend.clinica.exceptions.ResourceNotFoundException;
import com.backend.clinica.service.ITurnoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/turnos")
@CrossOrigin
public class TurnoController {
    private final ITurnoService turnoService;
    public TurnoController(ITurnoService turnoService) {
        this.turnoService = turnoService;
    }
    @PostMapping("/registrar")
    public ResponseEntity<TurnoSalidaDto> registrarTurno(@RequestBody @Valid TurnoEntradaDto turnoEntradaDto) {
        TurnoSalidaDto turnoSalidaDto = turnoService.registrarTurno(turnoEntradaDto);
        return new ResponseEntity<>(turnoSalidaDto, HttpStatus.CREATED);
    }
    @GetMapping("/listar")
    public ResponseEntity<List<TurnoSalidaDto>> listarTurnos() {
        return new ResponseEntity<>(turnoService.listarTurnos(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TurnoSalidaDto> buscarTurnoPorId(@PathVariable Long id) {
        return new ResponseEntity<>(turnoService.buscarTurnoPorId(id), HttpStatus.OK);
    }
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<TurnoSalidaDto> actualizarTurno(@RequestBody @Valid TurnoEntradaDto turno, @PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(turnoService.actualizarTurno(turno, id), HttpStatus.OK);
    }
    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarTurno(@RequestParam Long id) throws ResourceNotFoundException {
        turnoService.eliminarTurno(id);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Turno eliminado correctamente");
    }
}
