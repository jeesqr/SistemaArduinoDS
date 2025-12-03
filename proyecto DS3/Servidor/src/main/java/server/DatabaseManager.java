package server;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:monitorBD.db";

    public DatabaseManager() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS datos_sensor (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "x INTEGER NOT NULL," +
                "y INTEGER NOT NULL," +
                "z INTEGER NOT NULL," +
                "fecha_de_captura TEXT NOT NULL," +
                "hora_de_captura TEXT NOT NULL" +
                ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("[BD] Tabla verificada/creada correctamente.");
        } catch (Exception e) {
            System.out.println("[BD] ERROR creando tabla: " + e.getMessage());
        }
    }

    public void insertData(int x, int y, int z, String fecha, String hora) {
        String sql = "INSERT INTO datos_sensor(x,y,z,fecha_de_captura,hora_de_captura) VALUES(?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, x);
            ps.setInt(2, y);
            ps.setInt(3, z);
            ps.setString(4, fecha);
            ps.setString(5, hora);
            ps.executeUpdate();
            System.out.println("[BD] Registro insertado: " + x + "," + y + "," + z);
        } catch (Exception e) {
            System.out.println("[BD] ERROR al insertar: " + e.getMessage());
        }
    }

    public ArrayList<String> queryData(String fecha) {
        ArrayList<String> list = new ArrayList<>();

        String sql = "SELECT * FROM datos_sensor WHERE fecha_de_captura = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fecha);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(
                        rs.getInt("x") + "," +
                                rs.getInt("y") + "," +
                                rs.getInt("z") + "," +
                                rs.getString("fecha_de_captura") + "," +
                                rs.getString("hora_de_captura")
                );
            }

        } catch (Exception e) {
            System.out.println("[BD] ERROR consultando: " + e.getMessage());
        }

        return list;
    }
}
