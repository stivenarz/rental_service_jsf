# Rental Service JSF

Aplicación Java con **JSF (Mojarra)**, **CDI (Weld)** y **JPA (Hibernate)** sobre **MySQL**.
Usa la misma base de datos y la misma tabla `categories` que `rental-services-api-java`.

Módulos: **Categorías** (implementado). Los demás módulos se agregarán siguiendo la misma estructura.

## Estructura MVC

```
src/main/java/com/rentalservices/jsf/
├── model/                      # Modelo: datos, acceso y reglas de negocio
│   ├── entity/Category.java    # Entidad JPA (tabla categories)
│   ├── dao/CategoryDAO.java    # Acceso a datos con EntityManager
│   └── service/CategoryService.java  # Reglas de negocio y validaciones
├── controller/                 # Controlador: beans JSF (CDI, @ViewScoped)
│   └── CategoryController.java
├── util/                       # Infraestructura compartida
│   ├── JpaUtil.java            # EntityManagerFactory único
│   ├── JpaContextListener.java # Inicia y cierra JPA con la aplicación
│   └── FacesUtil.java          # Mensajes de información y error
└── exception/
    └── DuplicateCategoryException.java

src/main/webapp/                # Vista: páginas XHTML
├── index.xhtml
├── categorias/list.xhtml
├── template/layout.xhtml       # Plantilla común (cabecera, menú, pie)
├── resources/css/styles.css    # Estilos de RentalService_WA
└── WEB-INF/                    # web.xml y beans.xml

src/main/resources/META-INF/persistence.xml   # Unidad de persistencia rentalServicesPU
```

## Requisitos

- JDK 21 o superior
- Maven 3.9+
- MySQL 8 en `localhost:3306`

La base `rental_services_db` se crea automáticamente con `createDatabaseIfNotExist=true`.
La tabla `categories` se crea o actualiza con `hibernate.hbm2ddl.auto=update`, igual que en la API.

## Credenciales

Por defecto usa `root` sin contraseña. Puedes cambiarlas con variables de entorno:

```bash
export DB_USERNAME=mi_usuario
export DB_PASSWORD=mi_clave
```

## Ejecutar

```bash
mvn org.eclipse.jetty.ee10:jetty-ee10-maven-plugin:12.0.16:run
```

Abre en el navegador: http://localhost:8081/rental_service_jsf/

El puerto 8081 evita el conflicto con la API, que usa el 8080.

## Empaquetar el WAR

```bash
mvn clean package
```

Genera `target/rental-service-jsf.war`.
