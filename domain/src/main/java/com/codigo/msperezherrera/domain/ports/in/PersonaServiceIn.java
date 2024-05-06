package com.codigo.msperezherrera.domain.ports.in;

import com.codigo.msperezherrera.domain.aggregate.dto.PersonaDto;
import com.codigo.msperezherrera.domain.aggregate.request.PersonaRequest;
import java.util.List;
import java.util.Optional;

public interface PersonaServiceIn {
    PersonaDto crearPersonaIn(PersonaRequest personaRequest);
    Optional<PersonaDto> buscarXIdIn(Long id);
    List<PersonaDto> obtenerTodosIn();
    PersonaDto actualizarIn(Long id, PersonaRequest personaRequest);
    PersonaDto deleteIn(Long id);
}
