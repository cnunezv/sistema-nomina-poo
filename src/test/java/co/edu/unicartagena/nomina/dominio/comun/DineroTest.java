package co.edu.unicartagena.nomina.dominio.comun;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Dinero: operaciones monetarias")
class DineroTest {

    @Test
    @DisplayName("Redondea a dos decimales con redondeo comercial")
    void redondeaConHalfUp() {
        assertEquals(new BigDecimal("10.13"), Dinero.redondear(new BigDecimal("10.125")));
        assertEquals(new BigDecimal("10.12"), Dinero.redondear(new BigDecimal("10.124")));
    }

    @Test
    @DisplayName("Calcula porcentajes sobre una base")
    void calculaPorcentaje() {
        BigDecimal resultado = Dinero.porcentaje(Dinero.de("1040000"), new BigDecimal("0.00522"));

        assertEquals(Dinero.de("5428.80"), resultado);
    }

    @Test
    @DisplayName("La suma de una lista vacía es cero")
    void sumaVaciaEsCero() {
        assertEquals(Dinero.CERO, Dinero.sumar(List.of()));
    }
}
