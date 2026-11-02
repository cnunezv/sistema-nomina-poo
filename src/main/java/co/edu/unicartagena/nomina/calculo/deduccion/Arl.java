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
        return new ConceptoNomina(describir(nivel), Dinero.porcentaje(salarioBruto, nivel.getTasa()));
    }

    /**
     * @param nivel clase de riesgo
     * @return por ejemplo {@code "ARL clase I (0,522 %)"}
     */
    private static String describir(NivelRiesgoArl nivel) {
        String clase = nivel.name().replace("CLASE_", "");
        String porcentaje = nivel.getTasa().movePointRight(2).stripTrailingZeros().toPlainString().replace('.', ',');
        return "ARL clase " + clase + " (" + porcentaje + " %)";
    }
}
