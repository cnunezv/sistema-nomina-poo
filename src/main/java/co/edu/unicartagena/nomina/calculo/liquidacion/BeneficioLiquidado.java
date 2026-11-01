package co.edu.unicartagena.nomina.calculo.liquidacion;

import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.Validaciones;

import java.math.BigDecimal;

/**
 * Beneficio adicional ya calculado para un empleado en un periodo.
 *
 * @param descripcion texto que se muestra en el comprobante
 * @param valor       valor del beneficio, no negativo
 * @param modalidad   cómo se entrega el beneficio
 */
public record BeneficioLiquidado(String descripcion, BigDecimal valor, ModalidadBeneficio modalidad) {

    /**
     * Valida y normaliza los datos.
     */
    public BeneficioLiquidado {
        descripcion = Validaciones.requerirTexto(descripcion, "descripción del beneficio");
        valor = Dinero.redondear(Validaciones.requerirNoNegativo(valor, "valor del beneficio"));
        Validaciones.requerirNoNulo(modalidad, "modalidad del beneficio");
    }

    /**
     * @return {@code true} si el beneficio se suma al salario neto
     */
    public boolean seSumaAlNeto() {
        return modalidad.sumaAlNeto();
    }
}
