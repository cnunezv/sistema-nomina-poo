package co.edu.unicartagena.nomina.calculo.deduccion;

import co.edu.unicartagena.nomina.dominio.comun.ConceptoNomina;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.empleado.Empleado;

import java.math.BigDecimal;

/**
 * Deducción obligatoria de seguro social y pensión: 4 % del salario bruto.
 */
public class SeguridadSocialPension implements Deduccion {

    /** Tasa que se descuenta del salario bruto. */
    public static final BigDecimal TASA = new BigDecimal("0.04");

    @Override
    public ConceptoNomina calcular(Empleado empleado, BigDecimal salarioBruto) {
        return new ConceptoNomina("Seguro social y pensión (4 %)", Dinero.porcentaje(salarioBruto, TASA));
    }
}
