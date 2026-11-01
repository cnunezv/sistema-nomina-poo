package co.edu.unicartagena.nomina.calculo.beneficio;

import co.edu.unicartagena.nomina.calculo.liquidacion.BeneficioLiquidado;
import co.edu.unicartagena.nomina.dominio.empleado.Empleado;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Beneficio adicional que la empresa otorga a ciertos empleados (patrón
 * Estrategia). Cada beneficio decide a quién le aplica y cuánto vale.
 */
public interface BeneficioAdicional {

    /**
     * @param empleado   empleado a evaluar
     * @param fechaCorte fecha de corte del periodo
     * @return {@code true} si el empleado tiene derecho al beneficio
     */
    boolean aplicaA(Empleado empleado, LocalDate fechaCorte);

    /**
     * Calcula el beneficio. Solo debe llamarse si {@link #aplicaA} es verdadero.
     *
     * @param empleado     empleado beneficiado
     * @param salarioBruto salario bruto del periodo
     * @return beneficio con su valor y su modalidad de entrega
     */
    BeneficioLiquidado calcular(Empleado empleado, BigDecimal salarioBruto);
}
