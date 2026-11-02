package co.edu.unicartagena.nomina.repositorio;

import co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException;
import co.edu.unicartagena.nomina.dominio.comun.Validaciones;
import co.edu.unicartagena.nomina.dominio.empleado.Empleado;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de empleados, suficiente para la
 * demostración y las pruebas. Conserva el orden de registro.
 */
public class RepositorioEmpleadosEnMemoria implements RepositorioEmpleados {

    private final Map<String, Empleado> empleadosPorDocumento = new LinkedHashMap<>();

    @Override
    public void guardar(Empleado empleado) {
        Validaciones.requerirNoNulo(empleado, "empleado");
        if (empleadosPorDocumento.containsKey(empleado.getDocumento())) {
            throw new ValidacionNominaException(
                    "Ya existe un empleado con el documento " + empleado.getDocumento() + ".");
        }
        empleadosPorDocumento.put(empleado.getDocumento(), empleado);
    }

    @Override
    public Optional<Empleado> buscarPorDocumento(String documento) {
        return Optional.ofNullable(empleadosPorDocumento.get(documento));
    }

    @Override
    public List<Empleado> listarTodos() {
        return List.copyOf(empleadosPorDocumento.values());
    }
}
