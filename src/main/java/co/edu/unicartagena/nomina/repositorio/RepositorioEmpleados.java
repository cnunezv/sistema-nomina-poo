package co.edu.unicartagena.nomina.repositorio;

import co.edu.unicartagena.nomina.dominio.empleado.Empleado;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de persistencia de empleados. El servicio depende de esta interfaz y
 * no de una base de datos concreta, por lo que la implementación en memoria
 * se puede reemplazar por una con JDBC o JPA sin tocar la lógica de negocio.
 */
public interface RepositorioEmpleados {

    /**
     * Guarda un empleado nuevo.
     *
     * @param empleado empleado a guardar
     * @throws co.edu.unicartagena.nomina.dominio.comun.ValidacionNominaException
     *         si ya existe un empleado con el mismo documento
     */
    void guardar(Empleado empleado);

    /**
     * @param documento documento del empleado
     * @return el empleado, si existe
     */
    Optional<Empleado> buscarPorDocumento(String documento);

    /**
     * @return todos los empleados, en el orden en que se registraron
     */
    List<Empleado> listarTodos();
}
