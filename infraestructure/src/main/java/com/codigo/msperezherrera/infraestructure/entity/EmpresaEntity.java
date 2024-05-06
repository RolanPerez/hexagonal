package com.codigo.msperezherrera.infraestructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "empresa_info")
@Getter
@Setter

public class EmpresaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "razonSocial", length = 255)
    private String razonSocial;

    @Column(name = "tipoDocumento", length = 5)
    private String tipoDocumento;

    @Column(name = "numeroDocumento", length = 20)
    private String numeroDocumento;

    @Column(name = "estado")
    private Integer estado;

    @Column(name = "condicion", length = 50)
    private String condicion;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "distrito", length = 100)
    private String distrito;

    @Column(name = "provincia", length = 100)
    private String provincia;

    @Column(name = "departamento", length = 100)
    private String departamento;

    @Column(name = "EsAgenteRetencion")
    private Boolean EsAgenteRetencion;

    @Column(name = "usuaCrea", length = 45)
    private String usuaCrea;

    @Column(name = "dateCreate")
    private Timestamp dateCreate;

    @Column(name = "usuaModif", length = 45)
    private String usuaModif;

    @Column(name = "dateModif")
    private Timestamp dateModif;

    @Column(name = "usuaDelet", length = 45)
    private String usuaDelet;

    @Column(name = "dateDelet")
    private Timestamp dateDelet;

    @OneToMany(mappedBy = "empresa")
    private Set<PersonaEntity> personas;
}
