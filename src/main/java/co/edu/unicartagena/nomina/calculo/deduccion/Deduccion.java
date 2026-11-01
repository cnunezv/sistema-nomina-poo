package co.edu.unicartagena.nomina.calculo.deduccion;

import co.edu.unicartagena.nomina.dominio.comun.ConceptoNomina;
import co.edu.unicartagena.nomina.dominio.empleado.Empleado;

import java.math.BigDecimal;

/**
 * Regla de deducción que se aplica sobre el salario bruto (patrón Estrategia).
 *
 * <p>Para agregar una deducción nueva (por ejemplo, retención en la fuente o
 * una libranza) basta con crear una clase que implemente esta interfaz y
 * registrarla en {@code ConfiguracionNomina}; la calculadora no cambia
 * (principio abierto/cerrado).</p>
 */
public interface Deduccion {

    /**
     * @param empleado     empleado al que se le aplica la deducción
     * @param salarioBruto salario bruto del periodo
     * @return concepto con la descripción y el valor descontado
     */
    ConceptoNomina calcular(Empleado empleado, BigDecimal salarioBruto);
}
