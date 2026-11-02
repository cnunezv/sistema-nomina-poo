# Guion sugerido para el video de sustentación (YouTube)

Duración aproximada: 10 a 12 minutos. Si el CIPA tiene varios integrantes, repartan los bloques
para que todos expongan.

| Tiempo | Qué mostrar | Qué decir |
|---|---|---|
| 0:00–0:45 | Cámara | Presentación del CIPA, la asignatura y el objetivo: un sistema de nómina con POO. |
| 0:45–2:00 | `docs/METODOLOGIA.md` | Metodología: Scrum adaptado al CIPA con prácticas XP. Backlog, sprints, definición de terminado. Por qué ágil y no cascada. |
| 2:00–3:30 | `docs/DIAGRAMA_CLASES.md` | Arquitectura por capas. Clase abstracta `Empleado` y sus cuatro subclases. Interfaces pequeñas. |
| 3:30–5:00 | `Empleado.java`, `EmpleadoPorHoras.java`, `EmpleadoPorComision.java` | Herencia, polimorfismo y método plantilla. Horas extra y bono por ventas. Constantes con nombre y Javadoc. |
| 5:00–6:45 | `Deduccion.java`, `BonoAlimentacion.java`, `CalculadoraNomina.java`, `ConfiguracionNomina.java` | Los cinco principios SOLID con un ejemplo de cada uno (README, sección 4). Cómo agregar una deducción nueva sin tocar la calculadora. |
| 6:45–8:00 | GitHub → Commits → commit `refactor(calculo)` | Refactorización: la primera versión usaba `instanceof`; se extrajeron estrategias. Las pruebas conservaron sus valores. |
| 8:00–9:45 | Terminal: `mvn test` · `CalculadoraNominaTest` · reporte JaCoCo | 60 pruebas unitarias, esquema Preparar–Actuar–Verificar, casos borde y doble de prueba para el neto negativo. |
| 9:45–10:45 | Terminal: `java -jar target/sistema-nomina-poo-1.0.0.jar` | Demostración: comprobantes, totales y validaciones. Verificar a mano: 40 × 20.000 + 8 × 30.000 = 1.040.000. |
| 10:45–11:30 | GitHub: historial de commits y pestaña Actions | Control de versiones con Conventional Commits e integración continua. |
| 11:30–12:00 | Cámara | Conclusiones: SOLID facilitó cambiar reglas; las pruebas dieron confianza para refactorizar. |

## Lista de verificación

- [ ] Mencionar los **cinco principios SOLID** con un ejemplo del código.
- [ ] Mostrar **comentarios** (Javadoc).
- [ ] Mostrar el **diff de la refactorización**.
- [ ] Ejecutar las **pruebas unitarias** en vivo.
- [ ] Explicar la **metodología**.
- [ ] Mostrar el **historial de commits** en GitHub.
- [ ] Explicar los **supuestos** (ARL por clase de riesgo, fondo de ahorro, bono de alimentación).
