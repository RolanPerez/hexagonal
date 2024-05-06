package com.codigo.msperezherrera.domain.aggregate.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReniecDto {
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipoDocumento;
    private String numeroDocumento;
    private String digitoVerificador;
}
