package co.edu.unicartagena.nomina.dominio.empleado;

import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Empleado por horas")
class EmpleadoPorHorasTest {

    private static final LocalDate CORTE = LocalDate.of(2026, 11, 30);
    private static final DatosEmpleado DATOS =
            new DatosEmpleado("2002", "Luis Gómez", LocalDate.of(2018, 3, 1));

    private static EmpleadoPorHoras conHoras(String horas) {
        return new EmpleadoPorHoras(DATOS, Dinero.de("20000"), new BigDecimal(horas), false);
    }

    @Test
    @DisplayName("Hasta 40 horas se pagan a la tarifa base")
    void sinHorasExtra() {
        EmpleadoPorHoras empleado = conHoras("40");

        assertEquals(Dinero.de("800000"), empleado.calcularSalarioBruto(CORTE));
        assertEquals(BigDecimal.ZERO, empleado.horasExtra());
    }

    @Test
    @DisplayName("Las horas por encima de 40 se pagan a 1.5 veces la tarifa")
    void conHorasExtra() {
        EmpleadoPorHoras empleado = conHoras("48");

        // 40 h x 20.000 + 8 h x 30.000 = 1.040.000
        assertEquals(Dinero.de("1040000"), empleado.calcularSalarioBruto(CORTE));
        assertEquals(2, empleado.calcularDevengos(CORTE).size());
    }

    @Test
    @DisplayName("Admite horas fraccionarias")
    void horasFraccionarias() {
        EmpleadoPorHoras empleado = conHoras("40.5");

        // 40 h x 20.000 + 0.5 h x 30.000 = 815.000
        assertEquals(Dinero.de("815000"), empleado.calcularSalarioBruto(CORTE));
    }

    @Test
    @DisplayName("Con cero horas el bruto es cero")
    void ceroHoras() {
        assertEquals(Dinero.CERO, conHoras("0").calcularSalarioBruto(CORTE));
    }

    @Test
    @DisplayName("No recibe bonos aunque tenga mucha antigüedad")
    void noRecibeBonos() {
        EmpleadoPorHoras empleado = conHoras("10");

        assertEquals(1, empleado.calcularDevengos(CORTE).size());
    }

    @Test
    @DisplayName("Rechaza horas trabajadas negativas al crear y al registrar")
    void rechazaHorasNegativas() {
        assertThrows(ValidacionNominaException.class, () -> conHoras("-1"));

        EmpleadoPorHoras empleado = conHoras("10");
        assertThrows(ValidacionNominaException.class,
                () -> empleado.registrarHorasTrabajadas(new BigDecimal("-5")));
        assertEquals(new BigDecimal("10"), empleado.getHorasTrabajadas());
    }

    @Test
    @DisplayName("Rechaza una tarifa negativa")
    void rechazaTarifaNegativa() {
        assertThrows(ValidacionNominaException.class,
                () -> new EmpleadoPorHoras(DATOS, Dinero.de("-100"), BigDecimal.TEN, false));
    }
}
