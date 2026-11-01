package co.edu.unicartagena.nomina.dominio.empleado;

import java.time.LocalDate;

/**
 * Capacidad de los empleados que pueden acceder al fondo de ahorro
 * (empleados por horas). Separarla en su propia interfaz evita obligar a los
 * demás tipos a implementar un método que no usan.
 */
public interface ElegibleFondoAhorro {

    /**
     * @return {@code true} si el empleado aceptó el acceso al fondo de ahorro
     */
    boolean aceptaFondoAhorro();

    /**
     * @param anios      años de antigüedad que deben superarse
     * @param fechaCorte fecha en la que se evalúa
     * @return {@code true} si la antigüedad supera los años indicados
     */
    boolean tieneAntiguedadMayorA(int anios, LocalDate fechaCorte);
}
