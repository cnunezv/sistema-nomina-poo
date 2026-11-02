package co.edu.unicartagena.nomina.servicio;

import co.edu.unicartagena.nomina.calculo.CalculadoraNomina;
import co.edu.unicartagena.nomina.calculo.liquidacion.LiquidacionNomina;
import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import co.edu.unicartagena.nomina.dominio.comun.Validaciones;
import co.edu.unicartagena.nomina.dominio.empleado.Empleado;
import co.edu.unicartagena.nomina.repositorio.RepositorioEmpleados;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Casos de uso de la nómina: registrar empleados, liquidar un empleado y
 * liquidar la nómina completa de un mes.
 *
 * <p>Coordina el repositorio y la calculadora, que recibe por constructor
 * (inyección de dependencias), sin contener reglas de cálculo propias.</p>
 */
public class ServicioNomina {

    private final RepositorioEmpleados repositorio;
    private final CalculadoraNomina calculadora;

    /**
     * @param repositorio repositorio de empleados
     * @param calculadora calculadora con las reglas vigentes
     */
    public ServicioNomina(RepositorioEmpleados repositorio, CalculadoraNomina calculadora) {
        this.repositorio = Validaciones.requerirNoNulo(repositorio, "repositorio");
        this.calculadora = Validaciones.requerirNoNulo(calculadora, "calculadora");
    }

    /**
     * @param empleado empleado nuevo
     */
    public void registrarEmpleado(Empleado empleado) {
        repositorio.guardar(empleado);
    }

    /**
     * Liquida a un empleado específico.
     *
     * @param documento documento del empleado
     * @param periodo   mes a liquidar
     * @return comprobante de nómina
     * @throws ValidacionNominaException si el empleado no existe o no se puede liquidar
     */
    public LiquidacionNomina liquidarEmpleado(String documento, YearMonth periodo) {
        Empleado empleado = repositorio.buscarPorDocumento(documento)
                .orElseThrow(() -> new ValidacionNominaException(
                        "No existe un empleado con el documento " + documento + "."));
        return calculadora.liquidar(empleado, periodo);
    }

    /**
     * Liquida la nómina del mes para todos los empleados vigentes. Los que no
     * tienen contrato vigente al corte quedan listados aparte.
     *
     * @param periodo mes a liquidar
     * @return resumen con los comprobantes y los totales
     */
    public ResumenNomina liquidarNomina(YearMonth periodo) {
        Validaciones.requerirNoNulo(periodo, "periodo");
        LocalDate fechaCorte = periodo.atEndOfMonth();
        List<LiquidacionNomina> liquidaciones = new ArrayList<>();
        List<String> noLiquidados = new ArrayList<>();

        for (Empleado empleado : repositorio.listarTodos()) {
            if (empleado.estaVigenteEn(fechaCorte)) {
                liquidaciones.add(calculadora.liquidar(empleado, periodo));
            } else {
                noLiquidados.add(empleado.getNombre() + " (contrato no vigente)");
            }
        }
        return new ResumenNomina(periodo, liquidaciones, noLiquidados);
    }
}
