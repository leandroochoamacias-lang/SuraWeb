<%@ page import="java.util.List" %>
<%@ page import="com.sura.model.Paciente" %>
<!DOCTYPE html>
<html lang="es">
<head><meta charset="UTF-8"><title>Pacientes</title></head>
<body>
<h1>Listado de pacientes</h1>
<a href="registrarPaciente.jsp">Nuevo paciente</a>
<table border="1" cellpadding="8">
<tr><th>ID</th><th>Nombre</th><th>Apellido</th><th>Edad</th><th>Acciones</th></tr>
<%
List<Paciente> pacientes = (List<Paciente>) request.getAttribute("pacientes");
if (pacientes != null) {
    for (Paciente p : pacientes) {
%>
<tr>
<td><%= p.getId() %></td>
<td><%= p.getNombre() %></td>
<td><%= p.getApellido() %></td>
<td><%= p.getEdad() %></td>
<td>
<a href="PacienteServlet?accion=editar&id=<%=p.getId()%>">Editar (GET)</a>
<form action="PacienteServlet" method="POST" style="display:inline">
<input type="hidden" name="accion" value="eliminar">
<input type="hidden" name="id" value="<%=p.getId()%>">
<button type="submit">Eliminar (POST)</button>
</form>
</td>
</tr>
<%
    }
}
%>
</table>
<br><a href="index.jsp">Inicio</a>
</body>
</html>
