package com.sura.api;

import com.sura.config.Conexion;
import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.ServletException; 
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servicio web (API REST) para gestionar pacientes.
 * Endpoints:
 *   GET    /api/pacientes         -> lista todos los pacientes
 *   GET    /api/pacientes?id=1    -> obtiene un paciente por id
 *   POST   /api/pacientes         -> crea un paciente nuevo
 *   PUT    /api/pacientes?id=1    -> actualiza un paciente existente
 *   DELETE /api/pacientes?id=1    -> elimina un paciente
 */
@WebServlet("/api/pacientes")
public class PacienteApiServlet extends HttpServlet {

    // Lee el cuerpo (body) de la petición y lo devuelve como String
    private String leerBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                sb.append(linea);
            }
        }
        return sb.toString();
    }

    // GET: lista todos los pacientes, o uno solo si se pasa ?id=
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String idParam = request.getParameter("id");

        try (Connection con = Conexion.conectar()) {
            if (idParam != null) {
                // Buscar un solo paciente por id
                PreparedStatement ps = con.prepareStatement("SELECT * FROM paciente1 WHERE id = ?");
                ps.setInt(1, Integer.parseInt(idParam));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JSONObject paciente = new JSONObject();
                    paciente.put("id", rs.getInt("id"));
                    paciente.put("nombre", rs.getString("nombre"));
                    paciente.put("apellido", rs.getString("apellido"));
                    paciente.put("edad", rs.getInt("edad"));
                    out.print(paciente.toString());
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(new JSONObject().put("mensaje", "Paciente no encontrado"));
                }
            } else {
                // Listar todos los pacientes
                PreparedStatement ps = con.prepareStatement("SELECT * FROM paciente1");
                ResultSet rs = ps.executeQuery();

                JSONArray lista = new JSONArray();
                while (rs.next()) {
                    JSONObject paciente = new JSONObject();
                    paciente.put("id", rs.getInt("id"));
                    paciente.put("nombre", rs.getString("nombre"));
                    paciente.put("apellido", rs.getString("apellido"));
                    paciente.put("edad", rs.getInt("edad"));
                    lista.put(paciente);
                }
                out.print(lista.toString());
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(new JSONObject().put("mensaje", "Error al consultar: " + e.getMessage()));
        }
    }

    // POST: crea un paciente nuevo
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            JSONObject body = new JSONObject(leerBody(request));
            String nombre = body.getString("nombre");
            String apellido = body.getString("apellido");
            int edad = body.getInt("edad");

            try (Connection con = Conexion.conectar()) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO paciente1 (nombre, apellido, edad) VALUES (?, ?, ?)");
                ps.setString(1, nombre);
                ps.setString(2, apellido);
                ps.setInt(3, edad);
                ps.executeUpdate();

                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print(new JSONObject().put("mensaje", "Paciente creado con éxito"));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(new JSONObject().put("mensaje", "Error al crear: " + e.getMessage()));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new JSONObject().put("mensaje", "Datos inválidos: " + e.getMessage()));
        }
    }

    // PUT: actualiza un paciente existente (requiere ?id=)
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new JSONObject().put("mensaje", "Debe indicar el id del paciente (?id=)"));
            return;
        }

        try {
            JSONObject body = new JSONObject(leerBody(request));
            String nombre = body.getString("nombre");
            String apellido = body.getString("apellido");
            int edad = body.getInt("edad");

            try (Connection con = Conexion.conectar()) {
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE paciente1 SET nombre = ?, apellido = ?, edad = ? WHERE id = ?");
                ps.setString(1, nombre);
                ps.setString(2, apellido);
                ps.setInt(3, edad);
                ps.setInt(4, Integer.parseInt(idParam));
                int filas = ps.executeUpdate();

                if (filas > 0) {
                    out.print(new JSONObject().put("mensaje", "Paciente actualizado con éxito"));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(new JSONObject().put("mensaje", "Paciente no encontrado"));
                }
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(new JSONObject().put("mensaje", "Error al actualizar: " + e.getMessage()));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new JSONObject().put("mensaje", "Datos inválidos: " + e.getMessage()));
        }
    }

    // DELETE: elimina un paciente (requiere ?id=)
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new JSONObject().put("mensaje", "Debe indicar el id del paciente (?id=)"));
            return;
        }

        try (Connection con = Conexion.conectar()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM paciente1 WHERE id = ?");
            ps.setInt(1, Integer.parseInt(idParam));
            int filas = ps.executeUpdate();

            if (filas > 0) {
                out.print(new JSONObject().put("mensaje", "Paciente eliminado con éxito"));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(new JSONObject().put("mensaje", "Paciente no encontrado"));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(new JSONObject().put("mensaje", "Error al eliminar: " + e.getMessage()));
        }
    }
}