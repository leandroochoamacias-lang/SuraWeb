<%@ page import="com.sura.model.Paciente" %>
<%
Paciente p = (Paciente) request.getAttribute("paciente");
%>
<!DOCTYPE html>
<html lang="es">
<head><meta charset="UTF-8"><title>Editar paciente</title></head>
<body>
<h1>Editar paciente</h1>
<form action="PacienteServlet" method="POST">
    <input type="hidden" name="accion" value="actualizar">
    <input type="hidden" name="id" value="<%=p.getId()%>">
    <label>Nombre:</label><input type="text" name="nombre" value="<%=p.getNombre()%>" required><br><br>
    <label>Apellido:</label><input type="text" name="apellido" value="<%=p.getApellido()%>" required><br><br>
    <label>Edad:</label><input type="number" name="edad" value="<%=p.getEdad()%>" min="0" max="120" required><br><br>
    <button type="submit">Guardar cambios</button>
</form>
<br><a href="PacienteServlet">Cancelar</a>
</body>
</html>
