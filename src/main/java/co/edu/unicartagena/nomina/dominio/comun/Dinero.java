package co.edu.unicartagena.nomina.dominio.comun;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

/**
 * Utilidades para operar valores monetarios en pesos colombianos.
 *
 * <p>Todo el sistema usa {@link BigDecimal} en lugar de {@code double} para
 * evitar errores de redondeo binario en los cálculos de dinero. Los resultados
 * se redondean a {@value #ESCALA} decimales con redondeo comercial
 * ({@link RoundingMode#HALF_UP}).</p>
 */
public final class Dinero {

    /** Número de decimales con que se guardan los valores monetarios. */
    public static final int ESCALA = 2;

    /** Valor cero ya redondeado a la escala monetaria. */
    public static final BigDecimal CERO = BigDecimal.ZERO.setScale(ESCALA, RoundingMode.HALF_UP);

    private Dinero() {
        // Clase de utilidades: no se instancia.
    }

    /**
     * Crea un valor monetario a partir de su representación en texto.
     *
     * @param valor por ejemplo {@code "1500000"} o {@code "20000.50"}
     * @return el valor redondeado a la escala monetaria
     */
    public static BigDecimal de(String valor) {
        return redondear(new BigDecimal(valor));
    }

    /**
     * Redondea un valor a la escala monetaria.
     *
     * @param valor valor a redondear
     * @return el valor con {@value #ESCALA} decimales
     */
    public static BigDecimal redondear(BigDecimal valor) {
        return valor.setScale(ESCALA, RoundingMode.HALF_UP);
    }

    /**
     * Calcula un porcentaje de una base.
     *
     * @param base valor sobre el que se aplica la tasa
     * @param tasa tasa expresada como fracción (0.04 equivale a 4 %)
     * @return {@code base × tasa}, redondeado
     */
    public static BigDecimal porcentaje(BigDecimal base, BigDecimal tasa) {
        return redondear(base.multiply(tasa));
    }

    /**
     * Suma una colección de valores monetarios.
     *
     * @param valores valores a sumar
     * @return la suma redondeada; {@link #CERO} si la colección está vacía
     */
    public static BigDecimal sumar(Collection<BigDecimal> valores) {
        return redondear(valores.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
