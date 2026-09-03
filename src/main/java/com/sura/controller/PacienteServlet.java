package com.sura.controller;

import com.sura.config.Conexion;
import com.sura.model.Paciente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

@WebServlet("/PacienteServlet")
public class PacienteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("editar".equals(accion)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Paciente paciente = buscarPorId(id);
                request.setAttribute("paciente", paciente);
                request.getRequestDispatcher("editarPaciente.jsp").forward(request, response);
            } catch (SQLException e) {
                throw new ServletException("Error al buscar paciente", e);
            }
            return;
        }
        try {
            List<Paciente> pacientes = listar();
            request.setAttribute("pacientes", pacientes);
            request.getRequestDispatcher("pacientes.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al listar pacientes", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        try {
            if ("registrar".equals(accion)) {
                registrar(request);
            } else if ("actualizar".equals(accion)) {
                actualizar(request);
            } else if ("eliminar".equals(accion)) {
                eliminar(request);
            }
            response.sendRedirect("PacienteServlet");
        } catch (SQLException | NumberFormatException e) {
            request.setAttribute("error", "No fue posible completar la operación: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private List<Paciente> listar() throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT ID, NOMBRE, APELLIDO, EDAD FROM paciente1 ORDER BY ID";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Paciente(rs.getInt("ID"), rs.getString("NOMBRE"),
                        rs.getString("APELLIDO"), rs.getInt("EDAD")));
            }
        }
        return lista;
    }

    private Paciente buscarPorId(int id) throws SQLException {
        String sql = "SELECT ID, NOMBRE, APELLIDO, EDAD FROM paciente1 WHERE ID=?";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Paciente(rs.getInt("ID"), rs.getString("NOMBRE"),
                        rs.getString("APELLIDO"), rs.getInt("EDAD"));
            }
        }
        return null;
    }

    private void registrar(HttpServletRequest r) throws SQLException {
        String sql = "INSERT INTO paciente1 (NOMBRE, APELLIDO, EDAD) VALUES (?, ?, ?)";
        try (Connection cn = Conexion.conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, r.getParameter("nombre"));
            ps.setString(2, r.getParameter("apellido"));
            ps.setInt(3, Integer.parseInt(r.getParameter("edad")));
            ps.executeUpdate();
        }
    }

    private void actualizar(HttpServletRequest r) throws SQLException {
        String sql = "UPDATE paciente1 SET NOMBRE=?, APELLIDO=?, EDAD=? WHERE ID=?";
        try (Connection cn = Conexion.conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, r.getParameter("nombre"));
            ps.setString(2, r.getParameter("apellido"));
            ps.setInt(3, Integer.parseInt(r.getParameter("edad")));
            ps.setInt(4, Integer.parseInt(r.getParameter("id")));
            ps.executeUpdate();
        }
    }

    private void eliminar(HttpServletRequest r) throws SQLException {
        String sql = "DELETE FROM paciente1 WHERE ID=?";
        try (Connection cn = Conexion.conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(r.getParameter("id")));
            ps.executeUpdate();
        }
    }
}