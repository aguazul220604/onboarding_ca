package com.casystem.onboarding_backend.service;

import com.casystem.onboarding_backend.model.LogTrazabilidad;
import com.casystem.onboarding_backend.repository.LogTrazabilidadRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrazabilidadService {

    @Autowired
    private LogTrazabilidadRepository logRepository;

    @Autowired
    private HttpServletRequest request;

    public void registrarAccion(Long usuarioId, String accion) {
        LogTrazabilidad log = new LogTrazabilidad();
        log.setUsuarioId(usuarioId);
        log.setAccion(accion);

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        log.setIpOrigen(ip);

        logRepository.save(log);
    }
}