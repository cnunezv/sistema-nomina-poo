package co.edu.unicartagena.nomina.dominio.empleado;

import co.edu.unicartagena.nomina.dominio.comun.Validaciones;

import java.time.LocalDate;

/**
 * Datos personales y de vinculación comunes a todos los empleados.
 *
 * <p>Agruparlos en un objeto de valor evita constructores con listas largas de
 * parámetros (un olor de código conocido como <em>long parameter list</em>).</p>
 *
 * @param documento      número de documento de identidad
 * @param nombre         nombre completo
 * @param fechaIngreso   fecha de ingreso a la empresa
 * @param nivelRiesgoArl clase de riesgo para la cotización a la ARL
 */
public record DatosEmpleado(String documento, String nombre, LocalDate fechaIngreso,
                            NivelRiesgoArl nivelRiesgoArl) {

    /**
     * Valida y normaliza los datos.
     */
    public DatosEmpleado {
        documento = Validaciones.requerirTexto(documento, "documento");
        nombre = Validaciones.requerirTexto(nombre, "nombre");
        Validaciones.requerirNoNulo(fechaIngreso, "fecha de ingreso");
        Validaciones.requerirNoNulo(nivelRiesgoArl, "nivel de riesgo ARL");
    }

    /**
     * Crea los datos con la clase de riesgo ARL por defecto (clase I).
     *
     * @param documento    número de documento
     * @param nombre       nombre completo
     * @param fechaIngreso fecha de ingreso a la empresa
     */
    public DatosEmpleado(String documento, String nombre, LocalDate fechaIngreso) {
        this(documento, nombre, fechaIngreso, NivelRiesgoArl.CLASE_I);
    }
}
