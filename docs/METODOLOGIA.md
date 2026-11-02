# Metodología de desarrollo

## Enfoque elegido

**Scrum adaptado al CIPA, combinado con prácticas de Programación Extrema (XP).**

Se eligió un enfoque ágil e incremental en lugar de uno en cascada porque:

- Las reglas del enunciado tenían puntos abiertos (ARL, fondo de ahorro) que se aclararon sobre la marcha; un enfoque ágil permite ajustar sin rehacer todo.
- El equipo es pequeño y trabaja a distancia: entregas cortas y verificables por commit facilitan la coordinación.
- Las pruebas automáticas permiten refactorizar con confianza, que es una exigencia de la actividad.

## Roles

| Rol | Responsabilidad en el CIPA |
|---|---|
| Dueño del producto | Interpreta el enunciado, prioriza el backlog y valida los supuestos de negocio. |
| Equipo de desarrollo | Diseña, programa, prueba y documenta. Todos los integrantes hacen commits. |
| Facilitador (Scrum Master) | Rol rotativo: organiza las reuniones y vela por la definición de terminado. |

## Backlog del producto (historias de usuario)

| ID | Historia | Criterio de aceptación |
|---|---|---|
| HU-01 | Como empresa quiero liquidar a un **asalariado** para pagarle su salario fijo. | Con más de 5 años recibe 10 % adicional; el aniversario exacto no cuenta. |
| HU-02 | Como empresa quiero liquidar a un empleado **por horas** según lo trabajado. | Horas > 40 a 1.5x la tarifa; sin bonos; horas negativas rechazadas. |
| HU-03 | Como empresa quiero liquidar a un empleado **por comisión**. | Base + comisión; bono 3 % si ventas > $20.000.000; ventas negativas rechazadas. |
| HU-04 | Como empresa quiero liquidar a un **temporal** mientras su contrato esté vigente. | Sin bonos ni beneficios; no se liquida con contrato vencido. |
| HU-05 | Como empresa quiero aplicar las **deducciones obligatorias**. | Seguro social y pensión 4 %; ARL según clase de riesgo. |
| HU-06 | Como empleado permanente quiero recibir el **bono de alimentación**. | $1.000.000 al mes para asalariados y por comisión. |
| HU-07 | Como empleado por horas quiero acceder al **fondo de ahorro**. | 2 % del salario si tiene más de 1 año y aceptó el fondo. |
| HU-08 | Como empresa quiero que **ningún neto sea negativo**. | La liquidación se rechaza si el neto es menor que cero. |
| HU-09 | Como responsable de nómina quiero **liquidar a todos** y ver los totales. | Resumen con totales; temporales vencidos listados aparte. |

## Sprints

| Sprint | Objetivo | Historias | Commits |
|---|---|---|---|
| 0 | Preparar el proyecto | — | `chore: estructura inicial` |
| 1 | Modelo de dominio y tipos de empleado | HU-01 a HU-04 | `feat(dominio)`, `feat(empleados)` |
| 2 | Cálculo de la liquidación | HU-05 a HU-08 | `feat(calculo)`, `refactor(calculo)` |
| 3 | Casos de uso, reporte y entrega | HU-09 | `feat(servicio)`, `ci`, `docs` |

Cada sprint cierra con una revisión (demostración del avance) y una retrospectiva corta. La
retrospectiva del sprint 2 motivó la refactorización: la primera calculadora cumplía las
reglas, pero tenía `instanceof` y obligaba a modificarla para cada regla nueva.

## Prácticas XP aplicadas

- **Desarrollo guiado por pruebas:** cada regla se acompañó de sus pruebas, incluidos los casos borde.
- **Refactorización continua** apoyada en las pruebas (el comportamiento se verifica antes y después).
- **Diseño simple:** solo lo que piden las reglas; nada de capas que no se usen.
- **Integración continua:** GitHub Actions ejecuta `mvn verify` en cada push.
- **Estándares de código:** convenciones de nombres, Javadoc y Conventional Commits.

## Definición de terminado

Una historia está terminada cuando:

1. El código compila sin advertencias (`-Xlint:all`).
2. Tiene pruebas unitarias de sus casos normales y de borde, y todas pasan.
3. No contiene números mágicos y sus clases públicas tienen Javadoc.
4. Está integrada en `main` mediante un commit descriptivo y la integración continua está en verde.
5. Sus supuestos están documentados en el README.

## Control de versiones

- Rama principal `main`, siempre estable.
- Mensajes con **Conventional Commits**: `feat`, `refactor`, `test`, `ci`, `docs`, `chore`.
- Cada commit deja el proyecto compilando y con las pruebas en verde.
