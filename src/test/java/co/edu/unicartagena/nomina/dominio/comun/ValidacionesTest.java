package co.edu.unicartagena.nomina.dominio.comun;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Validaciones de datos de entrada")
class ValidacionesTest {

    @Test
    @DisplayName("Rechaza cantidades negativas e informa el campo")
    void rechazaNegativos() {
        ValidacionNominaException error = assertThrows(ValidacionNominaException.class,
                () -> Validaciones.requerirNoNegativo(new BigDecimal("-1"), "horas trabajadas"));

        assertEquals("El campo 'horas trabajadas' no puede ser negativo.", error.getMessage());
    }

    @Test
    @DisplayName("Acepta el cero como cantidad válida")
    void aceptaCero() {
        assertEquals(BigDecimal.ZERO, Validaciones.requerirNoNegativo(BigDecimal.ZERO, "ventas"));
    }

    @Test
    @DisplayName("Rechaza tasas mayores que 1")
    void rechazaTasaMayorQueUno() {
        assertThrows(ValidacionNominaException.class,
                () -> Validaciones.requerirTasa(new BigDecimal("1.5"), "tasa"));
    }

    @Test
    @DisplayName("Rechaza textos vacíos y nulos")
    void rechazaTextosVacios() {
        assertThrows(ValidacionNominaException.class, () -> Validaciones.requerirTexto("   ", "nombre"));
        assertThrows(ValidacionNominaException.class, () -> Validaciones.requerirTexto(null, "nombre"));
    }
}
