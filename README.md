# Price Service

Servicio REST para consultar el precio aplicable a un producto de una cadena en una fecha
determinada.

Cuando existen varias tarifas vigentes para la misma marca y producto, el servicio selecciona la
de mayor prioridad.

El [enunciado de la prueba técnica](docs/technical-test.md) está incluido como referencia.

## Tecnologías

- Java 25
- Spring Boot 4.1.1
- Spring MVC
- Spring Data JPA
- Jakarta Validation
- H2 en memoria
- Flyway
- Maven Wrapper
- JUnit 5 y MockMvc

## Ejecución

El proyecto incluye Maven Wrapper, por lo que no es necesario instalar Maven globalmente.

```bash
./mvnw spring-boot:run
```

La aplicación se inicia en:

```text
http://localhost:8085
```

Para ejecutar todos los tests:

```bash
./mvnw clean test
```

Para generar el artefacto ejecutable:

```bash
./mvnw clean package
```

## Endpoint

```http
GET /api/v1/prices/applicable
```

### Parámetros

| Parámetro | Tipo | Descripción |
|---|---|---|
| `applicationDate` | ISO-8601 `LocalDateTime` | Fecha en la que debe estar vigente la tarifa |
| `productId` | Entero positivo | Identificador del producto |
| `brandId` | Entero positivo | Identificador de la cadena |

Ejemplo:

```bash
curl "http://localhost:8085/api/v1/prices/applicable?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"
```

Respuesta:

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "amount": 25.45,
  "currency": "EUR"
}
```

## Regla de selección

Una tarifa es aplicable cuando cumple todas estas condiciones:

```text
brandId coincide
productId coincide
startDate <= applicationDate <= endDate
```

Los límites del periodo son inclusivos. Si varias tarifas cumplen las condiciones, se devuelve la
que tenga el mayor valor de `priority`.

El filtrado, la ordenación por prioridad y el límite a un resultado se ejecutan en la base de
datos. No se cargan todas las tarifas para filtrarlas en memoria.

## Datos iniciales

Flyway crea el esquema de H2 e inserta los cuatro registros proporcionados en el enunciado. Las
migraciones se encuentran en:

```text
src/main/resources/db/migration
```

Hibernate está configurado con `ddl-auto=validate`: Flyway es responsable del esquema y Hibernate
comprueba que este coincide con la entidad JPA.

Mientras la aplicación está en ejecución, la consola de H2 está disponible en:

```text
http://localhost:8085/h2-console
```

Datos de conexión:

```text
JDBC URL: jdbc:h2:mem:pricedb
User: sa
Password: [vacío]
```

## Arquitectura

El servicio utiliza una arquitectura hexagonal ligera organizada por capacidad de negocio:

```text
com.inditex.prices.price
├── domain
│   ├── model
│   ├── valueobject
│   └── exception
├── application
│   ├── port
│   │   ├── in
│   │   └── out
│   ├── service
│   └── exception
└── infrastructure
    └── adapter
        ├── in/rest
        └── out/persistence
```

El flujo de una consulta es:

```text
HTTP request
  -> PriceQueryController
  -> FindApplicablePriceUseCase
  -> FindApplicablePriceService
  -> PriceRepository
  -> PricePersistenceAdapter
  -> PriceJpaRepository
  -> H2
```

El dominio no depende de Spring, JPA ni HTTP. Los DTO REST, los contratos de aplicación, el modelo
de dominio y la entidad JPA se mantienen separados mediante mappers explícitos.

## Modelo de dominio

`Money` es un value object inmutable que agrupa importe y moneda. Protege, entre otras, estas
reglas:

- Importe y moneda obligatorios.
- Importe mayor que cero.
- Código ISO de moneda válido.
- Escala compatible con la moneda.
- Precisión compatible con `DECIMAL(19,4)`.

`Price` es un modelo de dominio inmutable. No se modela como agregado porque el alcance del
servicio es exclusivamente de lectura y no existe un ciclo de vida de creación o modificación de
tarifas.

## Errores

Los errores REST se representan mediante `ProblemDetail`.

- `400 Bad Request`: parámetros ausentes, inválidos o con formato incorrecto.
- `404 Not Found`: no existe un precio aplicable para los criterios recibidos.

Ejemplo de validación:

```json
{
  "title": "Validation failed",
  "status": 400,
  "detail": "One or more request parameters are invalid",
  "instance": "/api/v1/prices/applicable",
  "timestamp": "2026-08-23T12:00:00Z",
  "errors": {
    "productId": [
      "Product identifier must be greater than zero"
    ]
  }
}
```

## Tests

La suite contiene 22 pruebas automatizadas:

- Cinco escenarios de integración requeridos por el enunciado.
- Integración para respuestas `400` y `404`.
- Pruebas unitarias del servicio de aplicación.
- Pruebas unitarias de las invariantes de `Money`.
- Pruebas unitarias de vigencia e invariantes de `Price`.

Los tests de integración usan MockMvc y recorren el flujo completo desde HTTP hasta H2. Flyway
inicializa el esquema y los datos antes de su ejecución.

## Decisiones de alcance

Se han aplicado separación de responsabilidades, dominio independiente y puertos/adaptadores sin
incorporar infraestructura innecesaria para un único caso de uso de lectura. Por ello no se han
añadido buses CQRS, eventos, mensajería, seguridad ni una segunda base de datos.
