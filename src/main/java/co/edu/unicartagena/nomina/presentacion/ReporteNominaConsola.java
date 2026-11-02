package co.edu.unicartagena.nomina.presentacion;

import co.edu.unicartagena.nomina.calculo.liquidacion.BeneficioLiquidado;
import co.edu.unicartagena.nomina.calculo.liquidacion.LiquidacionNomina;
import co.edu.unicartagena.nomina.dominio.comun.ConceptoNomina;
import co.edu.unicartagena.nomina.servicio.ResumenNomina;

import java.math.BigDecimal;

/**
 * Da formato de texto a los comprobantes y al resumen de la nómina. Su única
 * responsabilidad es la presentación: no calcula nada.
 */
public class ReporteNominaConsola {

    private static final String LINEA = "-".repeat(64);
    private static final String DOBLE_LINEA = "=".repeat(64);
    private static final String FORMATO_FILA = "  %-44s %17s%n";

    /**
     * @param liquidacion comprobante de un empleado
     * @return el comprobante como texto
     */
    public String formatear(LiquidacionNomina liquidacion) {
        StringBuilder texto = new StringBuilder();
        texto.append(LINEA).append('\n')
                .append(liquidacion.getNombre()).append(" | ")
                .append(liquidacion.getTipoEmpleado().getDescripcion()).append(" | Doc. ")
                .append(liquidacion.getDocumento()).append('\n')
                .append(LINEA).append('\n');

        texto.append("Devengos\n");
        liquidacion.getDevengos().forEach(concepto -> fila(texto, concepto.descripcion(), concepto.valor()));
        fila(texto, "SALARIO BRUTO", liquidacion.getSalarioBruto());

        texto.append("Deducciones\n");
        for (ConceptoNomina deduccion : liquidacion.getDeducciones()) {
            fila(texto, deduccion.descripcion(), deduccion.valor().negate());
        }

        if (!liquidacion.getBeneficios().isEmpty()) {
            texto.append("Beneficios adicionales\n");
            for (BeneficioLiquidado beneficio : liquidacion.getBeneficios()) {
                String descripcion = beneficio.seSumaAlNeto()
                        ? beneficio.descripcion()
                        : beneficio.descripcion() + " [depósito]";
                fila(texto, descripcion, beneficio.valor());
            }
        }
        fila(texto, "NETO A PAGAR", liquidacion.getSalarioNeto());
        return texto.toString();
    }

    /**
     * @param resumen nómina completa del mes
     * @return comprobantes y totales como texto
     */
    public String formatear(ResumenNomina resumen) {
        StringBuilder texto = new StringBuilder();
        texto.append(DOBLE_LINEA).append('\n')
                .append("NÓMINA DEL PERIODO ").append(resumen.periodo()).append('\n')
                .append(DOBLE_LINEA).append('\n');
        resumen.liquidaciones().forEach(liquidacion -> texto.append(formatear(liquidacion)));

        texto.append(DOBLE_LINEA).append('\n').append("TOTALES DE LA EMPRESA\n");
        fila(texto, "Empleados liquidados", BigDecimal.valueOf(resumen.liquidaciones().size()), false);
        fila(texto, "Total bruto", resumen.totalBruto());
        fila(texto, "Total deducciones", resumen.totalDeducciones());
        fila(texto, "Total beneficios pagados", resumen.totalBeneficiosPagados());
        fila(texto, "Total depositado en fondo de ahorro", resumen.totalDepositadoEnFondoAhorro());
        fila(texto, "TOTAL NETO A PAGAR", resumen.totalNeto());
        if (!resumen.noLiquidados().isEmpty()) {
            texto.append("No liquidados: ").append(String.join(", ", resumen.noLiquidados())).append('\n');
        }
        texto.append(DOBLE_LINEA).append('\n');
        return texto.toString();
    }

    private static void fila(StringBuilder texto, String descripcion, BigDecimal valor) {
        fila(texto, descripcion, valor, true);
    }

    private static void fila(StringBuilder texto, String descripcion, BigDecimal valor, boolean esDinero) {
        String valorTexto = esDinero ? FormatoMoneda.formatear(valor) : valor.toPlainString();
        texto.append(String.format(FORMATO_FILA, descripcion, valorTexto));
    }
}
