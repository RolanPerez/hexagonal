package com.codigo.msperezherrera.infraestructure.dao;

import com.codigo.msperezherrera.infraestructure.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<PersonaEntity, Long> {

}
