package co.edu.unicartagena.nomina.dominio.empleado;

import co.edu.unicartagena.nomina.dominio.comun.ConceptoNomina;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.Validaciones;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Empleado que cobra por horas trabajadas.
 *
 * <p>Reglas de negocio:</p>
 * <ul>
 *   <li>Las horas hasta {@value #UMBRAL_HORAS_ORDINARIAS} se pagan a la tarifa base.</li>
 *   <li>Las horas que pasan de ese umbral son extra y se pagan a 1.5 veces la tarifa.</li>
 *   <li>No recibe bonos.</li>
 *   <li>Las horas trabajadas no pueden ser negativas.</li>
 * </ul>
 */
public class EmpleadoPorHoras extends Empleado implements ElegibleFondoAhorro {

    /** Horas a partir de las cuales se considera trabajo extra. */
    public static final int UMBRAL_HORAS_ORDINARIAS = 40;

    /** Factor que multiplica la tarifa en las horas extra. */
    public static final BigDecimal FACTOR_HORA_EXTRA = new BigDecimal("1.5");

    private static final BigDecimal UMBRAL = BigDecimal.valueOf(UMBRAL_HORAS_ORDINARIAS);

    private final BigDecimal tarifaPorHora;
    private final boolean aceptaFondoAhorro;
    private BigDecimal horasTrabajadas;

    /**
     * @param datos             datos personales y de vinculación
     * @param tarifaPorHora     valor de la hora ordinaria, no negativo
     * @param horasTrabajadas   horas trabajadas en el periodo, no negativas
     * @param aceptaFondoAhorro si el empleado aceptó el acceso al fondo de ahorro
     */
    public EmpleadoPorHoras(DatosEmpleado datos, BigDecimal tarifaPorHora,
                            BigDecimal horasTrabajadas, boolean aceptaFondoAhorro) {
        super(datos);
        this.tarifaPorHora = Dinero.redondear(
                Validaciones.requerirNoNegativo(tarifaPorHora, "tarifa por hora"));
        this.aceptaFondoAhorro = aceptaFondoAhorro;
        registrarHorasTrabajadas(horasTrabajadas);
    }

    /**
     * Registra las horas trabajadas de un nuevo periodo.
     *
     * @param horas horas trabajadas, no negativas
     */
    public final void registrarHorasTrabajadas(BigDecimal horas) {
        this.horasTrabajadas = Validaciones.requerirNoNegativo(horas, "horas trabajadas");
    }

    @Override
    protected List<ConceptoNomina> conceptosSalariales() {
        List<ConceptoNomina> conceptos = new ArrayList<>();
        conceptos.add(new ConceptoNomina(
                "Horas ordinarias (" + horasOrdinarias().stripTrailingZeros().toPlainString() + " h)",
                Dinero.redondear(horasOrdinarias().multiply(tarifaPorHora))));
        if (horasExtra().signum() > 0) {
            conceptos.add(new ConceptoNomina(
                    "Horas extra x1.5 (" + horasExtra().stripTrailingZeros().toPlainString() + " h)",
                    Dinero.redondear(horasExtra().multiply(tarifaHoraExtra()))));
        }
        return conceptos;
    }

    /**
     * @return horas pagadas a tarifa ordinaria (como máximo el umbral)
     */
    public BigDecimal horasOrdinarias() {
        return horasTrabajadas.min(UMBRAL);
    }

    /**
     * @return horas que superan el umbral; cero si no lo superan
     */
    public BigDecimal horasExtra() {
        return horasTrabajadas.subtract(UMBRAL).max(BigDecimal.ZERO);
    }

    /**
     * @return tarifa de la hora extra (tarifa base × 1.5)
     */
    public BigDecimal tarifaHoraExtra() {
        return tarifaPorHora.multiply(FACTOR_HORA_EXTRA);
    }

    @Override
    public boolean aceptaFondoAhorro() {
        return aceptaFondoAhorro;
    }

    @Override
    public TipoEmpleado getTipo() {
        return TipoEmpleado.POR_HORAS;
    }

    public BigDecimal getTarifaPorHora() {
        return tarifaPorHora;
    }

    public BigDecimal getHorasTrabajadas() {
        return horasTrabajadas;
    }
}
