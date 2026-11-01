package co.edu.unicartagena.nomina.dominio.empleado;

import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Empleado temporal")
class EmpleadoTemporalTest {

    private static final LocalDate INGRESO = LocalDate.of(2020, 1, 1);
    private static final LocalDate FIN = LocalDate.of(2026, 12, 31);

    private static EmpleadoTemporal temporal() {
        return new EmpleadoTemporal(new DatosEmpleado("4004", "Pedro Díaz", INGRESO), Dinero.de("2800000"), FIN);
    }

    @Test
    @DisplayName("Devenga solo el salario fijo, sin bonos aunque supere 5 años")
    void soloSalarioFijo() {
        LocalDate corte = LocalDate.of(2026, 11, 30);

        assertEquals(Dinero.de("2800000"), temporal().calcularSalarioBruto(corte));
        assertEquals(1, temporal().calcularDevengos(corte).size());
    }

    @Test
    @DisplayName("Está vigente entre la fecha de ingreso y el último día del contrato")
    void vigenciaDelContrato() {
        EmpleadoTemporal empleado = temporal();

        assertTrue(empleado.estaVigenteEn(INGRESO));
        assertTrue(empleado.estaVigenteEn(FIN));
        assertFalse(empleado.estaVigenteEn(FIN.plusDays(1)));
        assertFalse(empleado.estaVigenteEn(INGRESO.minusDays(1)));
    }

    @Test
    @DisplayName("Rechaza un contrato que termina antes de empezar")
    void rechazaFechasInvertidas() {
        DatosEmpleado datos = new DatosEmpleado("4004", "Pedro Díaz", INGRESO);

        assertThrows(ValidacionNominaException.class,
                () -> new EmpleadoTemporal(datos, Dinero.de("2800000"), INGRESO.minusDays(1)));
    }
}
