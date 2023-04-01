package org.samples;


import com.spun.util.ObjectUtils;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.samples.CiUtils.skipTestOnCI;
import static org.samples.ProductionDatabase.createDatabase;

public class DatabaseTests {
    @Test
    void testConnection() throws SQLException {
        skipTestOnCI();
        assertEquals(42, SqlUtilites.executeNumericQuery("Select 42;", getConnection()));
    }

    @Test
    void testVersion() throws SQLException {
        skipTestOnCI();
        assertEquals(1, ProductionDatabase.getSchemaVersion(getConnection()));
    }
    @Test
    void testSchema() throws SQLException {
        skipTestOnCI();
        var schema = SqlUtilites.loadSchema(getConnection());
        Approvals.verify(SqlUtilites.print(schema));
    }

    @Test
    void testInMemoryConnection() throws SQLException {
        assertEquals(42, SqlUtilites.executeNumericQuery("Select 42;", getInMemoryConnection()));
    }
    @Test
    void testInMemoryVersion() throws SQLException {
        Connection connection = createInMemoryDatabase(1);
        assertEquals(1, ProductionDatabase.getSchemaVersion(connection));
    }

    
    void testInMemorySchema() throws SQLException {
        Connection connection = createInMemoryDatabase(1);
        var schema = SqlUtilites.loadSchema(connection);
        Approvals.verify(SqlUtilites.print(schema));
    }
    private Connection createInMemoryDatabase(int version) throws SQLException {
        Connection connection = getInMemoryConnection();
        createDatabase(connection, version);
        return connection;
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mariadb://localhost:3306/sakila", "root", "");
        } catch (SQLException e) {
            throw ObjectUtils.throwAsError(e);
        }
    }


    private Connection getInMemoryConnection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");

            connection.createStatement().execute("DROP SCHEMA IF EXISTS sakila CASCADE");
            connection.createStatement().execute("CREATE SCHEMA sakila");
            connection.createStatement().execute("SET SCHEMA sakila");
            return connection;
        } catch (SQLException e) {
            throw ObjectUtils.throwAsError(e);
        }
    }
}
