package co.edu.unicartagena.nomina.calculo.beneficio;

import co.edu.unicartagena.nomina.calculo.liquidacion.BeneficioLiquidado;
import co.edu.unicartagena.nomina.calculo.liquidacion.ModalidadBeneficio;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.empleado.ElegibleFondoAhorro;
import co.edu.unicartagena.nomina.dominio.empleado.Empleado;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Acceso al fondo de ahorro para empleados por horas con más de 1 año que
 * aceptaron el beneficio: la empresa deposita mensualmente el 2 % del salario
 * en el fondo del empleado.
 *
 * <p>El depósito queda en el fondo, por eso no suma al neto del mes.</p>
 */
public class FondoAhorro implements BeneficioAdicional {

    /** Porcentaje del salario bruto que se deposita. */
    public static final BigDecimal TASA = new BigDecimal("0.02");

    /** Años de antigüedad que deben superarse. */
    public static final int ANIOS_MINIMOS = 1;

    @Override
    public boolean aplicaA(Empleado empleado, LocalDate fechaCorte) {
        return empleado instanceof ElegibleFondoAhorro elegible
                && elegible.aceptaFondoAhorro()
                && elegible.tieneAntiguedadMayorA(ANIOS_MINIMOS, fechaCorte);
    }

    @Override
    public BeneficioLiquidado calcular(Empleado empleado, BigDecimal salarioBruto) {
        return new BeneficioLiquidado("Fondo de ahorro (2 %)",
                Dinero.porcentaje(salarioBruto, TASA), ModalidadBeneficio.DEPOSITO_FONDO_AHORRO);
    }
}
