package co.edu.unicartagena.nomina.calculo.liquidacion;

/**
 * Forma en que la empresa entrega un beneficio adicional.
 */
public enum ModalidadBeneficio {

    /** Se paga al empleado junto con su nómina y suma al neto. */
    PAGO_AL_EMPLEADO(true),

    /** Se deposita en el fondo de ahorro del empleado; no suma al neto del mes. */
    DEPOSITO_FONDO_AHORRO(false);

    private final boolean sumaAlNeto;

    ModalidadBeneficio(boolean sumaAlNeto) {
        this.sumaAlNeto = sumaAlNeto;
    }

    /**
     * @return {@code true} si el beneficio se suma al salario neto a pagar
     */
    public boolean sumaAlNeto() {
        return sumaAlNeto;
    }
}
