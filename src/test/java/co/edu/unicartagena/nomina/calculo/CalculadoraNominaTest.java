package co.edu.unicartagena.nomina.calculo;

import co.edu.unicartagena.nomina.calculo.liquidacion.LiquidacionNomina;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import co.edu.unicartagena.nomina.dominio.empleado.DatosEmpleado;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoAsalariado;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoPorComision;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoPorHoras;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoTemporal;
import co.edu.unicartagena.nomina.dominio.empleado.NivelRiesgoArl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Calculadora de nómina: liquidación completa")
class CalculadoraNominaTest {

    private static final YearMonth NOVIEMBRE_2026 = YearMonth.of(2026, 11);
    private static final LocalDate CORTE = NOVIEMBRE_2026.atEndOfMonth();

    private CalculadoraNomina calculadora;

    @BeforeEach
    void prepararCalculadora() {
        calculadora = ConfiguracionNomina.crearCalculadoraEstandar();
    }

    private static EmpleadoPorHoras porHoras(LocalDate ingreso, boolean aceptaFondo) {
        return new EmpleadoPorHoras(new DatosEmpleado("2002", "Luis Gómez", ingreso),
                Dinero.de("20000"), new BigDecimal("48"), aceptaFondo);
    }

    @Test
    @DisplayName("Asalariado con más de 5 años: bono 10 %, deducciones y bono de alimentación")
    void liquidaAsalariado() {
        EmpleadoAsalariado empleado = new EmpleadoAsalariado(
                new DatosEmpleado("1001", "Ana Pérez", LocalDate.of(2019, 2, 1)), Dinero.de("4000000"));

        LiquidacionNomina liquidacion = calculadora.liquidar(empleado, NOVIEMBRE_2026);

        // Bruto 4.400.000; 4 % = 176.000; ARL I 0,522 % = 22.968; + alimentación 1.000.000
        assertEquals(Dinero.de("4400000"), liquidacion.getSalarioBruto());
        assertEquals(Dinero.de("198968"), liquidacion.getTotalDeducciones());
        assertEquals(Dinero.de("1000000"), liquidacion.totalBeneficiosPagados());
        assertEquals(Dinero.de("5201032"), liquidacion.getSalarioNeto());
    }

    @Test
    @DisplayName("Por horas con más de 1 año que acepta el fondo: el 2 % se deposita y no suma al neto")
    void liquidaPorHorasConFondoDeAhorro() {
        LiquidacionNomina liquidacion = calculadora.liquidar(porHoras(LocalDate.of(2024, 5, 1), true), NOVIEMBRE_2026);

        assertEquals(Dinero.de("1040000"), liquidacion.getSalarioBruto());
        assertEquals(Dinero.de("47028.80"), liquidacion.getTotalDeducciones());
        assertEquals(Dinero.de("20800"), liquidacion.totalDepositadoEnFondoAhorro());
        assertEquals(Dinero.de("992971.20"), liquidacion.getSalarioNeto());
    }

    @Test
    @DisplayName("Por horas que no acepta el fondo no recibe beneficios")
    void porHorasSinAceptarFondo() {
        LiquidacionNomina liquidacion = calculadora.liquidar(porHoras(LocalDate.of(2024, 5, 1), false), NOVIEMBRE_2026);

        assertTrue(liquidacion.getBeneficios().isEmpty());
    }

    @Test
    @DisplayName("Por horas con exactamente 1 año todavía no accede al fondo")
    void porHorasConUnAnioExacto() {
        LiquidacionNomina liquidacion = calculadora.liquidar(porHoras(CORTE.minusYears(1), true), NOVIEMBRE_2026);

        assertTrue(liquidacion.getBeneficios().isEmpty());
    }

    @Test
    @DisplayName("Por comisión con ventas altas: comisión, bono 3 % y bono de alimentación")
    void liquidaPorComision() {
        EmpleadoPorComision empleado = new EmpleadoPorComision(
                new DatosEmpleado("3003", "María Torres", LocalDate.of(2023, 6, 1)),
                Dinero.de("1800000"), new BigDecimal("0.05"), Dinero.de("25000000"));

        LiquidacionNomina liquidacion = calculadora.liquidar(empleado, NOVIEMBRE_2026);

        // Bruto 3.800.000; 4 % = 152.000; ARL = 19.836; + 1.000.000
        assertEquals(Dinero.de("3800000"), liquidacion.getSalarioBruto());
        assertEquals(Dinero.de("4628164"), liquidacion.getSalarioNeto());
    }

    @Test
    @DisplayName("Temporal: solo deducciones, sin bonos ni beneficios")
    void liquidaTemporal() {
        EmpleadoTemporal empleado = new EmpleadoTemporal(
                new DatosEmpleado("4004", "Pedro Díaz", LocalDate.of(2026, 2, 1)),
                Dinero.de("2800000"), LocalDate.of(2027, 1, 31));

        LiquidacionNomina liquidacion = calculadora.liquidar(empleado, NOVIEMBRE_2026);

        assertTrue(liquidacion.getBeneficios().isEmpty());
        assertEquals(Dinero.de("2673384"), liquidacion.getSalarioNeto());
    }

    @Test
    @DisplayName("La ARL usa la tasa de la clase de riesgo del empleado")
    void arlSegunClaseDeRiesgo() {
        EmpleadoAsalariado empleado = new EmpleadoAsalariado(
                new DatosEmpleado("5005", "Jorge Ruiz", LocalDate.of(2025, 1, 1), NivelRiesgoArl.CLASE_V),
                Dinero.de("4000000"));

        LiquidacionNomina liquidacion = calculadora.liquidar(empleado, NOVIEMBRE_2026);

        // 4 % = 160.000; ARL V 6,96 % = 278.400
        assertEquals(Dinero.de("438400"), liquidacion.getTotalDeducciones());
    }

    @Test
    @DisplayName("No liquida a un temporal con el contrato vencido")
    void rechazaContratoVencido() {
        EmpleadoTemporal empleado = new EmpleadoTemporal(
                new DatosEmpleado("4004", "Pedro Díaz", LocalDate.of(2025, 1, 1)),
                Dinero.de("2800000"), LocalDate.of(2026, 6, 30));

        assertThrows(ValidacionNominaException.class, () -> calculadora.liquidar(empleado, NOVIEMBRE_2026));
    }
}
