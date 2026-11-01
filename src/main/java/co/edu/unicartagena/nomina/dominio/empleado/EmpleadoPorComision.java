package co.edu.unicartagena.nomina.dominio.empleado;

import co.edu.unicartagena.nomina.dominio.comun.ConceptoNomina;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.Validaciones;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Empleado que cobra un salario base más una comisión sobre sus ventas.
 *
 * <p>Reglas de negocio:</p>
 * <ul>
 *   <li>Devenga salario base + porcentaje de comisión sobre las ventas.</li>
 *   <li>Si las ventas superan $20.000.000 recibe un bono adicional del 3 % de
 *       las ventas (con ventas exactamente iguales al umbral no hay bono).</li>
 *   <li>Las ventas no pueden ser negativas.</li>
 * </ul>
 */
public class EmpleadoPorComision extends Empleado implements EmpleadoPermanente {

    /** Valor de ventas que debe superarse para recibir el bono. */
    public static final BigDecimal UMBRAL_VENTAS_BONO = new BigDecimal("20000000");

    /** Porcentaje de las ventas que se paga como bono. */
    public static final BigDecimal TASA_BONO_VENTAS = new BigDecimal("0.03");

    private final BigDecimal salarioBase;
    private final BigDecimal tasaComision;
    private BigDecimal ventas;

    /**
     * @param datos        datos personales y de vinculación
     * @param salarioBase  salario base mensual, no negativo
     * @param tasaComision porcentaje de comisión como fracción (0.05 = 5 %)
     * @param ventas       ventas del periodo, no negativas
     */
    public EmpleadoPorComision(DatosEmpleado datos, BigDecimal salarioBase,
                               BigDecimal tasaComision, BigDecimal ventas) {
        super(datos);
        this.salarioBase = Dinero.redondear(
                Validaciones.requerirNoNegativo(salarioBase, "salario base"));
        this.tasaComision = Validaciones.requerirTasa(tasaComision, "tasa de comisión");
        registrarVentas(ventas);
    }

    /**
     * Registra las ventas de un nuevo periodo.
     *
     * @param ventas ventas del periodo, no negativas
     */
    public final void registrarVentas(BigDecimal ventas) {
        this.ventas = Dinero.redondear(Validaciones.requerirNoNegativo(ventas, "ventas"));
    }

    @Override
    protected List<ConceptoNomina> conceptosSalariales() {
        return List.of(
                new ConceptoNomina("Salario base", salarioBase),
                new ConceptoNomina("Comisión sobre ventas", Dinero.porcentaje(ventas, tasaComision)));
    }

    @Override
    protected List<ConceptoNomina> conceptosDeBonificacion(LocalDate fechaCorte) {
        if (!superaUmbralDeVentas()) {
            return List.of();
        }
        return List.of(new ConceptoNomina("Bono por ventas (3 %)",
                Dinero.porcentaje(ventas, TASA_BONO_VENTAS)));
    }

    /**
     * @return {@code true} si las ventas superan estrictamente el umbral del bono
     */
    public boolean superaUmbralDeVentas() {
        return ventas.compareTo(UMBRAL_VENTAS_BONO) > 0;
    }

    @Override
    public TipoEmpleado getTipo() {
        return TipoEmpleado.POR_COMISION;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }

    public BigDecimal getTasaComision() {
        return tasaComision;
    }

    public BigDecimal getVentas() {
        return ventas;
    }
}
