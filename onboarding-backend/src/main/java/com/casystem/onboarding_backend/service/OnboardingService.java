package com.casystem.onboarding_backend.service;

import com.casystem.onboarding_backend.model.DatosOnboarding;
import com.casystem.onboarding_backend.model.Usuario;
import com.casystem.onboarding_backend.repository.DatosOnboardingRepository;
import com.casystem.onboarding_backend.repository.UsuarioRepository;
import com.casystem.onboarding_backend.util.CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class OnboardingService {

    @Autowired
    private DatosOnboardingRepository onboardingRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TrazabilidadService trazabilidadService;

    @Transactional
    public DatosOnboarding guardarOModificarDatos(Long usuarioId, DatosOnboarding nuevosDatos) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        DatosOnboarding datos = onboardingRepository.findByUsuarioAndActivoTrue(usuario)
                .orElse(new DatosOnboarding());

        datos.setUsuario(usuario);

        datos.setTelefono(CryptoUtil.encrypt(nuevosDatos.getTelefono()));
        datos.setFechaNacimiento(CryptoUtil.encrypt(nuevosDatos.getFechaNacimiento()));

        datos.setCalle(CryptoUtil.encrypt(nuevosDatos.getCalle()));
        datos.setCiudad(CryptoUtil.encrypt(nuevosDatos.getCiudad()));
        datos.setCodigoPostal(CryptoUtil.encrypt(nuevosDatos.getCodigoPostal()));

        datos.setTipoDocumento(nuevosDatos.getTipoDocumento());
        datos.setNumeroDocumento(CryptoUtil.encrypt(nuevosDatos.getNumeroDocumento()));

        DatosOnboarding guardado = onboardingRepository.save(datos);

        String tipoAccion = (datos.getId() == null) ? "Información registrada"
                : "Información modificada";
        trazabilidadService.registrarAccion(usuarioId, tipoAccion);

        return guardado;
    }

    public Optional<DatosOnboarding> obtenerDatosDesencriptados(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return onboardingRepository.findByUsuarioAndActivoTrue(usuario).map(datos -> {
            DatosOnboarding desencriptados = new DatosOnboarding();
            desencriptados.setId(datos.getId());
            desencriptados.setUsuario(datos.getUsuario());

            desencriptados.setTelefono(CryptoUtil.decrypt(datos.getTelefono()));
            desencriptados.setFechaNacimiento(CryptoUtil.decrypt(datos.getFechaNacimiento()));
            desencriptados.setCalle(CryptoUtil.decrypt(datos.getCalle()));
            desencriptados.setCiudad(CryptoUtil.decrypt(datos.getCiudad()));
            desencriptados.setCodigoPostal(CryptoUtil.decrypt(datos.getCodigoPostal()));
            desencriptados.setTipoDocumento(datos.getTipoDocumento());
            desencriptados.setNumeroDocumento(CryptoUtil.decrypt(datos.getNumeroDocumento()));

            return desencriptados;
        });
    }

    @Transactional
    public void eliminarUsuarioLogico(Long usuarioId) {
        usuarioRepository.darDeBajaUsuario(usuarioId.intValue());
        trazabilidadService.registrarAccion(usuarioId, "lógica de usuario ejecutada mediante Stored Procedure");
    }
}