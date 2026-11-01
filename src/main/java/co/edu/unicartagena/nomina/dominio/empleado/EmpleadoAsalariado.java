package co.edu.unicartagena.nomina.dominio.empleado;

import co.edu.unicartagena.nomina.dominio.comun.ConceptoNomina;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.Validaciones;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Empleado con salario fijo mensual y contrato a término indefinido.
 *
 * <p>Regla de negocio: recibe un bono mensual del 10 % del salario si lleva
 * más de 5 años en la empresa.</p>
 */
public class EmpleadoAsalariado extends Empleado implements EmpleadoPermanente {

    /** Años de antigüedad que deben superarse para recibir el bono. */
    public static final int ANIOS_PARA_BONO_ANTIGUEDAD = 5;

    /** Porcentaje del salario que se paga como bono de antigüedad. */
    public static final BigDecimal TASA_BONO_ANTIGUEDAD = new BigDecimal("0.10");

    private final BigDecimal salarioMensual;

    /**
     * @param datos          datos personales y de vinculación
     * @param salarioMensual salario fijo mensual, no negativo
     */
    public EmpleadoAsalariado(DatosEmpleado datos, BigDecimal salarioMensual) {
        super(datos);
        this.salarioMensual = Dinero.redondear(
                Validaciones.requerirNoNegativo(salarioMensual, "salario mensual"));
    }

    @Override
    protected List<ConceptoNomina> conceptosSalariales() {
        return List.of(new ConceptoNomina("Salario fijo mensual", salarioMensual));
    }

    @Override
    protected List<ConceptoNomina> conceptosDeBonificacion(LocalDate fechaCorte) {
        if (!tieneAntiguedadMayorA(ANIOS_PARA_BONO_ANTIGUEDAD, fechaCorte)) {
            return List.of();
        }
        BigDecimal bono = Dinero.porcentaje(salarioMensual, TASA_BONO_ANTIGUEDAD);
        return List.of(new ConceptoNomina("Bono por antigüedad (10 %)", bono));
    }

    @Override
    public TipoEmpleado getTipo() {
        return TipoEmpleado.ASALARIADO;
    }

    public BigDecimal getSalarioMensual() {
        return salarioMensual;
    }
}
