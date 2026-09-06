package ru.diplom.db;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbHelper {

    // ============================================
    // ЧИТАЕМ ПАРАМЕТРЫ ИЗ СИСТЕМНЫХ СВОЙСТВ
    // ============================================

    private static final String DB_URL = System.getProperty("db.url", "jdbc:mysql://localhost:3307/app");
    private static final String DB_USER = System.getProperty("db.user", "app");
    private static final String DB_PASSWORD = System.getProperty("db.password", "pass");

    private static final String POSTGRES_URL = System.getProperty("db.postgres.url", "jdbc:postgresql://localhost:5432/app");
    private static final String POSTGRES_USER = System.getProperty("db.postgres.user", "app");
    private static final String POSTGRES_PASSWORD = System.getProperty("db.postgres.password", "pass");

    private static final QueryRunner runner = new QueryRunner();

    private DbHelper() {}

    // ============================================
    // МЕТОДЫ ПОДКЛЮЧЕНИЯ
    // ============================================

    public static Connection getMySqlConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static Connection getPostgresConnection() throws SQLException {
        return DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD);
    }

    // ============================================
    // МЕТОДЫ ДЛЯ РАБОТЫ С БД
    // ============================================

    public static void cleanDatabase(String dbType) throws SQLException {
        Connection connection = dbType.equalsIgnoreCase("mysql")
                ? getMySqlConnection()
                : getPostgresConnection();

        try {
            runner.update(connection, "DELETE FROM credit_request_entity");
        } catch (SQLException ignored) {}
        try {
            runner.update(connection, "DELETE FROM payment_entity");
        } catch (SQLException ignored) {}
        try {
            runner.update(connection, "DELETE FROM order_entity");
        } catch (SQLException ignored) {}

        connection.close();
        System.out.println("База данных " + dbType + " очищена.");
    }

    public static String getPaymentStatus(String dbType) throws SQLException {
        Connection connection = dbType.equalsIgnoreCase("mysql")
                ? getMySqlConnection()
                : getPostgresConnection();

        String sql = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        String status = runner.query(connection, sql, new ScalarHandler<>());

        connection.close();
        return status;
    }
}