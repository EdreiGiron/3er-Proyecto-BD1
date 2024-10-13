/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tercerproyecto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author edrei
 */
public class MySQLConnect {

    private Connection conexion;
    JSONLogger jsonLogger = new JSONLogger();

    // Datos de conexión a MySQL
    String usuario = "root";
    String contrasenia = "1234";
    String bd = "tercerproyectobd";
    String ip = "localhost";
    String puerto = "3306";
    String cadenaCon = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd;

    // Establece la conexión con MySQL
    public Connection establecerConexion() throws SQLException {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(cadenaCon, usuario, contrasenia);
                System.out.println("Se conectó correctamente a MySQL");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error al establecer la conexión: " + e.getMessage());
        }
        return conexion;
    }

    // Cierra la conexión a MySQL
    public void cerrarConexion() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
            System.out.println("Conexión cerrada correctamente.");
        }
    }

    // Inserta un empleado en la base de datos MySQL
    public boolean insertarEmpleado(Empleado empleado) throws SQLException {
        boolean inserto = false;
        establecerConexion();
        try {
            String sql = "INSERT INTO datos (dpi_empleado, primer_nombre, segundo_nombre, tercer_nombre, primer_apellido, segundo_apellido, direccion_domicilio, telefono_domicilio, telefono_movil, sueldo_empleado, bonificacion) values (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement stm = conexion.prepareStatement(sql);
            stm.setString(1, empleado.getDpi());
            stm.setString(2, empleado.getPrimerNombre());
            stm.setString(3, empleado.getSegundoNombre());
            stm.setString(4, empleado.getTercerNombre());
            stm.setString(5, empleado.getPrimerApellido());
            stm.setString(6, empleado.getSegundoApellido());
            stm.setString(7, empleado.getDireccion());
            stm.setString(8, empleado.getTelefonoResidencia());
            stm.setString(9, empleado.getTelefonoMovil());
            stm.setDouble(10, empleado.getSueldo());
            stm.setDouble(11, empleado.getBonificacion());
            int registrosInsertados = stm.executeUpdate();
            if (registrosInsertados > 0) {
                inserto = true;
                guardarBitacora(empleado.getDpi(), "INSERT", stm.toString());
                System.out.println("Empleado insertado correctamente en MySQL.");
            }
        } finally {
            cerrarConexion();
        }
        return inserto;
    }

    // Busca un empleado en la base de datos MySQL
    public Empleado buscarEmpleado(String dpi) throws SQLException {
        Empleado empleado = null;
        establecerConexion();
        try {
            String sql = "SELECT * FROM datos WHERE dpi_empleado = ?";
            PreparedStatement stm = conexion.prepareStatement(sql);
            stm.setString(1, dpi);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                empleado = new Empleado();
                empleado.setDpi(rs.getString("dpi_empleado"));
                empleado.setPrimerNombre(rs.getString("primer_nombre"));
                empleado.setSegundoNombre(rs.getString("segundo_nombre"));
                empleado.setTercerNombre(rs.getString("tercer_nombre"));
                empleado.setPrimerApellido(rs.getString("primer_apellido"));
                empleado.setSegundoApellido(rs.getString("segundo_apellido"));
                empleado.setDireccion(rs.getString("direccion_domicilio"));
                empleado.setTelefonoResidencia(rs.getString("telefono_domicilio"));
                empleado.setTelefonoMovil(rs.getString("telefono_movil"));
                empleado.setSueldo(rs.getDouble("sueldo_empleado"));
                empleado.setBonificacion(rs.getDouble("bonificacion"));
            } else {
                System.out.println("Empleado no encontrado en MySQL.");
            }
        } finally {
            cerrarConexion();
        }
        return empleado;
    }

    // Modifica un empleado en la base de datos MySQL
    public boolean modificarEmpleado(Empleado empleado) throws SQLException {
        boolean actualizo = false;
        establecerConexion();
        try {
            String sql = "UPDATE datos SET primer_nombre = ?, segundo_nombre = ?, tercer_nombre = ?, primer_apellido = ?, segundo_apellido = ?, direccion_domicilio = ?, telefono_domicilio = ?, telefono_movil = ?, sueldo_empleado = ?, bonificacion = ? WHERE dpi_empleado = ?";
            PreparedStatement stm = conexion.prepareStatement(sql);
            stm.setString(1, empleado.getPrimerNombre());
            stm.setString(2, empleado.getSegundoNombre());
            stm.setString(3, empleado.getTercerNombre());
            stm.setString(4, empleado.getPrimerApellido());
            stm.setString(5, empleado.getSegundoApellido());
            stm.setString(6, empleado.getDireccion());
            stm.setString(7, empleado.getTelefonoResidencia());
            stm.setString(8, empleado.getTelefonoMovil());
            stm.setDouble(9, empleado.getSueldo());
            stm.setDouble(10, empleado.getBonificacion());
            stm.setString(11, empleado.getDpi());
            int filasModificadas = stm.executeUpdate();
            if (filasModificadas > 0) {
                actualizo = true;
                guardarBitacora(empleado.getDpi(), "UPDATE", stm.toString());
                System.out.println("Empleado modificado correctamente en MySQL.");
            } else {
                System.out.println("No se encontró el empleado con el DPI especificado en MySQL.");
            }
        } finally {
            cerrarConexion();
        }
        return actualizo;
    }

    // Elimina un empleado de la base de datos MySQL
    public boolean eliminarEmpleado(String dpi) throws SQLException {
        boolean elimino = false;
        establecerConexion();
        try {
            String sql = "DELETE FROM datos WHERE dpi_empleado = ?";
            PreparedStatement stm = conexion.prepareStatement(sql);
            stm.setString(1, dpi);
            int filasEliminadas = stm.executeUpdate();
            if (filasEliminadas > 0) {
                elimino = true;
                guardarBitacora(dpi, "DELETE", stm.toString());
                System.out.println("Empleado eliminado correctamente en MySQL.");
            } else {
                System.out.println("No se encontró el empleado con el DPI especificado en MySQL.");
            }
        } finally {
            cerrarConexion();
        }
        return elimino;
    }

    // Obtener un empleado por DPI para MySQL
    public Empleado obtenerEmpleadoPorDPI(String dpi) throws SQLException {
        Empleado empleado = null;
        establecerConexion();
        try {
            String sql = "SELECT * FROM datos WHERE dpi_empleado = ?";
            PreparedStatement stm = conexion.prepareStatement(sql);
            stm.setString(1, dpi);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                empleado = new Empleado();
                empleado.setDpi(rs.getString("dpi_empleado"));
                empleado.setPrimerNombre(rs.getString("primer_nombre"));
                empleado.setSegundoNombre(rs.getString("segundo_nombre"));
                empleado.setTercerNombre(rs.getString("tercer_nombre"));
                empleado.setPrimerApellido(rs.getString("primer_apellido"));
                empleado.setSegundoApellido(rs.getString("segundo_apellido"));
                empleado.setDireccion(rs.getString("direccion_domicilio"));
                empleado.setTelefonoResidencia(rs.getString("telefono_domicilio"));
                empleado.setTelefonoMovil(rs.getString("telefono_movil"));
                empleado.setSueldo(rs.getDouble("sueldo_empleado"));
                empleado.setBonificacion(rs.getDouble("bonificacion"));
            } else {
                System.out.println("Empleado no encontrado en MySQL.");
            }
        } finally {
            cerrarConexion();
        }
        return empleado;
    }

    // Método auxiliar para guardar en la bitácora
    private void guardarBitacora(String dpi, String operacion, String query) {
        jsonLogger.agregarBitacora("MySQL", dpi, operacion, query);
    }
}
