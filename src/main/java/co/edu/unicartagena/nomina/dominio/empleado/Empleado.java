package co.edu.unicartagena.nomina.dominio.empleado;

import co.edu.unicartagena.nomina.dominio.comun.ConceptoNomina;
import co.edu.unicartagena.nomina.dominio.comun.Validaciones;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstracción de un empleado de la empresa.
 *
 * <p>Aplica el patrón <strong>método plantilla</strong>: el algoritmo para
 * obtener los devengos y el salario bruto ({@link #calcularDevengos(LocalDate)})
 * es fijo y está definido aquí, mientras que cada subclase aporta los pasos
 * que cambian según su tipo de contrato:</p>
 * <ul>
 *   <li>{@link #conceptosSalariales()}: cómo se paga el trabajo (obligatorio);</li>
 *   <li>{@link #conceptosDeBonificacion(LocalDate)}: los bonos propios del
 *       tipo de empleado (opcional; por defecto no hay).</li>
 * </ul>
 *
 * <p>Las deducciones y los beneficios adicionales <em>no</em> se calculan
 * aquí: son reglas de la empresa que se aplican desde fuera
 * (ver {@code CalculadoraNomina}), lo que mantiene a esta clase con una sola
 * responsabilidad.</p>
 */
public abstract class Empleado {

    private final DatosEmpleado datos;

    /**
     * @param datos datos personales y de vinculación
     */
    protected Empleado(DatosEmpleado datos) {
        this.datos = Validaciones.requerirNoNulo(datos, "datos del empleado");
    }

    /**
     * Devengos del periodo: primero los conceptos salariales y luego los bonos.
     * Es {@code final} para que ninguna subclase altere el orden del algoritmo.
     *
     * @param fechaCorte fecha de corte del periodo que se liquida
     * @return lista inmutable de devengos
     */
    public final List<ConceptoNomina> calcularDevengos(LocalDate fechaCorte) {
        Validaciones.requerirNoNulo(fechaCorte, "fecha de corte");
        List<ConceptoNomina> devengos = new ArrayList<>(conceptosSalariales());
        devengos.addAll(conceptosDeBonificacion(fechaCorte));
        return List.copyOf(devengos);
    }

    /**
     * Salario bruto del periodo: la suma de todos los devengos.
     *
     * @param fechaCorte fecha de corte del periodo que se liquida
     * @return salario bruto redondeado
     */
    public final BigDecimal calcularSalarioBruto(LocalDate fechaCorte) {
        return ConceptoNomina.sumar(calcularDevengos(fechaCorte));
    }

    /**
     * Paso obligatorio del método plantilla: pago por el trabajo realizado.
     *
     * @return conceptos salariales del periodo
     */
    protected abstract List<ConceptoNomina> conceptosSalariales();

    /**
     * Paso opcional del método plantilla: bonos propios del tipo de empleado.
     *
     * @param fechaCorte fecha de corte, necesaria para reglas de antigüedad
     * @return bonos del periodo; vacío por defecto
     */
    protected List<ConceptoNomina> conceptosDeBonificacion(LocalDate fechaCorte) {
        return List.of();
    }

    /**
     * @return tipo de empleado, para reportes
     */
    public abstract TipoEmpleado getTipo();

    /**
     * Indica si el empleado lleva <em>más</em> de cierto número de años en la
     * empresa. El día exacto del aniversario todavía no cumple la condición.
     *
     * @param anios      años de antigüedad que deben superarse
     * @param fechaCorte fecha en la que se evalúa la antigüedad
     * @return {@code true} si la antigüedad supera los años indicados
     */
    public boolean tieneAntiguedadMayorA(int anios, LocalDate fechaCorte) {
        Validaciones.requerirNoNulo(fechaCorte, "fecha de corte");
        return datos.fechaIngreso().plusYears(anios).isBefore(fechaCorte);
    }

    /**
     * Indica si el empleado tiene contrato vigente en una fecha. Por defecto,
     * todo empleado que ya ingresó está vigente; los contratos a término fijo
     * sobrescriben este método.
     *
     * @param fecha fecha a evaluar
     * @return {@code true} si el contrato está vigente
     */
    public boolean estaVigenteEn(LocalDate fecha) {
        Validaciones.requerirNoNulo(fecha, "fecha");
        return !datos.fechaIngreso().isAfter(fecha);
    }

    public String getDocumento() {
        return datos.documento();
    }

    public String getNombre() {
        return datos.nombre();
    }

    public LocalDate getFechaIngreso() {
        return datos.fechaIngreso();
    }

    public NivelRiesgoArl getNivelRiesgoArl() {
        return datos.nivelRiesgoArl();
    }

    @Override
    public String toString() {
        return getTipo().getDescripcion() + " " + getNombre() + " (" + getDocumento() + ")";
    }
}
