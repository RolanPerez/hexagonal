package com.codigo.msperezherrera.domain.ports.out;

import com.codigo.msperezherrera.domain.aggregate.dto.PersonaDto;
import com.codigo.msperezherrera.domain.aggregate.request.PersonaRequest;

import java.util.List;
import java.util.Optional;

public interface PersonaServiceOut {

    PersonaDto crearPersonaOut(PersonaRequest personaRequest);
    Optional<PersonaDto> buscarXIdOut(Long id);
    List<PersonaDto> obtenerTodosOut();
    PersonaDto actualizarOut(Long id, PersonaRequest personaRequest);
    PersonaDto deleteOut(Long id);

}
