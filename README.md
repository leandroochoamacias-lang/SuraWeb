# SuraWeb - GA7-220501096-AA2-EV02

Proyecto web académico basado en el proyecto Java Swing anterior.
Tecnologías: Java 17, Jakarta Servlet, JSP, HTML, MySQL y Maven.

## Módulo
Gestión de pacientes: registrar, consultar, editar y eliminar.

## Métodos HTTP
- GET: consultar pacientes y cargar datos para edición.
- POST: registrar, actualizar y eliminar.

## Base de datos
Se conserva la tabla `paciente1` del proyecto anterior:
`ID`, `NOMBRE`, `APELLIDO`, `EDAD`.

Ajusta las credenciales de MySQL en `Conexion.java`.

## Ejecución
1. Crear/importar la base de datos.
2. Configurar MySQL.
3. Abrir el proyecto como Maven en NetBeans.
4. Configurar Tomcat 10+.
5. Ejecutar.
