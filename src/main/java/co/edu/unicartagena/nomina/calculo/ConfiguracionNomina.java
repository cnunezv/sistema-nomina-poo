package co.edu.unicartagena.nomina.calculo;

import co.edu.unicartagena.nomina.calculo.beneficio.BeneficioAdicional;
import co.edu.unicartagena.nomina.calculo.beneficio.BonoAlimentacion;
import co.edu.unicartagena.nomina.calculo.beneficio.FondoAhorro;
import co.edu.unicartagena.nomina.calculo.deduccion.Arl;
import co.edu.unicartagena.nomina.calculo.deduccion.Deduccion;
import co.edu.unicartagena.nomina.calculo.deduccion.SeguridadSocialPension;

import java.util.List;

/**
 * Punto único donde se decide qué reglas de la empresa están vigentes.
 *
 * <p>Si mañana se agrega una deducción o un beneficio, este es el único
 * archivo existente que se toca, además de crear la clase nueva.</p>
 */
public final class ConfiguracionNomina {

    private ConfiguracionNomina() {
        // Clase de configuración: no se instancia.
    }

    /**
     * @return deducciones obligatorias: seguro social y pensión, y ARL
     */
    public static List<Deduccion> deduccionesObligatorias() {
        return List.of(new SeguridadSocialPension(), new Arl());
    }

    /**
     * @return beneficios adicionales: bono de alimentación y fondo de ahorro
     */
    public static List<BeneficioAdicional> beneficiosAdicionales() {
        return List.of(new BonoAlimentacion(), new FondoAhorro());
    }

    /**
     * @return calculadora con las reglas vigentes de la empresa
     */
    public static CalculadoraNomina crearCalculadoraEstandar() {
        return new CalculadoraNomina(deduccionesObligatorias(), beneficiosAdicionales());
    }
}
