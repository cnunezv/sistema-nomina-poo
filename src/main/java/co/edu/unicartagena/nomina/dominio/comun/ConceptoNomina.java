package co.edu.unicartagena.nomina.dominio.comun;

import java.math.BigDecimal;
import java.util.List;

/**
 * Línea de un comprobante de nómina: un devengo o una deducción con su
 * descripción y su valor.
 *
 * @param descripcion texto que se muestra en el comprobante
 * @param valor       valor monetario, nunca negativo
 */
public record ConceptoNomina(String descripcion, BigDecimal valor) {

    /**
     * Valida y normaliza los datos del concepto.
     */
    public ConceptoNomina {
        descripcion = Validaciones.requerirTexto(descripcion, "descripción del concepto");
        valor = Dinero.redondear(Validaciones.requerirNoNegativo(valor, "valor del concepto"));
    }

    /**
     * Suma los valores de una lista de conceptos.
     *
     * @param conceptos conceptos a sumar
     * @return el total redondeado
     */
    public static BigDecimal sumar(List<ConceptoNomina> conceptos) {
        return Dinero.sumar(conceptos.stream().map(ConceptoNomina::valor).toList());
    }
}
