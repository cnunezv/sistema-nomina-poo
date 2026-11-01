package co.edu.unicartagena.nomina.calculo;

import co.edu.unicartagena.nomina.calculo.deduccion.Deduccion;
import co.edu.unicartagena.nomina.calculo.liquidacion.LiquidacionNomina;
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

/**
 * Comprueba que la calculadora acepta reglas nuevas sin modificarse
 * (principio abierto/cerrado), usando dobles de prueba.
 */
@DisplayName("Calculadora de nómina: extensibilidad y validación del neto")
class CalculadoraNominaExtensibilidadTest {

    private static final YearMonth PERIODO = YearMonth.of(2026, 11);
    private static final EmpleadoTemporal EMPLEADO = new EmpleadoTemporal(
            new DatosEmpleado("4004", "Pedro Díaz", LocalDate.of(2026, 1, 1)),
            Dinero.de("1000000"), LocalDate.of(2026, 12, 31));

    /** Doble de prueba: una libranza de valor fijo. */
    private static Deduccion libranzaDe(String valor) {
        return (empleado, bruto) -> new ConceptoNomina("Libranza", Dinero.de(valor));
    }

    @Test
    @DisplayName("Aplica una deducción nueva sin cambiar la calculadora")
    void aceptaDeduccionNueva() {
        CalculadoraNomina calculadora = new CalculadoraNomina(List.of(libranzaDe("300000")), List.of());

        LiquidacionNomina liquidacion = calculadora.liquidar(EMPLEADO, PERIODO);

        assertEquals(Dinero.de("700000"), liquidacion.getSalarioNeto());
    }

    @Test
    @DisplayName("Rechaza la liquidación si las deducciones dejan el neto negativo")
    void rechazaNetoNegativo() {
        CalculadoraNomina calculadora = new CalculadoraNomina(List.of(libranzaDe("1500000")), List.of());

        ValidacionNominaException error = assertThrows(ValidacionNominaException.class,
                () -> calculadora.liquidar(EMPLEADO, PERIODO));
        assertEquals("El salario neto de Pedro Díaz no puede ser negativo (resultado: -500000.00).",
                error.getMessage());
    }
}
