package co.edu.unicartagena.nomina.dominio.comun;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Validaciones reutilizables de los datos de entrada de la nómina.
 *
 * <p>Concentrar las validaciones en un solo lugar evita duplicar las mismas
 * comprobaciones en cada tipo de empleado (principio DRY) y garantiza que los
 * mensajes de error sean uniformes.</p>
 */
public final class Validaciones {

    private Validaciones() {
        // Clase de utilidades: no se instancia.
    }

    /**
     * Exige que un valor no sea nulo.
     *
     * @param valor valor a comprobar
     * @param campo nombre del campo, para el mensaje de error
     * @param <T>   tipo del valor
     * @return el mismo valor, para encadenar asignaciones
     * @throws ValidacionNominaException si el valor es nulo
     */
    public static <T> T requerirNoNulo(T valor, String campo) {
        if (Objects.isNull(valor)) {
            throw new ValidacionNominaException("El campo '" + campo + "' es obligatorio.");
        }
        return valor;
    }

    /**
     * Exige que un texto tenga contenido.
     *
     * @param texto texto a comprobar
     * @param campo nombre del campo, para el mensaje de error
     * @return el texto sin espacios al inicio ni al final
     * @throws ValidacionNominaException si el texto es nulo o está vacío
     */
    public static String requerirTexto(String texto, String campo) {
        if (texto == null || texto.isBlank()) {
            throw new ValidacionNominaException("El campo '" + campo + "' no puede estar vacío.");
        }
        return texto.strip();
    }

    /**
     * Exige que una cantidad no sea negativa.
     *
     * @param valor valor a comprobar
     * @param campo nombre del campo, para el mensaje de error
     * @return el mismo valor
     * @throws ValidacionNominaException si el valor es nulo o menor que cero
     */
    public static BigDecimal requerirNoNegativo(BigDecimal valor, String campo) {
        requerirNoNulo(valor, campo);
        if (valor.signum() < 0) {
            throw new ValidacionNominaException("El campo '" + campo + "' no puede ser negativo.");
        }
        return valor;
    }

    /**
     * Exige que una tasa esté entre 0 y 1 (0 % y 100 %).
     *
     * @param tasa  tasa expresada como fracción
     * @param campo nombre del campo, para el mensaje de error
     * @return la misma tasa
     * @throws ValidacionNominaException si la tasa está fuera del rango
     */
    public static BigDecimal requerirTasa(BigDecimal tasa, String campo) {
        requerirNoNegativo(tasa, campo);
        if (tasa.compareTo(BigDecimal.ONE) > 0) {
            throw new ValidacionNominaException("El campo '" + campo + "' debe estar entre 0 y 1.");
        }
        return tasa;
    }
}
