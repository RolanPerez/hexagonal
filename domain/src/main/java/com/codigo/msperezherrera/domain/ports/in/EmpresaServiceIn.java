package com.codigo.msperezherrera.domain.ports.in;
import com.codigo.msperezherrera.domain.aggregate.dto.EmpresaDto;
import com.codigo.msperezherrera.domain.aggregate.request.EmpresaRequest;

import java.util.List;
import java.util.Optional;

public interface EmpresaServiceIn {
    EmpresaDto crearEmpresaIn(EmpresaRequest empresaRequest);
    Optional<EmpresaDto> buscarXIdIn(Long id);
    List<EmpresaDto> obtenerTodosIn();
    EmpresaDto actualizarIn(Long id, EmpresaRequest empresaRequest);
    EmpresaDto deleteIn(Long id);
}
