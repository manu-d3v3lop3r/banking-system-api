# Sistema Bancario API

API REST desarrollada con **Java 21** y **Spring Boot** para la gestión de un sistema bancario. La aplicación implementa autenticación mediante JWT, control de acceso basado en roles y operaciones sobre clientes, cuentas, tarjetas y transacciones, siguiendo una arquitectura por capas y buenas prácticas de desarrollo.

---

## Badges

[![Java](https://img.shields.io/badge/Java-21-red?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-success?logo=springsecurity)](https://spring.io/projects/spring-security)
[![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5-success?logo=junit5)](https://junit.org/junit5/)
[![Mockito](https://img.shields.io/badge/Mockito-Tests-green)](https://site.mockito.org/)

---

# Índice

- [Características](#características)
- [Capturas](#capturas)
- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Modelo de dominio](#modelo-de-dominio)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Uso](#uso)
- [Documentación Swagger](#documentación-swagger)
- [Base de datos](#base-de-datos)
- [Testing](#testing)
- [Calidad del código](#calidad-del-código)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Roadmap](#roadmap)
- [Contribuir](#contribuir)
- [Autor](#autor)

---

# Características

La API ofrece las siguientes funcionalidades principales:

- ✔ Gestión de clientes.
- ✔ Gestión de cuentas bancarias.
- ✔ Gestión de tarjetas de débito y crédito.
- ✔ Operaciones de depósito, retirada y transferencia.
- ✔ Consulta del historial de transacciones.
- ✔ Autenticación mediante JWT.
- ✔ Autorización basada en roles.
- ✔ Validaciones con Jakarta Validation.
- ✔ Gestión global de excepciones.
- ✔ Documentación interactiva con Swagger/OpenAPI.
- ✔ Base de datos H2 para desarrollo.
- ✔ Tests unitarios e integración.

---

# Capturas

## Swagger UI

Interfaz principal de la documentación de la API.

![Swagger](docs/images/portada-swagger.png)

## Autenticación

Obtención de un token JWT.

![Login](docs/images/autenticacion-swagger.png)

## Gestión de clientes

![Cliente](docs/images/cliente-swagger.png)

## Gestión de cuentas

![Cuenta](docs/images/cuenta-swagger.png)

## Transferencias

![Transferencia](docs/images/transferencia-swagger.png)

## Gestión de tarjetas

![Tarjeta](docs/images/tarjeta-swagger.png)

## Bloqueo de tarjetas

![Bloquear tarjeta](docs/images/bloquear-swagger.png)

## Base de datos H2

![H2](docs/images/tablas-h2.png)

## Cobertura de pruebas

![Coverage](docs/images/coverage.png)

---

# Tecnologías

| Tecnología | Uso |
|------------|-----|
| Java 21 | Lenguaje de programación |
| Spring Boot | Framework principal |
| Spring Security | Autenticación y autorización |
| JWT (jjwt) | Gestión de tokens |
| Spring Data JPA | Persistencia |
| Hibernate | ORM |
| H2 Database | Base de datos para desarrollo |
| MapStruct | Conversión Entity ↔ DTO |
| Jakarta Validation | Validación de datos |
| Springdoc OpenAPI | Documentación Swagger |
| Maven | Gestión de dependencias |
| JUnit 5 | Tests unitarios |
| Mockito | Mocking |
| MockMvc | Tests de integración de controladores |

---

# Arquitectura

El proyecto sigue una **arquitectura por capas**, donde cada componente tiene una responsabilidad claramente definida.

```text
                 Cliente HTTP
                       │
                       ▼
               REST Controllers
                       │
                       ▼
                  Service Layer
                       │
                       ▼
                Repository Layer
                       │
                       ▼
                 Base de datos
```

## Responsabilidades

| Capa | Responsabilidad |
|------|-----------------|
| Controller | Expone la API REST y procesa las peticiones HTTP. |
| Service | Implementa la lógica de negocio. |
| Repository | Acceso a la base de datos mediante Spring Data JPA. |
| Entity | Modelo persistente del dominio. |
| DTO | Objetos utilizados para la comunicación con el cliente. |
| Mapper | Conversión entre entidades y DTOs mediante MapStruct. |
| Security | Autenticación, autorización y gestión de JWT. |
| Exception | Gestión centralizada de errores y excepciones. |

Esta separación facilita el mantenimiento, la escalabilidad y la realización de pruebas unitarias.

---

# Modelo de dominio

El sistema está compuesto por cuatro entidades principales.

```text
Cliente
│
├── Cuenta
│     │
│     ├── Tarjeta
│     │
│     └── Transacción
```

## Relaciones

| Entidad | Relación |
|----------|----------|
| Cliente | Puede tener una o varias cuentas bancarias. |
| Cuenta | Pertenece a un único cliente. |
| Cuenta | Puede tener varias tarjetas asociadas. |
| Cuenta | Registra múltiples transacciones. |
| Tarjeta | Está asociada a una única cuenta. |
| Transacción | Registra operaciones realizadas sobre una cuenta. |

---

# Instalación

## Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- Java 21 o superior
- Maven 3.9 o superior
- Git

Comprueba las versiones instaladas:

```bash
java --version
mvn --version
git --version
```

---

## Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/sistema-bancario-api.git
```

Accede al directorio del proyecto:

```bash
cd sistema-bancario-api
```

---

## Compilar el proyecto

```bash
mvn clean install
```

Este comando descargará todas las dependencias y generará el artefacto de la aplicación.

---

## Ejecutar la aplicación

Puedes iniciar la API mediante Maven:

```bash
mvn spring-boot:run
```

O ejecutando el archivo JAR generado:

```bash
java -jar target/sistema-bancario-api.jar
```

Una vez iniciada, la aplicación estará disponible en:

```
http://localhost:8080
```

---

# Configuración

La configuración principal se encuentra en:

```text
src/main/resources/application.properties
```

## Base de datos

Durante el desarrollo se utiliza una base de datos H2 en memoria.

```properties
spring.datasource.url=jdbc:h2:mem:bancodb
spring.datasource.username=sa
spring.datasource.password=
```

## JWT

La autenticación se realiza mediante JSON Web Token.

Configuración:

```properties
jwt.secret=CHANGE_ME_IN_PRODUCTION
jwt.issuer=BancoAPI
jwt.expiration=3600000
```

> **Importante:** Para un entorno de producción se recomienda utilizar una clave secreta segura y gestionarla mediante variables de entorno o un gestor de secretos.

---

# Uso

## Acceso a Swagger

Una vez iniciada la aplicación, la documentación estará disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

Desde Swagger es posible:

- Consultar todos los endpoints disponibles.
- Ejecutar peticiones directamente desde el navegador.
- Visualizar los modelos de petición y respuesta.
- Autenticarse mediante JWT.

---

## Autenticación

Obtén un token JWT realizando una petición al endpoint de login.

```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{
  "username":"admin",
  "password":"admin123"
}'
```

Respuesta:

```json
{
  "token":"eyJhbGciOiJIUzI1NiJ9...",
  "type":"Bearer"
}
```

Guarda el token y utilízalo en las peticiones protegidas:

```text
Authorization: Bearer <TOKEN>
```

---

## Crear un cliente

```bash
curl -X POST http://localhost:8080/api/clientes \
-H "Authorization: Bearer <TOKEN>" \
-H "Content-Type: application/json" \
-d '{
  "nombre":"Alfonso",
  "apellido":"Perez",
  "documento":"12345678A",
  "email":"alfonso@test.com"
}'
```

---

## Crear una cuenta bancaria

```bash
curl -X POST http://localhost:8080/api/cuentas \
-H "Authorization: Bearer <TOKEN>" \
-H "Content-Type: application/json" \
-d '{
  "clienteId":1
}'
```

---

## Realizar un depósito

```bash
curl -X POST http://localhost:8080/api/transacciones/deposito \
-H "Authorization: Bearer <TOKEN>" \
-H "Content-Type: application/json" \
-d '{
  "numeroCuenta":"12345678901234567890",
  "monto":500
}'
```

---

## Realizar una retirada

```bash
curl -X POST http://localhost:8080/api/transacciones/retiro \
-H "Authorization: Bearer <TOKEN>" \
-H "Content-Type: application/json" \
-d '{
  "numeroCuenta":"12345678901234567890",
  "monto":200
}'
```

---

## Realizar una transferencia

```bash
curl -X POST http://localhost:8080/api/transacciones/transferencia \
-H "Authorization: Bearer <TOKEN>" \
-H "Content-Type: application/json" \
-d '{
  "cuentaOrigen":"11111111111111111111",
  "cuentaDestino":"22222222222222222222",
  "monto":150
}'
```

---

## Crear una tarjeta

```bash
curl -X POST http://localhost:8080/api/tarjetas \
-H "Authorization: Bearer <TOKEN>" \
-H "Content-Type: application/json" \
-d '{
  "cuentaId":1,
  "tipo":"DEBITO"
}'
```

---

## Bloquear una tarjeta

```bash
curl -X PUT http://localhost:8080/api/tarjetas/{numeroTarjeta}/bloquear \
-H "Authorization: Bearer <TOKEN>"
```

> Todos los ejemplos anteriores pueden ejecutarse también desde la interfaz de Swagger sin necesidad de utilizar una herramienta externa como Postman o cURL.

---

# Documentación Swagger

La API incorpora documentación interactiva mediante **Springdoc OpenAPI**.

Una vez iniciada la aplicación, la interfaz estará disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

Swagger permite:

- Consultar todos los endpoints disponibles.
- Ejecutar peticiones directamente desde el navegador.
- Visualizar los modelos de petición y respuesta.
- Autenticarse mediante JWT utilizando el botón **Authorize**.

La galería de capturas de la interfaz puede consultarse en la sección [Capturas](#capturas).

---

# Base de datos

Durante el desarrollo la aplicación utiliza una base de datos **H2** en memoria.

La consola puede abrirse desde:

```
http://localhost:8080/h2-console
```

Configuración de acceso:

| Parámetro | Valor |
|-----------|-------|
| JDBC URL | `jdbc:h2:mem:bancodb` |
| Usuario | `sa` |
| Contraseña | *(vacía)* |

### Consola H2

![H2](docs/images/tablas-h2.png)

---

# Testing

El proyecto incluye una batería de pruebas para validar el comportamiento de la aplicación.

## Tipos de pruebas

- Tests unitarios.
- Tests de integración.
- Tests de controladores mediante MockMvc.
- Pruebas de servicios con Mockito.
- Validación de mappers.

Ejecutar todos los tests:

```bash
mvn clean test
```

Compilar y ejecutar las verificaciones:

```bash
mvn clean verify
```

---

## Cobertura

La cobertura puede comprobarse directamente desde IntelliJ IDEA ejecutando los tests con **Run with Coverage**.

### Resultado

![Coverage](docs/images/coverage.png)

---

# Calidad del código

El proyecto sigue una serie de buenas prácticas para mejorar su mantenibilidad y escalabilidad.

- Arquitectura por capas.
- Principios SOLID.
- Separación entre entidades y DTOs.
- Conversión automática mediante MapStruct.
- Validaciones con Jakarta Validation.
- Gestión centralizada de excepciones.
- Seguridad basada en JWT.
- Código desacoplado mediante inyección de dependencias.
- Cobertura mediante pruebas unitarias e integración.

---

# Estructura del proyecto

```text
src
├── main
│   ├── java
│   │   └── com.banco.api
│   │       ├── config
│   │       ├── constants
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── enums
│   │       ├── exception
│   │       ├── generator
│   │       ├── mapper
│   │       ├── repository
│   │       ├── security
│   │       ├── service
│   │       └── validation
│   │
│   └── resources
│
└── test
    └── java
        └── com.banco.api
            ├── controller
            ├── integration
            ├── mapper
            ├── repository
            └── service
```

---

# Roadmap

Las siguientes mejoras están previstas para futuras versiones del proyecto.

- [x] API REST.
- [x] Arquitectura por capas.
- [x] Spring Security.
- [x] Autenticación JWT.
- [x] Documentación Swagger/OpenAPI.
- [x] Base de datos H2.
- [x] Tests unitarios.
- [x] Tests de integración.
- [ ] Persistencia con PostgreSQL.
- [ ] Contenerización mediante Docker.
- [ ] Refresh Tokens.
- [ ] Paginación y filtrado.
- [ ] Auditoría de operaciones.
- [ ] Caché con Redis.
- [ ] Pipeline CI/CD con GitHub Actions.
- [ ] Despliegue en la nube.

---

# Contribuir

Las contribuciones son bienvenidas.

Si deseas colaborar con el proyecto:

1. Haz un **Fork** del repositorio.
2. Crea una nueva rama.

```bash
git checkout -b feature/nueva-funcionalidad
```

3. Realiza tus cambios.
4. Ejecuta todos los tests.

```bash
mvn clean test
```

5. Realiza un commit descriptivo.

```bash
git commit -m "Añadir nueva funcionalidad"
```

6. Sube los cambios y abre un **Pull Request**.

---

# Autor

**Manuel**

Proyecto desarrollado como práctica de Backend utilizando **Java**, **Spring Boot** y **Spring Security**, aplicando principios SOLID, arquitectura por capas, autenticación mediante JWT y pruebas automatizadas.

GitHub:

```
https://github.com/manu-d3v3lop3r
```