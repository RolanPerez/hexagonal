package com.codigo.msperezherrera.application.controller;

import com.codigo.msperezherrera.domain.aggregate.dto.EmpresaDto;
import com.codigo.msperezherrera.domain.aggregate.request.EmpresaRequest;
import com.codigo.msperezherrera.domain.ports.in.EmpresaServiceIn;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ms-perez-herrera/v1/empresa")
@AllArgsConstructor
public class EmpresaController {
    private final EmpresaServiceIn empresaServiceIn;

    @PostMapping
    public ResponseEntity<EmpresaDto> registrar(@RequestBody EmpresaRequest requestEmpresa) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaServiceIn.crearEmpresaIn(requestEmpresa));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDto> bsucarXid(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaServiceIn.buscarXIdIn(id).get());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<EmpresaDto>> buscartodos() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaServiceIn.obtenerTodosIn());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDto> actualizar(@PathVariable Long id, @RequestBody EmpresaRequest empresaRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaServiceIn.actualizarIn(id, empresaRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmpresaDto> delete(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaServiceIn.deleteIn(id));
    }


}
