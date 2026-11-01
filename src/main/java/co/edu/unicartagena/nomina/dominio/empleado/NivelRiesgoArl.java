package co.edu.unicartagena.nomina.dominio.empleado;

import java.math.BigDecimal;

/**
 * Clases de riesgo laboral para la cotización a la ARL.
 *
 * <p>El enunciado no fija un porcentaje para la ARL, así que se usan las
 * tarifas de las cinco clases de riesgo vigentes en Colombia (Decreto 1772 de
 * 1994). Cada empleado tiene asignada una clase; por defecto, la clase I
 * (riesgo mínimo, trabajo de oficina).</p>
 */
public enum NivelRiesgoArl {

    /** Riesgo mínimo: actividades administrativas y de oficina. */
    CLASE_I("0.00522"),
    /** Riesgo bajo. */
    CLASE_II("0.01044"),
    /** Riesgo medio. */
    CLASE_III("0.02436"),
    /** Riesgo alto. */
    CLASE_IV("0.04350"),
    /** Riesgo máximo. */
    CLASE_V("0.06960");

    private final BigDecimal tasa;

    NivelRiesgoArl(String tasa) {
        this.tasa = new BigDecimal(tasa);
    }

    /**
     * @return la tasa de cotización como fracción del salario bruto
     */
    public BigDecimal getTasa() {
        return tasa;
    }
}
