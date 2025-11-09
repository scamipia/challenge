# API de Detalle de Items

Backend estilo Mercado Libre que expone datos de un producto para su página de detalle.

## Requisitos

- Java 21
- Maven 3.9+ (o wrapper `mvnw` si lo agregas más adelante)

## Ejecución

```bash
mvn spring-boot:run
```

La aplicación inicia en `http://localhost:8080`.

## Documentación interactiva

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Tests

```bash
mvn test
```

Se ejecutan pruebas unitarias (`src/test/java/com/hackerrank/sample/...`) y el runner dinámico `HttpJsonDynamicUnitTest`, que consume los casos en `src/test/resources/testcases/*.json` para validar el contrato HTTP end-to-end. El reporte personalizado queda en `target/customReports`.

## Automatización CI

El workflow de GitHub Actions (`.github/workflows/ci.yml`) compila y ejecuta `mvn verify` en cada push y pull request, usando Java 21 en Ubuntu.

## Endpoints

- `GET /api/items/{id}`: detalle completo del item.
- `GET /api/items/{id}/related`: sugerencias de productos (id, título, precio, thumbnail).
- `GET /api/items/{id}/questions`: preguntas frecuentes con respuestas del vendedor.
- `GET /api/items/{id}/reviews?page=0&size=10`: reseñas paginadas con métricas de promedio y distribución.
- `GET /api/items/{id}/shipping-options?zipcode=1000`: opciones de envío calculadas según código postal.
- `GET /api/sellers/{sellerId}`: reputación, ventas y políticas principales del vendedor.

Ejemplo con `curl`:

```bash
curl http://localhost:8080/api/items/MLA123456/reviews?page=0\&size=5 | jq
```

El repositorio en memoria (`InMemoryItemRepository`) precarga datos coherentes para el item `MLA123456`, su vendedor (`S123`) y los recursos relacionados (preguntas, reseñas, envíos, relacionados, etc.).

## Estructura relevante

- `src/main/java/com/hackerrank/sample/model/Item` — Modelo de dominio.
- `src/main/java/com/hackerrank/sample/dto/ItemDetailDto` — DTO expuesto por la API.
- `src/main/java/com/hackerrank/sample/controller/ItemController` — Controlador REST.
- `src/main/java/com/hackerrank/sample/service/ItemService` — Capa de servicio y mapeos.
- `src/main/java/com/hackerrank/sample/repository/InMemoryItemRepository` — Fuente de datos en memoria.

## Próximos pasos sugeridos

- Reemplazar el repositorio en memoria por integración con un servicio real o base de datos.
- Añadir validaciones de entrada y `@ControllerAdvice` para un manejo uniforme de errores.
- Configurar perfiles (`application.yml`) para distintas fuentes de datos.

