package co.edu.unicartagena.nomina.calculo.deduccion;

import co.edu.unicartagena.nomina.dominio.comun.ConceptoNomina;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.empleado.DatosEmpleado;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoAsalariado;
import co.edu.unicartagena.nomina.dominio.empleado.NivelRiesgoArl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Deducciones obligatorias")
class DeduccionesTest {

    private static EmpleadoAsalariado empleadoConRiesgo(NivelRiesgoArl nivel) {
        return new EmpleadoAsalariado(
                new DatosEmpleado("1001", "Ana Pérez", LocalDate.of(2024, 1, 1), nivel), Dinero.de("2000000"));
    }

    @Test
    @DisplayName("Seguro social y pensión descuenta el 4 % del bruto")
    void seguridadSocialCuatroPorCiento() {
        ConceptoNomina concepto = new SeguridadSocialPension()
                .calcular(empleadoConRiesgo(NivelRiesgoArl.CLASE_I), Dinero.de("2000000"));

        assertEquals(Dinero.de("80000"), concepto.valor());
    }

    @Test
    @DisplayName("ARL clase I descuenta el 0,522 % del bruto")
    void arlClaseUno() {
        ConceptoNomina concepto = new Arl().calcular(empleadoConRiesgo(NivelRiesgoArl.CLASE_I), Dinero.de("2000000"));

        assertEquals(Dinero.de("10440"), concepto.valor());
        assertEquals("ARL clase I (0,522 %)", concepto.descripcion());
    }

    @Test
    @DisplayName("ARL clase III descuenta el 2,436 % del bruto")
    void arlClaseTres() {
        ConceptoNomina concepto = new Arl().calcular(empleadoConRiesgo(NivelRiesgoArl.CLASE_III), Dinero.de("2000000"));

        assertEquals(Dinero.de("48720"), concepto.valor());
    }
}
