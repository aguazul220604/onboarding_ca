package com.casystem.onboarding_backend;

import com.casystem.onboarding_backend.model.DatosOnboarding;
import com.casystem.onboarding_backend.model.Usuario;
import com.casystem.onboarding_backend.repository.DatosOnboardingRepository;
import com.casystem.onboarding_backend.repository.UsuarioRepository;
import com.casystem.onboarding_backend.service.OnboardingService;
import com.casystem.onboarding_backend.service.TrazabilidadService;
import com.casystem.onboarding_backend.util.CryptoUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OnboardingServiceTest {

    @Mock
    private DatosOnboardingRepository onboardingRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TrazabilidadService trazabilidadService;

    @InjectMocks
    private OnboardingService onboardingService;

    private Usuario usuarioMock;
    private DatosOnboarding datosInput;

    @BeforeEach
    void setUp() {
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setUsername("testuser");
        usuarioMock.setNombreCompleto("Juan Pérez");

        datosInput = new DatosOnboarding();
        datosInput.setTelefono("5512345678");
        datosInput.setFechaNacimiento("1990-01-01");
        datosInput.setCalle("Av. Reforma 123");
        datosInput.setCiudad("CDMX");
        datosInput.setCodigoPostal("06000");
        datosInput.setTipoDocumento("INE");
        datosInput.setNumeroDocumento("ABCD123456XYZ");
    }

    @Test
    void guardarOModificarDatos_NuevoRegistro_Exitoso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        when(onboardingRepository.findByUsuarioAndActivoTrue(usuarioMock)).thenReturn(Optional.empty());
        when(onboardingRepository.save(any(DatosOnboarding.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DatosOnboarding resultado = onboardingService.guardarOModificarDatos(1L, datosInput);

        assertNotNull(resultado);
        assertEquals(usuarioMock, resultado.getUsuario());

        assertNotEquals("5512345678", resultado.getTelefono());
        assertEquals("5512345678", CryptoUtil.decrypt(resultado.getTelefono()));
        assertEquals("CDMX", CryptoUtil.decrypt(resultado.getCiudad()));

        verify(trazabilidadService, times(1))
                .registrarAccion(eq(1L), anyString());
        verify(onboardingRepository, times(1)).save(any(DatosOnboarding.class));
    }

    @Test
    void guardarOModificarDatos_UsuarioNoExiste_LanzaExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            onboardingService.guardarOModificarDatos(99L, datosInput);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));

        verify(onboardingRepository, never()).save(any(DatosOnboarding.class));
        verify(trazabilidadService, never()).registrarAccion(anyLong(), anyString());
    }

    @Test
    void obtenerDatosDesencriptados_Exitoso() {
        DatosOnboarding datosCifradosInDb = new DatosOnboarding();
        datosCifradosInDb.setUsuario(usuarioMock);
        datosCifradosInDb.setTelefono(CryptoUtil.encrypt("5512345678"));
        datosCifradosInDb.setCalle(CryptoUtil.encrypt("Av. Reforma 123"));
        datosCifradosInDb.setTipoDocumento("INE");
        datosCifradosInDb.setNumeroDocumento(CryptoUtil.encrypt("ABCD123456XYZ"));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        when(onboardingRepository.findByUsuarioAndActivoTrue(usuarioMock)).thenReturn(Optional.of(datosCifradosInDb));

        Optional<DatosOnboarding> resultadoOpt = onboardingService.obtenerDatosDesencriptados(1L);

        assertTrue(resultadoOpt.isPresent());
        DatosOnboarding descifrados = resultadoOpt.get();
        assertEquals("5512345678", descifrados.getTelefono());
        assertEquals("Av. Reforma 123", descifrados.getCalle());
        assertEquals("ABCD123456XYZ", descifrados.getNumeroDocumento());
    }
}