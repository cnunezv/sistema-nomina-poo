package co.edu.unicartagena.nomina.calculo.beneficio;

import co.edu.unicartagena.nomina.calculo.liquidacion.BeneficioLiquidado;
import co.edu.unicartagena.nomina.calculo.liquidacion.ModalidadBeneficio;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.empleado.Empleado;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoPermanente;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Bono de alimentación de $1.000.000 al mes, cubierto por la empresa, para
 * los empleados permanentes (asalariados y por comisión).
 *
 * <p>Es un pago no salarial: se suma al neto, pero no hace parte de la base
 * sobre la que se calculan las deducciones.</p>
 */
public class BonoAlimentacion implements BeneficioAdicional {

    /** Valor mensual del bono. */
    public static final BigDecimal VALOR_MENSUAL = Dinero.de("1000000");

    @Override
    public boolean aplicaA(Empleado empleado, LocalDate fechaCorte) {
        return empleado instanceof EmpleadoPermanente;
    }

    @Override
    public BeneficioLiquidado calcular(Empleado empleado, BigDecimal salarioBruto) {
        return new BeneficioLiquidado("Bono de alimentación", VALOR_MENSUAL, ModalidadBeneficio.PAGO_AL_EMPLEADO);
    }
}
