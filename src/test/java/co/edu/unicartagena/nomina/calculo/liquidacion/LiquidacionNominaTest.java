package co.edu.unicartagena.nomina.calculo.liquidacion;

import co.edu.unicartagena.nomina.dominio.comun.ConceptoNomina;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import co.edu.unicartagena.nomina.dominio.empleado.DatosEmpleado;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoTemporal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Liquidación de nómina: totales y salario neto")
class LiquidacionNominaTest {

    private static final YearMonth PERIODO = YearMonth.of(2026, 11);
    private static final EmpleadoTemporal EMPLEADO = new EmpleadoTemporal(
            new DatosEmpleado("4004", "Pedro Díaz", LocalDate.of(2026, 1, 1)),
            Dinero.de("1000000"), LocalDate.of(2026, 12, 31));

    private static final List<ConceptoNomina> DEVENGOS =
            List.of(new ConceptoNomina("Salario fijo mensual", Dinero.de("1000000")));

    @Test
    @DisplayName("Neto = bruto − deducciones + beneficios pagados; el fondo de ahorro no suma")
    void calculaNeto() {
        LiquidacionNomina liquidacion = new LiquidacionNomina(EMPLEADO, PERIODO, DEVENGOS,
                List.of(new ConceptoNomina("Deducción", Dinero.de("100000"))),
                List.of(new BeneficioLiquidado("Pago", Dinero.de("50000"), ModalidadBeneficio.PAGO_AL_EMPLEADO),
                        new BeneficioLiquidado("Fondo", Dinero.de("20000"), ModalidadBeneficio.DEPOSITO_FONDO_AHORRO)));

        assertEquals(Dinero.de("950000"), liquidacion.getSalarioNeto());
        assertEquals(Dinero.de("20000"), liquidacion.totalDepositadoEnFondoAhorro());
    }

    @Test
    @DisplayName("Un neto de exactamente cero es válido")
    void netoCeroValido() {
        LiquidacionNomina liquidacion = new LiquidacionNomina(EMPLEADO, PERIODO, DEVENGOS,
                List.of(new ConceptoNomina("Deducción total", Dinero.de("1000000"))), List.of());

        assertEquals(Dinero.CERO, liquidacion.getSalarioNeto());
    }

    @Test
    @DisplayName("Rechaza una liquidación con salario neto negativo")
    void rechazaNetoNegativo() {
        List<ConceptoNomina> deduccionesExcesivas =
                List.of(new ConceptoNomina("Libranza", Dinero.de("1000000.01")));

        assertThrows(ValidacionNominaException.class,
                () -> new LiquidacionNomina(EMPLEADO, PERIODO, DEVENGOS, deduccionesExcesivas, List.of()));
    }
}
