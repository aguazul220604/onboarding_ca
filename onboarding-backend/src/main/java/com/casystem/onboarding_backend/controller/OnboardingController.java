package com.casystem.onboarding_backend.controller;

import com.casystem.onboarding_backend.model.DatosOnboarding;
import com.casystem.onboarding_backend.service.OnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
@CrossOrigin(origins = "http://localhost:4200")
public class OnboardingController {

    @Autowired
    private OnboardingService onboardingService;

    @PostMapping("/guardar/{usuarioId}")
    public ResponseEntity<?> guardarOActualizarDatos(@PathVariable Long usuarioId, @RequestBody DatosOnboarding datos) {
        try {
            DatosOnboarding resultado = onboardingService.guardarOModificarDatos(usuarioId, datos);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/datos/{usuarioId}")
    public ResponseEntity<?> obtenerDatosCliente(@PathVariable Long usuarioId) {
        return onboardingService.obtenerDatosDesencriptados(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/eliminar/{usuarioId}")
    public ResponseEntity<?> darDeBajaCliente(@PathVariable Long usuarioId) {
        try {
            onboardingService.eliminarUsuarioLogico(usuarioId);
            return ResponseEntity.ok("Usuario y datos asociados dados de baja");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
}