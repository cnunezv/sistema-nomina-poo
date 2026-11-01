package co.edu.unicartagena.nomina.dominio.empleado;

import co.edu.unicartagena.nomina.dominio.comun.ConceptoNomina;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import co.edu.unicartagena.nomina.dominio.comun.Validaciones;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Empleado con salario fijo mensual y contrato por tiempo definido.
 *
 * <p>Reglas de negocio: no recibe bonos ni beneficios adicionales y solo se
 * liquida mientras su contrato esté vigente. Como no sobrescribe
 * {@link #conceptosDeBonificacion(LocalDate)} ni implementa
 * {@link EmpleadoPermanente} o {@link ElegibleFondoAhorro}, ninguna regla de
 * bonos o beneficios le aplica.</p>
 */
public class EmpleadoTemporal extends Empleado {

    private final BigDecimal salarioMensual;
    private final LocalDate fechaFinContrato;

    /**
     * @param datos            datos personales y de vinculación
     * @param salarioMensual   salario fijo mensual, no negativo
     * @param fechaFinContrato último día del contrato, posterior a la fecha de ingreso
     */
    public EmpleadoTemporal(DatosEmpleado datos, BigDecimal salarioMensual,
                            LocalDate fechaFinContrato) {
        super(datos);
        this.salarioMensual = Dinero.redondear(
                Validaciones.requerirNoNegativo(salarioMensual, "salario mensual"));
        this.fechaFinContrato = Validaciones.requerirNoNulo(fechaFinContrato, "fecha de fin del contrato");
        if (!fechaFinContrato.isAfter(datos.fechaIngreso())) {
            throw new ValidacionNominaException(
                    "La fecha de fin del contrato debe ser posterior a la fecha de ingreso.");
        }
    }

    @Override
    protected List<ConceptoNomina> conceptosSalariales() {
        return List.of(new ConceptoNomina("Salario fijo mensual", salarioMensual));
    }

    /**
     * El contrato está vigente desde el ingreso hasta la fecha de fin, inclusive.
     */
    @Override
    public boolean estaVigenteEn(LocalDate fecha) {
        return super.estaVigenteEn(fecha) && !fecha.isAfter(fechaFinContrato);
    }

    @Override
    public TipoEmpleado getTipo() {
        return TipoEmpleado.TEMPORAL;
    }

    public BigDecimal getSalarioMensual() {
        return salarioMensual;
    }

    public LocalDate getFechaFinContrato() {
        return fechaFinContrato;
    }
}
