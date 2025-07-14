package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:sqlite:banco_escolar.db";

    public static Connection conectar() {
        try{
            return DriverManager.getConnection(URL);
        } catch(SQLException e) {
            System.err.println("Erro na conexão com o banco SQLite: " + e.getMessage());
            return null;
        }
    }
}

