package co.edu.unicartagena.nomina.servicio;

import co.edu.unicartagena.nomina.calculo.ConfiguracionNomina;
import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import co.edu.unicartagena.nomina.dominio.empleado.DatosEmpleado;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoAsalariado;
import co.edu.unicartagena.nomina.dominio.empleado.EmpleadoTemporal;
import co.edu.unicartagena.nomina.repositorio.RepositorioEmpleadosEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Servicio de nómina: casos de uso")
class ServicioNominaTest {

    private static final YearMonth NOVIEMBRE_2026 = YearMonth.of(2026, 11);

    private ServicioNomina servicio;

    @BeforeEach
    void prepararServicio() {
        servicio = new ServicioNomina(new RepositorioEmpleadosEnMemoria(),
                ConfiguracionNomina.crearCalculadoraEstandar());
        servicio.registrarEmpleado(new EmpleadoAsalariado(
                new DatosEmpleado("1001", "Ana Pérez", LocalDate.of(2019, 2, 1)), Dinero.de("4000000")));
        servicio.registrarEmpleado(new EmpleadoTemporal(
                new DatosEmpleado("4001", "Pedro Díaz", LocalDate.of(2026, 2, 1)),
                Dinero.de("2800000"), LocalDate.of(2027, 1, 31)));
        servicio.registrarEmpleado(new EmpleadoTemporal(
                new DatosEmpleado("4002", "Sofía Mejía", LocalDate.of(2025, 1, 1)),
                Dinero.de("2500000"), LocalDate.of(2026, 6, 30)));
    }

    @Test
    @DisplayName("Liquida la nómina solo de los empleados con contrato vigente")
    void liquidaSoloVigentes() {
        ResumenNomina resumen = servicio.liquidarNomina(NOVIEMBRE_2026);

        assertEquals(2, resumen.liquidaciones().size());
        assertEquals(1, resumen.noLiquidados().size());
    }

    @Test
    @DisplayName("Totaliza el neto de la empresa")
    void totalizaNeto() {
        ResumenNomina resumen = servicio.liquidarNomina(NOVIEMBRE_2026);

        // 5.201.032 (Ana) + 2.673.384 (Pedro)
        assertEquals(Dinero.de("7874416"), resumen.totalNeto());
        assertEquals(Dinero.de("7200000"), resumen.totalBruto());
    }

    @Test
    @DisplayName("Liquida a un empleado por su documento")
    void liquidaPorDocumento() {
        assertEquals(Dinero.de("5201032"), servicio.liquidarEmpleado("1001", NOVIEMBRE_2026).getSalarioNeto());
    }

    @Test
    @DisplayName("Informa cuando el empleado no existe")
    void empleadoInexistente() {
        assertThrows(ValidacionNominaException.class, () -> servicio.liquidarEmpleado("9999", NOVIEMBRE_2026));
    }

    @Test
    @DisplayName("No permite registrar dos empleados con el mismo documento")
    void rechazaDocumentoDuplicado() {
        EmpleadoAsalariado duplicado = new EmpleadoAsalariado(
                new DatosEmpleado("1001", "Otra Persona", LocalDate.of(2020, 1, 1)), Dinero.de("1000000"));

        assertThrows(ValidacionNominaException.class, () -> servicio.registrarEmpleado(duplicado));
    }
}
