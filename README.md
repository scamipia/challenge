# 🛒 API de Detalle de Ítems

Backend inspirado en **Mercado Libre**, que expone los datos de un producto y sus recursos asociados (vendedor, reseñas, preguntas, opciones de envío y productos relacionados).  
Diseñado para demostrar buenas prácticas en arquitectura, testeo, documentación y CI/CD.

---

## 🚀 Tecnologías

- **Java 21**
- **Spring Boot 3**
- **Maven 3.9+**
- **Springdoc OpenAPI** (Swagger UI)
- **JUnit 5 / Mockito / AssertJ**
- **GitHub Actions** (CI/CD)

---

## 🧠 Descripción del proyecto

El servicio expone endpoints RESTful que permiten obtener el **detalle completo de un ítem**, junto con información complementaria del vendedor, reseñas, preguntas frecuentes y productos relacionados.  
Los datos de ejemplo estan en `src/main/resources/data/catalog.json`; `SampleDataLoader` los deserializa al iniciar la app y los repositorios en memoria los sirven para cumplir con el requerimiento de persistencia liviana.

La arquitectura sigue el patrón **Controller → Service → Repository**, con DTOs para desacoplar el modelo interno del contrato HTTP y pruebas unitarias en todas las capas.

---

## ⚙️ Ejecución local

Cloná el repositorio y ejecutá:

```bash
mvn spring-boot:run
```

La aplicación inicia en `http://localhost:8080`.

## Documentación interactiva

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Estas rutas se generan automáticamente mediante springdoc-openapi-starter-webmvc-ui.

## Endpoints

- `GET /api/items/{id}`: Devuelve el detalle completo del item.
- `GET /api/items/{id}/related`: Sugerencias de productos (id, título, precio, imagen).
- `GET /api/items/{id}/questions`: Preguntas frecuentes con respuestas del vendedor.
- `GET /api/items/{id}/reviews?page=0&size=10`: Reseñas paginadas con métricas de promedio y distribución.
- `GET /api/items/{id}/shipping-options?zipcode=1000`: Opciones de envío calculadas según código postal.
- `GET /api/sellers/{sellerId}`: Información del vendedor: reputación, ventas y políticas.

Ejemplo con `curl`:

```bash
curl http://localhost:8080/api/items/MLA123456/reviews?page=0\&size=5 | jq
```

Los datos de ejemplo se encuentran en `src/main/resources/data/catalog.json`.  
Al iniciar la aplicación, la clase `SampleDataLoader` los carga en memoria y los pone a disposición de los repositorios simulados.  

El catálogo incluye un ítem principal con **ID `MLA123456`**, su **vendedor asociado (`S123`)**, y los recursos relacionados:  
preguntas frecuentes, reseñas de usuarios, opciones de envío y productos sugeridos.

## Tests

```bash
mvn test
```

- Pruebas unitarias e integración ligera (`src/test/java/com/hackerrank/sample/...`), incluyendo `ItemApiE2ETest` que levanta la aplicación completa y valida `GET /api/items/{id}`.
- Runner dinámico `HttpJsonDynamicUnitTest`, que consume los casos en `src/test/resources/testcases/*.json` para validar escenarios HTTP end-to-end. El reporte personalizado queda en `target/customReports`.

## Automatización CI

El workflow de GitHub Actions (`.github/workflows/ci.yml`) compila y ejecuta `mvn verify` en cada push y pull request, usando Java 21 en Ubuntu.

## Estructura relevante

- `src/main/java/com/hackerrank/sample/model/Item` — Modelo de dominio.
- `src/main/java/com/hackerrank/sample/dto/ItemDetailDto` — DTO expuesto por la API.
- `src/main/java/com/hackerrank/sample/controller/ItemController` — Controlador REST.
- `src/main/java/com/hackerrank/sample/service/ItemService` — Capa de servicio y mapeos.
- `src/main/java/com/hackerrank/sample/repository/SampleDataLoader` — Carga el JSON de ejemplo y lo distribuye a los repositorios.
- `src/main/java/com/hackerrank/sample/repository/InMemoryItemRepository` — Fuente de datos en memoria que delega en el loader.