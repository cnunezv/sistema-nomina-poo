package co.edu.unicartagena.nomina.dominio.empleado;

import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Empleado por comisión")
class EmpleadoPorComisionTest {

    private static final LocalDate CORTE = LocalDate.of(2026, 11, 30);
    private static final DatosEmpleado DATOS =
            new DatosEmpleado("3003", "María Torres", LocalDate.of(2023, 6, 1));
    private static final BigDecimal TASA_5_POR_CIENTO = new BigDecimal("0.05");

    private static EmpleadoPorComision conVentas(String ventas) {
        return new EmpleadoPorComision(DATOS, Dinero.de("1800000"), TASA_5_POR_CIENTO, Dinero.de(ventas));
    }

    @Test
    @DisplayName("Devenga salario base más la comisión sobre las ventas")
    void salarioBaseMasComision() {
        EmpleadoPorComision empleado = conVentas("10000000");

        // 1.800.000 + 5 % de 10.000.000 = 2.300.000
        assertEquals(Dinero.de("2300000"), empleado.calcularSalarioBruto(CORTE));
    }

    @Test
    @DisplayName("Con ventas mayores a $20.000.000 recibe un bono del 3 % de las ventas")
    void bonoPorVentasAltas() {
        EmpleadoPorComision empleado = conVentas("25000000");

        // 1.800.000 + 1.250.000 (comisión) + 750.000 (bono) = 3.800.000
        assertTrue(empleado.superaUmbralDeVentas());
        assertEquals(Dinero.de("3800000"), empleado.calcularSalarioBruto(CORTE));
    }

    @Test
    @DisplayName("Con ventas exactamente de $20.000.000 no hay bono")
    void sinBonoEnElUmbralExacto() {
        EmpleadoPorComision empleado = conVentas("20000000");

        assertFalse(empleado.superaUmbralDeVentas());
        assertEquals(Dinero.de("2800000"), empleado.calcularSalarioBruto(CORTE));
    }

    @Test
    @DisplayName("Con ventas en cero devenga solo el salario base")
    void ventasEnCero() {
        assertEquals(Dinero.de("1800000"), conVentas("0").calcularSalarioBruto(CORTE));
    }

    @Test
    @DisplayName("Rechaza ventas negativas al crear y al registrar")
    void rechazaVentasNegativas() {
        assertThrows(ValidacionNominaException.class, () -> conVentas("-1"));

        EmpleadoPorComision empleado = conVentas("1000");
        assertThrows(ValidacionNominaException.class, () -> empleado.registrarVentas(Dinero.de("-500")));
        assertEquals(Dinero.de("1000"), empleado.getVentas());
    }

    @Test
    @DisplayName("Rechaza una tasa de comisión mayor al 100 %")
    void rechazaTasaInvalida() {
        assertThrows(ValidacionNominaException.class,
                () -> new EmpleadoPorComision(DATOS, Dinero.de("1800000"), new BigDecimal("1.2"), Dinero.CERO));
    }
}
