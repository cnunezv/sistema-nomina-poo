package co.edu.unicartagena.nomina.calculo.beneficio;

import co.edu.unicartagena.nomina.calculo.liquidacion.BeneficioLiquidado;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.empleado.DatosEmpleado;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoAsalariado;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoPorComision;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoPorHoras;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoTemporal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Beneficios adicionales")
class BeneficiosTest {

    private static final LocalDate CORTE = LocalDate.of(2026, 11, 30);
    private static final DatosEmpleado ANTIGUO = new DatosEmpleado("1", "Empleado Antiguo", LocalDate.of(2015, 1, 1));

    private final BonoAlimentacion bonoAlimentacion = new BonoAlimentacion();
    private final FondoAhorro fondoAhorro = new FondoAhorro();

    private static EmpleadoPorHoras porHoras(boolean aceptaFondo) {
        return new EmpleadoPorHoras(ANTIGUO, Dinero.de("20000"), BigDecimal.TEN, aceptaFondo);
    }

    @Test
    @DisplayName("El bono de alimentación aplica a asalariados y por comisión")
    void bonoAlimentacionParaPermanentes() {
        assertTrue(bonoAlimentacion.aplicaA(new EmpleadoAsalariado(ANTIGUO, Dinero.de("1")), CORTE));
        assertTrue(bonoAlimentacion.aplicaA(
                new EmpleadoPorComision(ANTIGUO, Dinero.de("1"), BigDecimal.ZERO, Dinero.CERO), CORTE));
    }

    @Test
    @DisplayName("El bono de alimentación no aplica a por horas ni a temporales")
    void bonoAlimentacionNoParaOtros() {
        assertFalse(bonoAlimentacion.aplicaA(porHoras(true), CORTE));
        assertFalse(bonoAlimentacion.aplicaA(
                new EmpleadoTemporal(ANTIGUO, Dinero.de("1"), LocalDate.of(2030, 1, 1)), CORTE));
    }

    @Test
    @DisplayName("El bono de alimentación vale $1.000.000 y se paga al empleado")
    void valorBonoAlimentacion() {
        BeneficioLiquidado bono = bonoAlimentacion.calcular(porHoras(true), Dinero.de("5000000"));

        assertEquals(Dinero.de("1000000"), bono.valor());
        assertTrue(bono.seSumaAlNeto());
    }

    @Test
    @DisplayName("El fondo de ahorro exige más de 1 año y aceptar el fondo")
    void condicionesFondoAhorro() {
        assertTrue(fondoAhorro.aplicaA(porHoras(true), CORTE));
        assertFalse(fondoAhorro.aplicaA(porHoras(false), CORTE));

        EmpleadoPorHoras nuevo = new EmpleadoPorHoras(
                new DatosEmpleado("2", "Nuevo", CORTE.minusMonths(6)), Dinero.de("20000"), BigDecimal.TEN, true);
        assertFalse(fondoAhorro.aplicaA(nuevo, CORTE));
    }

    @Test
    @DisplayName("El fondo de ahorro no aplica a asalariados")
    void fondoAhorroSoloPorHoras() {
        assertFalse(fondoAhorro.aplicaA(new EmpleadoAsalariado(ANTIGUO, Dinero.de("1")), CORTE));
    }

    @Test
    @DisplayName("El fondo de ahorro deposita el 2 % del bruto y no suma al neto")
    void valorFondoAhorro() {
        BeneficioLiquidado deposito = fondoAhorro.calcular(porHoras(true), Dinero.de("1040000"));

        assertEquals(Dinero.de("20800"), deposito.valor());
        assertFalse(deposito.seSumaAlNeto());
    }
}
