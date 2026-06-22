package com.casystem.onboarding_backend.repository;

import com.casystem.onboarding_backend.model.LogTrazabilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogTrazabilidadRepository extends JpaRepository<LogTrazabilidad, Long> {
}