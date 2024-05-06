package com.codigo.msperezherrera.domain.impl;

import com.codigo.msperezherrera.domain.aggregate.dto.EmpresaDto;
import com.codigo.msperezherrera.domain.aggregate.request.EmpresaRequest;
import com.codigo.msperezherrera.domain.ports.in.EmpresaServiceIn;
import com.codigo.msperezherrera.domain.ports.out.EmpresaServiceOut;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmpresaServiceImpl implements EmpresaServiceIn {

    private final EmpresaServiceOut empresaServiceOut; // Tiene que implementar Adapter

    @Override
    public EmpresaDto crearEmpresaIn(EmpresaRequest empresaRequest) {
        return empresaServiceOut.crearEmpresaOut(empresaRequest);
    }

    @Override
    public Optional<EmpresaDto> buscarXIdIn(Long id) {
        return empresaServiceOut.buscarXIdOut(id);
    }

    @Override
    public List<EmpresaDto> obtenerTodosIn() {
        return empresaServiceOut.obtenerTodosOut();
    }

    @Override
    public EmpresaDto actualizarIn(Long id, EmpresaRequest empresaRequest) {
        return empresaServiceOut.actualizarOut(id, empresaRequest);
    }

    @Override
    public EmpresaDto deleteIn(Long id) {
        return empresaServiceOut.deleteOut(id);
    }
}
