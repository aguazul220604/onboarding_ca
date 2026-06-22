package com.casystem.onboarding_backend.controller;

import com.casystem.onboarding_backend.model.Usuario;
import com.casystem.onboarding_backend.repository.UsuarioRepository;
import com.casystem.onboarding_backend.service.TrazabilidadService;
import com.casystem.onboarding_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TrazabilidadService trazabilidadService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            if (usuarioRepository.findByUsernameAndActivoTrue(usuario.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body("El nombre de usuario ya se encuentra registrado");
            }

            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            Usuario nuevoUsuario = usuarioRepository.save(usuario);

            trazabilidadService.registrarAccion(nuevoUsuario.getId(), "Cuenta creada con éxito");

            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginRequest) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsernameAndActivoTrue(loginRequest.getUsername());

            if (usuarioOpt.isPresent()
                    && passwordEncoder.matches(loginRequest.getPassword(), usuarioOpt.get().getPassword())) {
                Usuario usuario = usuarioOpt.get();

                trazabilidadService.registrarAccion(usuario.getId(), "Inicio de sesión exitoso");

                String token = jwtUtil.generateToken(usuario.getUsername());

                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("username", usuario.getUsername());
                response.put("nombreCompleto", usuario.getNombreCompleto());
                response.put("usuarioId", usuario.getId().toString());

                return ResponseEntity.ok(response);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales de acceso incorrectas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error");
        }
    }
}