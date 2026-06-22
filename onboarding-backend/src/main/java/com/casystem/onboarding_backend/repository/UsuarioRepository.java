package com.casystem.onboarding_backend.repository;

import com.casystem.onboarding_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsernameAndActivoTrue(String username);

    @Procedure(procedureName = "dar_de_baja_usuario")
    void darDeBajaUsuario(@Param("p_usuario_id") Integer usuarioId);
}