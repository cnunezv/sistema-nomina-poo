package co.edu.unicartagena.nomina.dominio.comun;

/**
 * Excepción de negocio que se lanza cuando un dato o un resultado de la nómina
 * incumple una regla de validación (horas negativas, ventas negativas,
 * salario neto negativo, contrato no vigente, etc.).
 *
 * <p>Es una excepción no verificada porque representa un error del dato de
 * entrada que el llamador debe corregir, no una condición recuperable del
 * sistema.</p>
 */
public class ValidacionNominaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción con un mensaje legible para el usuario.
     *
     * @param mensaje descripción de la regla incumplida
     */
    public ValidacionNominaException(String mensaje) {
        super(mensaje);
    }
}
