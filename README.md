# Sistema de Nómina POO

![Pruebas unitarias](https://github.com/cnunezv/sistema-nomina-poo/actions/workflows/pruebas.yml/badge.svg)

Sistema de nómina desarrollado con **programación orientada a objetos** para una empresa con
cuatro tipos de empleados (asalariado, por horas, por comisión y temporal), cada uno con su
propia forma de calcular el salario, los beneficios y las deducciones.

**Actividad Unidad Tres (CIPA)** — Ingeniería de Software · Universidad de Cartagena (CTEV)
**Autor:** Carlos Andrés Núñez Vargas

| | |
|---|---|
| Lenguaje | Java 17 |
| Construcción | Maven |
| Pruebas | JUnit 5 (60 pruebas) + cobertura con JaCoCo |
| Integración continua | GitHub Actions |

## 1. Cómo ejecutarlo

```bash
mvn test                                   # pruebas unitarias
mvn verify                                 # pruebas + jar + reporte de cobertura
java -jar target/sistema-nomina-poo-1.0.0.jar   # demostración en consola
```

El reporte de cobertura queda en `target/site/jacoco/index.html`.

## 2. Reglas de negocio y dónde están implementadas

| Regla | Clase | Pruebas |
|---|---|---|
| Asalariado: salario fijo; bono 10 % con más de 5 años | `EmpleadoAsalariado` | `EmpleadoAsalariadoTest` |
| Por horas: tarifa base; horas > 40 a 1.5x; sin bonos | `EmpleadoPorHoras` | `EmpleadoPorHorasTest` |
| Por comisión: base + comisión; bono 3 % si ventas > $20.000.000 | `EmpleadoPorComision` | `EmpleadoPorComisionTest` |
| Temporal: salario fijo, tiempo definido, sin bonos ni beneficios | `EmpleadoTemporal` | `EmpleadoTemporalTest` |
| Seguro social y pensión: 4 % del bruto | `SeguridadSocialPension` | `DeduccionesTest` |
| ARL | `Arl` + `NivelRiesgoArl` | `DeduccionesTest` |
| Bono de alimentación $1.000.000 para permanentes | `BonoAlimentacion` | `BeneficiosTest` |
| Fondo de ahorro 2 % para por horas con más de 1 año que lo acepten | `FondoAhorro` | `BeneficiosTest` |
| El salario neto no puede ser negativo | `LiquidacionNomina` | `LiquidacionNominaTest`, `CalculadoraNominaExtensibilidadTest` |
| Las horas trabajadas no pueden ser negativas | `EmpleadoPorHoras` + `Validaciones` | `EmpleadoPorHorasTest` |
| Las ventas no pueden ser menores a $0 | `EmpleadoPorComision` + `Validaciones` | `EmpleadoPorComisionTest` |

### Supuestos de interpretación

El enunciado deja algunos puntos abiertos. Estas son las decisiones tomadas; cada una está
aislada en una sola clase, así que cambiarla no afecta al resto del sistema.

1. **Salario bruto** = conceptos salariales + bonos del tipo de empleado. Las deducciones se calculan sobre el bruto.
2. **ARL.** El enunciado no da un porcentaje. Se usan las cinco clases de riesgo de Colombia (Decreto 1772 de 1994): I = 0,522 %, II = 1,044 %, III = 2,436 %, IV = 4,350 %, V = 6,960 %. Cada empleado tiene su clase (por defecto, la I).
3. **Bono de alimentación.** Pago no salarial cubierto por la empresa: se suma al neto, pero no es base de deducciones.
4. **Fondo de ahorro.** Aporte de la empresa del 2 % del bruto que se deposita en el fondo del empleado; aparece en el comprobante pero no suma al neto del mes.
5. **"Más de 5 años" / "más de 1 año"** son estrictos: el día exacto del aniversario aún no cumple.
6. **"Superan $20.000.000"** es estricto: con ventas de exactamente $20.000.000 no hay bono.
7. **Horas extra** se cuentan sobre las horas registradas en el periodo liquidado.
8. **Temporales** solo se liquidan mientras el contrato está vigente.
9. Los valores monetarios usan `BigDecimal` con redondeo comercial a 2 decimales.

**Fórmula del neto:** `neto = bruto − deducciones + beneficios pagados al empleado`

## 3. Arquitectura

```
src/main/java/co/edu/unicartagena/nomina
├── App.java                  Raíz de composición y demostración
├── dominio
│   ├── comun                 Dinero, Validaciones, ConceptoNomina, ValidacionNominaException
│   └── empleado              Empleado (abstracta) y sus 4 subclases, DatosEmpleado,
│                             NivelRiesgoArl, TipoEmpleado, EmpleadoPermanente, ElegibleFondoAhorro
├── calculo
│   ├── CalculadoraNomina     Orquesta la liquidación de un empleado
│   ├── ConfiguracionNomina   Ensambla las reglas vigentes
│   ├── deduccion             Deduccion + SeguridadSocialPension, Arl
│   ├── beneficio             BeneficioAdicional + BonoAlimentacion, FondoAhorro
│   └── liquidacion           LiquidacionNomina, BeneficioLiquidado, ModalidadBeneficio
├── repositorio               RepositorioEmpleados + implementación en memoria
├── servicio                  ServicioNomina, ResumenNomina
└── presentacion              ReporteNominaConsola, FormatoMoneda
```

Diagramas de clases y de secuencia: [`docs/DIAGRAMA_CLASES.md`](docs/DIAGRAMA_CLASES.md).

## 4. Principios SOLID

| Principio | Aplicación |
|---|---|
| **S** — Responsabilidad única | Cada subclase de `Empleado` solo sabe calcular su salario; cada `Deduccion` y `BeneficioAdicional` encapsula una regla; `CalculadoraNomina` orquesta; `ServicioNomina` coordina casos de uso; `ReporteNominaConsola` solo da formato. |
| **O** — Abierto/cerrado | Un tipo de empleado nuevo es una subclase de `Empleado`; una deducción o beneficio nuevo es una clase que implementa la interfaz y se registra en `ConfiguracionNomina`. La calculadora no se modifica (`CalculadoraNominaExtensibilidadTest` lo demuestra con una libranza). |
| **L** — Sustitución de Liskov | Cualquier subclase se usa donde se espera un `Empleado`: la calculadora y el servicio nunca preguntan por el tipo concreto. Los métodos del algoritmo son `final`, así que ninguna subclase rompe el contrato. |
| **I** — Segregación de interfaces | `EmpleadoPermanente` y `ElegibleFondoAhorro` son interfaces pequeñas; solo las implementan los tipos a los que aplican. El temporal no implementa ninguna. |
| **D** — Inversión de dependencias | `CalculadoraNomina` depende de `Deduccion` y `BeneficioAdicional`; `ServicioNomina` depende de `RepositorioEmpleados`. Las implementaciones concretas se conectan solo en `App` y `ConfiguracionNomina`. |

**Patrones de diseño:** método plantilla (`Empleado.calcularDevengos`), estrategia (deducciones y
beneficios), repositorio, objeto de valor (`DatosEmpleado`, `ConceptoNomina`) e inyección de
dependencias por constructor.

## 5. Código limpio y buenas prácticas

- Nombres en español del dominio (`calcularSalarioBruto`, `tieneAntiguedadMayorA`).
- Sin números mágicos: cada valor de negocio es una constante con nombre (`TASA_BONO_ANTIGUEDAD`, `UMBRAL_VENTAS_BONO`…).
- `BigDecimal` para el dinero; nunca `double`.
- Objetos inmutables donde es posible (`record`, listas con `List.copyOf`).
- Validaciones centralizadas en `Validaciones` con mensajes uniformes.
- Métodos cortos con una sola tarea; Javadoc en todas las clases públicas.
- Compilación con `-Xlint:all` sin advertencias.

## 6. Refactorización

| Commit | Cambio |
|---|---|
| `feat(calculo): primera versión…` | Calculadora funcional con todas las reglas dentro y selección por `instanceof`. |
| `refactor(calculo): extraer deducciones y beneficios…` | Las reglas pasan a estrategias inyectadas por constructor; `ConfiguracionNomina` las ensambla. |

Las 8 pruebas de `CalculadoraNominaTest` conservaron sus valores esperados durante la
refactorización (solo cambió la forma de crear la calculadora), lo que demuestra que el
comportamiento se mantuvo.

## 7. Pruebas unitarias

60 pruebas en 14 clases, escritas con el esquema *Preparar – Actuar – Verificar*. Cubren los
casos normales de cada regla, los casos borde (exactamente 5 años, exactamente 1 año,
exactamente $20.000.000, cero horas, cero ventas, neto exactamente cero) y los rechazos de
datos inválidos. Para probar el neto negativo se usa un doble de prueba (`Deduccion` con valor
fijo).

## 8. Metodología de desarrollo

Scrum adaptado al CIPA con prácticas de Programación Extrema (XP): historias de usuario,
sprints cortos, desarrollo guiado por pruebas, refactorización continua, integración continua
y Conventional Commits. Detalle en [`docs/METODOLOGIA.md`](docs/METODOLOGIA.md).

## 9. Video de sustentación

Guion sugerido en [`docs/GUION_VIDEO.md`](docs/GUION_VIDEO.md).
