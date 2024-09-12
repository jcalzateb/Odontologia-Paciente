package com.backend.clinica.controller;

import com.backend.clinica.dto.entrada.PacienteEntradaDto;
import com.backend.clinica.dto.salida.PacienteSalidaDto;

import com.backend.clinica.exceptions.ResourceNotFoundException;
import com.backend.clinica.service.IPacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
@CrossOrigin
public class PacienteController {

    private IPacienteService pacienteService;
    public PacienteController(IPacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }
    @PostMapping("/registrar")
    public ResponseEntity<PacienteSalidaDto> registrarPaciente(@RequestBody @Valid PacienteEntradaDto pacienteEntradaDto){
        PacienteSalidaDto pacienteSalidaDto = pacienteService.registrarPaciente(pacienteEntradaDto);
        return new ResponseEntity<>(pacienteSalidaDto, HttpStatus.CREATED);
    }
    @GetMapping("/listar")
    public ResponseEntity<List<PacienteSalidaDto>> listarPacientes(){
        return new ResponseEntity<>(pacienteService.listarPacientes(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PacienteSalidaDto> buscarPacientePorId(@PathVariable Long id){
        return new ResponseEntity<>(pacienteService.buscarPacientePorId(id), HttpStatus.OK);
    }
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<PacienteSalidaDto> actualizarPaciente(@RequestBody @Valid PacienteEntradaDto paciente, @PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(pacienteService.actualizarPaciente(paciente, id), HttpStatus.OK);
    }
    @DeleteMapping("/eliminar")//localhost:8080/pacientes/eliminar?id=x
    public ResponseEntity<String> eliminarPaciente(@RequestParam Long id) throws ResourceNotFoundException {
        pacienteService.eliminarPaciente(id);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Paciente eliminado correctamente");
    }
}
