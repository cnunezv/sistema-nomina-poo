package co.edu.unicartagena.nomina.calculo;

import co.edu.unicartagena.nomina.calculo.beneficio.BeneficioAdicional;
import co.edu.unicartagena.nomina.calculo.deduccion.Deduccion;
import co.edu.unicartagena.nomina.calculo.liquidacion.BeneficioLiquidado;
import co.edu.unicartagena.nomina.calculo.liquidacion.LiquidacionNomina;
import co.edu.unicartagena.nomina.dominio.comun.ConceptoNomina;
import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import co.edu.unicartagena.nomina.dominio.comun.Validaciones;
import co.edu.unicartagena.nomina.dominio.empleado.Empleado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Orquesta el cálculo de la liquidación de nómina de un empleado.
 *
 * <p>No conoce ninguna regla concreta: recibe por constructor las
 * deducciones y los beneficios que debe aplicar (inyección de dependencias).
 * Depende solo de las abstracciones {@link Deduccion} y
 * {@link BeneficioAdicional}, no de clases concretas (principio de inversión
 * de dependencias), y no necesita modificarse cuando cambian o se agregan
 * reglas (principio abierto/cerrado).</p>
 */
public class CalculadoraNomina {

    private final List<Deduccion> deducciones;
    private final List<BeneficioAdicional> beneficios;

    /**
     * @param deducciones deducciones que se aplican a todos los empleados
     * @param beneficios  beneficios adicionales que se evalúan para cada empleado
     */
    public CalculadoraNomina(List<Deduccion> deducciones, List<BeneficioAdicional> beneficios) {
        this.deducciones = List.copyOf(Validaciones.requerirNoNulo(deducciones, "deducciones"));
        this.beneficios = List.copyOf(Validaciones.requerirNoNulo(beneficios, "beneficios"));
    }

    /**
     * Liquida la nómina de un empleado en un mes.
     *
     * @param empleado empleado a liquidar
     * @param periodo  mes a liquidar; la fecha de corte es su último día
     * @return comprobante de nómina
     * @throws ValidacionNominaException si el contrato no está vigente o el neto es negativo
     */
    public LiquidacionNomina liquidar(Empleado empleado, YearMonth periodo) {
        Validaciones.requerirNoNulo(empleado, "empleado");
        LocalDate fechaCorte = Validaciones.requerirNoNulo(periodo, "periodo").atEndOfMonth();
        validarVigencia(empleado, periodo, fechaCorte);

        List<ConceptoNomina> devengos = empleado.calcularDevengos(fechaCorte);
        BigDecimal salarioBruto = ConceptoNomina.sumar(devengos);

        return new LiquidacionNomina(empleado, periodo, devengos,
                aplicarDeducciones(empleado, salarioBruto),
                otorgarBeneficios(empleado, salarioBruto, fechaCorte));
    }

    private static void validarVigencia(Empleado empleado, YearMonth periodo, LocalDate fechaCorte) {
        if (!empleado.estaVigenteEn(fechaCorte)) {
            throw new ValidacionNominaException(
                    empleado.getNombre() + " no tiene contrato vigente en " + periodo + ".");
        }
    }

    private List<ConceptoNomina> aplicarDeducciones(Empleado empleado, BigDecimal salarioBruto) {
        return deducciones.stream()
                .map(deduccion -> deduccion.calcular(empleado, salarioBruto))
                .toList();
    }

    private List<BeneficioLiquidado> otorgarBeneficios(Empleado empleado, BigDecimal salarioBruto,
                                                       LocalDate fechaCorte) {
        return beneficios.stream()
                .filter(beneficio -> beneficio.aplicaA(empleado, fechaCorte))
                .map(beneficio -> beneficio.calcular(empleado, salarioBruto))
                .toList();
    }
}
