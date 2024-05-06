package com.codigo.msperezherrera.infraestructure.dao;

import com.codigo.msperezherrera.infraestructure.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Long> {

}
