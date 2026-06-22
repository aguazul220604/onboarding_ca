package com.casystem.onboarding_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "datos_onboarding")
public class DatosOnboarding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @Column(name = "telefono_encriptado")
    private String telefono;

    @Column(name = "fecha_nacimiento_encriptada")
    private String fechaNacimiento;

    @Column(name = "calle_encriptada")
    private String calle;

    @Column(name = "ciudad_encriptada")
    private String ciudad;

    @Column(name = "codigo_postal_encriptado")
    private String codigoPostal;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "numero_documento_encriptado")
    private String numeroDocumento;

    @Column(nullable = false)
    private Boolean activo = true;
}