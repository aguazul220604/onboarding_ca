package com.casystem.onboarding_backend.repository;

import com.casystem.onboarding_backend.model.DatosOnboarding;
import com.casystem.onboarding_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DatosOnboardingRepository extends JpaRepository<DatosOnboarding, Long> {
    Optional<DatosOnboarding> findByUsuarioAndActivoTrue(Usuario usuario);
}