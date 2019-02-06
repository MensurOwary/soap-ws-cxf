package com.owary.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;

public class DBHelper {

    private static PasswordEncoder encoder = new BCryptPasswordEncoder(11);

    public static Connection getConnection() throws Exception {
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
        if (connection != null){
            return connection;
        }
        return null;
    }

    public static PreparedStatement preparedStatement(Connection connection,
                                                      String sqlQuery,
                                                      ExceptionThrowingConsumer<PreparedStatement> preparedStatementSetter) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatementSetter.accept(preparedStatement);
        return preparedStatement;
    }

    public static boolean doesTableExists(Connection connection, String table) throws SQLException {
        ResultSet tables = connection.getMetaData().getTables(null, null, table, null);
        boolean res = tables.next();
        if (!tables.isClosed()) {
            tables.close();
        }
        return res;
    }

    public static void destroyTable(Connection connection, String table) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(String.format("DROP TABLE IF EXISTS %s", table));
        preparedStatement.execute();
    }

    public static String encode(String toBeEncoded){
        return encoder.encode(toBeEncoded);
    }

    public static boolean passwordMatches(String raw, String encoded){
        return encoder.matches(raw, encoded);
    }


}
