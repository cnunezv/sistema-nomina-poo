package co.edu.unicartagena.nomina.calculo.deduccion;

import co.edu.unicartagena.nomina.dominio.comun.ConceptoNomina;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.empleado.Empleado;
import co.edu.unicartagena.nomina.dominio.empleado.NivelRiesgoArl;

import java.math.BigDecimal;

/**
 * Deducción obligatoria de ARL (Administradora de Riesgos Laborales).
 *
 * <p>La tasa depende de la clase de riesgo asignada al empleado
 * ({@link NivelRiesgoArl}).</p>
 */
public class Arl implements Deduccion {

    @Override
    public ConceptoNomina calcular(Empleado empleado, BigDecimal salarioBruto) {
        NivelRiesgoArl nivel = empleado.getNivelRiesgoArl();
        String descripcion = "ARL clase " + nivel.name().replace("CLASE_", "")
                + " (" + nivel.getTasa().movePointRight(2).stripTrailingZeros().toPlainString() + " %)";
        return new ConceptoNomina(descripcion, Dinero.porcentaje(salarioBruto, nivel.getTasa()));
    }
}
