package co.edu.unicartagena.nomina.presentacion;

import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Formato de moneda colombiano")
class FormatoMonedaTest {

    @Test
    @DisplayName("Usa punto como separador de miles y omite centavos en cero")
    void formateaSinCentavos() {
        assertEquals("$ 1.040.000", FormatoMoneda.formatear(Dinero.de("1040000")));
    }

    @Test
    @DisplayName("Muestra los centavos con coma decimal cuando existen")
    void formateaConCentavos() {
        assertEquals("$ 5.428,80", FormatoMoneda.formatear(Dinero.de("5428.8")));
    }

    @Test
    @DisplayName("Formatea valores negativos")
    void formateaNegativos() {
        assertEquals("$ -176.000", FormatoMoneda.formatear(Dinero.de("-176000")));
    }
}
