<!DOCTYPE html>
<html lang="es">
<head><meta charset="UTF-8"><title>Registrar paciente</title></head>
<body>
<h1>Registrar paciente</h1>
<form action="PacienteServlet" method="POST">
    <input type="hidden" name="accion" value="registrar">
    <label>Nombre:</label><input type="text" name="nombre" required><br><br>
    <label>Apellido:</label><input type="text" name="apellido" required><br><br>
    <label>Edad:</label><input type="number" name="edad" min="0" max="120" required><br><br>
    <button type="submit">Registrar</button>
</form>
<br><a href="PacienteServlet">Volver</a>
</body>
</html>
