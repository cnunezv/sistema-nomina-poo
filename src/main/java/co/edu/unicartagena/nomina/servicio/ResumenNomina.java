package co.edu.unicartagena.nomina.servicio;

import co.edu.unicartagena.nomina.calculo.liquidacion.LiquidacionNomina;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.function.Function;

/**
 * Nómina completa de un mes: las liquidaciones de los empleados vigentes y
 * los totales de la empresa.
 *
 * @param periodo       mes liquidado
 * @param liquidaciones comprobantes de los empleados vigentes
 * @param noLiquidados  empleados que no se liquidaron por no estar vigentes
 */
public record ResumenNomina(YearMonth periodo, List<LiquidacionNomina> liquidaciones,
                            List<String> noLiquidados) {

    /**
     * Copia las listas para que el resumen sea inmutable.
     */
    public ResumenNomina {
        liquidaciones = List.copyOf(liquidaciones);
        noLiquidados = List.copyOf(noLiquidados);
    }

    public BigDecimal totalBruto() {
        return totalizar(LiquidacionNomina::getSalarioBruto);
    }

    public BigDecimal totalDeducciones() {
        return totalizar(LiquidacionNomina::getTotalDeducciones);
    }

    public BigDecimal totalBeneficiosPagados() {
        return totalizar(LiquidacionNomina::totalBeneficiosPagados);
    }

    public BigDecimal totalDepositadoEnFondoAhorro() {
        return totalizar(LiquidacionNomina::totalDepositadoEnFondoAhorro);
    }

    public BigDecimal totalNeto() {
        return totalizar(LiquidacionNomina::getSalarioNeto);
    }

    private BigDecimal totalizar(Function<LiquidacionNomina, BigDecimal> campo) {
        return Dinero.sumar(liquidaciones.stream().map(campo).toList());
    }
}
