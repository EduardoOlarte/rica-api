# Auditoría de Lenguaje Ubicuo

## 1. Revisión de nombres genéricos

En `InvestigadorController`, `InvestigadorRequest` e `InvestigadorResponse` no se identifican campos o métodos con nombres genéricos como `data`, `info`, `value` o `item` que deban reemplazarse por términos específicos del dominio.

Los nombres utilizados representan conceptos concretos del sistema, como `nombreCompleto`, `correoInstitucional`, `grupoInvestigacion` y las operaciones `listar`, `buscarPorId` y `registrar`.

En `Publicacion` tampoco se encuentran campos con nombres genéricos de ese tipo. Los atributos `titulo`, `tipo`, `anio`, `detalles` e `investigadorCorreo` permiten identificar directamente la información asociada con una publicación.

## 2. `correoInstitucional` y `grupoInvestigacion`

Los términos `correoInstitucional` y `grupoInvestigacion` se mantienen sin cambios porque corresponden directamente a conceptos reconocibles dentro del contexto de una Facultad y de un grupo de investigación.

`correoInstitucional` identifica el correo utilizado por el investigador dentro de la institución, mientras que `grupoInvestigacion` identifica el grupo al que pertenece.

Por lo tanto, estos nombres forman parte del Lenguaje Ubicuo del dominio y no necesitan ser reemplazados por nombres técnicos o genéricos.

## 3. `investigadorCorreo` en `Publicacion`

El campo `investigadorCorreo` es más preciso para el Lenguaje Ubicuo que un nombre genérico como `refId`, porque expresa directamente qué representa la relación: el correo institucional del investigador asociado con la publicación.

Un nombre como `refId` solamente describiría una referencia técnica y no permitiría conocer, a partir del nombre, qué entidad o dato del dominio representa dicha referencia.

Por esta razón, se conserva el nombre `investigadorCorreo`.


# 4. Límite del Agregado Investigador

## Raíz del Agregado

La raíz del Agregado Investigador es la entidad `Investigador`, ya que es la única clase con la anotación `@Entity` dentro del paquete `investigadores`.

## Elementos dentro del límite

Dentro del límite del Agregado Investigador se encuentran los siguientes elementos:

- `id`
- `nombreCompleto`
- `correoInstitucional`, representado actualmente mediante el Value Object `CorreoInstitucional`
- `grupoInvestigacion`

Estos elementos forman parte del estado propio del investigador y son administrados por la entidad `Investigador`.

## ¿Por qué Publicacion no está dentro del límite?

`Publicacion` no pertenece al Agregado Investigador porque un investigador puede tener múltiples publicaciones y ambos conceptos pueden evolucionar de manera independiente.

Mantener las publicaciones dentro del agregado haría que el Agregado Investigador fuera innecesariamente grande y podría provocar cargas y transacciones mayores al modificar información del investigador.

Además, el código actual respeta la regla de referenciar otros Agregados mediante identidad. `Publicacion` utiliza el campo `investigadorCorreo` como referencia al investigador, en lugar de mantener un objeto `Investigador` completo.

## ¿Qué pasaría si se agregara `List<Publicacion> publicaciones` a `Investigador`?

Agregar directamente un `List<Publicacion>` dentro de `Investigador` rompería el límite actual del Agregado. El investigador pasaría a contener directamente sus publicaciones, aumentando el tamaño y el alcance de las operaciones realizadas sobre el agregado.

Esto podría generar conflictos entre transacciones que modificaran simultáneamente las publicaciones de un investigador y dificultaría que `Publicacion` evolucionara o se desplegara de manera independiente.

Por esta razón, `Investigador` y `Publicacion` se mantienen como Agregados separados y `Publicacion` referencia al investigador únicamente mediante su identidad representada por `investigadorCorreo`.