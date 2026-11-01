package co.edu.unicartagena.nomina.dominio.empleado;

import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Datos del empleado")
class DatosEmpleadoTest {

    @Test
    @DisplayName("Usa la clase de riesgo ARL I por defecto")
    void claseRiesgoPorDefecto() {
        DatosEmpleado datos = new DatosEmpleado("1001", "Ana Pérez", LocalDate.of(2020, 1, 15));

        assertEquals(NivelRiesgoArl.CLASE_I, datos.nivelRiesgoArl());
    }

    @Test
    @DisplayName("Elimina espacios sobrantes del nombre")
    void normalizaNombre() {
        DatosEmpleado datos = new DatosEmpleado("1001", "  Ana Pérez ", LocalDate.of(2020, 1, 15));

        assertEquals("Ana Pérez", datos.nombre());
    }

    @Test
    @DisplayName("Exige documento y fecha de ingreso")
    void exigeCamposObligatorios() {
        assertThrows(ValidacionNominaException.class,
                () -> new DatosEmpleado("", "Ana Pérez", LocalDate.of(2020, 1, 15)));
        assertThrows(ValidacionNominaException.class,
                () -> new DatosEmpleado("1001", "Ana Pérez", null));
    }
}
