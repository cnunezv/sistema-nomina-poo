package co.edu.unicartagena.nomina.calculo;

import co.edu.unicartagena.nomina.calculo.liquidacion.BeneficioLiquidado;
import co.edu.unicartagena.nomina.calculo.liquidacion.LiquidacionNomina;
import co.edu.unicartagena.nomina.calculo.liquidacion.ModalidadBeneficio;
import co.edu.unicartagena.nomina.dominio.comun.ConceptoNomina;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import co.edu.unicartagena.nomina.dominio.empleado.Empleado;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoAsalariado;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoPorComision;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoPorHoras;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Calcula la liquidación de nómina de un empleado.
 *
 * <p>Primera versión funcional: las deducciones y los beneficios se calculan
 * directamente en esta clase.</p>
 */
public class CalculadoraNomina {

    private static final BigDecimal TASA_SEGURIDAD_SOCIAL = new BigDecimal("0.04");
    private static final BigDecimal BONO_ALIMENTACION = new BigDecimal("1000000");
    private static final BigDecimal TASA_FONDO_AHORRO = new BigDecimal("0.02");

    public LiquidacionNomina liquidar(Empleado empleado, YearMonth periodo) {
        LocalDate fechaCorte = periodo.atEndOfMonth();
        if (!empleado.estaVigenteEn(fechaCorte)) {
            throw new ValidacionNominaException(empleado.getNombre() + " no tiene contrato vigente en " + periodo + ".");
        }
        List<ConceptoNomina> devengos = empleado.calcularDevengos(fechaCorte);
        BigDecimal bruto = ConceptoNomina.sumar(devengos);

        List<ConceptoNomina> deducciones = new ArrayList<>();
        deducciones.add(new ConceptoNomina("Seguro social y pensión (4 %)",
                Dinero.porcentaje(bruto, TASA_SEGURIDAD_SOCIAL)));
        deducciones.add(new ConceptoNomina("ARL",
                Dinero.porcentaje(bruto, empleado.getNivelRiesgoArl().getTasa())));

        List<BeneficioLiquidado> beneficios = new ArrayList<>();
        if (empleado instanceof EmpleadoAsalariado || empleado instanceof EmpleadoPorComision) {
            beneficios.add(new BeneficioLiquidado("Bono de alimentación",
                    BONO_ALIMENTACION, ModalidadBeneficio.PAGO_AL_EMPLEADO));
        }
        if (empleado instanceof EmpleadoPorHoras porHoras
                && porHoras.aceptaFondoAhorro()
                && porHoras.tieneAntiguedadMayorA(1, fechaCorte)) {
            beneficios.add(new BeneficioLiquidado("Fondo de ahorro (2 %)",
                    Dinero.porcentaje(bruto, TASA_FONDO_AHORRO), ModalidadBeneficio.DEPOSITO_FONDO_AHORRO));
        }

        return new LiquidacionNomina(empleado, periodo, devengos, deducciones, beneficios);
    }
}
