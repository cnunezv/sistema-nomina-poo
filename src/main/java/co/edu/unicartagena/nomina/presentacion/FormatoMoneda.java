package co.edu.unicartagena.nomina.presentacion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Formatea valores monetarios al estilo colombiano: punto como separador de
 * miles y coma decimal, por ejemplo {@code $ 1.040.000} o {@code $ 5.428,80}.
 */
public final class FormatoMoneda {

    private static final DecimalFormatSymbols SIMBOLOS = new DecimalFormatSymbols();

    static {
        SIMBOLOS.setGroupingSeparator('.');
        SIMBOLOS.setDecimalSeparator(',');
    }

    private FormatoMoneda() {
        // Clase de utilidades: no se instancia.
    }

    /**
     * @param valor valor a formatear
     * @return el valor con signo de pesos; los decimales solo aparecen si no son cero
     */
    public static String formatear(BigDecimal valor) {
        BigDecimal redondeado = valor.setScale(2, RoundingMode.HALF_UP);
        boolean tieneCentavos = redondeado.remainder(BigDecimal.ONE).signum() != 0;
        DecimalFormat formato = new DecimalFormat(tieneCentavos ? "#,##0.00" : "#,##0", SIMBOLOS);
        return "$ " + formato.format(redondeado);
    }
}
