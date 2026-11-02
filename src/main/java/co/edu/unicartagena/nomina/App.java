package co.edu.unicartagena.nomina;

import co.edu.unicartagena.nomina.calculo.ConfiguracionNomina;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import co.edu.unicartagena.nomina.dominio.empleado.DatosEmpleado;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoAsalariado;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoPorComision;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoPorHoras;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoTemporal;
import co.edu.unicartagena.nomina.dominio.empleado.NivelRiesgoArl;
import co.edu.unicartagena.nomina.presentacion.ReporteNominaConsola;
import co.edu.unicartagena.nomina.repositorio.RepositorioEmpleadosEnMemoria;
import co.edu.unicartagena.nomina.servicio.ServicioNomina;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Punto de entrada y raíz de composición: aquí, y solo aquí, se crean las
 * implementaciones concretas y se conectan entre sí.
 *
 * <p>Registra empleados de ejemplo de los cuatro tipos, liquida la nómina de
 * noviembre de 2026 y muestra cómo el sistema rechaza datos inválidos.</p>
 */
public final class App {

    private static final YearMonth PERIODO = YearMonth.of(2026, 11);

    private App() {
        // Clase de arranque: no se instancia.
    }

    /**
     * @param args no se usan
     */
    public static void main(String[] args) {
        ServicioNomina servicio = new ServicioNomina(
                new RepositorioEmpleadosEnMemoria(), ConfiguracionNomina.crearCalculadoraEstandar());
        ReporteNominaConsola reporte = new ReporteNominaConsola();

        registrarEmpleadosDeEjemplo(servicio);
        System.out.println(reporte.formatear(servicio.liquidarNomina(PERIODO)));
        demostrarValidaciones();
    }

    private static void registrarEmpleadosDeEjemplo(ServicioNomina servicio) {
        servicio.registrarEmpleado(new EmpleadoAsalariado(
                new DatosEmpleado("1001", "Ana Pérez", LocalDate.of(2019, 2, 1)), Dinero.de("4000000")));
        servicio.registrarEmpleado(new EmpleadoAsalariado(
                new DatosEmpleado("1002", "Jorge Ruiz", LocalDate.of(2024, 3, 15), NivelRiesgoArl.CLASE_III),
                Dinero.de("3200000")));
        servicio.registrarEmpleado(new EmpleadoPorHoras(
                new DatosEmpleado("2001", "Luis Gómez", LocalDate.of(2024, 5, 1)),
                Dinero.de("20000"), new BigDecimal("48"), true));
        servicio.registrarEmpleado(new EmpleadoPorComision(
                new DatosEmpleado("3001", "María Torres", LocalDate.of(2023, 6, 1)),
                Dinero.de("1800000"), new BigDecimal("0.05"), Dinero.de("25000000")));
        servicio.registrarEmpleado(new EmpleadoTemporal(
                new DatosEmpleado("4001", "Pedro Díaz", LocalDate.of(2026, 2, 1)),
                Dinero.de("2800000"), LocalDate.of(2027, 1, 31)));
        servicio.registrarEmpleado(new EmpleadoTemporal(
                new DatosEmpleado("4002", "Sofía Mejía", LocalDate.of(2025, 1, 1)),
                Dinero.de("2500000"), LocalDate.of(2026, 6, 30)));
    }

    private static void demostrarValidaciones() {
        System.out.println("VALIDACIONES");
        intentar("Horas negativas", () -> new EmpleadoPorHoras(
                new DatosEmpleado("9001", "Prueba Horas", LocalDate.of(2025, 1, 1)),
                Dinero.de("20000"), new BigDecimal("-5"), false));
        intentar("Ventas negativas", () -> new EmpleadoPorComision(
                new DatosEmpleado("9002", "Prueba Ventas", LocalDate.of(2025, 1, 1)),
                Dinero.de("1800000"), new BigDecimal("0.05"), Dinero.de("-100")));
    }

    private static void intentar(String caso, Runnable accion) {
        try {
            accion.run();
            System.out.println("  " + caso + ": aceptado");
        } catch (ValidacionNominaException error) {
            System.out.println("  " + caso + ": rechazado -> " + error.getMessage());
        }
    }
}
