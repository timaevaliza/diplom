package ru.diplom.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbHelper {

    private static final String MYSQL_URL = "jdbc:mysql://localhost:3307/app";
    private static final String MYSQL_USER = "app";
    private static final String MYSQL_PASSWORD = "pass";
    
    private static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/app";
    private static final String POSTGRES_USER = "app";
    private static final String POSTGRES_PASSWORD = "pass";


    private DbHelper() {}


    public static Connection getMySqlConnection() throws SQLException {
        return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
    }


    public static Connection getPostgresConnection() throws SQLException {
        return DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD);
    }


    public static void cleanDatabase(String dbType) throws SQLException {
        Connection connection;
        if (dbType.equalsIgnoreCase("mysql")) {
            connection = getMySqlConnection();
        } else {
            connection = getPostgresConnection();
        }

        Statement statement = connection.createStatement();
        try {
            statement.executeUpdate("DELETE FROM credit_request_entity");
        } catch (Exception ignored) {}
        try {
            statement.executeUpdate("DELETE FROM payment_entity");
        } catch (Exception ignored) {}
        try {
            statement.executeUpdate("DELETE FROM order_entity");
        } catch (Exception ignored) {}

        statement.close();
        connection.close();
        System.out.println("База данных " + dbType + " очищена.");
    }

    public static String getPaymentStatus(String dbType) throws SQLException {
        Connection connection;
        if (dbType.equalsIgnoreCase("mysql")) {
            connection = getMySqlConnection();
        } else {
            connection = getPostgresConnection();
        }

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1"
        );

        String status = null;
        if (resultSet.next()) {
            status = resultSet.getString("status");
        }

        resultSet.close();
        statement.close();
        connection.close();

        return status;
    }
}
