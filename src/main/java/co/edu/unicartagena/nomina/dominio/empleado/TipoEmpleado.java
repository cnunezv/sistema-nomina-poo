package co.edu.unicartagena.nomina.dominio.empleado;

/**
 * Tipos de empleado que maneja la empresa. Se usa solo para mostrar
 * información; el comportamiento de cada tipo vive en su propia clase.
 */
public enum TipoEmpleado {

    ASALARIADO("Asalariado"),
    POR_HORAS("Por horas"),
    POR_COMISION("Por comisión"),
    TEMPORAL("Temporal");

    private final String descripcion;

    TipoEmpleado(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return nombre legible del tipo de empleado
     */
    public String getDescripcion() {
        return descripcion;
    }
}
