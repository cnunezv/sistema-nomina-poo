package co.edu.unicartagena.nomina.calculo.liquidacion;

import co.edu.unicartagena.nomina.dominio.comun.ConceptoNomina;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import co.edu.unicartagena.nomina.dominio.comun.Validaciones;
import co.edu.unicartagena.nomina.dominio.empleado.Empleado;
import co.edu.unicartagena.nomina.dominio.empleado.TipoEmpleado;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

/**
 * Comprobante de nómina de un empleado en un periodo. Es inmutable: una vez
 * calculado, ningún valor puede cambiar.
 *
 * <p>Fórmula del neto:</p>
 * <pre>
 *   salario neto = salario bruto − total deducciones + beneficios que se pagan al empleado
 * </pre>
 *
 * <p>El constructor garantiza la regla de negocio <em>"ningún empleado puede
 * tener un salario neto negativo"</em>: no es posible crear una liquidación
 * que la incumpla.</p>
 */
public final class LiquidacionNomina {

    private final String documento;
    private final String nombre;
    private final TipoEmpleado tipoEmpleado;
    private final YearMonth periodo;
    private final List<ConceptoNomina> devengos;
    private final List<ConceptoNomina> deducciones;
    private final List<BeneficioLiquidado> beneficios;
    private final BigDecimal salarioBruto;
    private final BigDecimal totalDeducciones;
    private final BigDecimal salarioNeto;

    /**
     * Calcula los totales y valida el neto.
     *
     * @param empleado    empleado liquidado
     * @param periodo     mes liquidado
     * @param devengos    devengos del empleado
     * @param deducciones deducciones aplicadas
     * @param beneficios  beneficios adicionales otorgados
     * @throws ValidacionNominaException si el salario neto resulta negativo
     */
    public LiquidacionNomina(Empleado empleado, YearMonth periodo, List<ConceptoNomina> devengos,
                             List<ConceptoNomina> deducciones, List<BeneficioLiquidado> beneficios) {
        Validaciones.requerirNoNulo(empleado, "empleado");
        this.documento = empleado.getDocumento();
        this.nombre = empleado.getNombre();
        this.tipoEmpleado = empleado.getTipo();
        this.periodo = Validaciones.requerirNoNulo(periodo, "periodo");
        this.devengos = List.copyOf(devengos);
        this.deducciones = List.copyOf(deducciones);
        this.beneficios = List.copyOf(beneficios);

        this.salarioBruto = ConceptoNomina.sumar(this.devengos);
        this.totalDeducciones = ConceptoNomina.sumar(this.deducciones);
        this.salarioNeto = salarioBruto.subtract(totalDeducciones).add(totalBeneficiosPagados());

        if (salarioNeto.signum() < 0) {
            throw new ValidacionNominaException("El salario neto de " + nombre
                    + " no puede ser negativo (resultado: " + salarioNeto.toPlainString() + ").");
        }
    }

    /**
     * @return suma de los beneficios que se pagan al empleado en la nómina
     */
    public BigDecimal totalBeneficiosPagados() {
        return Dinero.sumar(beneficios.stream()
                .filter(BeneficioLiquidado::seSumaAlNeto)
                .map(BeneficioLiquidado::valor)
                .toList());
    }

    /**
     * @return suma de los beneficios que se depositan en el fondo de ahorro
     */
    public BigDecimal totalDepositadoEnFondoAhorro() {
        return Dinero.sumar(beneficios.stream()
                .filter(beneficio -> !beneficio.seSumaAlNeto())
                .map(BeneficioLiquidado::valor)
                .toList());
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoEmpleado getTipoEmpleado() {
        return tipoEmpleado;
    }

    public YearMonth getPeriodo() {
        return periodo;
    }

    public List<ConceptoNomina> getDevengos() {
        return devengos;
    }

    public List<ConceptoNomina> getDeducciones() {
        return deducciones;
    }

    public List<BeneficioLiquidado> getBeneficios() {
        return beneficios;
    }

    public BigDecimal getSalarioBruto() {
        return salarioBruto;
    }

    public BigDecimal getTotalDeducciones() {
        return totalDeducciones;
    }

    public BigDecimal getSalarioNeto() {
        return salarioNeto;
    }
}
