package com.casystem.onboarding_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "logs_trazabilidad")
public class LogTrazabilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(nullable = false, length = 100)
    private String accion;

    @Column(name = "ip_origen", nullable = false, length = 45)
    private String ipOrigen;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}