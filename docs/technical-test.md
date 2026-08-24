# Enunciado de la prueba técnica

Este documento reproduce el enunciado proporcionado para la realización del ejercicio.

## Contexto

En la base de datos de comercio electrónico de la compañía se dispone de la tabla `PRICES`, que
refleja el precio final de venta y la tarifa que se aplica a un producto de una cadena entre unas
fechas determinadas.

## Datos de ejemplo

| BRAND_ID | START_DATE | END_DATE | PRICE_LIST | PRODUCT_ID | PRIORITY | PRICE | CURR |
|---:|---|---|---:|---:|---:|---:|---|
| 1 | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 | 1 | 35455 | 0 | 35.50 | EUR |
| 1 | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 | 2 | 35455 | 1 | 25.45 | EUR |
| 1 | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 | 3 | 35455 | 1 | 30.50 | EUR |
| 1 | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 | 4 | 35455 | 1 | 38.95 | EUR |

## Descripción de los campos

- `BRAND_ID`: clave foránea de la cadena del grupo (`1` representa ZARA).
- `START_DATE` y `END_DATE`: rango de fechas en el que se aplica el precio de la tarifa.
- `PRICE_LIST`: identificador de la tarifa de precios aplicable.
- `PRODUCT_ID`: código identificador del producto.
- `PRIORITY`: desambiguador de aplicación. Si dos tarifas coinciden en un rango de fechas, se
  aplica la de mayor prioridad, entendida como el mayor valor numérico.
- `PRICE`: precio final de venta.
- `CURR`: código ISO de la moneda.

## Requisitos

Construir una aplicación o servicio con Spring Boot que proporcione un endpoint REST de consulta.

### Entrada

El endpoint debe aceptar:

- Fecha de aplicación.
- Identificador de producto.
- Identificador de cadena.

### Salida

El endpoint debe devolver:

- Identificador de producto.
- Identificador de cadena.
- Tarifa aplicable.
- Fechas de aplicación.
- Precio final aplicable.

Se debe utilizar una base de datos en memoria, como H2, e inicializarla con los datos del ejemplo.
Se permite cambiar el nombre de los campos, añadir otros nuevos y elegir los tipos de datos que se
consideren adecuados.

## Casos de prueba obligatorios

Desarrollar tests contra el endpoint REST que validen las siguientes peticiones con los datos del
ejemplo:

1. Petición a las 10:00 del día 14 para el producto `35455` y la marca `1` (ZARA).
2. Petición a las 16:00 del día 14 para el producto `35455` y la marca `1` (ZARA).
3. Petición a las 21:00 del día 14 para el producto `35455` y la marca `1` (ZARA).
4. Petición a las 10:00 del día 15 para el producto `35455` y la marca `1` (ZARA).
5. Petición a las 21:00 del día 16 para el producto `35455` y la marca `1` (ZARA).

## Criterios de valoración

- Diseño y construcción del servicio.
- Calidad del código.
- Resultados correctos en los tests.