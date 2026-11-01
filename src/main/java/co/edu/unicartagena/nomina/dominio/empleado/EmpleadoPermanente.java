package co.edu.unicartagena.nomina.dominio.empleado;

/**
 * Marca a los empleados con contrato a término indefinido (asalariados y por
 * comisión). Las reglas de la empresa que dependen de la permanencia, como el
 * bono de alimentación, consultan esta interfaz en lugar de preguntar por
 * clases concretas.
 *
 * <p>Es una interfaz pequeña y específica (principio de segregación de
 * interfaces): solo la implementan los tipos a los que les aplica.</p>
 */
public interface EmpleadoPermanente {
}
