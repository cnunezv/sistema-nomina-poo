package co.edu.unicartagena.nomina.dominio.empleado;

import co.edu.unicartagena.nomina.dominio.comun.Dinero;
import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Empleado asalariado")
class EmpleadoAsalariadoTest {

    private static final LocalDate CORTE = LocalDate.of(2026, 11, 30);

    private static EmpleadoAsalariado asalariadoDesde(LocalDate ingreso) {
        return new EmpleadoAsalariado(new DatosEmpleado("1001", "Ana Pérez", ingreso), Dinero.de("4000000"));
    }

    @Test
    @DisplayName("Con menos de 5 años devenga solo el salario fijo")
    void sinBonoConMenosDeCincoAnios() {
        EmpleadoAsalariado empleado = asalariadoDesde(CORTE.minusYears(3));

        assertEquals(Dinero.de("4000000"), empleado.calcularSalarioBruto(CORTE));
        assertEquals(1, empleado.calcularDevengos(CORTE).size());
    }

    @Test
    @DisplayName("Con más de 5 años recibe un bono del 10 % del salario")
    void bonoConMasDeCincoAnios() {
        EmpleadoAsalariado empleado = asalariadoDesde(CORTE.minusYears(5).minusDays(1));

        assertEquals(Dinero.de("4400000"), empleado.calcularSalarioBruto(CORTE));
    }

    @Test
    @DisplayName("El día exacto en que cumple 5 años todavía no recibe el bono")
    void sinBonoEnElAniversarioExacto() {
        EmpleadoAsalariado empleado = asalariadoDesde(CORTE.minusYears(5));

        assertEquals(Dinero.de("4000000"), empleado.calcularSalarioBruto(CORTE));
    }

    @Test
    @DisplayName("Rechaza un salario negativo")
    void rechazaSalarioNegativo() {
        DatosEmpleado datos = new DatosEmpleado("1001", "Ana Pérez", CORTE.minusYears(1));

        assertThrows(ValidacionNominaException.class,
                () -> new EmpleadoAsalariado(datos, Dinero.de("-1")));
    }
}
